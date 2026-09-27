
package com.example.wahsly.utilidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.AtomicFile
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.util.Locale

object FotoPerfilStorage {

    private const val TAMANO_FOTO_LOCAL = 1024
    private const val TAMANO_FOTO_FIRESTORE = 1024
    private const val MAX_BYTES_FIRESTORE = 500_000

    private fun obtenerArchivo(
        context: Context,
        correo: String
    ): File {

        val correoNormalizado =
            correo.trim().lowercase(Locale.ROOT)

        val identificador =
            MessageDigest.getInstance("SHA-256")
                .digest(correoNormalizado.toByteArray())
                .joinToString("") {
                    "%02x".format(it.toInt() and 0xff)
                }

        val carpeta = File(
            context.filesDir,
            "fotos_perfil"
        )

        return File(
            carpeta,
            "$identificador.jpg"
        )
    }

    suspend fun guardarFoto(
        context: Context,
        correo: String,
        uri: Uri
    ): ImageBitmap = withContext(Dispatchers.IO) {

        val resolver = context.contentResolver

        val opciones = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        resolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(
                it,
                null,
                opciones
            )
        }

        if (
            opciones.outWidth <= 0 ||
            opciones.outHeight <= 0
        ) {
            throw IOException(
                "La imagen seleccionada no es válida"
            )
        }

        var muestra = 1

        while (
            maxOf(
                opciones.outWidth,
                opciones.outHeight
            ) / muestra > TAMANO_FOTO_LOCAL
        ) {
            muestra *= 2
        }

        val opcionesLectura =
            BitmapFactory.Options().apply {
                inSampleSize = muestra
            }

        val bitmap =
            resolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(
                    it,
                    null,
                    opcionesLectura
                )
            } ?: throw IOException(
                "No se pudo abrir la fotografía"
            )

        val ladoMayor =
            maxOf(bitmap.width, bitmap.height)

        val escala =
            minOf(
                1f,
                TAMANO_FOTO_LOCAL.toFloat() / ladoMayor
            )

        val imagenFinal =
            if (escala < 1f) {

                Bitmap.createScaledBitmap(
                    bitmap,
                    maxOf(
                        1,
                        (bitmap.width * escala).toInt()
                    ),
                    maxOf(
                        1,
                        (bitmap.height * escala).toInt()
                    ),
                    true
                )

            } else {

                bitmap

            }

        val archivo = obtenerArchivo(
            context,
            correo
        )

        archivo.parentFile?.mkdirs()

        val archivoAtomico = AtomicFile(archivo)
        val salida = archivoAtomico.startWrite()

        try {

            val guardado = imagenFinal.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                salida
            )

            if (!guardado) {
                throw IOException(
                    "No se pudo guardar la fotografía"
                )
            }

            archivoAtomico.finishWrite(salida)

        } catch (e: Exception) {

            archivoAtomico.failWrite(salida)

            throw e
        }

        imagenFinal.asImageBitmap()
    }

    suspend fun cargarFoto(
        context: Context,
        correo: String
    ): ImageBitmap? = withContext(Dispatchers.IO) {

        val archivo = obtenerArchivo(
            context,
            correo
        )

        if (!archivo.exists()) {
            return@withContext null
        }

        BitmapFactory.decodeFile(
            archivo.absolutePath
        )?.asImageBitmap()
    }

    /**
     * Prepara una copia pequeña de la fotografía local
     * para guardarla como texto Base64 en Firestore.
     */
    suspend fun prepararFotoParaFirestore(
        context: Context,
        correo: String
    ): String = withContext(Dispatchers.IO) {

        val archivo = obtenerArchivo(
            context,
            correo
        )

        if (!archivo.exists()) {
            throw IOException(
                "No existe una fotografía local para sincronizar"
            )
        }

        val bitmap = BitmapFactory.decodeFile(
            archivo.absolutePath
        ) ?: throw IOException(
            "No se pudo leer la fotografía local"
        )

        val ladoMayor = maxOf(
            bitmap.width,
            bitmap.height
        )

        val escala = minOf(
            1f,
            TAMANO_FOTO_FIRESTORE.toFloat() / ladoMayor
        )

        val imagenReducida =
            if (escala < 1f) {

                Bitmap.createScaledBitmap(
                    bitmap,
                    maxOf(
                        1,
                        (bitmap.width * escala).toInt()
                    ),
                    maxOf(
                        1,
                        (bitmap.height * escala).toInt()
                    ),
                    true
                )

            } else {

                bitmap

            }

        var calidad = 90
        var bytes: ByteArray

        do {

            val salida = ByteArrayOutputStream()

            val comprimida = imagenReducida.compress(
                Bitmap.CompressFormat.JPEG,
                calidad,
                salida
            )

            if (!comprimida) {
                throw IOException(
                    "No se pudo comprimir la fotografía"
                )
            }

            bytes = salida.toByteArray()

            calidad -= 10

        } while (
            bytes.size > MAX_BYTES_FIRESTORE &&
            calidad >= 40
        )

        if (bytes.size > MAX_BYTES_FIRESTORE) {
            throw IOException(
                "La fotografía sigue siendo demasiado grande"
            )
        }

        Base64.encodeToString(
            bytes,
            Base64.NO_WRAP
        )
    }

    /**
     * Recupera una fotografía de Firestore,
     * la guarda en el teléfono y la devuelve
     * para mostrarla en Compose.
     */
    suspend fun guardarFotoDesdeFirestore(
        context: Context,
        correo: String,
        fotoBase64: String
    ): ImageBitmap = withContext(Dispatchers.IO) {

        val bytes = Base64.decode(
            fotoBase64,
            Base64.DEFAULT
        )

        if (bytes.size > MAX_BYTES_FIRESTORE) {
            throw IOException(
                "La fotografía descargada supera el tamaño permitido"
            )
        }

        val bitmap = BitmapFactory.decodeByteArray(
            bytes,
            0,
            bytes.size
        ) ?: throw IOException(
            "La fotografía descargada no es válida"
        )

        val archivo = obtenerArchivo(
            context,
            correo
        )

        archivo.parentFile?.mkdirs()

        val archivoAtomico = AtomicFile(archivo)
        val salida = archivoAtomico.startWrite()

        try {

            salida.write(bytes)

            archivoAtomico.finishWrite(salida)

        } catch (e: Exception) {

            archivoAtomico.failWrite(salida)

            throw e
        }

        bitmap.asImageBitmap()
    }

    fun eliminarFoto(
        context: Context,
        correo: String
    ) {
        obtenerArchivo(
            context,
            correo
        ).delete()
    }
}

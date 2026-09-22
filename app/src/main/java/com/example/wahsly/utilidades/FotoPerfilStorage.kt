package com.example.wahsly.utilidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.AtomicFile
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.util.Locale

object FotoPerfilStorage {

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
            throw IOException("La imagen seleccionada no es válida")
        }

        var muestra = 1

        while (
            maxOf(
                opciones.outWidth,
                opciones.outHeight
            ) / muestra > 1024
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
            minOf(1f, 1024f / ladoMayor)

        val imagenFinal =
            if (escala < 1f) {

                Bitmap.createScaledBitmap(
                    bitmap,
                    maxOf(1, (bitmap.width * escala).toInt()),
                    maxOf(1, (bitmap.height * escala).toInt()),
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
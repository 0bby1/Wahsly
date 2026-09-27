package com.example.wahsly.utilidades

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException

object FotoRutinaStorage {
    private const val TAMANO_MAXIMO = 1000
    private const val MAX_BYTES = 300_000

    suspend fun prepararFoto(
        context: Context,
        uri: Uri
    ): String = withContext(Dispatchers.IO) {

        val resolver = context.contentResolver

        // Primero obtenemos el tamaño de la imagen
        val opcionesTamano = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        resolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(
                it,
                null,
                opcionesTamano
            )
        }

        if (
            opcionesTamano.outWidth <= 0 ||
            opcionesTamano.outHeight <= 0
        ) {
            throw IOException(
                "No se pudo leer la fotografía"
            )
        }

        // Reducimos la imagen para no guardar una foto enorme
        var muestra = 1

        while (
            maxOf(
                opcionesTamano.outWidth,
                opcionesTamano.outHeight
            ) / muestra > TAMANO_MAXIMO
        ) {
            muestra *= 2
        }

        val opcionesLectura = BitmapFactory.Options().apply {
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

        // Comprimir hasta que tenga un tamaño adecuado
        var calidad = 85
        var bytes: ByteArray

        do {
            val salida = ByteArrayOutputStream()
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                calidad,
                salida
            )
            bytes = salida.toByteArray()
            calidad -= 10
        } while (
            bytes.size > MAX_BYTES &&
            calidad >= 40
        )
        Base64.encodeToString(
            bytes,
            Base64.NO_WRAP
        )
    }

    fun cargarFoto(
        fotoBase64: String
    ): ImageBitmap? {
        if (fotoBase64.isBlank()) {
            return null
        }

        return try {
            val bytes = Base64.decode(
                fotoBase64,
                Base64.DEFAULT
            )
            BitmapFactory.decodeByteArray(
                bytes,
                0,
                bytes.size
            )?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
}
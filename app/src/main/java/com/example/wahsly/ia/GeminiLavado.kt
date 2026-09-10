package com.example.wahsly.ia

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.example.wahsly.BuildConfig
import com.example.wahsly.ui.pantallas.DatosEscaneo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

// RESULTADO DEL ANALISIS
data class ResultadoLavado(
    // INFORMACION ORIGINAL
    val fotoUri: String,
    val cantidad: Int,
    val tieneMancha: Boolean,
    val tipoMancha: String,
    val informacionAdicional: String,

    // INFORMACION DETECTADA POR GEMINI
    val etiquetaLegible: Boolean,
    val mensaje: String,
    val composicion: String,
    val lavado: String,
    val temperatura: String,
    val blanqueador: String,
    val secado: String,
    val planchado: String,
    val limpiezaProfesional: String,
    val tratamientoMancha: String,
    val precauciones: String
)


// CONEXION CON GEMINI
object GeminiLavado {
    private const val MODELO =
        "gemini-3.5-flash"
    private const val URL_API =
        "https://generativelanguage.googleapis.com/v1beta/interactions"


    // ANALIZAR ETIQUETA
    suspend fun analizarEtiqueta(
        context: Context,
        datos: DatosEscaneo
    ): ResultadoLavado {
        return withContext(Dispatchers.IO) {
            // API KEY
            val apiKey =
                BuildConfig.GEMINI_API_KEY
            if (apiKey.isBlank()) {
                throw IllegalStateException(
                    "No se encontró la API Key de Gemini."
                )
            }

            // LEER FOTO
            val bytesImagen =
                leerImagen(
                    context = context,
                    uri = datos.fotoUri
                )

            val imagenBase64 =
                Base64.encodeToString(
                    bytesImagen,
                    Base64.NO_WRAP
                )

            val mimeType =
                obtenerMimeType(
                    context = context,
                    uri = datos.fotoUri
                )

            // DATOS SOBRE LA MANCHA
            val textoMancha =
                if (datos.tieneMancha) {
                    "La prenda tiene una mancha.Tipo de mancha: ${datos.tipoMancha}".trimIndent()
                } else {
                    "El usuario no indicó ninguna mancha."
                }

            // INFORMACION ADICIONAL
            val textoAdicional =
                if (datos.informacionAdicional.isBlank()) {
                    "No hay información adicional."
                } else {
                    "Información adicional del usuario:${datos.informacionAdicional}".trimIndent()
                }

            // PROMPT DE WASHLY
            val prompt =
                """
                Eres el asistente especializado en cuidado de ropa
                de una aplicación llamada Washly.

                Tu tarea es analizar la fotografía de una ETIQUETA
                DE CUIDADO DE ROPA proporcionada por el fabricante.

                IMPORTANTE:

                Analiza principalmente la etiqueta y sus instrucciones.

                No inventes instrucciones basándote únicamente en el
                aspecto visual de la prenda.

                Identifica, cuando sea visible:

                - composición de la prenda
                - símbolos de lavado
                - lavado a máquina o a mano
                - temperatura máxima
                - uso de blanqueador o cloro
                - instrucciones de secado
                - uso de secadora
                - planchado
                - limpieza profesional o lavado en seco
                - cualquier advertencia escrita por el fabricante

                Las instrucciones del fabricante SIEMPRE tienen
                prioridad sobre cualquier recomendación adicional.

                Si la etiqueta está borrosa, cortada, demasiado oscura,
                no corresponde a una etiqueta de cuidado o los símbolos
                no pueden interpretarse con suficiente seguridad:

                etiqueta_legible debe ser false.

                En ese caso:

                - No inventes instrucciones.
                - Explica brevemente que debe tomarse otra fotografía.

                La información corresponde a:
                ${datos.cantidad} prenda(s).

                $textoMancha

                $textoAdicional

                Si existe una mancha, recomienda un tratamiento previo
                seguro solamente cuando no contradiga las instrucciones
                del fabricante.

                Responde siempre en español.

                Mantén las recomendaciones breves y fáciles de entender.
                """.trimIndent()

            // IMAGEN PARA GEMINI
            val parteImagen =
                JSONObject()
                    .put(
                        "type",
                        "image"
                    )
                    .put(
                        "data",
                        imagenBase64
                    )
                    .put(
                        "mime_type",
                        mimeType
                    )

            // TEXTO PARA GEMINI
            val parteTexto =
                JSONObject()
                    .put(
                        "type",
                        "text"
                    )
                    .put(
                        "text",
                        prompt
                    )

            val input =
                JSONArray()
                    .put(parteTexto)
                    .put(parteImagen)

            // FORMATO DEL JSON
            val propiedades =
                JSONObject()
                    .put(
                        "etiqueta_legible",
                        JSONObject()
                            .put(
                                "type",
                                "boolean"
                            )
                            .put(
                                "description",
                                "True únicamente si la etiqueta se puede interpretar con suficiente claridad."
                            )
                    )

                    .put(
                        "mensaje",
                        campoTexto(
                            "Resumen breve del análisis."
                        )
                    )

                    .put(
                        "composicion",
                        campoTexto(
                            "Composición visible de la prenda. Si no aparece, responde No visible."
                        )
                    )

                    .put(
                        "lavado",
                        campoTexto(
                            "Instrucciones de lavado indicadas en la etiqueta."
                        )
                    )

                    .put(
                        "temperatura",
                        campoTexto(
                            "Temperatura de lavado indicada. Si no es visible, responde No visible."
                        )
                    )

                    .put(
                        "blanqueador",
                        campoTexto(
                            "Indicaciones sobre blanqueador o cloro."
                        )
                    )

                    .put(
                        "secado",
                        campoTexto(
                            "Indicaciones sobre secado y secadora."
                        )
                    )

                    .put(
                        "planchado",
                        campoTexto(
                            "Indicaciones de planchado."
                        )
                    )

                    .put(
                        "limpieza_profesional",
                        campoTexto(
                            "Indicaciones de limpieza profesional o lavado en seco."
                        )
                    )

                    .put(
                        "tratamiento_mancha",
                        campoTexto(
                            "Tratamiento seguro para la mancha sin contradecir la etiqueta. Si no existe mancha responde No aplica."
                        )
                    )

                    .put(
                        "precauciones",
                        campoTexto(
                            "Precauciones importantes."
                        )
                    )

            val camposObligatorios =
                JSONArray()
                    .put("etiqueta_legible")
                    .put("mensaje")
                    .put("composicion")
                    .put("lavado")
                    .put("temperatura")
                    .put("blanqueador")
                    .put("secado")
                    .put("planchado")
                    .put("limpieza_profesional")
                    .put("tratamiento_mancha")
                    .put("precauciones")

            val esquema =
                JSONObject()
                    .put(
                        "type",
                        "object"
                    )
                    .put(
                        "properties",
                        propiedades
                    )
                    .put(
                        "required",
                        camposObligatorios
                    )

            // Gemini permite solicitar JSON estructurado.
            val formatoRespuesta =
                JSONObject()
                    .put(
                        "type",
                        "text"
                    )
                    .put(
                        "mime_type",
                        "application/json"
                    )
                    .put(
                        "schema",
                        esquema
                    )

            // CUERPO COMPLETO
            val cuerpo =
                JSONObject()
                    .put(
                        "model",
                        MODELO
                    )
                    .put(
                        "input",
                        input
                    )
                    .put(
                        "response_format",
                        formatoRespuesta
                    )

            // PETICION
            val respuesta =
                realizarPeticion(
                    apiKey = apiKey,
                    cuerpo = cuerpo
                )

            // EXTRAER RESPUESTA
            val textoGemini =
                obtenerTextoGemini(
                    respuesta
                )

            val json =
                JSONObject(
                    textoGemini
                )

            // RESULTADO
            ResultadoLavado(
                fotoUri = datos.fotoUri.toString(),
                cantidad = datos.cantidad,
                tieneMancha = datos.tieneMancha,
                tipoMancha = datos.tipoMancha,
                informacionAdicional = datos.informacionAdicional,
                etiquetaLegible =
                    json.optBoolean(
                        "etiqueta_legible",
                        false
                    ),
                mensaje =
                    json.optString(
                        "mensaje",
                        ""
                    ),
                composicion =
                    json.optString(
                        "composicion",
                        ""
                    ),
                lavado =
                    json.optString(
                        "lavado",
                        ""
                    ),
                temperatura =
                    json.optString(
                        "temperatura",
                        ""
                    ),
                blanqueador =
                    json.optString(
                        "blanqueador",
                        ""
                    ),
                secado =
                    json.optString(
                        "secado",
                        ""
                    ),
                planchado =
                    json.optString(
                        "planchado",
                        ""
                    ),
                limpiezaProfesional =
                    json.optString(
                        "limpieza_profesional",
                        ""
                    ),
                tratamientoMancha =
                    json.optString(
                        "tratamiento_mancha",
                        ""
                    ),
                precauciones =
                    json.optString(
                        "precauciones",
                        ""
                    )
            )
        }
    }

    // CREAR CAMPO STRING DEL JSON
    private fun campoTexto(
        descripcion: String
    ): JSONObject {
        return JSONObject()
            .put(
                "type",
                "string"
            )
            .put(
                "description",
                descripcion
            )
    }

    // LEER LA IMAGEN
    private fun leerImagen(
        context: Context,
        uri: Uri
    ): ByteArray {
        val bytes =
            if (uri.scheme == "file") {
                val ruta =
                    uri.path
                        ?: throw IllegalStateException(
                            "No se encontró la fotografía."
                        )
                File(ruta)
                    .readBytes()
            } else {
                context
                    .contentResolver
                    .openInputStream(uri)
                    ?.use {
                        it.readBytes()
                    }
                    ?: throw IllegalStateException(
                        "No se pudo leer la fotografía."
                    )
            }

        if (
            bytes.size >
            14 * 1024 * 1024
        ) {
            throw IllegalStateException(
                "La fotografía es demasiado grande. Intenta tomar otra foto."
            )
        }
        return bytes
    }

    // TIPO DE IMAGEN
    private fun obtenerMimeType(
        context: Context,
        uri: Uri
    ): String {
        val tipo =
            context
                .contentResolver
                .getType(uri)
        if (!tipo.isNullOrBlank()) {
            return tipo
        }

        val extension =
            uri.path
                ?.substringAfterLast(
                    ".",
                    ""
                )
                ?.lowercase()

        return when (extension) {
            "png" ->
                "image/png"

            "webp" ->
                "image/webp"

            else ->
                "image/jpeg"
        }
    }

    // LLAMADA HTTP
    private fun realizarPeticion(
        apiKey: String,
        cuerpo: JSONObject
    ): String {
        val url =
            URL(
                URL_API
            )
        val conexion =
            url.openConnection()
                    as HttpURLConnection
        try {
            conexion.requestMethod =
                "POST"
            conexion.connectTimeout =
                30_000
            conexion.readTimeout =
                60_000
            conexion.doOutput =
                true
            conexion.setRequestProperty(
                "Content-Type",
                "application/json; charset=UTF-8"
            )
            conexion.setRequestProperty(
                "x-goog-api-key",
                apiKey
            )
            conexion.setRequestProperty(
                "Api-Revision",
                "2026-05-20"
            )

            conexion
                .outputStream
                .bufferedWriter(
                    Charsets.UTF_8
                )
                .use { writer ->

                    writer.write(
                        cuerpo.toString()
                    )
                }

            val codigo = conexion.responseCode

            val stream = if (codigo in 200..299) {
                    conexion.inputStream
                } else {
                    conexion.errorStream
                }

            val respuesta =
                stream
                    ?.bufferedReader()
                    ?.use {
                        it.readText()
                    }
                    .orEmpty()

            if (codigo !in 200..299) {
                val mensajeServidor =
                    try {
                        JSONObject(
                            respuesta
                        )
                            .optJSONObject(
                                "error"
                            )
                            ?.optString(
                                "message"
                            )

                    } catch (_: Exception) {
                        null
                    }

                throw IllegalStateException(
                    mensajeServidor
                        ?.takeIf {
                            it.isNotBlank()
                        }
                        ?: "Error de Gemini: HTTP $codigo"
                )
            }

            return respuesta
        } finally {
            conexion.disconnect()
        }
    }

    // SACAR EL TEXTO DE LA RESPUESTA
    private fun obtenerTextoGemini(
        respuesta: String
    ): String {
        val json =
            JSONObject(
                respuesta
            )

        val steps =
            json.optJSONArray(
                "steps"
            )
                ?: throw IllegalStateException(
                    "Gemini no devolvió una respuesta válida."
                )

        for (
        i in 0 until steps.length()
        ) {
            val step =
                steps.getJSONObject(i)
            if (
                step.optString(
                    "type"
                ) == "model_output"
            ) {
                val contenido =
                    step.optJSONArray(
                        "content"
                    )
                        ?: continue
                for (
                j in 0 until contenido.length()
                ) {
                    val parte =
                        contenido
                            .getJSONObject(j)
                    if (
                        parte.optString(
                            "type"
                        ) == "text"
                    ) {
                        val texto =
                            parte.optString(
                                "text"
                            )
                        if (
                            texto.isNotBlank()
                        ) {
                            return texto
                        }
                    }
                }
            }
        }
        throw IllegalStateException(
            "Gemini no generó una respuesta."
        )
    }
}
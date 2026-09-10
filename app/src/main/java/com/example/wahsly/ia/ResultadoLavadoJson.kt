package com.example.wahsly.ia

import org.json.JSONObject

// Convertir resultado a JSON para guardarlo en ROOM
fun ResultadoLavado.aJson(): String {
    return JSONObject()
        .put("fotoUri", fotoUri)
        .put("cantidad", cantidad)
        .put("tieneMancha", tieneMancha)
        .put("tipoMancha", tipoMancha)
        .put("informacionAdicional", informacionAdicional)
        .put("etiquetaLegible", etiquetaLegible)
        .put("mensaje", mensaje)
        .put("composicion", composicion)
        .put("lavado", lavado)
        .put("temperatura", temperatura)
        .put("blanqueador", blanqueador)
        .put("secado", secado)
        .put("planchado", planchado)
        .put("limpiezaProfesional", limpiezaProfesional)
        .put("tratamientoMancha", tratamientoMancha)
        .put("precauciones", precauciones)
        .toString()
}

// Convertir el JSON guardado nuevamente a ResultadoLavado
fun resultadoLavadoDesdeJson(
    texto: String
): ResultadoLavado? {
    return try {
        val json = JSONObject(texto)
        // Sirve para ignorar registros antiguos
        // que no tienen nuestro nuevo formato.
        if (!json.has("etiquetaLegible")) {
            return null
        }
        ResultadoLavado(
            fotoUri =
                json.optString(
                    "fotoUri",
                    ""
                ),

            cantidad =
                json.optInt(
                    "cantidad",
                    1
                ),

            tieneMancha =
                json.optBoolean(
                    "tieneMancha",
                    false
                ),

            tipoMancha =
                json.optString(
                    "tipoMancha",
                    ""
                ),

            informacionAdicional =
                json.optString(
                    "informacionAdicional",
                    ""
                ),

            etiquetaLegible =
                json.optBoolean(
                    "etiquetaLegible",
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
                    "limpiezaProfesional",
                    ""
                ),

            tratamientoMancha =
                json.optString(
                    "tratamientoMancha",
                    ""
                ),

            precauciones =
                json.optString(
                    "precauciones",
                    ""
                )
        )
    } catch (_: Exception) {
        null
    }
}
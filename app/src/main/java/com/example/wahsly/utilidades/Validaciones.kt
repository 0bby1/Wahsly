package com.example.wahsly.utilidades

fun correoValido(correo: String): Boolean {
    val regex = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com$", RegexOption.IGNORE_CASE)
    return regex.matches(correo.trim())
}

/**
 * Valida que la contraseña tenga al menos 8 caracteres, una mayúscula,
 * un número y un símbolo (carácter especial).
 */
fun contrasenaValida(contrasena: String): Boolean {
    val tieneLongitudMinima = contrasena.length >= 8
    val tieneMayuscula = Regex("[A-Z]").containsMatchIn(contrasena)
    val tieneNumero = Regex("[0-9]").containsMatchIn(contrasena)
    val tieneSimbolo = Regex("[^A-Za-z0-9]").containsMatchIn(contrasena)
    return tieneLongitudMinima && tieneMayuscula && tieneNumero && tieneSimbolo
}

/**
 * Devuelve un mensaje describiendo qué requisito falta, o null si la
 * contraseña cumple con todos los requisitos.
 */
fun mensajeErrorContrasena(contrasena: String): String? {
    if (contrasena.length < 8) return "La contraseña debe tener al menos 8 caracteres"
    if (!Regex("[A-Z]").containsMatchIn(contrasena)) return "La contraseña debe incluir al menos una mayúscula"
    if (!Regex("[0-9]").containsMatchIn(contrasena)) return "La contraseña debe incluir al menos un número"
    if (!Regex("[^A-Za-z0-9]").containsMatchIn(contrasena)) return "La contraseña debe incluir al menos un símbolo (carácter especial)"
    return null
}
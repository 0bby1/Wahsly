package com.example.wahsly

fun correoValido(correo: String): Boolean {
    val regex = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com$", RegexOption.IGNORE_CASE)
    return regex.matches(correo.trim())
}
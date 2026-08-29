package com.example.wahsly

import android.util.Patterns

fun correoValido(correo: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()
}

package com.example.wahsly

interface Autenticador {
    fun iniciarSesion(correo: String, contrasena: String): Usuario
}

class CredencialesIncorrectasException(mensaje: String) : Exception(mensaje)

class AutenticadorLocal : Autenticador {
    override fun iniciarSesion(correo: String, contrasena: String): Usuario {
        if (correo.isBlank()) throw IllegalArgumentException("Ingresa tu correo electrónico")
        if (contrasena.isBlank()) throw IllegalArgumentException("Ingresa tu contraseña")
        if (!correoValido(correo)) throw IllegalArgumentException("Ingresa un correo electrónico válido")

        val usuario = base_de_datos_Usuarios.buscarUsuario(correo)
        if (usuario?.contrasena == contrasena) return usuario
        throw CredencialesIncorrectasException("Correo o contraseña incorrectos")
    }
}

package com.example.wahsly.auth

import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.datos.repository.UsuarioRepository
import com.example.wahsly.utilidades.correoValido

interface Autenticador {
    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario
}

class CredencialesIncorrectasException(mensaje: String) : Exception(mensaje)

class AutenticadorLocal(private val repository: UsuarioRepository) : Autenticador {
    override suspend fun iniciarSesion(correo: String, contrasena: String): Usuario {
        if (correo.isBlank()) throw IllegalArgumentException("Ingresa tu correo electrónico")
        if (contrasena.isBlank()) throw IllegalArgumentException("Ingresa tu contraseña")
        if (!correoValido(correo)) throw IllegalArgumentException("Ingresa un correo electrónico válido")

        val usuario = repository.buscarUsuario(correo)
        if (usuario?.contrasena == contrasena) return usuario
        throw CredencialesIncorrectasException("Correo o contraseña incorrectos")
    }
}
package com.example.wahsly.auth

import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.datos.repository.FirebaseUsuarioRepository
import com.example.wahsly.utilidades.correoValido
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.CancellationException

interface Autenticador {

    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): Usuario

}

class CredencialesIncorrectasException(
    mensaje: String
) : Exception(mensaje)

class AutenticadorFirebase(

    private val repository: FirebaseUsuarioRepository =
        FirebaseUsuarioRepository()

) : Autenticador {

    override suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): Usuario {

        if (correo.isBlank()) {
            throw IllegalArgumentException(
                "Ingresa tu correo electrónico"
            )
        }

        if (contrasena.isBlank()) {
            throw IllegalArgumentException(
                "Ingresa tu contraseña"
            )
        }

        if (!correoValido(correo.trim())) {
            throw IllegalArgumentException(
                "Ingresa un correo electrónico válido"
            )
        }

        try {

            val usuarioFirebase =
                repository.iniciarSesion(
                    correo = correo.trim(),
                    contrasena = contrasena
                )

            return Usuario(
                nombre = usuarioFirebase.nombre,
                apellido = usuarioFirebase.apellido,
                correo = usuarioFirebase.correo,
                contrasena = ""
            )

        } catch (e: CancellationException) {

            throw e

        } catch (e: FirebaseAuthInvalidCredentialsException) {

            throw CredencialesIncorrectasException(
                "Correo o contraseña incorrectos"
            )

        } catch (e: FirebaseAuthInvalidUserException) {

            throw CredencialesIncorrectasException(
                "Correo o contraseña incorrectos"
            )

        }

    }

}

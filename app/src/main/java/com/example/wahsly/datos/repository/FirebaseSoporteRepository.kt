package com.example.wahsly.datos.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseSoporteRepository {

    private val auth = FirebaseAuth.getInstance()

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun enviarSolicitud(
        nombre: String,
        categoria: String,
        mensaje: String
    ) {

        val usuario = auth.currentUser
            ?: throw IllegalStateException(
                "Debes iniciar sesión para enviar una solicitud."
            )

        val correo = usuario.email
            ?: throw IllegalStateException(
                "No se encontró el correo de tu cuenta."
            )

        val mensajeLimpio = mensaje.trim()

        val categoriasPermitidas = listOf(
            "Reportar un error",
            "Enviar una sugerencia",
            "Presentar una queja",
            "Otro"
        )

        require(categoria in categoriasPermitidas) {
            "Selecciona el motivo de tu mensaje."
        }

        require(mensajeLimpio.isNotBlank()) {
            "Escribe tu mensaje."
        }

        require(mensajeLimpio.length <= 2000) {
            "El mensaje no puede superar los 2000 caracteres."
        }

        val solicitud = hashMapOf(
            "uid" to usuario.uid,
            "nombre" to nombre.take(100),
            "correo" to correo,
            "categoria" to categoria,
            "mensaje" to mensajeLimpio,
            "fecha" to FieldValue.serverTimestamp(),
            "estado" to "pendiente"
        )

        firestore
            .collection("soporte")
            .add(solicitud)
            .await()
    }
}

package com.example.wahsly.datos.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

data class RegistroHistorialFirebase(
    val id: String,
    val nombre: String,
    val fecha: String,
    val informacion: String
)

class FirebaseHistorialRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private fun historialUsuario() =
        firestore
            .collection("usuarios")
            .document(
                auth.currentUser?.uid
                    ?: throw IllegalStateException(
                        "No hay una sesión activa"
                    )
            )
            .collection("historial")

    fun registrosPorUsuario(): Flow<List<RegistroHistorialFirebase>> =
        callbackFlow {

            val listener = try {

                historialUsuario()
                    .orderBy(
                        "creadoEn",
                        Query.Direction.DESCENDING
                    )
                    .addSnapshotListener { snapshot, error ->

                        if (error != null) {
                            Log.e(
                                "WAHSLY_HISTORIAL",
                                "Error al consultar historial",
                                error
                            )

                            close(error)
                            return@addSnapshotListener
                        }

                        val registros =
                            snapshot?.documents?.map { documento ->

                                RegistroHistorialFirebase(
                                    id = documento.id,
                                    nombre = documento.getString("nombre")
                                        .orEmpty(),
                                    fecha = documento.getString("fecha")
                                        .orEmpty(),
                                    informacion = documento.getString("informacion")
                                        .orEmpty()
                                )

                            }.orEmpty()

                        trySend(registros)
                    }

            } catch (e: Exception) {

                close(e)
                return@callbackFlow
            }

            awaitClose {
                listener.remove()
            }
        }

    suspend fun agregarRegistro(
        nombre: String,
        fecha: String,
        informacion: String
    ) {

        val datos = hashMapOf(
            "nombre" to nombre,
            "fecha" to fecha,
            "informacion" to informacion,
            "creadoEn" to FieldValue.serverTimestamp()
        )

        historialUsuario()
            .add(datos)
            .await()
    }

    suspend fun eliminarRegistro(
        id: String
    ) {

        historialUsuario()
            .document(id)
            .delete()
            .await()
    }
}

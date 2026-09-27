
package com.example.wahsly.datos.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class UsuarioFirebase(
    val uid: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = ""
)

class FirebaseUsuarioRepository(

    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance(),

    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()

) {

    suspend fun registrarUsuario(
        nombre: String,
        apellido: String,
        correo: String,
        contrasena: String
    ): UsuarioFirebase {

        val correoNormalizado =
            correo.trim().lowercase()

        val resultado = auth
            .createUserWithEmailAndPassword(
                correoNormalizado,
                contrasena
            )
            .await()

        val firebaseUser = resultado.user
            ?: throw IllegalStateException(
                "No se pudo crear el usuario"
            )

        val uid = firebaseUser.uid

        val usuario = UsuarioFirebase(
            uid = uid,
            nombre = nombre.trim(),
            apellido = apellido.trim(),
            correo = firebaseUser.email
                ?: correoNormalizado
        )

        val datosUsuario = hashMapOf(
            "uid" to usuario.uid,
            "nombre" to usuario.nombre,
            "apellido" to usuario.apellido,
            "correo" to usuario.correo,
            "fechaRegistro" to FieldValue.serverTimestamp()
        )

        firestore
            .collection("usuarios")
            .document(uid)
            .set(datosUsuario)
            .await()

        return usuario
    }

    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): UsuarioFirebase {

        val resultado = auth
            .signInWithEmailAndPassword(
                correo.trim(),
                contrasena
            )
            .await()

        val firebaseUser = resultado.user
            ?: throw IllegalStateException(
                "No se pudo iniciar sesión"
            )

        return obtenerUsuario(firebaseUser.uid)
    }

    suspend fun obtenerUsuario(
        uid: String
    ): UsuarioFirebase {

        val documento = firestore
            .collection("usuarios")
            .document(uid)
            .get()
            .await()

        if (!documento.exists()) {
            throw IllegalStateException(
                "No se encontró el perfil del usuario"
            )
        }

        return UsuarioFirebase(
            uid = uid,
            nombre = documento.getString("nombre") ?: "",
            apellido = documento.getString("apellido") ?: "",
            correo = documento.getString("correo") ?: ""
        )
    }

    /**
     * Guarda la fotografía comprimida
     * en el documento del usuario actual.
     */
    suspend fun guardarFotoPerfil(
        fotoBase64: String
    ) {

        val firebaseUser = auth.currentUser
            ?: throw IllegalStateException(
                "No hay una sesión activa"
            )

        val documentoUsuario = firestore
            .collection("usuarios")
            .document(firebaseUser.uid)

        documentoUsuario.update(
            mapOf(
                "fotoPerfilBase64" to fotoBase64,
                "fotoPerfilActualizadaEn" to
                        FieldValue.serverTimestamp()
            )
        ).await()
    }

    /**
     * Recupera la fotografía desde Firestore.
     * Devuelve null si aún no se ha subido una.
     */
    suspend fun obtenerFotoPerfilBase64(): String? {

        val firebaseUser = auth.currentUser
            ?: throw IllegalStateException(
                "No hay una sesión activa"
            )

        val documento = firestore
            .collection("usuarios")
            .document(firebaseUser.uid)
            .get()
            .await()

        if (!documento.exists()) {
            throw IllegalStateException(
                "No se encontró el perfil del usuario"
            )
        }

        return documento
            .getString("fotoPerfilBase64")
            ?.takeIf { it.isNotBlank() }
    }

    fun obtenerUsuarioActual() =
        auth.currentUser

    fun cerrarSesion() {
        auth.signOut()
    }
}

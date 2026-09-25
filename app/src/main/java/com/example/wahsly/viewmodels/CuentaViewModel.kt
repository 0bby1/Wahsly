
package com.example.wahsly.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.utilidades.mensajeErrorContrasena
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CuentaViewModel : ViewModel() {

    var cargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    var contrasenaCambiada by mutableStateOf(false)
        private set

    var cuentaEliminada by mutableStateOf(false)
        private set

    fun mensajeErrorMostrado() {
        mensajeError = null
    }

    fun contrasenaCambiadaManejada() {
        contrasenaCambiada = false
    }

    fun cambiarContrasena(
        usuario: Usuario,
        contrasenaActual: String,
        contrasenaNueva: String,
        confirmarContrasenaNueva: String
    ) {

        if (cargando) return

        if (
            contrasenaActual.isBlank() ||
            contrasenaNueva.isBlank() ||
            confirmarContrasenaNueva.isBlank()
        ) {
            mensajeError = "Todos los campos son obligatorios"
            return
        }

        val errorContrasena =
            mensajeErrorContrasena(contrasenaNueva)

        if (errorContrasena != null) {
            mensajeError = errorContrasena
            return
        }

        if (contrasenaNueva != confirmarContrasenaNueva) {
            mensajeError =
                "Las contraseñas nuevas no coinciden"
            return
        }

        if (contrasenaNueva == contrasenaActual) {
            mensajeError =
                "La nueva contraseña debe ser diferente a la actual"
            return
        }

        viewModelScope.launch {

            cargando = true
            mensajeError = null

            try {

                val firebaseAuth =
                    FirebaseAuth.getInstance()

                val firebaseUser =
                    firebaseAuth.currentUser
                        ?: throw IllegalStateException(
                            "No hay una sesión activa"
                        )

                val correo =
                    firebaseUser.email
                        ?: throw IllegalStateException(
                            "No se encontró el correo del usuario"
                        )

                if (
                    !correo.equals(
                        usuario.correo,
                        ignoreCase = true
                    )
                ) {
                    throw IllegalStateException(
                        "La cuenta activa no coincide con el perfil"
                    )
                }

                val credencial =
                    EmailAuthProvider.getCredential(
                        correo,
                        contrasenaActual
                    )

                firebaseUser
                    .reauthenticate(credencial)
                    .await()

                firebaseUser
                    .updatePassword(contrasenaNueva)
                    .await()

                contrasenaCambiada = true

            } catch (e: CancellationException) {

                throw e

            } catch (
                e: FirebaseAuthInvalidCredentialsException
            ) {

                mensajeError =
                    "La contraseña actual no es correcta"

            } catch (
                e: FirebaseAuthInvalidUserException
            ) {

                mensajeError =
                    "La cuenta ya no está disponible. Inicia sesión nuevamente."

            } catch (e: Exception) {

                Log.e(
                    "WAHSLY_CUENTA",
                    "Error al cambiar contraseña",
                    e
                )

                mensajeError =
                    "No se pudo actualizar la contraseña. Comprueba tu conexión e inténtalo nuevamente."

            } finally {

                cargando = false

            }

        }

    }

    fun eliminarCuenta(
        usuario: Usuario,
        contrasena: String,
        onEliminada: () -> Unit
    ) {

        if (cargando) return

        if (contrasena.isBlank()) {
            mensajeError =
                "Ingresa tu contraseña para confirmar"
            return
        }

        viewModelScope.launch {

            cargando = true
            mensajeError = null

            try {

                val firebaseAuth =
                    FirebaseAuth.getInstance()

                val firebaseUser =
                    firebaseAuth.currentUser
                        ?: throw IllegalStateException(
                            "No hay una sesión activa"
                        )

                val uid = firebaseUser.uid

                val correo =
                    firebaseUser.email
                        ?: throw IllegalStateException(
                            "No se encontró el correo del usuario"
                        )

                if (
                    !correo.equals(
                        usuario.correo,
                        ignoreCase = true
                    )
                ) {
                    throw IllegalStateException(
                        "La cuenta activa no coincide con el perfil"
                    )
                }

                val credencial =
                    EmailAuthProvider.getCredential(
                        correo,
                        contrasena
                    )

                // Verificar la identidad antes de eliminar datos.
                firebaseUser
                    .reauthenticate(credencial)
                    .await()

                val firestore =
                    FirebaseFirestore.getInstance()

                val documentoUsuario =
                    firestore
                        .collection("usuarios")
                        .document(uid)

                val historial =
                    documentoUsuario.collection("historial")

                // Firestore no elimina automáticamente
                // las subcolecciones al borrar un documento.
                // Por eso eliminamos primero el historial.
                while (true) {

                    val documentos =
                        historial
                            .limit(100)
                            .get()
                            .await()
                            .documents

                    if (documentos.isEmpty()) {
                        break
                    }

                    val lote =
                        firestore.batch()

                    documentos.forEach { documento ->
                        lote.delete(documento.reference)
                    }

                    lote.commit().await()
                }

                // Eliminar el perfil de Firestore.
                documentoUsuario
                    .delete()
                    .await()

                // Eliminar la cuenta de Authentication
                // únicamente después de limpiar Firestore.
                firebaseUser
                    .delete()
                    .await()

                cuentaEliminada = true

                Log.d(
                    "WAHSLY_CUENTA",
                    "Cuenta eliminada de Firebase"
                )


                firebaseUser
                    .delete()
                    .await()

                cuentaEliminada = true

                Log.d(
                    "WAHSLY_CUENTA",
                    "Cuenta eliminada de Firebase"
                )

                onEliminada()


                onEliminada()

            } catch (e: CancellationException) {

                throw e

            } catch (
                e: FirebaseAuthInvalidCredentialsException
            ) {

                mensajeError =
                    "La contraseña no es correcta"

            } catch (
                e: FirebaseAuthInvalidUserException
            ) {

                mensajeError =
                    "La cuenta ya no está disponible. Inicia sesión nuevamente."

            } catch (e: Exception) {

                Log.e(
                    "WAHSLY_CUENTA",
                    "Error al eliminar cuenta",
                    e
                )

                mensajeError =
                    "No se pudo completar la eliminación. Comprueba tu conexión e inténtalo nuevamente. Si vuelve a ocurrir, revisa los registros de Wahsly."

            } finally {

                cargando = false

            }

        }

    }

}


package com.example.wahsly.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.datos.repository.FirebaseUsuarioRepository
import com.example.wahsly.utilidades.correoValido
import com.example.wahsly.utilidades.mensajeErrorContrasena
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val repository: FirebaseUsuarioRepository
) : ViewModel() {

    var nombre by mutableStateOf("")
        private set

    var apellido by mutableStateOf("")
        private set

    var correo by mutableStateOf("")
        private set

    var contrasena by mutableStateOf("")
        private set

    var confirmarContrasena by mutableStateOf("")
        private set

    var aceptaPolitica by mutableStateOf(false)
        private set

    var mostrarContrasena by mutableStateOf(false)
        private set

    var mostrarConfirmacion by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    var mostrarRegistroExitoso by mutableStateOf(false)
        private set

    var usuarioRegistrado by mutableStateOf<Usuario?>(null)
        private set

    var cargando by mutableStateOf(false)
        private set

    fun onNombreChange(valor: String) {
        nombre = valor
    }

    fun onApellidoChange(valor: String) {
        apellido = valor
    }

    fun onCorreoChange(valor: String) {
        correo = valor
    }

    fun onContrasenaChange(valor: String) {
        contrasena = valor
    }

    fun onConfirmarContrasenaChange(valor: String) {
        confirmarContrasena = valor
    }

    fun toggleAceptaPolitica() {
        aceptaPolitica = !aceptaPolitica
    }

    fun toggleMostrarContrasena() {
        mostrarContrasena = !mostrarContrasena
    }

    fun toggleMostrarConfirmacion() {
        mostrarConfirmacion = !mostrarConfirmacion
    }

    fun mensajeErrorMostrado() {
        mensajeError = null
    }

    fun registroExitosoManejado() {
        mostrarRegistroExitoso = false
    }


    fun registrar() {

        android.util.Log.d(
            "WAHSLY_REGISTRO",
            "Se presionó el botón de registro"
        )

        android.util.Log.d(
            "WAHSLY_REGISTRO",
            "Privacidad aceptada: $aceptaPolitica"
        )

        if (cargando) {
            android.util.Log.d(
                "WAHSLY_REGISTRO",
                "Registro detenido: ya existe una operación en curso"
            )
            return
        }

        if (
            nombre.isBlank() ||
            apellido.isBlank() ||
            correo.isBlank() ||
            contrasena.isBlank() ||
            confirmarContrasena.isBlank()
        ) {

            mensajeError = "Todos los campos son obligatorios"

            android.util.Log.d(
                "WAHSLY_REGISTRO",
                "Registro detenido: faltan campos obligatorios"
            )

            return
        }

        if (!correoValido(correo.trim())) {

            mensajeError = "Correo electrónico no válido"

            android.util.Log.d(
                "WAHSLY_REGISTRO",
                "Registro detenido: correo inválido"
            )

            return
        }

        val errorContrasena =
            mensajeErrorContrasena(contrasena)

        if (errorContrasena != null) {

            mensajeError = errorContrasena

            android.util.Log.d(
                "WAHSLY_REGISTRO",
                "Registro detenido: contraseña no cumple los requisitos"
            )

            return
        }

        if (contrasena != confirmarContrasena) {

            mensajeError = "Las contraseñas no coinciden"

            android.util.Log.d(
                "WAHSLY_REGISTRO",
                "Registro detenido: las contraseñas no coinciden"
            )

            return
        }

        if (!aceptaPolitica) {

            mensajeError =
                "Debes aceptar la política de privacidad"

            android.util.Log.d(
                "WAHSLY_REGISTRO",
                "Registro detenido: política de privacidad no aceptada"
            )

            return
        }

        android.util.Log.d(
            "WAHSLY_REGISTRO",
            "Validaciones completadas correctamente"
        )

        viewModelScope.launch {

            cargando = true
            mensajeError = null

            try {

                android.util.Log.d(
                    "WAHSLY_REGISTRO",
                    "Iniciando registro en Firebase"
                )

                val usuarioFirebase =
                    repository.registrarUsuario(
                        nombre = nombre,
                        apellido = apellido,
                        correo = correo,
                        contrasena = contrasena
                    )

                android.util.Log.d(
                    "WAHSLY_REGISTRO",
                    "Registro y perfil de Firebase completados"
                )

                usuarioRegistrado = Usuario(
                    nombre = usuarioFirebase.nombre,
                    apellido = usuarioFirebase.apellido,
                    correo = usuarioFirebase.correo,
                    contrasena = ""
                )

                mostrarRegistroExitoso = true

            } catch (e: kotlinx.coroutines.CancellationException) {

                throw e

            } catch (e: FirebaseAuthUserCollisionException) {

                android.util.Log.e(
                    "WAHSLY_REGISTRO",
                    "La cuenta ya existe en Firebase Authentication",
                    e
                )

                mensajeError =
                    "Este correo electrónico ya está registrado"

            } catch (e: FirebaseNetworkException) {

                android.util.Log.e(
                    "WAHSLY_REGISTRO",
                    "Error de conexión con Firebase",
                    e
                )

                mensajeError =
                    "No hay conexión a internet. Inténtalo nuevamente."

            } catch (e: Exception) {

                android.util.Log.e(
                    "WAHSLY_REGISTRO",
                    "Error durante el registro en Firebase",
                    e
                )

                mensajeError =
                    "No se pudo completar el registro. Revisa Logcat."

            } finally {

                cargando = false

                android.util.Log.d(
                    "WAHSLY_REGISTRO",
                    "Proceso de registro finalizado"
                )

            }

        }

    }
}

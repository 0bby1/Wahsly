package com.example.wahsly

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegistroViewModel(private val repository: UsuarioRepository) : ViewModel() {

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

    fun onNombreChange(valor: String) { nombre = valor }
    fun onApellidoChange(valor: String) { apellido = valor }
    fun onCorreoChange(valor: String) { correo = valor }
    fun onContrasenaChange(valor: String) { contrasena = valor }
    fun onConfirmarContrasenaChange(valor: String) { confirmarContrasena = valor }
    fun toggleAceptaPolitica() { aceptaPolitica = !aceptaPolitica }
    fun toggleMostrarContrasena() { mostrarContrasena = !mostrarContrasena }
    fun toggleMostrarConfirmacion() { mostrarConfirmacion = !mostrarConfirmacion }
    fun mensajeErrorMostrado() { mensajeError = null }
    fun registroExitosoManejado() { mostrarRegistroExitoso = false }

    fun registrar() {
        if (nombre.isBlank() || apellido.isBlank() || correo.isBlank() ||
            contrasena.isBlank() || confirmarContrasena.isBlank()
        ) {
            mensajeError = "Todos los campos son obligatorios"
            return
        }
        if (!correoValido(correo)) {
            mensajeError = "Correo electrónico no válido"
            return
        }
        if (contrasena != confirmarContrasena) {
            mensajeError = "Las contraseñas no coinciden"
            return
        }
        if (!aceptaPolitica) {
            mensajeError = "Debes aceptar la política de privacidad"
            return
        }

        viewModelScope.launch {
            val nuevoUsuario = Usuario(
                nombre = nombre.trim(),
                apellido = apellido.trim(),
                correo = correo.trim(),
                contrasena = contrasena
            )
            if (repository.agregarUsuario(nuevoUsuario)) {
                usuarioRegistrado = nuevoUsuario
                mostrarRegistroExitoso = true
            } else {
                mensajeError = "El correo ya está registrado"
            }
        }
    }
}
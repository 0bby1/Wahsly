package com.example.wahsly.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.datos.repository.HistorialRepository
import com.example.wahsly.datos.repository.UsuarioRepository
import kotlinx.coroutines.launch

class CuentaViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val historialRepository: HistorialRepository
) : ViewModel() {

    var cargando by mutableStateOf(false)
        private set
    var mensajeError by mutableStateOf<String?>(null)
        private set
    var contrasenaCambiada by mutableStateOf(false)
        private set
    var cuentaEliminada by mutableStateOf(false)
        private set

    fun mensajeErrorMostrado() { mensajeError = null }
    fun contrasenaCambiadaManejada() { contrasenaCambiada = false }

    fun cambiarContrasena(
        usuario: Usuario,
        contrasenaActual: String,
        contrasenaNueva: String,
        confirmarContrasenaNueva: String
    ) {
        if (contrasenaActual.isBlank() || contrasenaNueva.isBlank() || confirmarContrasenaNueva.isBlank()) {
            mensajeError = "Todos los campos son obligatorios"
            return
        }
        if (usuario.contrasena != contrasenaActual) {
            mensajeError = "La contraseña actual no es correcta"
            return
        }
        if (contrasenaNueva.length < 6) {
            mensajeError = "La nueva contraseña debe tener al menos 6 caracteres"
            return
        }
        if (contrasenaNueva != confirmarContrasenaNueva) {
            mensajeError = "Las contraseñas nuevas no coinciden"
            return
        }
        if (contrasenaNueva == contrasenaActual) {
            mensajeError = "La nueva contraseña debe ser diferente a la actual"
            return
        }

        viewModelScope.launch {
            cargando = true
            usuarioRepository.actualizarContrasena(usuario.correo, contrasenaNueva)
            cargando = false
            contrasenaCambiada = true
        }
    }

    fun eliminarCuenta(
        usuario: Usuario,
        contrasena: String,
        onEliminada: () -> Unit
    ) {
        if (contrasena.isBlank()) {
            mensajeError = "Ingresa tu contraseña para confirmar"
            return
        }
        if (usuario.contrasena != contrasena) {
            mensajeError = "La contraseña no es correcta"
            return
        }

        viewModelScope.launch {
            cargando = true
            historialRepository.eliminarPorUsuario(usuario.correo)
            usuarioRepository.eliminarUsuario(usuario.correo)
            cargando = false
            cuentaEliminada = true
            onEliminada()
        }
    }
}

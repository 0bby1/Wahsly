package com.example.wahsly.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wahsly.datos.repository.HistorialRepository
import com.example.wahsly.datos.repository.UsuarioRepository

class ViewModelFactory(
    private val usuarioRepository: UsuarioRepository,
    private val historialRepository: HistorialRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegistroViewModel::class.java) ->
                RegistroViewModel(usuarioRepository) as T

            modelClass.isAssignableFrom(CuentaViewModel::class.java) -> {
                requireNotNull(historialRepository) {
                    "Se requiere historialRepository para crear CuentaViewModel"
                }
                CuentaViewModel(usuarioRepository, historialRepository) as T
            }

            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}
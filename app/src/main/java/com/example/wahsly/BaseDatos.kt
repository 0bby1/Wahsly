package com.example.wahsly

import androidx.compose.runtime.mutableStateListOf

// Historial en memoria
object BaseDatosHistorial {
    val registros = mutableStateListOf<RegistroEscaneo>()
}

// Usuarios de prueba (mutable)
object base_de_datos_Usuarios {
    val usuarios = mutableListOf(
        Usuario("César", "Avalos", "cesar@gmail.com", "1234"),
        Usuario("María", "Gonzalez", "maria@gmail.com", "5678"),
        Usuario("Juan", "Gabriel", "juan@gmail.com", "abcd")
    )

    fun buscarUsuario(correo: String): Usuario? {
        return usuarios.find { it.correo.equals(correo.trim(), ignoreCase = true) }
    }

    fun agregarUsuario(nuevo: Usuario): Boolean {
        if (buscarUsuario(nuevo.correo) != null) return false
        usuarios.add(nuevo)
        return true
    }
}

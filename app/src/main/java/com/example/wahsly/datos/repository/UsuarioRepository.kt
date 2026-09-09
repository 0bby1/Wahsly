package com.example.wahsly.datos.repository

import com.example.wahsly.datos.database.UsuarioDao
import com.example.wahsly.datos.model.Usuario

class UsuarioRepository(private val dao: UsuarioDao) {
    suspend fun buscarUsuario(correo: String): Usuario? = dao.buscarUsuario(correo.trim())

    suspend fun agregarUsuario(nuevo: Usuario): Boolean {
        if (buscarUsuario(nuevo.correo) != null) return false
        return dao.insertar(nuevo) != -1L
    }
}
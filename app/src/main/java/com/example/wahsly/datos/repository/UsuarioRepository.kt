package com.example.wahsly.datos.repository

import com.example.wahsly.datos.database.UsuarioDao
import com.example.wahsly.datos.model.Usuario

class UsuarioRepository(private val dao: UsuarioDao) {
    suspend fun buscarUsuario(correo: String): Usuario? = dao.buscarUsuario(correo.trim())

    suspend fun agregarUsuario(nuevo: Usuario): Boolean {
        if (buscarUsuario(nuevo.correo) != null) return false
        return dao.insertar(nuevo) != -1L
    }

    suspend fun actualizarContrasena(correo: String, nuevaContrasena: String) {
        dao.actualizarContrasena(correo.trim(), nuevaContrasena)
    }

    suspend fun eliminarUsuario(correo: String) {
        dao.eliminarPorCorreo(correo.trim())
    }
}
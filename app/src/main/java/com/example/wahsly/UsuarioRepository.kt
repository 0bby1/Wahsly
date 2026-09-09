package com.example.wahsly

class UsuarioRepository(private val dao: UsuarioDao) {
    suspend fun buscarUsuario(correo: String): Usuario? = dao.buscarUsuario(correo.trim())

    suspend fun agregarUsuario(nuevo: Usuario): Boolean {
        if (buscarUsuario(nuevo.correo) != null) return false
        return dao.insertar(nuevo) != -1L
    }
}
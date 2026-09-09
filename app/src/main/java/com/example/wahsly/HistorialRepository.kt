package com.example.wahsly

class HistorialRepository(private val dao: HistorialDao) {
    val registros = dao.obtenerTodos() // Flow<List<RegistroEscaneo>>

    suspend fun agregarRegistro(registro: RegistroEscaneo) = dao.insertar(registro)
}
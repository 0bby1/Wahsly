package com.example.wahsly.datos.repository

import com.example.wahsly.datos.database.HistorialDao
import com.example.wahsly.datos.model.RegistroEscaneo

class HistorialRepository(private val dao: HistorialDao) {
    val registros = dao.obtenerTodos() // Flow<List<RegistroEscaneo>>

    suspend fun agregarRegistro(registro: RegistroEscaneo) = dao.insertar(registro)
}
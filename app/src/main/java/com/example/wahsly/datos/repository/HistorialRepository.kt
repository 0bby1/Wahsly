package com.example.wahsly.datos.repository

import com.example.wahsly.datos.database.HistorialDao
import com.example.wahsly.datos.model.RegistroEscaneo
import kotlinx.coroutines.flow.Flow

class HistorialRepository(
    private val dao: HistorialDao
) {
    fun registrosPorUsuario(
        correo: String
    ): Flow<List<RegistroEscaneo>> {
        return dao.obtenerPorUsuario(
            correo.trim()
        )
    }

    suspend fun agregarRegistro(
        registro: RegistroEscaneo
    ) {
        dao.insertar(registro)
    }
}
package com.example.wahsly.datos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wahsly.datos.model.RegistroEscaneo
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialDao {
    @Query(" SELECT * FROM historial WHERE correoUsuario = :correo ORDER BY id DESC")
    fun obtenerPorUsuario(correo: String): Flow<List<RegistroEscaneo>>

    @Insert
    suspend fun insertar(registro: RegistroEscaneo)
}
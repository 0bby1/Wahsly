package com.example.wahsly

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialDao {
    @Query("SELECT * FROM historial ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<RegistroEscaneo>>

    @Insert
    suspend fun insertar(registro: RegistroEscaneo)
}
package com.example.wahsly.datos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wahsly.datos.model.RegistroEscaneo
import kotlinx.coroutines.flow.Flow
import androidx.room.Delete

@Dao
interface HistorialDao {
    @Insert
    suspend fun insertar(registro: RegistroEscaneo)

    @Delete
    suspend fun eliminar(registro: RegistroEscaneo)

    @Query(" SELECT * FROM historial WHERE correoUsuario = :correo ORDER BY id DESC")
    fun obtenerPorUsuario(correo: String): Flow<List<RegistroEscaneo>>

    @Query("DELETE FROM historial WHERE correoUsuario = :correo COLLATE NOCASE")
    suspend fun eliminarPorUsuario(correo: String)

}
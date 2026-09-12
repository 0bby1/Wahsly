package com.example.wahsly.datos.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wahsly.datos.model.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE correo = :correo COLLATE NOCASE LIMIT 1")
    suspend fun buscarUsuario(correo: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: Usuario): Long

    @Query("UPDATE usuarios SET contrasena = :nuevaContrasena WHERE correo = :correo COLLATE NOCASE")
    suspend fun actualizarContrasena(correo: String, nuevaContrasena: String)

    @Query("DELETE FROM usuarios WHERE correo = :correo COLLATE NOCASE")
    suspend fun eliminarPorCorreo(correo: String)
}
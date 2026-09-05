package com.example.wahsly

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.runtime.mutableStateListOf

private class WahslyDbHelper(context: Context) :
    SQLiteOpenHelper(context.applicationContext, "wahsly.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                contrasena TEXT NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE historial (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                fecha TEXT NOT NULL,
                informacion TEXT NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL("INSERT INTO usuarios (nombre, apellido, correo, contrasena) VALUES ('César','Avalos','cesar@gmail.com','1234')")
        db.execSQL("INSERT INTO usuarios (nombre, apellido, correo, contrasena) VALUES ('María','Gonzalez','maria@gmail.com','5678')")
        db.execSQL("INSERT INTO usuarios (nombre, apellido, correo, contrasena) VALUES ('Juan','Gabriel','juan@gmail.com','abcd')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS historial")
        onCreate(db)
    }
}

// Usuarios
object base_de_datos_Usuarios {
    private lateinit var helper: WahslyDbHelper

    fun inicializar(context: Context) {
        if (!::helper.isInitialized) {
            helper = WahslyDbHelper(context)
        }
    }

    fun buscarUsuario(correo: String): Usuario? {
        val db = helper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT nombre, apellido, correo, contrasena FROM usuarios WHERE correo = ? COLLATE NOCASE LIMIT 1",
            arrayOf(correo.trim())
        )
        cursor.use {
            if (it.moveToFirst()) {
                return Usuario(it.getString(0), it.getString(1), it.getString(2), it.getString(3))
            }
        }
        return null
    }

    fun agregarUsuario(nuevo: Usuario): Boolean {
        if (buscarUsuario(nuevo.correo) != null) return false
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nuevo.nombre)
            put("apellido", nuevo.apellido)
            put("correo", nuevo.correo)
            put("contrasena", nuevo.contrasena)
        }
        return db.insert("usuarios", null, valores) != -1L
    }
}

// Historial
object BaseDatosHistorial {
    private lateinit var helper: WahslyDbHelper
    val registros = mutableStateListOf<RegistroEscaneo>()

    fun inicializar(context: Context) {
        if (!::helper.isInitialized) {
            helper = WahslyDbHelper(context)
            cargarDesdeDb()
        }
    }

    private fun cargarDesdeDb() {
        registros.clear()
        val db = helper.readableDatabase
        val cursor = db.rawQuery("SELECT nombre, fecha, informacion FROM historial ORDER BY id DESC", null)
        cursor.use {
            while (it.moveToNext()) {
                registros.add(RegistroEscaneo(it.getString(0), it.getString(1), it.getString(2)))
            }
        }
    }

    fun agregarRegistro(registro: RegistroEscaneo) {
        val db = helper.writableDatabase
        val valores = ContentValues().apply {
            put("nombre", registro.nombre)
            put("fecha", registro.fecha)
            put("informacion", registro.informacion)
        }
        db.insert("historial", null, valores)
        registros.add(0, registro)
    }
}
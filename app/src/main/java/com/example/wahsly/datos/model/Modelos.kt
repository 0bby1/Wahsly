package com.example.wahsly.datos.model

data class Usuario(
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contrasena: String
)
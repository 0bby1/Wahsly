package com.example.wahsly

// Clase del Usuario
data class Usuario(
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contrasena: String
)

// Registro de escaneo
data class RegistroEscaneo(
    val nombre: String,
    val fecha: String,
    val informacion: String
)

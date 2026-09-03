package com.example.wahsly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // Primera pantalla que se mostrará
                var pantallaActual by remember {
                    mutableStateOf("SPLASH")
                }

                // Guarda qué usuario inició sesión
                var usuarioActual by remember {
                    mutableStateOf<Usuario?>(null)
                }

                // Esto indica a qué pantalla ir después del video de carga
                var destinoDespuesCarga by remember {
                    mutableStateOf("LOGIN")
                }

                val preferencias = remember {
                    getSharedPreferences(
                        "configuracion_washly",
                        MODE_PRIVATE
                    )
                }

                var modoOscuro by remember {
                    mutableStateOf(
                        preferencias.getBoolean(
                            "modoOscuro",
                            false
                        )
                    )
                }

                when (pantallaActual) {
                    // ---------------- SPLASH ----------------
                    "SPLASH" -> {
                        PantallaSplash(
                            modoOscuro = modoOscuro,
                            onTerminar = {
                                pantallaActual = "LOGIN"
                            }
                        )
                    }

                    // ---------------- VIDEO DE CARGA ----------------
                    "CARGA_VIDEO" -> {
                        PantallaCargaVideo(
                            modoOscuro = modoOscuro,
                            onTerminar = {
                                pantallaActual = destinoDespuesCarga
                            }
                        )
                    }

                    // ---------------- LOGIN ----------------
                    "LOGIN" -> {
                        PantallaInicioSesion(
                            modoOscuro = modoOscuro,
                            onCrearCuenta = {
                                pantallaActual = "REGISTRO"
                            },
                            onLoginExitoso = { usuario ->
                                usuarioActual = usuario
                                destinoDespuesCarga = "INICIO"
                                pantallaActual = "CARGA_VIDEO"
                            }
                        )
                    }

                    // ---------------- REGISTRO ----------------
                    "REGISTRO" -> {
                        PantallaRegistro(
                            modoOscuro = modoOscuro,
                            onRegistroExitoso = { usuario ->
                                usuarioActual = usuario
                                destinoDespuesCarga = "INICIO"
                                pantallaActual = "CARGA_VIDEO"
                            },
                            onVolverLogin = {
                                pantallaActual = "LOGIN"
                            }
                        )
                    }

                    // ---------------- PANTALLA PRINCIPAL ----------------
                    "INICIO" -> {
                        PantallaPrincipal(
                            usuario = usuarioActual,
                            modoOscuro = modoOscuro,
                            onPerfil = {
                                pantallaActual = "PERFIL"
                            },
                            onCerrarSesion = {
                                usuarioActual = null
                                pantallaActual = "LOGIN"
                            }
                        )
                    }

                    // ---------------- PERFIL ----------------
                    "PERFIL" -> {
                        PantallaPerfil(
                            usuario = usuarioActual,
                            modoOscuro = modoOscuro,
                            onConfiguracion = {
                                pantallaActual = "CONFIGURACION"
                            },
                            onVolver = {
                                pantallaActual = "INICIO"
                            },
                            onCerrarSesion = {
                                usuarioActual = null
                                pantallaActual = "LOGIN"
                            }
                        )
                    }

                    // ---------------- CONFIGURACIÓN ----------------
                    "CONFIGURACION" -> {
                        PantallaConfiguracion(
                            modoOscuro = modoOscuro,
                            onCambiarModoOscuro = { nuevoValor ->
                                modoOscuro = nuevoValor
                                preferencias
                                    .edit()
                                    .putBoolean(
                                        "modoOscuro",
                                        nuevoValor
                                    )
                                    .apply()
                            },
                            onVolver = {
                                pantallaActual = "PERFIL"
                            }
                        )
                    }
                }
            }
        }
    }
}

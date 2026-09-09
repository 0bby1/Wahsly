package com.example.wahsly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)


        setContent {
            MaterialTheme {
                // NAVCONTROLLER
                val navController = rememberNavController()

                // ESTADOS DE LA APP
                var usuarioActual by remember {
                    mutableStateOf<Usuario?>(null)
                }

                // Controla Inicio y Perfil sin destruir
                // NavegacionPrincipal.
                var seccionActual by remember {
                    mutableStateOf("INICIO")
                }

                // PREFERENCIAS
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

                // NAVHOST
                NavHost(
                    navController = navController,
                    startDestination = "SPLASH",
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }
                ) {
                    // SPLASH
                    composable("SPLASH") {
                        PantallaSplash(
                            modoOscuro = modoOscuro,
                            onTerminar = {
                                navController.navigate("LOGIN") {
                                    popUpTo("SPLASH") {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    // LOGIN
                    composable("LOGIN") {
                        PantallaInicioSesion(
                            modoOscuro = modoOscuro,
                            onCrearCuenta = {
                                navController.navigate(
                                    "REGISTRO"
                                )
                            },
                            onLoginExitoso = { usuario ->
                                usuarioActual = usuario
                                // Siempre iniciamos en Inicio
                                seccionActual = "INICIO"
                                navController.navigate(
                                    "CARGA_VIDEO"
                                )
                            }
                        )
                    }

                    // REGISTRO
                    composable("REGISTRO") {
                        PantallaRegistro(
                            modoOscuro = modoOscuro,
                            onRegistroExitoso = { usuario ->
                                usuarioActual = usuario
                                seccionActual = "INICIO"
                                navController.navigate(
                                    "CARGA_VIDEO"
                                ) {
                                    popUpTo("REGISTRO") {
                                        inclusive = true
                                    }
                                }
                            },
                            onVolverLogin = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // VIDEO DE CARGA
                    composable("CARGA_VIDEO") {
                        PantallaCargaVideo(
                            modoOscuro = modoOscuro,
                            onTerminar = {
                                navController.navigate(
                                    "PRINCIPAL"
                                ) {
                                    popUpTo("LOGIN") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    // PRINCIPAL
                    composable("PRINCIPAL") {
                        NavegacionPrincipal(
                            seccionActual = seccionActual,
                            usuario = usuarioActual,
                            modoOscuro = modoOscuro,
                            onCambiarSeccion = { nuevaSeccion ->
                                seccionActual = nuevaSeccion
                            },
                            onConfiguracion = {
                                navController.navigate(
                                    "CONFIGURACION"
                                )
                            },
                            onCerrarSesion = {
                                usuarioActual = null
                                seccionActual = "INICIO"
                                navController.navigate(
                                    "LOGIN"
                                ) {
                                    popUpTo("PRINCIPAL") {
                                        inclusive = true
                                    }

                                    launchSingleTop = true
                                }
                            }
                        )
                    }

                    // CONFIGURACION
                    composable("CONFIGURACION") {
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
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
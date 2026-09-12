package com.example.wahsly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wahsly.animaciones.AnimacionTransicionAgua
import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.navegacion.NavegacionPrincipal
import com.example.wahsly.ui.pantallas.PantallaCargaVideo
import com.example.wahsly.ui.pantallas.PantallaConfiguracion
import com.example.wahsly.ui.pantallas.PantallaInicioSesion
import com.example.wahsly.ui.pantallas.PantallaRegistro
import com.example.wahsly.ui.pantallas.PantallaSplash

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {

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

            // G TAMAÑO DE TEXTO
            var tamanoTexto by remember {
                mutableStateOf(
                    preferencias.getFloat(
                        "tamanoTexto",
                        1.0f
                    )
                )
            }

            // G ESCALA
            val densidad = LocalDensity.current

            val densidadTexto = Density(
                density = densidad.density,
                fontScale = densidad.fontScale * tamanoTexto
            )

            CompositionLocalProvider(
                LocalDensity provides densidadTexto
            ) {

                MaterialTheme {

                    // NAVCONTROLLER
                    val navController = rememberNavController()

                    // ESTADOS DE LA APP
                    var usuarioActual by remember {
                        mutableStateOf<Usuario?>(null)
                    }

                    var seccionActual by remember {
                        mutableStateOf("INICIO")
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
                                    seccionActual = "INICIO"

                                    navController.navigate(
                                        "TRANSICION_AGUA"
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
                                        "TRANSICION_AGUA"
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

                        // TRANSICION DE AGUA
                        composable("TRANSICION_AGUA") {

                            AnimacionTransicionAgua(
                                modoOscuro = modoOscuro,

                                onTerminar = {

                                    navController.navigate(
                                        "CARGA_VIDEO"
                                    ) {

                                        popUpTo("TRANSICION_AGUA") {
                                            inclusive = true
                                        }

                                        launchSingleTop = true
                                    }
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
                                usuario = usuarioActual,

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

                                // G TAMAÑO
                                tamanoTexto = tamanoTexto,

                                onCambiarTamanoTexto = { nuevoTamano ->

                                    tamanoTexto = nuevoTamano

                                    preferencias
                                        .edit()
                                        .putFloat(
                                            "tamanoTexto",
                                            nuevoTamano
                                        )
                                        .apply()
                                },

                                onVolver = {

                                    navController.popBackStack()
                                },

                                onCuentaEliminada = {

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
                    }
                }
            }
        }
    }
}
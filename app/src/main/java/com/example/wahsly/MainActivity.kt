package com.example.wahsly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.wahsly.utilidades.FotoPerfilStorage
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import android.content.pm.ActivityInfo
import com.example.wahsly.ui.pantallas.PantallaAyudaSoporte
import com.example.wahsly.datos.repository.FirebaseUsuarioRepository
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val esTablet = resources.configuration.smallestScreenWidthDp >= 600
        requestedOrientation =
            if (esTablet) {
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            } else {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
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

            CompositionLocalProvider(LocalDensity provides densidadTexto) {
                MaterialTheme {
                    // NAVCONTROLLER
                    val navController = rememberNavController()

                    val firebaseRepository = remember {
                        FirebaseUsuarioRepository()
                    }

                    var usuarioActual by remember {
                        mutableStateOf<Usuario?>(null)
                    }
                    var cargandoUsuarioGuardado by remember {
                        mutableStateOf(true)
                    }
                    var seccionActual by rememberSaveable {
                        mutableStateOf("INICIO")
                    }

                    // RESTAURAR USUARIO DESPUÉS DE ROTAR

                    LaunchedEffect(Unit) {

                        cargandoUsuarioGuardado = true

                        try {

                            val firebaseUser =
                                firebaseRepository.obtenerUsuarioActual()

                            usuarioActual =
                                if (firebaseUser != null) {

                                    val perfil =
                                        firebaseRepository.obtenerUsuario(
                                            firebaseUser.uid
                                        )

                                    Usuario(
                                        nombre = perfil.nombre,
                                        apellido = perfil.apellido,
                                        correo = perfil.correo,
                                        contrasena = ""
                                    )

                                } else {

                                    null

                                }

                        } catch (e: Exception) {

                            usuarioActual = null

                        } finally {

                            cargandoUsuarioGuardado = false

                        }

                    }


                    // NAVHOST
                    if (!cargandoUsuarioGuardado) {
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
                                val scope = rememberCoroutineScope()
                                PantallaSplash(
                                    modoOscuro = modoOscuro,

                                    onTerminar = {

                                        if (usuarioActual != null) {

                                            seccionActual = "INICIO"

                                            navController.navigate("PRINCIPAL") {

                                                popUpTo("SPLASH") {
                                                    inclusive = true
                                                }

                                                launchSingleTop = true

                                            }

                                        } else {

                                            navController.navigate("LOGIN") {

                                                popUpTo("SPLASH") {
                                                    inclusive = true
                                                }

                                                launchSingleTop = true

                                            }

                                        }

                                    }

                                )
                            }

                            // LOGIN
                            composable("LOGIN") {
                                PantallaInicioSesion(
                                    modoOscuro = modoOscuro,
                                    windowSizeClass = windowSizeClass,
                                    onCrearCuenta = {
                                        navController.navigate("REGISTRO")
                                    },

                                    onLoginExitoso = { usuario ->

                                        usuarioActual = usuario

                                        seccionActual = "INICIO"

                                        navController.navigate(
                                            "TRANSICION_AGUA"
                                        ) {

                                            popUpTo("LOGIN") {
                                                inclusive = true
                                            }

                                            launchSingleTop = true

                                        }

                                    }

                                )
                            }


                            // REGISTRO
                            composable("REGISTRO") {

                                PantallaRegistro(
                                    modoOscuro = modoOscuro,
                                    windowSizeClass = windowSizeClass,

                                    onRegistroExitoso = { usuario ->

                                        usuarioActual = usuario

                                        seccionActual = "INICIO"

                                        navController.navigate("TRANSICION_AGUA") {

                                            popUpTo("REGISTRO") {
                                                inclusive = true
                                            }

                                            launchSingleTop = true
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
                                            popUpTo("TRANSICION_AGUA") { inclusive = true }
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
                                            popUpTo("LOGIN") { inclusive = true }
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
                                    windowSizeClass = windowSizeClass,
                                    onCambiarSeccion = { nuevaSeccion ->
                                        seccionActual = nuevaSeccion
                                    },
                                    onConfiguracion = { navController.navigate("CONFIGURACION") },

                                    onAyudaSoporte = {
                                        navController.navigate("AYUDA_SOPORTE") {
                                            launchSingleTop = true
                                        }
                                    },



                                    onCerrarSesion = {

                                        // Cerrar sesión en Firebase
                                        FirebaseAuth.getInstance().signOut()

                                        // Eliminar la sesión antigua de Room
                                        preferencias
                                            .edit()
                                            .remove("correo_usuario_activo")
                                            .apply()

                                        // Limpiar el usuario activo
                                        usuarioActual = null

                                        // Restaurar sección inicial
                                        seccionActual = "INICIO"

                                        // Regresar al inicio de sesión
                                        navController.navigate("LOGIN") {

                                            popUpTo(navController.graph.id) {
                                                inclusive = true
                                            }

                                            launchSingleTop = true

                                        }

                                    }

                                )
                            }


                            // AYUDA Y SOPORTE
                            composable("AYUDA_SOPORTE") {

                                PantallaAyudaSoporte(
                                    usuario = usuarioActual,
                                    modoOscuro = modoOscuro,
                                    onVolver = {
                                        navController.popBackStack()
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
                                    onVolver = { navController.popBackStack() },

                                    onCuentaEliminada = {

                                        // Cerrar cualquier sesión que pudiera permanecer activa
                                        FirebaseAuth.getInstance().signOut()

                                        // Borrar referencia de la sesión anterior
                                        preferencias
                                            .edit()
                                            .remove("correo_usuario_activo")
                                            .apply()

                                        // Eliminar foto de perfil guardada localmente
                                        usuarioActual?.correo?.let { correo ->
                                            FotoPerfilStorage.eliminarFoto(
                                                this@MainActivity,
                                                correo
                                            )
                                        }

                                        // Limpiar datos del usuario en la aplicación
                                        usuarioActual = null
                                        seccionActual = "INICIO"

                                        // Regresar al login sin conservar pantallas anteriores
                                        navController.navigate("LOGIN") {

                                            popUpTo(navController.graph.id) {
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
}
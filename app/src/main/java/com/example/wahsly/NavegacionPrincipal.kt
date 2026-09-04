package com.example.wahsly

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun NavegacionPrincipal(
    seccionActual: String,
    usuario: Usuario?,
    modoOscuro: Boolean,
    onCambiarSeccion: (String) -> Unit,
    onConfiguracion: () -> Unit,
    onCerrarSesion: () -> Unit
) {

    val context = LocalContext.current

    var primeraCarga by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        primeraCarga = false
    }

    val animarCambioCabecera = !primeraCarga

    val seleccionado = when (seccionActual) {
        "INICIO" -> 0
        "PERFIL" -> 2
        else -> 0
    }

    val colorFondo =
        if (modoOscuro) FondoOscuro else FondoClaro

    Scaffold(
        containerColor = colorFondo,

        bottomBar = {

            BarraInferiorAnimada(
                seleccionado = seleccionado,
                modoOscuro = modoOscuro,

                onInicio = {
                    if (seccionActual != "INICIO") {
                        onCambiarSeccion("INICIO")
                    }
                },

                onEscanear = {
                    Toast.makeText(
                        context,
                        "Escáner próximamente",
                        Toast.LENGTH_SHORT
                    ).show()
                },

                onCuenta = {
                    if (seccionActual != "PERFIL") {
                        onCambiarSeccion("PERFIL")
                    }
                }
            )
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = padding.calculateBottomPadding()
                )
        ) {

            when (seccionActual) {

                "INICIO" -> {

                    PantallaPrincipal(
                        usuario = usuario,
                        modoOscuro = modoOscuro,
                        mostrarBarraInferior = false,
                        animarCabecera = animarCambioCabecera,

                        onPerfil = {
                            onCambiarSeccion("PERFIL")
                        },

                        onCerrarSesion = onCerrarSesion
                    )
                }

                "PERFIL" -> {

                    PantallaPerfil(
                        usuario = usuario,
                        modoOscuro = modoOscuro,
                        mostrarBarraInferior = false,
                        animarCabecera = animarCambioCabecera,

                        onConfiguracion = onConfiguracion,

                        onVolver = {
                            onCambiarSeccion("INICIO")
                        },

                        onCerrarSesion = onCerrarSesion
                    )
                }
            }
        }
    }
}
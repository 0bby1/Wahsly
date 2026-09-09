package com.example.wahsly.navegacion

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.wahsly.ui.componentes.BarraInferiorAnimada
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.pantallas.PantallaPerfil
import com.example.wahsly.ui.pantallas.PantallaPrincipal
import com.example.wahsly.datos.model.Usuario
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

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
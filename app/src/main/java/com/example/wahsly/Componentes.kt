package com.example.wahsly

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// BOTÓN DEGRADADO
@Composable
fun BotonDegradado(
    texto: String,
    modoOscuro: Boolean,
    onClick: () -> Unit
) {

    val coloresBoton = if (modoOscuro) {
        listOf(
            RosaOscuro,
            CremaOscuro
        )
    } else {
        listOf(
            Color(0xFF7B92A4),
            Color(0xFF334055)
        )
    }

    val colorTexto = if (modoOscuro) {
        FondoOscuro
    } else {
        Color.White
    }

    Box(
        modifier = Modifier
            .width(255.dp)
            .height(61.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = coloresBoton
                )
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = texto,
            color = colorTexto,
            fontSize = 23.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


// BOTÓN CREAR CUENTA
@Composable
fun BotonCrearCuenta(
    onClick: () -> Unit,
    modoOscuro: Boolean
) {

    val coloresBoton = if (modoOscuro) {
        listOf(
            RosaOscuro,
            CremaOscuro
        )
    } else {
        listOf(
            Color(0xFF7B92A4),
            Color(0xFF334055)
        )
    }

    val colorTexto = if (modoOscuro) {
        FondoOscuro
    } else {
        Color.White
    }

    Box(
        modifier = Modifier
            .width(230.dp)
            .height(61.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = coloresBoton
                )
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Crear Cuenta",
            color = colorTexto,
            fontSize = 23.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


// BARRA INFERIOR ANIMADA
@Composable
fun BarraInferiorAnimada(
    seleccionado: Int,
    modoOscuro: Boolean,
    onInicio: () -> Unit,
    onEscanear: () -> Unit,
    onCuenta: () -> Unit
) {

    // Evita valores menores de 0 o mayores de 2
    val opcionSeleccionada = seleccionado.coerceIn(0, 2)

    val colorFondo = if (modoOscuro) {
        FondoOscuro
    } else {
        FondoClaro
    }

    val colorBarra = if (modoOscuro) {
        RosaOscuro
    } else {
        AzulPrincipalClaro
    }

    val colorPastilla = if (modoOscuro) {
        FondoOscuro
    } else {
        CremaOscuro
    }

    // Color de la opción seleccionada
    val colorSeleccionado = if (modoOscuro) {
        CremaOscuro
    } else {
        AzulPrincipalClaro
    }

    // Color de las opciones NO seleccionadas
    val colorInactivo = if (modoOscuro) {
        FondoOscuro
    } else {
        CremaOscuro
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorFondo)
            .padding(
                start = 10.dp,
                end = 10.dp,
                bottom = 8.dp
            )
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp),
            shape = RoundedCornerShape(45.dp),
            color = colorBarra,
            shadowElevation = 5.dp
        ) {

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {

                val anchoElemento = maxWidth / 3


                // POSICIÓN DE LA PASTILLA
                val posicionPastilla by animateDpAsState(
                    targetValue = anchoElemento * opcionSeleccionada.toFloat(),

                    animationSpec = spring(
                        dampingRatio = 0.70f,
                        stiffness = 300f
                    ),

                    label = "MovimientoPastilla"
                )


                // PASTILLA ANIMADA
                Box(
                    modifier = Modifier
                        .offset(x = posicionPastilla)
                        .width(anchoElemento)
                        .fillMaxHeight()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(38.dp))
                        .background(colorPastilla)
                )


                // OPCIONES
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    // INICIO
                    Column(
                        modifier = Modifier
                            .width(anchoElemento)
                            .clickable {
                                onInicio()
                            },

                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",

                            tint = if (opcionSeleccionada == 0) {
                                colorSeleccionado
                            } else {
                                colorInactivo
                            },

                            modifier = Modifier.size(34.dp)
                        )

                        Text(
                            text = "Inicio",
                            fontSize = 12.sp,

                            color = if (opcionSeleccionada == 0) {
                                colorSeleccionado
                            } else {
                                colorInactivo
                            }
                        )
                    }


                    // ESCANEAR
                    Column(
                        modifier = Modifier
                            .width(anchoElemento)
                            .clickable {
                                onEscanear()
                            },

                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Escanear",

                            tint = if (opcionSeleccionada == 1) {
                                colorSeleccionado
                            } else {
                                colorInactivo
                            },

                            modifier = Modifier.size(38.dp)
                        )

                        Text(
                            text = "Escanear",
                            fontSize = 12.sp,

                            color = if (opcionSeleccionada == 1) {
                                colorSeleccionado
                            } else {
                                colorInactivo
                            }
                        )
                    }


                    // CUENTA
                    Column(
                        modifier = Modifier
                            .width(anchoElemento)
                            .clickable {
                                onCuenta()
                            },

                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Cuenta",

                            tint = if (opcionSeleccionada == 2) {
                                colorSeleccionado
                            } else {
                                colorInactivo
                            },

                            modifier = Modifier.size(30.dp)
                        )

                        Text(
                            text = "Cuenta",
                            fontSize = 12.sp,

                            color = if (opcionSeleccionada == 2) {
                                colorSeleccionado
                            } else {
                                colorInactivo
                            }
                        )
                    }
                }
            }
        }
    }
}
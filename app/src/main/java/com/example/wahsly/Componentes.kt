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

    val coloresBoton =
        if (modoOscuro) {
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

    val colorTexto =
        if (modoOscuro)
            FondoOscuro
        else
            Color.White


    val forma = RoundedCornerShape(50.dp)


    Box(
        modifier = Modifier
            .width(255.dp)
            .height(61.dp)
            .clip(forma)
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
    onClick: () -> Unit
) {

    val forma = RoundedCornerShape(50.dp)

    Box(
        modifier = Modifier
            .width(230.dp)
            .height(61.dp)
            .clip(forma)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFD29FAD),
                        Color(0xFFD9D9D9)
                    )
                )
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Crear Cuenta",
            color = Color(0xFF334055),
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

    val colorFondo =
        if (modoOscuro)
            FondoOscuro
        else
            FondoClaro


    val colorBarra =
        if (modoOscuro)
            RosaOscuro
        else
            AzulPrincipalClaro


    val colorPastilla =
        if (modoOscuro)
            CremaOscuro
        else
            Color.White


    val colorIconoInactivo =
        if (modoOscuro)
            CremaOscuro
        else
            IconoSecundarioClaro


    val colorTextoInactivo =
        if (modoOscuro)
            CremaOscuro
        else
            Color.White


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


                // POSICIÓN DE LA PASTILLA BLANCA
                val posicionPastilla by animateDpAsState(
                    targetValue =
                        anchoElemento * seleccionado.toFloat(),

                    animationSpec = spring(
                        dampingRatio = 0.70f,
                        stiffness = 300f
                    ),

                    label = "MovimientoPastilla"
                )



                // PASTILLA BLANCA ANIMADA


                Box(
                    modifier = Modifier
                        .offset(
                            x = posicionPastilla
                        )
                        .width(anchoElemento)
                        .fillMaxHeight()
                        .padding(vertical = 6.dp)
                        .clip(
                            RoundedCornerShape(38.dp)
                        )
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

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Home,

                            contentDescription =
                                "Inicio",

                            tint =
                                if (seleccionado == 0)
                                    colorBarra
                                else
                                    colorIconoInactivo,

                            modifier =
                                Modifier.size(34.dp)
                        )


                        Text(
                            text = "Inicio",

                            fontSize = 12.sp,

                            color =
                                if (seleccionado == 0)
                                    colorBarra
                                else
                                    colorTextoInactivo
                        )
                    }


                    // ESCANEAR

                    Column(
                        modifier = Modifier
                            .width(anchoElemento)
                            .clickable {
                                onEscanear()
                            },

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Add,

                            contentDescription =
                                "Escanear",

                            tint =
                                if (seleccionado == 1)
                                    colorBarra
                                else
                                    colorIconoInactivo,

                            modifier =
                                Modifier.size(38.dp)
                        )


                        Text(
                            text = "Escanear",

                            fontSize = 12.sp,

                            color =
                                if (seleccionado == 1)
                                    colorBarra
                                else
                                    colorTextoInactivo
                        )
                    }


                    // CUENTA

                    Column(
                        modifier = Modifier
                            .width(anchoElemento)
                            .clickable {
                                onCuenta()
                            },

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Person,

                            contentDescription =
                                "Cuenta",

                            tint =
                                if (seleccionado == 2)
                                    colorBarra
                                else
                                    colorIconoInactivo,

                            modifier =
                                Modifier.size(30.dp)
                        )


                        Text(
                            text = "Cuenta",

                            fontSize = 12.sp,

                            color =
                                if (seleccionado == 2)
                                    colorBarra
                                else
                                    colorTextoInactivo
                        )
                    }
                }
            }
        }
    }
}
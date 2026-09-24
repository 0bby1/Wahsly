package com.example.wahsly.ui.componentes

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wahsly.ui.theme.AzulPrincipalClaro
import com.example.wahsly.ui.theme.CremaOscuro
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.theme.RosaOscuro
import androidx.compose.runtime.getValue
import com.example.wahsly.utilidades.TipoPantalla

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
            .width(255.dp)
            .height(61.dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(50.dp))
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
    tipoPantalla: TipoPantalla,
    onInicio: () -> Unit,
    onEscanear: () -> Unit,
    onCuenta: () -> Unit
) {

    val opcionSeleccionada = seleccionado.coerceIn(0, 2)

    // =====================================================
    // COLORES
    // =====================================================

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

    val colorSeleccionado = if (modoOscuro) {
        CremaOscuro
    } else {
        AzulPrincipalClaro
    }

    val colorInactivo = if (modoOscuro) {
        FondoOscuro
    } else {
        CremaOscuro
    }


    // =====================================================
    // TAMAÑOS RESPONSIVOS
    // =====================================================

    val fraccionAnchoBarra = when (tipoPantalla) {

        TipoPantalla.TELEFONO ->
            0.96f

        TipoPantalla.TABLET_VERTICAL ->
            0.82f

        TipoPantalla.TABLET_HORIZONTAL ->
            0.62f
    }

    val alturaBarra = when (tipoPantalla) {

        TipoPantalla.TELEFONO ->
            78.dp

        TipoPantalla.TABLET_VERTICAL ->
            82.dp

        TipoPantalla.TABLET_HORIZONTAL ->
            80.dp
    }

    val anchoPastilla = when (tipoPantalla) {

        TipoPantalla.TELEFONO ->
            105.dp

        TipoPantalla.TABLET_VERTICAL ->
            118.dp

        TipoPantalla.TABLET_HORIZONTAL ->
            100.dp
    }

    val tamanoIconoInicio = when (tipoPantalla) {

        TipoPantalla.TELEFONO ->
            34.dp

        TipoPantalla.TABLET_VERTICAL ->
            38.dp

        TipoPantalla.TABLET_HORIZONTAL ->
            42.dp
    }

    val tamanoIconoEscanear = when (tipoPantalla) {

        TipoPantalla.TELEFONO ->
            38.dp

        TipoPantalla.TABLET_VERTICAL ->
            42.dp

        TipoPantalla.TABLET_HORIZONTAL ->
            48.dp
    }

    val tamanoIconoCuenta = when (tipoPantalla) {

        TipoPantalla.TELEFONO ->
            30.dp

        TipoPantalla.TABLET_VERTICAL ->
            34.dp

        TipoPantalla.TABLET_HORIZONTAL ->
            38.dp
    }

    // En horizontal tus capturas muestran solamente iconos.
    val mostrarTexto = true


    // =====================================================
    // BARRA
    // =====================================================

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = when (tipoPantalla) {
                    TipoPantalla.TELEFONO -> 8.dp
                    TipoPantalla.TABLET_VERTICAL -> 12.dp
                    TipoPantalla.TABLET_HORIZONTAL -> 10.dp
                }
            ),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth(fraccionAnchoBarra)
                .height(alturaBarra),

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


                // =================================================
                // POSICIÓN DE LA PASTILLA
                // =================================================

                val posicionPastilla by animateDpAsState(

                    targetValue =
                        (anchoElemento * opcionSeleccionada.toFloat()) +
                                ((anchoElemento - anchoPastilla) / 2),

                    // MISMA ANIMACIÓN QUE YA TENÍAS
                    animationSpec = spring(
                        dampingRatio = 0.70f,
                        stiffness = 300f
                    ),

                    label = "MovimientoPastilla"
                )


                // =================================================
                // PASTILLA SELECCIONADA
                // =================================================

                Box(
                    modifier = Modifier
                        .offset(x = posicionPastilla)
                        .width(anchoPastilla)
                        .fillMaxHeight()
                        .padding(vertical = 6.dp)
                        .clip(
                            RoundedCornerShape(30.dp)
                        )
                        .background(colorPastilla)
                )


                // =================================================
                // OPCIONES
                // =================================================

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    // =========================
                    // INICIO
                    // =========================

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
                            imageVector = Icons.Default.Home,
                            contentDescription = "Inicio",

                            tint =
                                if (opcionSeleccionada == 0) {
                                    colorSeleccionado
                                } else {
                                    colorInactivo
                                },

                            modifier = Modifier.size(
                                tamanoIconoInicio
                            )
                        )

                        if (mostrarTexto) {

                            Text(
                                text = "Inicio",
                                fontSize = 12.sp,

                                color =
                                    if (opcionSeleccionada == 0) {
                                        colorSeleccionado
                                    } else {
                                        colorInactivo
                                    }
                            )
                        }
                    }


                    // =========================
                    // ESCANEAR
                    // =========================

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
                            imageVector = Icons.Default.Add,
                            contentDescription = "Escanear",

                            tint =
                                if (opcionSeleccionada == 1) {
                                    colorSeleccionado
                                } else {
                                    colorInactivo
                                },

                            modifier = Modifier.size(
                                tamanoIconoEscanear
                            )
                        )

                        if (mostrarTexto) {

                            Text(
                                text = "Escanear",
                                fontSize = 12.sp,

                                color =
                                    if (opcionSeleccionada == 1) {
                                        colorSeleccionado
                                    } else {
                                        colorInactivo
                                    }
                            )
                        }
                    }


                    // =========================
                    // CUENTA
                    // =========================

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
                            imageVector = Icons.Default.Person,
                            contentDescription = "Cuenta",

                            tint =
                                if (opcionSeleccionada == 2) {
                                    colorSeleccionado
                                } else {
                                    colorInactivo
                                },

                            modifier = Modifier.size(
                                tamanoIconoCuenta
                            )
                        )

                        if (mostrarTexto) {

                            Text(
                                text = "Cuenta",
                                fontSize = 12.sp,

                                color =
                                    if (opcionSeleccionada == 2) {
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
}
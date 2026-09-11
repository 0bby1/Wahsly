package com.example.wahsly.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.wahsly.ui.theme.AzulPrincipalClaro
import com.example.wahsly.ui.theme.AzulTextoClaro
import com.example.wahsly.ui.theme.CremaOscuro
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.theme.RosaOscuro
import com.example.wahsly.ui.theme.TarjetaPerfilOscuro

@Composable
fun PantallaConfiguracion(
    modoOscuro: Boolean,
    onCambiarModoOscuro: (Boolean) -> Unit,
    tamanoTexto: Float,
    onCambiarTamanoTexto: (Float) -> Unit,
    onVolver: () -> Unit
) {

    // COLORES
    val colorFondo =
        if (modoOscuro) FondoOscuro else FondoClaro

    val colorTexto =
        if (modoOscuro) CremaOscuro else AzulTextoClaro

    val colorTarjeta =
        if (modoOscuro) TarjetaPerfilOscuro else Color.White

    val colorAcento =
        if (modoOscuro) RosaOscuro else AzulPrincipalClaro


    // G DIALOGO
    var mostrarDialogo by remember {
        mutableStateOf(false)
    }

    var seleccion by remember {
        mutableStateOf(tamanoTexto)
    }


    // G NOMBRE
    val nombreTamano = when {
        tamanoTexto < 0.95f -> "Pequeño"
        tamanoTexto > 1.10f -> "Grande"
        else -> "Mediano"
    }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(24.dp)
    ) {

        val (
            botonVolver,
            titulo,
            tarjetaModoOscuro,
            tituloAccesibilidad,
            tarjetaTamanoTexto
        ) = createRefs()


        // BOTÓN VOLVER
        IconButton(
            onClick = onVolver,
            modifier = Modifier.constrainAs(botonVolver) {
                top.linkTo(parent.top, margin = 30.dp)
                start.linkTo(parent.start)
            }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = colorTexto
            )
        }


        // TÍTULO
        Text(
            text = "Configuración",
            color = colorTexto,
            fontSize = 26.sp,
            modifier = Modifier.constrainAs(titulo) {
                top.linkTo(botonVolver.top)
                bottom.linkTo(botonVolver.bottom)
                start.linkTo(
                    botonVolver.end,
                    margin = 10.dp
                )
            }
        )


        // TARJETA MODO OSCURO
        Surface(
            modifier = Modifier
                .constrainAs(tarjetaModoOscuro) {
                    top.linkTo(
                        botonVolver.bottom,
                        margin = 40.dp
                    )

                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                },

            shape = RoundedCornerShape(18.dp),
            color = colorTarjeta,
            shadowElevation = 4.dp
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 18.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Modo oscuro",
                    color = colorTexto,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = modoOscuro,

                    onCheckedChange = { activado ->
                        onCambiarModoOscuro(activado)
                    },

                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CremaOscuro,
                        checkedTrackColor = RosaOscuro,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFD9D9D9)
                    )
                )
            }
        }


        // G ACCESIBILIDAD
        Text(
            text = "Accesibilidad",
            color = colorTexto,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,

            modifier = Modifier.constrainAs(
                tituloAccesibilidad
            ) {
                top.linkTo(
                    tarjetaModoOscuro.bottom,
                    margin = 30.dp
                )

                start.linkTo(parent.start)
            }
        )


        // G TAMAÑO DE TEXTO
        Surface(
            modifier = Modifier
                .constrainAs(tarjetaTamanoTexto) {
                    top.linkTo(
                        tituloAccesibilidad.bottom,
                        margin = 12.dp
                    )

                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                }
                .clickable {
                    seleccion = tamanoTexto
                    mostrarDialogo = true
                },

            shape = RoundedCornerShape(18.dp),
            color = colorTarjeta,
            shadowElevation = 4.dp
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 18.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Tamaño del texto",
                        color = colorTexto,
                        fontSize = 18.sp
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = nombreTamano,
                        color = colorTexto.copy(
                            alpha = 0.65f
                        ),
                        fontSize = 14.sp
                    )
                }

                Icon(
                    imageVector =
                        Icons.Default.KeyboardArrowRight,

                    contentDescription =
                        "Tamaño del texto",

                    tint = colorTexto
                )
            }
        }
    }


    // G DIALOGO
    if (mostrarDialogo) {

        val densidadActual =
            LocalDensity.current

        val ajuste =
            seleccion / tamanoTexto

        val densidadPrevia = Density(
            density = densidadActual.density,
            fontScale =
                densidadActual.fontScale * ajuste
        )

        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
            },

            containerColor = colorTarjeta,

            title = {
                Text(
                    text = "Tamaño del texto",
                    color = colorTexto
                )
            },

            text = {

                Column {

                    Text(
                        text = "Vista previa",
                        color = colorTexto.copy(
                            alpha = 0.65f
                        ),
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    CompositionLocalProvider(
                        LocalDensity provides densidadPrevia
                    ) {

                        Text(
                            text = "Así se verá el texto dentro de Wahsly",
                            color = colorTexto,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )


                    OpcionTamano(
                        texto = "Pequeño",
                        escala = 0.85f,
                        seleccion = seleccion,
                        colorTexto = colorTexto,
                        colorAcento = colorAcento
                    ) {
                        seleccion = 0.85f
                    }


                    OpcionTamano(
                        texto = "Mediano",
                        escala = 1.0f,
                        seleccion = seleccion,
                        colorTexto = colorTexto,
                        colorAcento = colorAcento
                    ) {
                        seleccion = 1.0f
                    }


                    OpcionTamano(
                        texto = "Grande",
                        escala = 1.25f,
                        seleccion = seleccion,
                        colorTexto = colorTexto,
                        colorAcento = colorAcento
                    ) {
                        seleccion = 1.25f
                    }
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        onCambiarTamanoTexto(seleccion)
                        mostrarDialogo = false
                    }
                ) {
                    Text(
                        text = "Aceptar",
                        color = colorAcento
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        seleccion = tamanoTexto
                        mostrarDialogo = false
                    }
                ) {
                    Text(
                        text = "Cancelar",
                        color = colorTexto
                    )
                }
            }
        )
    }
}


// G OPCION
@Composable
private fun OpcionTamano(
    texto: String,
    escala: Float,
    seleccion: Float,
    colorTexto: Color,
    colorAcento: Color,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 6.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        RadioButton(
            selected = seleccion == escala,

            onClick = {
                onClick()
            },

            colors = RadioButtonDefaults.colors(
                selectedColor = colorAcento,
                unselectedColor =
                    colorTexto.copy(alpha = 0.60f)
            )
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = texto,
            color = colorTexto,
            fontSize = 16.sp
        )
    }
}
package com.example.wahsly.animaciones

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.min

@Composable
fun AnimacionTransicionAgua(
    modoOscuro: Boolean,
    onTerminar: () -> Unit
) {

    val escalaBurbuja = remember {
        Animatable(0f)
    }

    val nivelAgua = remember {
        Animatable(0f)
    }

    val expansion = remember {
        Animatable(0f)
    }

    // Movimiento continuo de la ola
    val transicion = rememberInfiniteTransition(
        label = "MovimientoAgua"
    )

    val movimientoOla by transicion.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 700,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "Ola"
    )

    // Movimiento de burbujitas
    val movimientoBurbujas by transicion.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "Burbujas"
    )

    LaunchedEffect(Unit) {

        // Aparece la burbuja
        escalaBurbuja.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 350,
                easing = FastOutSlowInEasing
            )
        )

        // Empieza a llenarse
        nivelAgua.animateTo(
            targetValue = 0.55f,
            animationSpec = tween(
                durationMillis = 450,
                easing = FastOutSlowInEasing
            )
        )

        // La burbuja crece y el agua termina de subir
        coroutineScope {

            launch {
                expansion.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 650,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            launch {
                nivelAgua.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 650,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        }

        delay(150)

        onTerminar()
    }

    val colorFondo =
        if (modoOscuro) FondoOscuro
        else FondoClaro

    val azulAgua =
        if (modoOscuro) {
            Color(0xFF6F879E)
        } else {
            Color(0xFF7FA8C9)
        }

    val azulBorde =
        if (modoOscuro) {
            Color(0xFFAEC7DA)
        } else {
            Color(0xFF34536F)
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val ancho = size.width
            val alto = size.height

            val centro = Offset(
                x = ancho / 2f,
                y = alto / 2f
            )

            val tamanoBase =
                min(ancho, alto) * 0.17f

            val radioInicial =
                tamanoBase * escalaBurbuja.value

            val radioFinal =
                hypot(ancho, alto)

            val radioActual =
                radioInicial +
                        ((radioFinal - radioInicial) * expansion.value)

            // BURBUJITAS PEQUEÑAS
            if (expansion.value < 0.75f) {

                val desplazamiento =
                    movimientoBurbujas * 180f

                val burbujas = listOf(
                    Triple(0.18f, 0.82f, 18f),
                    Triple(0.30f, 0.70f, 11f),
                    Triple(0.78f, 0.76f, 15f),
                    Triple(0.68f, 0.63f, 9f),
                    Triple(0.85f, 0.88f, 12f),
                    Triple(0.12f, 0.60f, 8f)
                )

                burbujas.forEachIndexed { index, burbuja ->

                    var y =
                        alto * burbuja.second -
                                desplazamiento -
                                (index * 25f)

                    if (y < -50f) {
                        y += alto
                    }

                    drawCircle(
                        color = azulBorde.copy(
                            alpha = 0.55f
                        ),
                        radius = burbuja.third,
                        center = Offset(
                            x = ancho * burbuja.first,
                            y = y
                        ),
                        style = Stroke(
                            width = 3f
                        )
                    )

                    // Reflejo pequeño
                    drawCircle(
                        color = Color.White.copy(
                            alpha = 0.65f
                        ),
                        radius = burbuja.third * 0.18f,
                        center = Offset(
                            x = ancho * burbuja.first -
                                    burbuja.third * 0.3f,
                            y = y -
                                    burbuja.third * 0.3f
                        )
                    )
                }
            }

            if (radioActual > 0f) {

                // Fondo transparente de la burbuja
                drawCircle(
                    color = azulAgua.copy(
                        alpha = 0.10f
                    ),
                    radius = radioActual,
                    center = centro
                )

                // Path circular para recortar el agua
                val circulo = Path().apply {
                    addOval(
                        androidx.compose.ui.geometry.Rect(
                            center = centro,
                            radius = radioActual
                        )
                    )
                }

                clipPath(circulo) {

                    val aguaY =
                        centro.y +
                                radioActual -
                                (
                                        radioActual *
                                                2f *
                                                nivelAgua.value
                                        )

                    val amplitud =
                        18f * (1f - expansion.value * 0.6f)

                    val desplazamientoOla =
                        movimientoOla * 160f

                    val ola = Path()

                    ola.moveTo(
                        -200f,
                        aguaY
                    )

                    var x = -200f

                    while (x < ancho + 300f) {

                        ola.quadraticTo(
                            x + 40f + desplazamientoOla,
                            aguaY - amplitud,
                            x + 80f + desplazamientoOla,
                            aguaY
                        )

                        ola.quadraticTo(
                            x + 120f + desplazamientoOla,
                            aguaY + amplitud,
                            x + 160f + desplazamientoOla,
                            aguaY
                        )

                        x += 160f
                    }

                    ola.lineTo(
                        ancho + 300f,
                        alto + 300f
                    )

                    ola.lineTo(
                        -300f,
                        alto + 300f
                    )

                    ola.close()

                    // Agua principal
                    drawPath(
                        path = ola,
                        color = azulAgua
                    )

                    // Segunda capa para dar profundidad
                    drawPath(
                        path = ola,
                        color = Color.White.copy(
                            alpha = 0.08f
                        )
                    )
                }

                // Borde de la burbuja
                if (expansion.value < 0.85f) {

                    drawCircle(
                        color = azulBorde.copy(
                            alpha = 1f - expansion.value
                        ),
                        radius = radioActual,
                        center = centro,
                        style = Stroke(
                            width = 6f
                        )
                    )

                    // Brillo superior
                    drawCircle(
                        color = Color.White.copy(
                            alpha =
                                0.75f *
                                        (1f - expansion.value)
                        ),
                        radius =
                            radioActual * 0.055f,
                        center = Offset(
                            x = centro.x -
                                    radioActual * 0.32f,
                            y = centro.y -
                                    radioActual * 0.33f
                        )
                    )
                }
            }
        }
    }
}
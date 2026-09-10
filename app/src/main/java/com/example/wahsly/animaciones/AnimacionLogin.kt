package com.example.wahsly.AnimacionesVectoriales

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wahsly.R
import kotlinx.coroutines.delay

@Composable
fun WashlyBienvenidaAnimado(
    modoOscuro: Boolean,
    modifier: Modifier = Modifier,
    tamano: Dp = 350.dp
) {
    // PARPADEO
    var ojoCerrado by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            // Ojo abierto
            delay(2800)

            // Cierra el ojo
            ojoCerrado = true

            delay(600)

            // Abre el ojo
            ojoCerrado = false
        }
    }

    // SELECCIONAR VECTOR SEGÚN TEMA Y PARPADEO
    val recursoVector = when {
        modoOscuro && ojoCerrado -> {
            R.drawable.washly_bienvenida_cerrado_oscuro
        }
        modoOscuro -> {
            R.drawable.washly_bienvenida_abierto_oscuro
        }
        ojoCerrado -> {
            R.drawable.washly_bienvenida_cerrado
        } else -> {
            R.drawable.washly_bienvenida_abierto
        }
    }

    // Cargamos el VectorDrawable como ImageVector
    val vectorActual = ImageVector.vectorResource(
        id = recursoVector
    )

    val painterVector = rememberVectorPainter(image = vectorActual)

    // ANIMACIÓN DEL BRILLO DEL OJO
    val transicion = rememberInfiniteTransition(
        label = "MovimientoOjoWashly"
    )

    val movimientoOjo by transicion.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 900
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "MovimientoBrillo"
    )

    val intensidadBrillo by transicion.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "IntensidadBrillo"
    )

    // DIBUJAR VECTOR
    Canvas(
        modifier = modifier.requiredSize(tamano)
    ) {
        val escala = size.height / 792f
        val centroLogo = Offset(
            x = size.width / 2f,
            y = 403.81f * escala
        )
        val radioLogo = 237.83f * escala
        if (modoOscuro) {
            drawCircle(
                Color(0xFF6F879E),
                radius = radioLogo,
                center = centroLogo
            )
        }

        // Mantiene la proporción original del vector
        val proporcion = 612f / 792f
        val anchoVector = size.height * proporcion
        val desplazamientoX = (size.width - anchoVector) / 2f

        translate(
            left = desplazamientoX,
            top = 0f
        ) {
            with(painterVector) {
                draw(
                    size = Size(
                        width = anchoVector,
                        height = size.height
                    )
                )
            }
        }

        // BRILLO DENTRO DEL OJO
        if (!ojoCerrado) {
            val escala = size.height / 792f
            val centroX = desplazamientoX + ((407f + movimientoOjo) * escala)
            val centroY = 401f * escala

            val colorBrillo =
                if (modoOscuro) {
                    Color(0xFFF4EFEB)
                } else {
                    Color.White
                }

            // Brillo principal
            drawCircle(
                color = colorBrillo.copy(
                    alpha = intensidadBrillo
                ),
                radius = 3.2f * escala,
                center = Offset(
                    x = centroX,
                    y = centroY
                )
            )

            // Brillito pequeño
            drawCircle(
                color = colorBrillo.copy(
                    alpha = intensidadBrillo * 0.75f
                ),
                radius = 1.4f * escala,
                center = Offset(
                    x = centroX + (5f * escala),
                    y = centroY + (5f * escala)
                )
            )
        }
    }
}
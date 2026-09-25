package com.example.wahsly.AnimacionesVectoriales

import android.content.res.Configuration
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wahsly.R
import kotlinx.coroutines.delay

@Composable
fun WashlyBienvenidaAnimado(
    modoOscuro: Boolean,
    modifier: Modifier = Modifier,
    tamano: Dp? = null
) {
    // Por ahora esta versión usa el nuevo vector de modo claro.
    // Se conserva modoOscuro en la firma para no romper las llamadas existentes.

    val configuration = LocalConfiguration.current
    val anchoDp = configuration.screenWidthDp
    val esTablet = anchoDp >= 600
    val esHorizontal =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val tamanoFinal: Dp = tamano ?: when {
        esTablet && esHorizontal -> 380.dp
        esTablet -> 320.dp
        esHorizontal -> 220.dp
        else -> 280.dp
    }

    // PARPADEO: misma lógica que ya tenías.
    var ojoCerrado by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2800)
            ojoCerrado = true
            delay(600)
            ojoCerrado = false
        }
    }

    // NUEVOS VECTORES DE MODO CLARO.
    val recursoVector = if (ojoCerrado) {
        R.drawable.washly_bienvenida_cerrado
    } else {
        R.drawable.washly_bienvenida_abierto
    }

    val vectorActual = ImageVector.vectorResource(id = recursoVector)
    val painterVector = rememberVectorPainter(image = vectorActual)

    // Movimiento e intensidad del brillito.
    val transicion = rememberInfiniteTransition(
        label = "MovimientoOjoWashly"
    )

    val movimientoOjo by transicion.animateFloat(
        initialValue = -2.5f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "MovimientoBrillo"
    )

    val intensidadBrillo by transicion.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "IntensidadBrillo"
    )

    Canvas(
        modifier = modifier.size(tamanoFinal)
    ) {
        // El SVG nuevo tiene viewBox="0 0 495.66 504.09".
        val viewportWidth = 495.66f
        val viewportHeight = 504.09f

        // Mantener la proporción original y centrarlo horizontalmente.
        val escala = size.height / viewportHeight
        val anchoVector = viewportWidth * escala
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

        // BRILLO PEQUEÑO DENTRO DEL OJO DERECHO.
        // Solo se dibuja mientras el ojo está abierto.
        if (!ojoCerrado) {
            val centroX =
                desplazamientoX + ((341.5f + movimientoOjo) * escala)
            val centroY = 249.0f * escala

            drawCircle(
                color = Color.White.copy(alpha = intensidadBrillo),
                radius = 3.2f * escala,
                center = Offset(
                    x = centroX,
                    y = centroY
                )
            )

            drawCircle(
                color = Color.White.copy(
                    alpha = intensidadBrillo * 0.75f
                ),
                radius = 1.4f * escala,
                center = Offset(
                    x = centroX + (5f * escala),
                    y = centroY + (4f * escala)
                )
            )
        }
    }
}

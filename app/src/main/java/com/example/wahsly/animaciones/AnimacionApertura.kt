package com.example.wahsly.AnimacionesVectoriales

import android.content.res.Configuration
import android.graphics.drawable.AnimatedVectorDrawable
import android.widget.ImageView
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.wahsly.R
import kotlinx.coroutines.delay

@Composable
fun WashlyAperturaAnimado(
    modifier: Modifier = Modifier,
    tamano: Dp? = null, //
    onTerminar: () -> Unit = {}
) {

    // ============================================
    // RESPONSIVE: TAMAÑO SEGÚN DISPOSITIVO
    // ============================================
    val configuration = LocalConfiguration.current
    val anchoDp = configuration.screenWidthDp
    val esTablet = anchoDp >= 600
    val esHorizontal = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val tamanoFinal: Dp = tamano ?: when {
        esTablet -> 470.dp                // Tablet (vertical u horizontal)
        esHorizontal -> 200.dp            // Celular horizontal
        else -> 280.dp                    // Celular vertical
    }

    val onTerminarActual by rememberUpdatedState(
        newValue = onTerminar
    )

    AndroidView(
        modifier = modifier.size(tamanoFinal),
        factory = { context ->

            ImageView(context).apply {

                scaleType = ImageView.ScaleType.FIT_CENTER

                setImageResource(
                    R.drawable.washly_apertura_animado
                )

                post {
                    (drawable as? AnimatedVectorDrawable)
                        ?.start()
                }
            }
        }
    )

    LaunchedEffect(Unit) {

        delay(2450)

        onTerminarActual()
    }
}
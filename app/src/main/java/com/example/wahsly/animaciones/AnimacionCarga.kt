package com.example.wahsly.AnimacionesVectoriales

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wahsly.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun WashlyLavandoAnimado(
    modoOscuro: Boolean,
    modifier: Modifier = Modifier,
    tamano: Dp = 560.dp,
    onTerminar: () -> Unit = {}
) {

    // VECTORES
    val vectorLavadora = ImageVector.vectorResource(id = R.drawable.washly_lavadora_base)
    val vectorCara = ImageVector.vectorResource(id = R.drawable.washly_lavadora_cara)
    val vectorAgua = ImageVector.vectorResource(id = R.drawable.washly_lavadora_agua)

    val painterLavadora = rememberVectorPainter(vectorLavadora)
    val painterCara = rememberVectorPainter(vectorCara)
    val painterAgua = rememberVectorPainter(vectorAgua)

    // ANIMACIONES
    val progresoAgua = remember { Animatable(0f) }
    val giroCara = remember { Animatable(0f) }
    val onTerminarActual by rememberUpdatedState(onTerminar)

    LaunchedEffect(Unit) {
        repeat(2) {
            progresoAgua.snapTo(0f)
            giroCara.snapTo(0f)
            coroutineScope {
                // Agua sube de abajo hasta arriba
                launch {
                    progresoAgua.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 1200,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
                // La cara da 1 vuelta completa
                launch {
                    giroCara.animateTo(
                        targetValue = 360f,
                        animationSpec = tween(
                            durationMillis = 1200,
                            easing = LinearEasing
                        )
                    )
                }
            }
        }
        onTerminarActual()
    }

    // DIBUJO
    Canvas(
        modifier = modifier.requiredSize(tamano)
    ) {
        // Hacerlo mas grande
        val escalaBase = size.height / 792f
        val escalaDibujo = escalaBase * 1.18f
        val anchoVector = 612f * escalaDibujo
        val altoVector = 792f * escalaDibujo
        val desplazamientoX = (size.width - anchoVector) / 2f
        val desplazamientoY = -42f * escalaBase

        // LAVADORA FIJA
        translate(
            left = desplazamientoX,
            top = desplazamientoY
        ) {
            with(painterLavadora) {
                draw(
                    size = Size(
                        width = anchoVector,
                        height = altoVector
                    )
                )
            }
        }

        // DATOS DEL TAMBOR
        val centroTambor = Offset(
            x = desplazamientoX + (313.77f * escalaDibujo),
            y = desplazamientoY + (414.94f * escalaDibujo)
        )

        val radioTambor = 105f * escalaDibujo

        val recorteTambor = Path().apply {
            addOval(
                Rect(
                    left = centroTambor.x - radioTambor,
                    top = centroTambor.y - radioTambor,
                    right = centroTambor.x + radioTambor,
                    bottom = centroTambor.y + radioTambor
                )
            )
        }

        // 2. CARITA GIRANDO
        clipPath(recorteTambor) {
            rotate(
                degrees = giroCara.value,
                pivot = centroTambor
            ) {
                translate(
                    left = desplazamientoX,
                    top = desplazamientoY
                ) {
                    with(painterCara) {
                        draw(
                            size = Size(
                                width = anchoVector,
                                height = altoVector
                            )
                        )
                    }
                }
            }
        }

        // 3. AGUA SUBIENDO
        clipPath(recorteTambor) {
            val aguaInicioY = 155f * escalaDibujo
            val aguaFinY = -130f * escalaDibujo
            val desplazamientoAguaY =
                aguaInicioY + ((aguaFinY - aguaInicioY) * progresoAgua.value)

            translate(
                left = desplazamientoX,
                top = desplazamientoY + desplazamientoAguaY
            ) {
                with(painterAgua) {
                    draw(
                        size = Size(
                            width = anchoVector,
                            height = altoVector
                        ),
                        alpha = if (modoOscuro) 0.32f else 0.42f
                    )
                }
            }
        }
    }
}
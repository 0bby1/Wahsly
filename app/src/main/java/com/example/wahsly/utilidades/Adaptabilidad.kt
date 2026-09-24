package com.example.wahsly.utilidades

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.dp

enum class TipoPantalla {
    TELEFONO,
    TABLET_VERTICAL,
    TABLET_HORIZONTAL
}

fun obtenerTipoPantalla(
    windowSizeClass: WindowSizeClass
): TipoPantalla {

    val ancho = windowSizeClass.widthSizeClass
    val alto = windowSizeClass.heightSizeClass

    return when {
        // Celular normal
        ancho == WindowWidthSizeClass.Compact -> {
            TipoPantalla.TELEFONO
        }

        // Evita considerar un celular acostado como tablet
        ancho == WindowWidthSizeClass.Medium &&
                alto == WindowHeightSizeClass.Compact -> {
            TipoPantalla.TELEFONO
        }

        // Tablet principalmente en vertical
        ancho == WindowWidthSizeClass.Medium -> {
            TipoPantalla.TABLET_VERTICAL
        }

        // Tablet principalmente en horizontal
        ancho == WindowWidthSizeClass.Expanded -> {
            TipoPantalla.TABLET_HORIZONTAL
        }

        else -> {
            TipoPantalla.TELEFONO
        }
    }
}

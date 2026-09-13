package com.example.wahsly.utilidades

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun esTablet(): Boolean {
    val configuracion = LocalConfiguration.current
    return configuracion.screenWidthDp >= 600
}

val AnchoMaximoContenidoTablet = 600.dp
val AnchoMaximoFormularioTablet = 450.dp

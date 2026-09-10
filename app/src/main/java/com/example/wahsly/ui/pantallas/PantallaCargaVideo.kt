package com.example.wahsly.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import androidx.compose.ui.unit.dp
import com.example.wahsly.AnimacionesVectoriales.WashlyLavandoAnimado


@Composable
fun PantallaCargaVideo(
    modoOscuro: Boolean,
    onTerminar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (modoOscuro) FondoOscuro else FondoClaro
            ),
        contentAlignment = Alignment.Center
    ) {
        WashlyLavandoAnimado(
            modoOscuro = modoOscuro,
            tamano = 560.dp,
            onTerminar = onTerminar
        )
    }
}
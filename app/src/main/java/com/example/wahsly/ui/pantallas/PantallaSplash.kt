package com.example.wahsly.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wahsly.AnimacionesVectoriales.WashlyAperturaAnimado
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro

@Composable
fun PantallaSplash(
    modoOscuro: Boolean,
    onTerminar: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (modoOscuro) FondoOscuro
                else FondoClaro
            ),
        contentAlignment = Alignment.Center
    ) {

        WashlyAperturaAnimado(
            tamano = 470.dp,
            onTerminar = onTerminar
        )
    }
}
package com.example.wahsly.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wahsly.AnimacionesVectoriales.WashlyLavandoAnimado
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro

@Composable
fun PantallaCargaVideo(
    modoOscuro: Boolean,
    onTerminar: () -> Unit
) {

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (modoOscuro) FondoOscuro else FondoClaro
            ),
        contentAlignment = Alignment.Center
    ) {

        val esHorizontal = maxWidth > maxHeight

        val medidaReferencia =
            if (esHorizontal) {
                maxHeight
            } else {
                maxWidth
            }

        val tamanoDinamico =
            (medidaReferencia * 0.78f)
                .coerceIn(
                    250.dp,
                    520.dp
                )

        WashlyLavandoAnimado(
            modoOscuro = modoOscuro,
            tamano = tamanoDinamico,
            onTerminar = onTerminar
        )
    }
}
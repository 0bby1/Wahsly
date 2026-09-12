package com.example.wahsly.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import androidx.compose.ui.unit.dp
import com.example.wahsly.AnimacionesVectoriales.WashlyAperturaAnimado
import androidx.compose.ui.Alignment

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
        // Detecta si está en horizontal o vertical
        val esHorizontal = maxWidth > maxHeight
        val medidaReferencia = if (esHorizontal) maxHeight else maxWidth

        val tamanoDinamico = (medidaReferencia * 0.60f).coerceIn(240.dp, 470.dp)

        WashlyAperturaAnimado(
            tamano = tamanoDinamico,
            onTerminar = onTerminar
        )
    }
}
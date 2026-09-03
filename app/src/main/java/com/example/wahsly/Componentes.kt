package com.example.wahsly

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Botón Degradado (compartido)
@Composable
fun BotonDegradado(
    texto: String,
    modoOscuro: Boolean,
    onClick: () -> Unit
) {
    val coloresBoton =
        if (modoOscuro) {
            listOf(
                RosaOscuro,
                CremaOscuro
            )
        } else {
            listOf(
                Color(0xFF7B92A4),
                Color(0xFF334055)
            )
        }

    val colorTexto =
        if (modoOscuro)
            FondoOscuro
        else
            Color.White
    val forma = RoundedCornerShape(50.dp)
    Box(
        modifier = Modifier
            .width(255.dp)
            .height(61.dp)
            .clip(forma)
            .background(
                brush = Brush.horizontalGradient(
                    colors = coloresBoton
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = colorTexto,
            fontSize = 23.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// Botón Crear Cuenta (usado en login)
@Composable
fun BotonCrearCuenta(onClick: () -> Unit) {
    val forma = RoundedCornerShape(50.dp)
    Box(
        modifier = Modifier
            .width(230.dp)
            .height(61.dp)
            .clip(forma)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFD29FAD), Color(0xFFD9D9D9))
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Crear Cuenta",
            color = Color(0xFF334055),
            fontSize = 23.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

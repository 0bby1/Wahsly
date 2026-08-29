package com.example.wahsly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Pantalla del historial

@Composable
fun PantallaHistorial(
    onVolver: () -> Unit
) {

    val colorFondo = Color(0xFFF9F9F9)
    val colorTexto = Color(0xFF334055)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(30.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Historial",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colorTexto
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (BaseDatosHistorial.registros.isEmpty()) {

            Text(
                text = "No hay escaneos guardados.",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                onVolver()
            }
        ) {
            Text("Volver")
        }
    }
}

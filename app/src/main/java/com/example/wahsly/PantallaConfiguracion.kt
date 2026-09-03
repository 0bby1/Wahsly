package com.example.wahsly

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaConfiguracion(
    modoOscuro: Boolean,
    onCambiarModoOscuro: (Boolean) -> Unit,
    onVolver: () -> Unit
) {

    // COLORES
    val colorFondo =
        if (modoOscuro) FondoOscuro else FondoClaro

    val colorTexto =
        if (modoOscuro) CremaOscuro else AzulTextoClaro

    val colorTarjeta =
        if (modoOscuro) TarjetaPerfilOscuro else Color.White

    val colorAcento =
        if (modoOscuro) RosaOscuro else AzulPrincipalClaro


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // Encabezado
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onVolver
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = colorTexto
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Configuración",
                color = colorTexto,
                fontSize = 26.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // TARJETA MODO OSCURO
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = colorTarjeta,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 18.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Modo oscuro",
                    color = colorTexto,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = modoOscuro,
                    onCheckedChange = { activado ->
                        onCambiarModoOscuro(activado)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CremaOscuro,
                        checkedTrackColor = RosaOscuro,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFD9D9D9)
                    )
                )
            }
        }
    }
}
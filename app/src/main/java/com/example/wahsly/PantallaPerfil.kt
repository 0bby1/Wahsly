package com.example.wahsly

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.outlined.Person

// Pantalla de perfil de usuario
@Composable
fun PantallaPerfil(
    usuario: Usuario?,
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val context = LocalContext.current
    val colorFondo = Color(0xFFF4EFEB)
    val colorTexto = Color(0xFF334055)
    val azulClaro = Color(0xFF2F4157)
    val azulPrincipal = Color(0xFF2F4157)
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = colorFondo,
        contentWindowInsets = WindowInsets.systemBars,

        // BARRA INFERIOR (igual que en PantallaPrincipal, pero con "Cuenta" activo)
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorFondo)
                    .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp),
                    shape = RoundedCornerShape(45.dp),
                    color = azulPrincipal,
                    shadowElevation = 5.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // ---------------- INICIO ----------------
                        Column(
                            modifier = Modifier
                                .width(105.dp)
                                .clickable { onVolver() },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Inicio",
                                tint = Color(0xFFC5D7E0),
                                modifier = Modifier.size(34.dp)
                            )
                            Text("Inicio", fontSize = 12.sp, color = Color.White)
                        }

                        // ---------------- ESCANEAR ----------------
                        Column(
                            modifier = Modifier
                                .width(105.dp)
                                .clickable {
                                    Toast.makeText(context, "Escáner próximamente", Toast.LENGTH_SHORT).show()
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Escanear",
                                tint = Color(0xFFC5D7E0),
                                modifier = Modifier.size(38.dp)
                            )
                            Text("Escanear", fontSize = 12.sp, color = Color.White)
                        }

                        // ---------------- CUENTA (activo) ----------------
                        Box(
                            modifier = Modifier
                                .width(105.dp)
                                .height(65.dp)
                                .clip(RoundedCornerShape(38.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Cuenta",
                                    tint = azulPrincipal,
                                    modifier = Modifier.size(28.dp)
                                )
                                Text("Cuenta", fontSize = 12.sp, color = azulPrincipal)
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondo)
                .padding(bottom = padding.calculateBottomPadding())
        ) {

            // ---------------- CABECERA ----------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(235.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 35.dp,
                            bottomEnd = 35.dp
                        )
                    )
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF718A9C),
                                Color(0xFF405B70)
                            )
                        )
                    )
            )


            // ---------------- AVATAR ----------------
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 85.dp)
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE5E5E5))
                    .border(
                        width = 5.dp,
                        color = Color(0xFF2F4157),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Foto de perfil",
                    tint = Color(0xFF9D9D9D),
                    modifier = Modifier.size(145.dp)
                )
            }


            // ---------------- TARJETA DE OPCIONES ----------------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 32.dp,
                        end = 32.dp,
                        top = 395.dp
                    ),
                shape = RoundedCornerShape(18.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )
            ) {

                Column {

                    OpcionPerfilConFlecha(
                        icono = Icons.Default.Settings,
                        texto = "Configuración",
                        onClick = { }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = Color(0xFFEAEAEA)
                    )

                    OpcionPerfilConFlecha(
                        icono = Icons.Default.Info,
                        texto = "Ayuda y Soporte",
                        onClick = { }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = Color(0xFFEAEAEA)
                    )

                    OpcionPerfilConFlecha(
                        icono = Icons.Default.ExitToApp,
                        texto = "Cerrar Sesión",
                        onClick = {
                            mostrarDialogoCerrarSesion = true
                        }
                    )
                }
            }


            // ---------------- POP-UP CERRAR SESIÓN ----------------
            if (mostrarDialogoCerrarSesion) {

                Dialog(
                    onDismissRequest = {
                        mostrarDialogoCerrarSesion = false
                    }
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFF2F4157))
                            .padding(24.dp)
                    ) {

                        IconButton(
                            onClick = {
                                mostrarDialogoCerrarSesion = false
                            },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {

                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.White
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 24.dp,
                                    bottom = 8.dp
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "¿Cerrar sesión?",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(
                                modifier = Modifier.height(24.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFFF4EFEB))
                                    .clickable {

                                        mostrarDialogoCerrarSesion = false

                                        onCerrarSesion()
                                    }
                                    .padding(
                                        horizontal = 40.dp,
                                        vertical = 12.dp
                                    )
                            ) {

                                Text(
                                    text = "Aceptar",
                                    color = Color(0xFF2F4157),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun OpcionPerfilConFlecha(
    icono: ImageVector,
    texto: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = Color(0xFF334055),
            modifier = Modifier.size(22.dp)
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = texto,
            fontSize = 14.sp,
            color = Color(0xFF334055),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFF334055),
            modifier = Modifier.size(22.dp)
        )
    }
}

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
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.ui.unit.TextUnit


// Pantalla de perfil de usuario
@Composable
fun PantallaPerfil(
    usuario: Usuario?,
    modoOscuro: Boolean,
    onConfiguracion: () -> Unit,
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit
) {

    val context = LocalContext.current

    var mostrarDialogoCerrarSesion by remember {
        mutableStateOf(false)
    }

    // COLORES SEGÚN EL MODO
    val colorFondo =
        if (modoOscuro)
            FondoOscuro
        else
            FondoClaro

    val colorTexto =
        if (modoOscuro)
            RosaOscuro
        else
            AzulTextoClaro

    val colorBarraInferior =
        if (modoOscuro)
            RosaOscuro
        else
            AzulPrincipalClaro

    val colorTarjeta =
        if (modoOscuro)
            TarjetaPerfilOscuro
        else
            Color.White

    val colorSeleccionado =
        if (modoOscuro)
            CremaOscuro
        else
            Color.White

    val colorDivisor =
        if (modoOscuro)
            DivisorOscuro
        else
            DivisorClaro

    val colorAvatarFondo =
        if (modoOscuro)
            AvatarFondoOscuro
        else
            AvatarFondoClaro

    val colorAvatarIcono =
        if (modoOscuro)
            AvatarIconoOscuro
        else
            AvatarIconoClaro

    val colorBordeAvatar =
        AzulPrincipalClaro

    val colorIconosBarra =
        if (modoOscuro)
            CremaOscuro
        else
            IconoSecundarioClaro

    val colorTextoBarra =
        if (modoOscuro)
            CremaOscuro
        else
            Color.White

    val coloresCabecera =
        if (modoOscuro) {
            listOf(
                CremaOscuro,
                RosaOscuro
            )
        } else {
            listOf(
                Color(0xFF718A9C),
                Color(0xFF405B70)
            )
        }

    val colorDialogo =
        if (modoOscuro)
            RosaOscuro
        else
            AzulPrincipalClaro

    val colorTextoDialogo =
        if (modoOscuro)
            AzulTextoClaro
        else
            Color.White

    val colorBotonDialogo =
        if (modoOscuro)
            RosaOscuro
        else
            FondoClaro

    val colorTextoBotonDialogo =
        AzulTextoClaro

    Scaffold(
        containerColor = colorFondo,
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorFondo)
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        bottom = 8.dp
                    )
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp),
                    shape = RoundedCornerShape(45.dp),
                    color = colorBarraInferior,
                    shadowElevation = 5.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // INICIO
                        Column(
                            modifier = Modifier
                                .width(105.dp)
                                .clickable {
                                    onVolver()
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Inicio",
                                tint = colorIconosBarra,
                                modifier = Modifier.size(34.dp)
                            )

                            Text(
                                text = "Inicio",
                                fontSize = 12.sp,
                                color = colorTextoBarra
                            )
                        }

                        // ESCANEAR
                        Column(
                            modifier = Modifier
                                .width(105.dp)
                                .clickable {
                                    Toast.makeText(
                                        context,
                                        "Escáner próximamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Escanear",
                                tint = colorIconosBarra,
                                modifier = Modifier.size(38.dp)
                            )
                            Text(
                                text = "Escanear",
                                fontSize = 12.sp,
                                color = colorTextoBarra
                            )
                        }

                        // CUENTA ACTIVA
                        Box(
                            modifier = Modifier
                                .width(105.dp)
                                .height(65.dp)
                                .clip(RoundedCornerShape(38.dp))
                                .background(colorSeleccionado),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Cuenta",
                                    tint = colorBarraInferior,
                                    modifier = Modifier.size(28.dp)
                                )
                                Text(
                                    text = "Cuenta",
                                    fontSize = 12.sp,
                                    color = colorBarraInferior
                                )
                            }
                        }
                    }
                }
            }
        }

    ) { padding ->
        // CONTENIDO
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondo)
                .padding(
                    bottom = padding.calculateBottomPadding()
                )
        ) {

            // CABECERA
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
                            colors = coloresCabecera
                        )
                    )
            )

            // AVATAR
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 85.dp)
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(colorAvatarFondo)
                    .border(
                        width = 5.dp,
                        color = colorBordeAvatar,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Foto de perfil",
                    tint = colorAvatarIcono,
                    modifier = Modifier.size(145.dp)
                )
            }

            // TARJETA DE OPCIONES
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
                    containerColor = colorTarjeta
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )
            ) {
                Column {
                    OpcionPerfilConFlecha(
                        icono = Icons.Default.Settings,
                        texto = "Configuración",
                        fontSize = 24.sp,
                        colorContenido = colorTexto,
                        onClick = onConfiguracion
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 16.dp
                        ),
                        thickness = 1.dp,
                        color = colorDivisor
                    )

                    // AYUDA
                    OpcionPerfilConFlecha(
                        icono = Icons.Default.Info,
                        texto = "Ayuda y Soporte",
                        fontSize = 24.sp,
                        colorContenido = colorTexto,
                        onClick = {
                            // Pantalla de Ayuda en un futuro
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            horizontal = 16.dp
                        ),
                        thickness = 1.dp,
                        color = colorDivisor
                    )

                    // CERRAR SESIÓN
                    OpcionPerfilConFlecha(
                        icono = Icons.Default.ExitToApp,
                        texto = "Cerrar Sesión",
                        fontSize = 24.sp,
                        colorContenido = colorTexto,
                        onClick = {
                            mostrarDialogoCerrarSesion = true
                        }
                    )
                }
            }

            // POP-UP CERRAR SESIÓN
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
                            .background(colorDialogo)
                            .padding(24.dp)
                    ) {
                        // BOTÓN X
                        IconButton(
                            onClick = {
                                mostrarDialogoCerrarSesion = false
                            },
                            modifier = Modifier.align(
                                Alignment.TopEnd
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = colorTextoDialogo
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
                            // TEXTO
                            Text(
                                text = "¿Cerrar sesión?",
                                color = colorTextoDialogo,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // BOTÓN ACEPTAR
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(colorBotonDialogo)
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
                                    color = colorTextoBotonDialogo,
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

// OPCIÓN DEL PERFIL
@Composable
fun OpcionPerfilConFlecha(
    icono: ImageVector,
    texto: String,
    fontSize: TextUnit = 14.sp,
    colorContenido: Color,
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
            tint = colorContenido,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = texto,
            fontSize = fontSize,
            color = colorContenido,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = colorContenido,
            modifier = Modifier.size(22.dp)
        )
    }
}
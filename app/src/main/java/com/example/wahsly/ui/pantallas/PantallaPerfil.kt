package com.example.wahsly.ui.pantallas

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.ui.theme.AvatarFondoClaro
import com.example.wahsly.ui.theme.AvatarFondoOscuro
import com.example.wahsly.ui.theme.AvatarIconoClaro
import com.example.wahsly.ui.theme.AvatarIconoOscuro
import com.example.wahsly.ui.theme.AzulPrincipalClaro
import com.example.wahsly.ui.theme.AzulTextoClaro
import com.example.wahsly.ui.theme.CremaOscuro
import com.example.wahsly.ui.theme.DivisorClaro
import com.example.wahsly.ui.theme.DivisorOscuro
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.theme.IconoSecundarioClaro
import com.example.wahsly.ui.theme.RosaOscuro
import com.example.wahsly.ui.theme.TarjetaPerfilOscuro


// Pantalla de perfil de usuario
@Composable
fun PantallaPerfil(
    usuario: Usuario?,
    modoOscuro: Boolean,
    onConfiguracion: () -> Unit,
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit,
    mostrarBarraInferior: Boolean = true,
    animarCabecera: Boolean = false
) {

    val context = LocalContext.current

    // =======================================
    // RESPONSIVE: DETECCIÓN DE DISPOSITIVO
    // =======================================
    val configuration = LocalConfiguration.current
    val anchoDp = configuration.screenWidthDp
    val altoDp = configuration.screenHeightDp
    val esTablet = anchoDp >= 600
    val esHorizontal = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val esCelularHorizontal = esHorizontal && !esTablet

    // Tamaños adaptativos
    val alturaCabecera = when {
        esCelularHorizontal -> 110.dp
        esTablet && esHorizontal -> 200.dp
        esTablet -> 235.dp
        else -> 200.dp // celular vertical
    }

    val tamanoAvatar = when {
        esCelularHorizontal -> 90.dp
        esTablet && esHorizontal -> 200.dp
        esTablet -> 240.dp
        else -> 170.dp
    }

    val paddingTopAvatar = when {
        esCelularHorizontal -> 30.dp
        esTablet && esHorizontal -> 60.dp
        esTablet -> 85.dp
        else -> 60.dp
    }

    val iconoAvatarTamano = when {
        esCelularHorizontal -> 55.dp
        esTablet && esHorizontal -> 120.dp
        esTablet -> 145.dp
        else -> 100.dp
    }

    val bordeAvatar = when {
        esCelularHorizontal -> 3.dp
        esTablet -> 5.dp
        else -> 4.dp
    }

    val paddingLateralTarjeta = if (esTablet) 32.dp else 16.dp
    val fontSizeOpciones = if (esTablet) 22.sp else 16.sp
    val iconoTamanoOpcion = if (esTablet) 24.dp else 20.dp

    val alturaBarraInferior = when {
        esCelularHorizontal -> 60.dp
        esTablet -> 78.dp
        else -> 70.dp
    }

    val tamanoIconoBarra = if (esTablet) 34.dp else 28.dp
    val tamanoIconoEscaner = if (esTablet) 38.dp else 32.dp
    val fontSizeBarra = if (esTablet) 12.sp else 11.sp

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
                Color(0xFF4F7188),
                AzulPrincipalClaro
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

    // =======================================
    // ANIMACIÓN DE LA CABECERA DE PERFIL
    // =======================================

    val alturaStatusBarPerfil =
        WindowInsets.statusBars
            .asPaddingValues()
            .calculateTopPadding()

    // Altura inicial (pequeña) proporcional a la cabecera final
    val alturaInicio =
        alturaStatusBarPerfil + (alturaCabecera * 0.4f)

    var iniciarAnimacionCabecera by remember(animarCabecera) {
        mutableStateOf(!animarCabecera)
    }

    LaunchedEffect(animarCabecera) {
        if (animarCabecera) {
            iniciarAnimacionCabecera = true
        }
    }

    val alturaCabeceraAnimada by animateDpAsState(
        targetValue =
            if (iniciarAnimacionCabecera)
                alturaCabecera
            else
                alturaInicio,

        animationSpec = spring(
            dampingRatio = 0.78f,
            stiffness = 220f
        ),

        label = "AlturaCabeceraPerfil"
    )

    val radioCabeceraAnimado by animateDpAsState(
        targetValue =
            if (iniciarAnimacionCabecera)
                35.dp
            else
                0.dp,

        animationSpec = tween(
            durationMillis = 320
        ),

        label = "RadioCabeceraPerfil"
    )

    Scaffold(
        containerColor = colorFondo,
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {
            if (mostrarBarraInferior) {
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
                            .height(alturaBarraInferior),
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
                                    .weight(1f)
                                    .selectable(
                                        selected = false,
                                        onClick = onVolver,
                                        role = Role.Tab
                                    )
                                    .semantics(mergeDescendants = true) {
                                        contentDescription = "Ir a la pantalla de Inicio"
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                    tint = colorIconosBarra,
                                    modifier = Modifier.size(tamanoIconoBarra)
                                )

                                Text(
                                    text = "Inicio",
                                    fontSize = fontSizeBarra,
                                    color = colorTextoBarra
                                )
                            }

                            // ESCANEAR
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .selectable(
                                        selected = false,
                                        onClick = {
                                            Toast.makeText(
                                                context,
                                                "Escáner próximamente",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        role = Role.Tab
                                    )
                                    .semantics(mergeDescendants = true) {
                                        contentDescription = "Abrir escáner de códigos"
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = colorIconosBarra,
                                    modifier = Modifier.size(tamanoIconoEscaner)
                                )
                                Text(
                                    text = "Escanear",
                                    fontSize = fontSizeBarra,
                                    color = colorTextoBarra
                                )
                            }

                            // CUENTA ACTIVA
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(alturaBarraInferior - 12.dp)
                                    .clip(RoundedCornerShape(38.dp))
                                    .background(colorSeleccionado)
                                    .selectable(
                                        selected = true,
                                        onClick = { },
                                        role = Role.Tab
                                    )
                                    .semantics(mergeDescendants = true) {
                                        contentDescription = "Pestaña de Cuenta"
                                        stateDescription = "Seleccionada"
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = colorTexto,
                                        modifier = Modifier.size(tamanoIconoBarra - 6.dp)
                                    )
                                    Text(
                                        text = "Cuenta",
                                        fontSize = fontSizeBarra,
                                        color = colorTexto
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    ) { padding ->
        // CONTENIDO SCROLLEABLE (para que quepa en celular horizontal)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondo)
                .padding(
                    bottom = padding.calculateBottomPadding()
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // =======================================
                // BLOQUE CABECERA + AVATAR
                // =======================================
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // CABECERA (degradado)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(alturaCabeceraAnimada)
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = radioCabeceraAnimado,
                                    bottomEnd = radioCabeceraAnimado
                                )
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = coloresCabecera
                                )
                            )
                    )

                    // AVATAR (encima de la cabecera)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = paddingTopAvatar)
                            .size(tamanoAvatar)
                            .clip(CircleShape)
                            .background(colorAvatarFondo)
                            .border(
                                width = bordeAvatar,
                                color = colorBordeAvatar,
                                shape = CircleShape
                            )
                            .semantics { role = Role.Image },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Foto de perfil de ${usuario?.nombre ?: "Usuario"}",
                            tint = colorAvatarIcono,
                            modifier = Modifier.size(iconoAvatarTamano)
                        )
                    }
                }

                // Espacio entre avatar y tarjeta
                Spacer(modifier = Modifier.height(20.dp))

                // =======================================
                // TARJETA DE OPCIONES
                // =======================================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = paddingLateralTarjeta),
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
                            fontSize = fontSizeOpciones,
                            iconoTamano = iconoTamanoOpcion,
                            colorContenido = colorTexto,
                            onClick = onConfiguracion
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 1.dp,
                            color = colorDivisor
                        )

                        // AYUDA
                        OpcionPerfilConFlecha(
                            icono = Icons.Default.Info,
                            texto = "Ayuda y Soporte",
                            fontSize = fontSizeOpciones,
                            iconoTamano = iconoTamanoOpcion,
                            colorContenido = colorTexto,
                            onClick = {
                                // Pantalla de Ayuda en un futuro
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 1.dp,
                            color = colorDivisor
                        )

                        // CERRAR SESIÓN
                        OpcionPerfilConFlecha(
                            icono = Icons.Default.ExitToApp,
                            texto = "Cerrar Sesión",
                            fontSize = fontSizeOpciones,
                            iconoTamano = iconoTamanoOpcion,
                            colorContenido = colorTexto,
                            onClick = {
                                mostrarDialogoCerrarSesion = true
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // POP-UP CERRAR SESIÓN (fuera del scroll para que quede fijo)
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
                            modifier = Modifier.align(Alignment.TopEnd)
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
                                )
                                .semantics {
                                    liveRegion = LiveRegionMode.Polite
                                    contentDescription = "Ventana de confirmación: ¿Deseas cerrar sesión?"
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // TEXTO
                            Text(
                                text = "¿Deseas cerrar sesión?",
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
                                    .clickable(
                                        role = Role.Button,
                                        onClickLabel = "Confirmar cierre de sesión",
                                        onClick = {
                                            mostrarDialogoCerrarSesion = false
                                            onCerrarSesion()
                                        }
                                    )
                                    .padding(
                                        horizontal = 40.dp,
                                        vertical = 12.dp
                                    )
                                    .semantics(mergeDescendants = true) { }
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
    iconoTamano: androidx.compose.ui.unit.Dp = 22.dp,
    colorContenido: Color,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                role = Role.Button,
                onClickLabel = "Acceder a $texto",
                onClick = onClick
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
            .semantics(mergeDescendants = true) { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = colorContenido,
            modifier = Modifier.size(iconoTamano)
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
            modifier = Modifier.size(iconoTamano)
        )
    }
}
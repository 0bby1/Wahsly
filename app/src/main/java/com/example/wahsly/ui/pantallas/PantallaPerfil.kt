package com.example.wahsly.ui.pantallas

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.example.wahsly.utilidades.FotoPerfilStorage
import kotlinx.coroutines.launch
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import com.example.wahsly.utilidades.TipoPantalla
import com.example.wahsly.utilidades.obtenerTipoPantalla
import com.example.wahsly.utilidades.vibrar


// Pantalla de perfil de usuario
@Composable
fun PantallaPerfil(
    usuario: Usuario?,
    modoOscuro: Boolean,
    windowSizeClass: WindowSizeClass,
    onConfiguracion: () -> Unit,
    onAyudaSoporte: () -> Unit,
    onVolver: () -> Unit,
    onCerrarSesion: () -> Unit,
    mostrarBarraInferior: Boolean = true,
    animarCabecera: Boolean = false
) {

    val context = LocalContext.current
    val scopeFoto = rememberCoroutineScope()
    val correoUsuario = usuario?.correo
    var fotoPerfil by remember(correoUsuario) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(correoUsuario) {
        fotoPerfil = null
        if (!correoUsuario.isNullOrBlank()) {
            try {
                fotoPerfil = FotoPerfilStorage.cargarFoto(
                    context,
                    correoUsuario
                )
            } catch (e: Exception) {
                fotoPerfil = null
            }
        }
    }

    val selectorFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null && correoUsuario != null) {
            scopeFoto.launch {
                try {
                    fotoPerfil = FotoPerfilStorage.guardarFoto(
                        context = context,
                        correo = correoUsuario,
                        uri = uri
                    )
                    Toast.makeText(
                        context,
                        "Fotografía actualizada",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "No se pudo guardar la fotografía",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // RESPONSIVE CON WindowSizeClass
    val tipoPantalla = obtenerTipoPantalla(windowSizeClass)
    val esTabletVertical = tipoPantalla == TipoPantalla.TABLET_VERTICAL
    val esTabletHorizontal = tipoPantalla == TipoPantalla.TABLET_HORIZONTAL
    val esTablet = esTabletVertical || esTabletHorizontal
    val esCelularHorizontal =
        tipoPantalla == TipoPantalla.TELEFONO &&
                windowSizeClass.heightSizeClass ==
                WindowHeightSizeClass.Compact

    // TAMAÑOS RESPONSIVOS
    // Altura de la parte azul
    val alturaCabecera = when {
        esCelularHorizontal -> 125.dp
        esTabletHorizontal -> 245.dp
        esTabletVertical -> 315.dp
        else -> 285.dp
    }

    // Tamaño del avatar
    val tamanoAvatar = when {
        esCelularHorizontal -> 120.dp
        esTabletHorizontal -> 190.dp
        esTabletVertical -> 240.dp
        else -> 220.dp
    }

    // Icono de persona dentro del avatar
    val iconoAvatarTamano = when {
        esCelularHorizontal -> 72.dp
        esTabletHorizontal -> 110.dp
        esTabletVertical -> 140.dp
        else -> 130.dp
    }

    val bordeAvatar = when {
        esCelularHorizontal -> 3.dp
        esTablet -> 5.dp
        else -> 4.dp
    }

    // Tamaño del pequeño botón de cámara
    val tamanoBotonCamaraAvatar = when {
        esCelularHorizontal -> 34.dp
        esTabletHorizontal -> 42.dp
        esTabletVertical -> 48.dp
        else -> 44.dp
    }

    val tamanoIconoCamaraAvatar = when {
        esCelularHorizontal -> 18.dp
        esTabletHorizontal -> 23.dp
        esTabletVertical -> 26.dp
        else -> 24.dp
    }

    // Separación entre avatar y tarjeta
    val espacioAvatarTarjeta = when {
        esCelularHorizontal -> 16.dp
        esTabletHorizontal -> 24.dp
        esTabletVertical -> 46.dp
        else -> 52.dp
    }

    // Ancho de la tarjeta blanca
    val fraccionAnchoTarjeta = when {
        esCelularHorizontal -> 0.68f
        esTabletHorizontal -> 0.50f
        esTabletVertical -> 0.78f
        else -> 0.86f
    }

    val anchoMaximoTarjeta = when {
        esCelularHorizontal -> 520.dp
        esTabletHorizontal -> 590.dp
        esTabletVertical -> 620.dp
        else -> 540.dp
    }

    val fontSizeOpciones = when {
        esCelularHorizontal -> 15.sp
        esTabletHorizontal -> 18.sp
        esTabletVertical -> 19.sp
        else -> 18.sp
    }

    val iconoTamanoOpcion = when {
        esCelularHorizontal -> 28.dp
        esTabletHorizontal -> 34.dp
        esTabletVertical -> 36.dp
        else -> 34.dp
    }

    val paddingVerticalOpcion = when {
        esCelularHorizontal -> 10.dp
        esTabletHorizontal -> 15.dp
        esTabletVertical -> 30.dp
        else -> 17.dp
    }

    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }

    // COLORES SEGÚN EL MODO
    val colorFondo = if (modoOscuro) FondoOscuro else FondoClaro
    val colorTexto = if (modoOscuro) RosaOscuro else AzulTextoClaro
    val colorBarraInferior = if (modoOscuro) RosaOscuro else AzulPrincipalClaro
    val colorTarjeta = if (modoOscuro) TarjetaPerfilOscuro else Color.White
    val colorSeleccionado = if (modoOscuro) CremaOscuro else Color.White
    val colorDivisor = if (modoOscuro) DivisorOscuro else DivisorClaro
    val colorAvatarFondo = if (modoOscuro) AvatarFondoOscuro else AvatarFondoClaro
    val colorAvatarIcono = if (modoOscuro) AvatarIconoOscuro else AvatarIconoClaro
    val colorBordeAvatar = AzulPrincipalClaro
    val colorIconosBarra = if (modoOscuro) CremaOscuro else IconoSecundarioClaro
    val colorTextoBarra = if (modoOscuro) CremaOscuro else Color.White
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

    val colorDialogo = if (modoOscuro) RosaOscuro else AzulPrincipalClaro
    val colorTextoDialogo = if (modoOscuro) AzulTextoClaro else Color.White
    val colorBotonDialogo = if (modoOscuro) RosaOscuro else FondoClaro
    val colorTextoBotonDialogo = AzulTextoClaro

    // ANIMACIÓN DE LA CABECERA DE PERFIL
    val alturaStatusBarPerfil =
        WindowInsets.statusBars
            .asPaddingValues()
            .calculateTopPadding()

    // Altura inicial (pequeña) proporcional a la cabecera final
    val alturaInicio = alturaStatusBarPerfil + (alturaCabecera * 0.4f)
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

        animationSpec = tween(durationMillis = 320),
        label = "RadioCabeceraPerfil"
    )

    Scaffold(
        containerColor = colorFondo,
        contentWindowInsets = WindowInsets.systemBars,
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
                // BLOQUE CABECERA + AVATAR
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaCabeceraAnimada + (tamanoAvatar / 2))
                ) {
                    // CABECERA
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
                                brush =
                                    Brush.verticalGradient(
                                        colors = coloresCabecera
                                    )
                            )
                    )

                    // AVATAR
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .size(tamanoAvatar),
                        contentAlignment = Alignment.Center
                    ) {
                        // CÍRCULO PRINCIPAL
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(colorAvatarFondo)
                                .border(
                                    width = bordeAvatar,
                                    color = colorBordeAvatar,
                                    shape = CircleShape
                                )
                                .clickable(
                                    enabled = usuario != null,
                                    role = Role.Button
                                ) {
                                    selectorFoto.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts
                                                .PickVisualMedia
                                                .ImageOnly
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (fotoPerfil != null) {
                                Image(
                                    bitmap = fotoPerfil!!,
                                    contentDescription = "Fotografía de perfil de ${usuario?.nombre ?: "Usuario"}",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(bordeAvatar)
                                        .clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = "Foto de perfil de ${usuario?.nombre ?: "Usuario"}",
                                    tint = colorAvatarIcono,
                                    modifier = Modifier.size(iconoAvatarTamano)
                                )
                            }
                        }

                        // BOTÓN DE CÁMARA
                        if (usuario != null) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(
                                        x = 2.dp,
                                        y = 2.dp
                                    )
                                    .size(tamanoBotonCamaraAvatar)
                                    .clip(CircleShape)
                                    .background(AzulPrincipalClaro)
                                    .border(
                                        width = 2.dp,
                                        color = if (modoOscuro) {
                                            CremaOscuro
                                        } else {
                                            FondoClaro
                                        },
                                        shape = CircleShape
                                    )
                                    .clickable(
                                        role = Role.Button
                                    ) {
                                        selectorFoto.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts
                                                    .PickVisualMedia
                                                    .ImageOnly
                                            )
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Cambiar foto de perfil",
                                    tint = Color.White,
                                    modifier = Modifier.size(tamanoIconoCamaraAvatar)
                                )
                            }
                        }
                    }
                }

                // Espacio entre avatar y tarjeta
                Spacer(modifier = Modifier.height(espacioAvatarTarjeta))

                // TARJETA DE OPCIONES
                Card(
                    modifier = Modifier
                        .fillMaxWidth(fraccionAnchoTarjeta)
                        .widthIn(max = anchoMaximoTarjeta),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = colorTarjeta),
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
                            paddingVertical = paddingVerticalOpcion,
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
                            paddingVertical = paddingVerticalOpcion,
                            colorContenido = colorTexto,
                            onClick = onAyudaSoporte
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
                            paddingVertical = paddingVerticalOpcion,
                            colorContenido = colorTexto,
                            onClick = {

                                android.util.Log.d(
                                    "WAHSLY_SESION",
                                    "Se presionó Cerrar Sesión"
                                )

                                mostrarDialogoCerrarSesion = true

                                android.util.Log.d(
                                    "WAHSLY_SESION",
                                    "Estado del diálogo: $mostrarDialogoCerrarSesion"
                                )

                            }
                        )

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
                                        top = 40.dp,
                                        bottom = 8.dp
                                    )
                                    .semantics {
                                        liveRegion = LiveRegionMode.Polite
                                        contentDescription =
                                            "Ventana de confirmación: ¿Deseas cerrar sesión?"
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                // TEXTO
                                Text(
                                    text = "¿Deseas cerrar sesión?",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp),
                                    color = colorTextoDialogo,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(
                                    modifier = Modifier.height(24.dp)
                                )


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
                                                vibrar(context)
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
}
    // OPCIÓN DEL PERFIL
    @Composable
    fun OpcionPerfilConFlecha(
        icono: ImageVector,
        texto: String,
        fontSize: TextUnit = 14.sp,
        iconoTamano: androidx.compose.ui.unit.Dp = 22.dp,
        paddingVertical: androidx.compose.ui.unit.Dp = 12.dp,
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
                    horizontal = 24.dp,
                    vertical = paddingVertical
                )
                .semantics(
                    mergeDescendants = true
                ) { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorContenido,
                modifier = Modifier.size(iconoTamano)
            )

            Spacer(modifier = Modifier.width(20.dp))

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

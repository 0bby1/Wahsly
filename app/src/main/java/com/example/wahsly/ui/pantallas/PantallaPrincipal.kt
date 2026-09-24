package com.example.wahsly.ui.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import com.example.wahsly.R
import com.example.wahsly.datos.database.AppDatabase
import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.datos.repository.HistorialRepository
import com.example.wahsly.ui.theme.AzulPrincipalClaro
import com.example.wahsly.ui.theme.AzulTextoClaro
import com.example.wahsly.ui.theme.CremaOscuro
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.theme.IconoSecundarioClaro
import com.example.wahsly.ui.theme.RosaOscuro
import com.example.wahsly.ui.theme.TarjetaRutinaClaro
import com.example.wahsly.datos.model.RegistroEscaneo
import com.example.wahsly.ia.resultadoLavadoDesdeJson
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.ImageBitmap
import com.example.wahsly.utilidades.FotoPerfilStorage
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import com.example.wahsly.utilidades.TipoPantalla
import com.example.wahsly.utilidades.obtenerTipoPantalla

@Composable
fun PantallaPrincipal(
    usuario: Usuario?,
    modoOscuro: Boolean,
    windowSizeClass: WindowSizeClass,
    onPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    mostrarBarraInferior: Boolean = true,
    animarCabecera: Boolean = false
){

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { AppDatabase.getDatabase(context) }
    val historialRepository = remember { HistorialRepository(db.historialDao()) }
    val correoUsuario = usuario?.correo.orEmpty()

    var fotoPerfilMenu by remember(correoUsuario) {
        mutableStateOf<ImageBitmap?>(null)
    }

    LaunchedEffect(correoUsuario) {
        fotoPerfilMenu = null
        if (correoUsuario.isNotBlank()) {
            fotoPerfilMenu = try {
                FotoPerfilStorage.cargarFoto(
                    context = context,
                    correo = correoUsuario
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    val registrosFlow =
        remember(correoUsuario) {
            historialRepository
                .registrosPorUsuario(
                    correoUsuario
                )
        }
    val registros by registrosFlow
        .collectAsState(
            initial = emptyList()
        )
    var busqueda by remember { mutableStateOf("") }
    var tabSeleccionado by remember { mutableIntStateOf(0) }
    var registroSeleccionado by remember { mutableStateOf<RegistroEscaneo?>(null) }

    // RESPONSIVE: DETECCIÓN DE DISPOSITIVO CON WindowSizeClass
    val configuration = LocalConfiguration.current
    val tipoPantalla = obtenerTipoPantalla(windowSizeClass)
    val esTabletVertical = tipoPantalla == TipoPantalla.TABLET_VERTICAL
    val esTabletHorizontal = tipoPantalla == TipoPantalla.TABLET_HORIZONTAL
    val esTablet = esTabletVertical || esTabletHorizontal
    val esCelularHorizontal = tipoPantalla == TipoPantalla.TELEFONO && windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
    val esHorizontal = esCelularHorizontal || esTabletHorizontal
    val columnasRutinas = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 1
        TipoPantalla.TABLET_VERTICAL -> 2
        TipoPantalla.TABLET_HORIZONTAL -> 3
    }

    // Colores
    val colorFondo = if (modoOscuro) FondoOscuro else FondoClaro
    val colorEncabezado = if (modoOscuro) RosaOscuro else AzulPrincipalClaro
    val colorTextoPrincipal = if (modoOscuro) RosaOscuro else AzulTextoClaro
    val colorTextoSecundario = if (modoOscuro) CremaOscuro else Color.Gray
    val colorLupa = if (modoOscuro) RosaOscuro else FondoOscuro
    val colorBuscador = if (modoOscuro) FondoOscuro else CremaOscuro
    val colorTarjetaRutina = if (modoOscuro) FondoOscuro else TarjetaRutinaClaro
    val colorBordeTarjeta = if (modoOscuro) RosaOscuro else Color.Transparent

    // Altura donde Android muestra hora, batería, WiFi, etc.
    val alturaStatusBar = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()

    // TAMAÑOS RESPONSIVOS
    val alturaCabeceraInicial = when {
        esCelularHorizontal -> 105.dp
        esTabletHorizontal -> 195.dp
        esTabletVertical -> 215.dp
        else -> 190.dp
    }

    val alturaNormalInicio = when {
        esCelularHorizontal ->
            alturaStatusBar + 55.dp
        esTabletHorizontal ->
            alturaStatusBar + 105.dp
        esTabletVertical ->
            alturaStatusBar + 105.dp
        else -> alturaStatusBar + 85.dp
    }

    val alturaBuscador = when {
        esCelularHorizontal -> 44.dp
        esTabletHorizontal -> 58.dp
        esTabletVertical -> 62.dp
        else -> 52.dp
    }

    val alturaTarjetaRutina = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 210.dp
        TipoPantalla.TABLET_VERTICAL -> 170.dp
        TipoPantalla.TABLET_HORIZONTAL -> 145.dp
    }

    val alturaImagenRutina = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 105.dp
        TipoPantalla.TABLET_VERTICAL -> 78.dp
        TipoPantalla.TABLET_HORIZONTAL -> 68.dp
    }

    val alturaFranja = when {
        esCelularHorizontal -> 52.dp
        esTablet -> 70.dp
        else -> 60.dp
    }

    val anchoMenu = when {
        esTablet -> 340.dp
        esHorizontal -> 260.dp
        else -> 305.dp
    }

    val paddingTopMenu = when {
        esCelularHorizontal -> alturaStatusBar + 100.dp
        esTablet -> alturaStatusBar + 150.dp
        else -> alturaStatusBar + 130.dp
    }

    val tamanoIconoMenu =
        if (esTablet) 34.dp else 30.dp

    val tamanoLogoRutina =
        if (esTablet) 56.dp else 48.dp

    val fontSizeTituloRutinas = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 28.sp
        TipoPantalla.TABLET_VERTICAL -> 30.sp
        TipoPantalla.TABLET_HORIZONTAL -> 28.sp
    }

    val maxAltoDialogo =
        if (esCelularHorizontal) { 220.dp
        } else { 520.dp }

    val espacioSuperiorRutinas = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 24.dp
        TipoPantalla.TABLET_VERTICAL -> 22.dp
        TipoPantalla.TABLET_HORIZONTAL -> 22.dp
    }

    val paddingHorizontalGrid = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 32.dp
        TipoPantalla.TABLET_VERTICAL -> 48.dp
        TipoPantalla.TABLET_HORIZONTAL -> 60.dp
    }

    val espacioHorizontalGrid = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 0.dp
        TipoPantalla.TABLET_VERTICAL -> 42.dp
        TipoPantalla.TABLET_HORIZONTAL -> 52.dp
    }

    val espacioVerticalGrid = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 24.dp
        TipoPantalla.TABLET_VERTICAL -> 46.dp
        TipoPantalla.TABLET_HORIZONTAL -> 28.dp
    }

    // ANIMACIÓN DE LA CABECERA AZUL
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
                alturaNormalInicio
            else
                alturaCabeceraInicial,

        animationSpec = spring(
            dampingRatio = 0.78f,
            stiffness = 220f
        ),
        label = "AlturaCabeceraInicio"
    )
    Scaffold(
        containerColor = colorFondo,
        contentWindowInsets = WindowInsets.systemBars,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondo)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorFondo)
                    .padding(bottom = padding.calculateBottomPadding())
            ) {
                // CABECERA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaCabeceraAnimada)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 35.dp,
                                bottomEnd = 35.dp
                            )
                        )
                        .background(colorEncabezado)
                ) {
                    // BARRA DE BÚSQUEDA
                    OutlinedTextField(
                        value = busqueda,
                        onValueChange = {
                            busqueda = it
                        },
                        modifier = Modifier
                            .fillMaxWidth(
                                when (tipoPantalla) {
                                    TipoPantalla.TELEFONO -> 0.94f
                                    TipoPantalla.TABLET_VERTICAL -> 0.90f
                                    TipoPantalla.TABLET_HORIZONTAL -> 0.82f
                                }
                            )
                            .align(Alignment.TopCenter)
                            .padding(top = alturaStatusBar + 12.dp)
                            .height(alturaBuscador)
                            .shadow(
                                elevation = 5.dp,
                                shape = RoundedCornerShape(40.dp)
                            )
                            .semantics { contentDescription = "Buscador de rutinas" },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = colorLupa,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(40.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colorBuscador,
                            unfocusedContainerColor = colorBuscador,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = colorTextoPrincipal,
                            unfocusedTextColor = colorTextoPrincipal,
                            cursorColor = colorTextoPrincipal
                        )
                    )
                }

                // MIS RUTINAS REALES
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = espacioSuperiorRutinas)
                ) {
                    Text(
                        text = "Mis Rutinas",
                        fontSize = fontSizeTituloRutinas,
                        fontWeight = FontWeight.Bold,
                        color = colorTextoPrincipal,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                bottom = when (tipoPantalla) {
                                    TipoPantalla.TELEFONO -> 18.dp
                                    TipoPantalla.TABLET_VERTICAL -> 28.dp
                                    TipoPantalla.TABLET_HORIZONTAL -> 16.dp
                                }
                            )
                    )

                    // FILTRAR CON EL BUSCADOR
                    val registrosFiltrados =
                        registros.filter { registro ->
                            if (busqueda.isBlank()) {
                                true
                            } else {
                                val resultado = resultadoLavadoDesdeJson(registro.informacion)

                                registro.nombre.contains(
                                    busqueda,
                                    ignoreCase = true
                                ) ||
                                        resultado
                                            ?.lavado
                                            ?.contains(
                                                busqueda,
                                                ignoreCase = true
                                            ) == true ||

                                        resultado
                                            ?.temperatura
                                            ?.contains(
                                                busqueda,
                                                ignoreCase = true
                                            ) == true ||

                                        resultado
                                            ?.composicion
                                            ?.contains(
                                                busqueda,
                                                ignoreCase = true
                                            ) == true
                            }
                        }

                    if (registrosFiltrados.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text =
                                    if (busqueda.isBlank()) {
                                        "Aún no tienes rutinas guardadas."
                                    } else {
                                        "No se encontraron rutinas."
                                    },
                                color = colorTextoSecundario,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        if (esTablet) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(columnasRutinas),
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    start = paddingHorizontalGrid,
                                    end = paddingHorizontalGrid,
                                    bottom = 20.dp
                                ),
                                horizontalArrangement = Arrangement.spacedBy(espacioHorizontalGrid),
                                verticalArrangement = Arrangement.spacedBy(espacioVerticalGrid)
                            ) {
                                items(
                                    items = registrosFiltrados,
                                    key = { it.id }
                                ) { registro ->
                                    TarjetaRutina(
                                        registro = registro,
                                        modoOscuro = modoOscuro,
                                        colorBordeTarjeta = colorBordeTarjeta,
                                        colorTarjetaRutina = colorTarjetaRutina,
                                        colorTextoPrincipal = colorTextoPrincipal,
                                        colorTextoSecundario = colorTextoSecundario,
                                        tamanoLogoRutina = tamanoLogoRutina,
                                        onVerMas = { registroSeleccionado = it },
                                        onEliminar = {
                                            scope.launch {
                                                historialRepository.eliminarRegistro(it)
                                            }
                                        }
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    start = paddingHorizontalGrid,
                                    end = paddingHorizontalGrid,
                                    bottom = 20.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(espacioVerticalGrid)
                            ) {
                                items(
                                    items = registrosFiltrados,
                                    key = { it.id }
                                ) { registro ->
                                    TarjetaRutina(
                                        registro = registro,
                                        modoOscuro = modoOscuro,
                                        colorBordeTarjeta = colorBordeTarjeta,
                                        colorTarjetaRutina = colorTarjetaRutina,
                                        colorTextoPrincipal = colorTextoPrincipal,
                                        colorTextoSecundario = colorTextoSecundario,
                                        tamanoLogoRutina = tamanoLogoRutina,
                                        onVerMas = { registroSeleccionado = it },
                                        onEliminar = {
                                            scope.launch {
                                                historialRepository.eliminarRegistro(it)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }

        // DETALLE DE UNA RUTINA GUARDADA
        registroSeleccionado?.let { registro ->
            val resultado =
                resultadoLavadoDesdeJson(
                    registro.informacion
                )
            if (resultado != null) {
                val colorDialogo = if (modoOscuro) { FondoOscuro } else { FondoClaro }
                val colorTexto = if (modoOscuro) { RosaOscuro } else { AzulPrincipalClaro }
                AlertDialog(
                    onDismissRequest = { registroSeleccionado = null },
                    containerColor = colorDialogo,
                    title = {
                        Text(
                            text = "Recomendación de lavado",
                            color = colorTexto,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = maxAltoDialogo)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "Prendas: ${resultado.cantidad}",
                                color = colorTexto
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Composición:",
                                fontWeight = FontWeight.Bold,
                                color = colorTexto
                            )

                            Text(
                                text = resultado.composicion,
                                color = colorTexto
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Lavado: ",
                                fontWeight = FontWeight.Bold,
                                color = colorTexto
                            )

                            Text(
                                text = resultado.lavado,
                                color = colorTexto
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text =
                                    "Temperatura:",
                                fontWeight = FontWeight.Bold,
                                color = colorTexto
                            )

                            Text(
                                text = resultado.temperatura,
                                color = colorTexto
                            )

                            Spacer(modifier = Modifier.height(14.dp))


                            Text(
                                text = "Blanqueador:",
                                fontWeight = FontWeight.Bold,
                                color = colorTexto
                            )

                            Text(
                                text = resultado.blanqueador,
                                color = colorTexto
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Secado:",
                                fontWeight = FontWeight.Bold,
                                color = colorTexto
                            )

                            Text(
                                text = resultado.secado,
                                color = colorTexto
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Planchado: ",
                                fontWeight = FontWeight.Bold,
                                color = colorTexto
                            )

                            Text(
                                text = resultado.planchado,
                                color = colorTexto
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Limpieza profesional:",
                                fontWeight = FontWeight.Bold,
                                color = colorTexto
                            )

                            Text(
                                text = resultado.limpiezaProfesional,
                                color = colorTexto
                            )

                            if (resultado.tieneMancha) {
                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Tratamiento de la mancha:",
                                    fontWeight = FontWeight.Bold,
                                    color = colorTexto
                                )

                                Text(
                                    text = resultado.tratamientoMancha,
                                    color = colorTexto
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "⚠️ Precauciones:",
                                fontWeight = FontWeight.Bold,
                                color = colorTexto
                            )

                            Text(
                                text = resultado.precauciones,
                                color = colorTexto
                            )

                            if (
                                resultado
                                    .informacionAdicional
                                    .isNotBlank()
                            ) {
                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text =
                                        "Información adicional:",
                                    fontWeight = FontWeight.Bold,
                                    color = colorTexto
                                )

                                Text(
                                    text = resultado.informacionAdicional,
                                    color = colorTexto
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Fecha: ${registro.fecha}",
                                color =
                                    colorTexto.copy(
                                        alpha = 0.7f
                                    ),
                                fontSize = 13.sp
                            )
                        }
                    },

                    confirmButton = {
                        Button(
                            onClick = {
                                registroSeleccionado =
                                    null
                            }
                        ) {
                            Text(
                                "Cerrar"
                            )
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarjetaRutina(
    registro: RegistroEscaneo,
    modoOscuro: Boolean,
    colorBordeTarjeta: Color,
    colorTarjetaRutina: Color,
    colorTextoPrincipal: Color,
    colorTextoSecundario: Color,
    tamanoLogoRutina: Dp,
    onVerMas: (RegistroEscaneo) -> Unit,
    onEliminar: (RegistroEscaneo) -> Unit
) {
    val resultado = resultadoLavadoDesdeJson(registro.informacion)
    val estadoDeslizar = rememberSwipeToDismissBoxState(
        confirmValueChange = { estado ->
            if (estado == SwipeToDismissBoxValue.EndToStart) {
                onEliminar(registro)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = estadoDeslizar,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(end = 22.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar rutina",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = if (modoOscuro) 2.dp else 0.dp,
                    color = colorBordeTarjeta,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(enabled = resultado != null) {
                    onVerMas(registro)
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = colorTarjetaRutina),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.washlylogo_inicio),
                    contentDescription = "Rutina de lavado",
                    modifier = Modifier
                        .size(tamanoLogoRutina)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = resultado?.composicion?.takeIf {
                            it.isNotBlank() && !it.equals("No visible", true)
                        } ?: registro.nombre,
                        fontWeight = FontWeight.SemiBold,
                        color = colorTextoPrincipal
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    if (resultado != null) {
                        Text(
                            text = "${resultado.cantidad} prenda(s)",
                            color = colorTextoSecundario,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Lavado: ${resultado.lavado}",
                            color = colorTextoSecundario,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "Temperatura: ${resultado.temperatura}",
                            color = colorTextoSecundario,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    } else {
                        Text(
                            text = registro.fecha,
                            color = colorTextoSecundario,
                            fontSize = 14.sp
                        )
                        Text(
                            text = registro.informacion,
                            color = colorTextoSecundario,
                            fontSize = 14.sp,
                            maxLines = 2
                        )
                    }
                }

                if (resultado != null) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Ver recomendación",
                        tint = colorTextoPrincipal
                    )
                }
            }
        }
    }
}
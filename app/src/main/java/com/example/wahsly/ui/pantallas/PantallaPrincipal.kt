package com.example.wahsly.ui.pantallas

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.zIndex
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
import com.example.wahsly.ia.ResultadoLavado
import com.example.wahsly.ia.resultadoLavadoDesdeJson
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun PantallaPrincipal(
    usuario: Usuario?,
    modoOscuro: Boolean,
    onPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    mostrarBarraInferior: Boolean = true,
    animarCabecera: Boolean = false
){

    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val historialRepository = remember { HistorialRepository(db.historialDao()) }
    val correoUsuario = usuario?.correo.orEmpty()
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
    var busqueda by remember {
        mutableStateOf("")
    }

    var tabSeleccionado by remember {
        mutableIntStateOf(0)
    }

    var mostrarMenu by remember {
        mutableStateOf(false)
    }

    var registroSeleccionado by remember {
        mutableStateOf<RegistroEscaneo?>(null)
    }

    val scope = rememberCoroutineScope()

    // Colores
    val colorFondo =
        if (modoOscuro) FondoOscuro else FondoClaro

    val colorEncabezado =
        if (modoOscuro) RosaOscuro else AzulPrincipalClaro

    val colorFranjaMenu =
        if (modoOscuro) RosaOscuro else AzulPrincipalClaro

    val colorBarraInferior =
        if (modoOscuro) RosaOscuro else AzulPrincipalClaro

    val colorTextoPrincipal =
        if (modoOscuro) RosaOscuro else AzulTextoClaro

    val colorTextoSecundario =
        if (modoOscuro) CremaOscuro else Color.Gray

    val colorLupa =
        if (modoOscuro) RosaOscuro else FondoOscuro

    val color3Barras =
        if (modoOscuro) FondoOscuro else CremaOscuro

    val colorBuscador =
        if (modoOscuro) FondoOscuro else CremaOscuro

    val colorTarjetaRutina =
        if (modoOscuro) FondoOscuro else TarjetaRutinaClaro

    val colorBordeTarjeta =
        if (modoOscuro) RosaOscuro else Color.Transparent

    val colorSeleccionado =
        if (modoOscuro) CremaOscuro else Color.White

    val colorIconosBarra =
        if (modoOscuro) FondoOscuro else IconoSecundarioClaro

    val colorTextoBarra =
        if (modoOscuro) FondoOscuro else Color.White

    val franja =
        if (modoOscuro) RosaOscuro else FondoOscuro

    val franjaTexto =
        if (modoOscuro) FondoOscuro else CremaOscuro

    // Altura donde Android muestra hora, batería, WiFi, etc.
    val alturaStatusBar = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()


// ANIMACIÓN DE LA CABECERA AZUL


    var iniciarAnimacionCabecera by remember(animarCabecera) {
        mutableStateOf(!animarCabecera)
    }

    LaunchedEffect(animarCabecera) {
        if (animarCabecera) {
            iniciarAnimacionCabecera = true
        }
    }

    val alturaNormalInicio =
        alturaStatusBar + 92.dp

    val alturaCabeceraAnimada by animateDpAsState(
        targetValue =
            if (iniciarAnimacionCabecera)
                alturaNormalInicio
            else
                235.dp,

        animationSpec = spring(
            dampingRatio = 0.78f,
            stiffness = 220f
        ),

        label = "AlturaCabeceraInicio"
    )

    Scaffold(
        containerColor = colorFondo,
        contentWindowInsets = WindowInsets.systemBars,

        // BARRA INFERIOR
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
                        .padding(
                            bottom = padding.calculateBottomPadding()
                        )
                ) {
                    // CABECERA
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(alturaCabeceraAnimada)
                            .background(colorEncabezado)
                    ) {
                        // BARRA DE BÚSQUEDA
                        OutlinedTextField(
                            value = busqueda,
                            onValueChange = {
                                busqueda = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 15.dp,
                                    end = 15.dp,
                                    top = alturaStatusBar + 12.dp
                                )
                                .height(58.dp)
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

                    // FRANJA DEL MENÚ
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 35.dp,
                                    bottomEnd = 35.dp
                                )
                            )
                            .background(franja)
                    ) {

                        IconButton(
                            onClick = {
                                mostrarMenu = !mostrarMenu
                            },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 12.dp)
                        ) {

                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = color3Barras,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    // MIS RUTINAS REALES
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(top = 16.dp)
                    ) {

                        Text(
                            text = "Mis Rutinas",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorTextoPrincipal,
                            modifier = Modifier
                                .align(
                                    Alignment.CenterHorizontally
                                )
                                .padding(bottom = 12.dp)
                        )


                        // FILTRAR CON EL BUSCADOR
                        val registrosFiltrados =
                            registros.filter { registro ->

                                if (busqueda.isBlank()) {

                                    true

                                } else {

                                    val resultado =
                                        resultadoLavadoDesdeJson(
                                            registro.informacion
                                        )


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

                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Text(
                                    text =
                                        if (busqueda.isBlank()) {
                                            "Aún no tienes rutinas guardadas."
                                        } else {
                                            "No se encontraron rutinas."
                                        },

                                    color =
                                        colorTextoSecundario,

                                    fontSize =
                                        16.sp
                                )
                            }

                        } else {

                            LazyColumn(
                                modifier =
                                    Modifier.fillMaxSize(),

                                contentPadding =
                                    PaddingValues(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 20.dp
                                    ),

                                verticalArrangement =
                                    Arrangement.spacedBy(
                                        10.dp
                                    )
                            ) {

                                items(
                                    items =
                                        registrosFiltrados,

                                    key = {
                                        it.id
                                    }
                                ) { registro ->


                                    val resultado =
                                        resultadoLavadoDesdeJson(
                                            registro.informacion
                                        )


                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width =
                                                    if (modoOscuro) {
                                                        2.dp
                                                    } else {
                                                        0.dp
                                                    },

                                                color =
                                                    colorBordeTarjeta,

                                                shape =
                                                    RoundedCornerShape(
                                                        12.dp
                                                    )
                                            )
                                            .clickable(
                                                enabled =
                                                    resultado != null
                                            ) {

                                                registroSeleccionado =
                                                    registro
                                            },

                                        shape =
                                            RoundedCornerShape(
                                                12.dp
                                            ),

                                        colors =
                                            CardDefaults.cardColors(
                                                containerColor =
                                                    colorTarjetaRutina
                                            ),

                                        elevation =
                                            CardDefaults.cardElevation(
                                                2.dp
                                            )
                                    ) {

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),

                                            verticalAlignment =
                                                Alignment.CenterVertically
                                        ) {


                                            Image(
                                                painter =
                                                    painterResource(
                                                        id =
                                                            R.drawable
                                                                .washlylogo_inicio
                                                    ),

                                                contentDescription =
                                                    "Rutina de lavado",

                                                modifier =
                                                    Modifier
                                                        .size(56.dp)
                                                        .clip(
                                                            RoundedCornerShape(
                                                                8.dp
                                                            )
                                                        )
                                            )


                                            Spacer(
                                                modifier =
                                                    Modifier.width(
                                                        16.dp
                                                    )
                                            )


                                            Column(
                                                modifier =
                                                    Modifier.weight(
                                                        1f
                                                    )
                                            ) {

                                                Text(
                                                    text =
                                                        resultado
                                                            ?.composicion
                                                            ?.takeIf {
                                                                it.isNotBlank() &&
                                                                        !it.equals(
                                                                            "No visible",
                                                                            true
                                                                        )
                                                            }
                                                            ?: registro.nombre,

                                                    fontWeight =
                                                        FontWeight.SemiBold,

                                                    color =
                                                        colorTextoPrincipal
                                                )


                                                Spacer(
                                                    modifier =
                                                        Modifier.height(
                                                            3.dp
                                                        )
                                                )


                                                if (resultado != null) {

                                                    Text(
                                                        text =
                                                            "${resultado.cantidad} prenda(s)",

                                                        color =
                                                            colorTextoSecundario,

                                                        fontSize =
                                                            14.sp
                                                    )


                                                    Text(
                                                        text =
                                                            "Lavado: ${resultado.lavado}",

                                                        color =
                                                            colorTextoSecundario,

                                                        fontSize =
                                                            14.sp,

                                                        maxLines =
                                                            1
                                                    )


                                                    Text(
                                                        text =
                                                            "Temperatura: ${resultado.temperatura}",

                                                        color =
                                                            colorTextoSecundario,

                                                        fontSize =
                                                            14.sp,

                                                        maxLines =
                                                            1
                                                    )

                                                } else {

                                                    // REGISTROS ANTIGUOS
                                                    Text(
                                                        text =
                                                            registro.fecha,

                                                        color =
                                                            colorTextoSecundario,

                                                        fontSize =
                                                            14.sp
                                                    )

                                                    Text(
                                                        text =
                                                            registro.informacion,

                                                        color =
                                                            colorTextoSecundario,

                                                        fontSize =
                                                            14.sp,

                                                        maxLines =
                                                            2
                                                    )
                                                }
                                            }


                                            if (resultado != null) {

                                                Icon(
                                                    imageVector =
                                                        Icons.Default
                                                            .KeyboardArrowRight,

                                                    contentDescription =
                                                        "Ver recomendación",

                                                    tint =
                                                        colorTextoPrincipal
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                        AnimatedVisibility(
                            visible = mostrarMenu,

                            enter =
                                slideInHorizontally(
                                    initialOffsetX = { -it },
                                    animationSpec = spring(
                                        dampingRatio = 0.72f,
                                        stiffness = 380f
                                    )
                                ) +
                                        fadeIn(
                                            animationSpec = tween(180)
                                        ),

                            exit =
                                slideOutHorizontally(
                                    targetOffsetX = { -it },
                                    animationSpec = tween(260)
                                ) +
                                        fadeOut(
                                            animationSpec = tween(180)
                                        ),

                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(
                                    start = 8.dp,
                                    top = alturaStatusBar + 150.dp
                                )
                                .zIndex(20f)
                        ) {

                            Surface(
                                modifier = Modifier.width(305.dp),
                                shape = RoundedCornerShape(
                                    topEnd = 30.dp,
                                    bottomEnd = 30.dp
                                ),
                                color = franja,
                                shadowElevation = 8.dp
                            ) {

                                Column(
                                    modifier = Modifier
                                        .padding(14.dp)
                                        .clip(
                                            RoundedCornerShape(
                                                topEnd = 24.dp,
                                                bottomEnd = 24.dp
                                            )
                                        )
                                        .background(
                                            if (modoOscuro)
                                                FondoOscuro
                                            else
                                                FondoClaro
                                        )
                                        .padding(
                                            horizontal = 14.dp,
                                            vertical = 14.dp
                                        )
                                ) {

                                    // SALUDO
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .clip(RoundedCornerShape(30.dp))
                                            .background(franja)
                                            .padding(horizontal = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(Color.White),
                                            contentAlignment = Alignment.Center
                                        ) {

                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                tint = AzulPrincipalClaro,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Spacer(
                                            modifier = Modifier.width(10.dp)
                                        )

                                        Text(
                                            text = "¡Hola, ${usuario?.nombre ?: "Usuario"}!",
                                            color = franjaTexto,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.height(22.dp)
                                    )

                                    Text(
                                        text = "General",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color =
                                            if (modoOscuro)
                                                CremaOscuro
                                            else
                                                Color(0xFF211421)
                                    )

                                    Spacer(
                                        modifier = Modifier.height(10.dp)
                                    )

                                    OpcionMenuAnimado(
                                        texto = "Tipos de lavado",
                                        modoOscuro = modoOscuro
                                    ) {
                                        mostrarMenu = false

                                        Toast.makeText(
                                            context,
                                            "Tipos de lavado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    OpcionMenuAnimado(
                                        texto = "Tipos de Tela",
                                        modoOscuro = modoOscuro
                                    ) {
                                        mostrarMenu = false

                                        Toast.makeText(
                                            context,
                                            "Tipos de Tela",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    OpcionMenuAnimado(
                                        texto = "Productos",
                                        modoOscuro = modoOscuro
                                    ) {
                                        mostrarMenu = false

                                        Toast.makeText(
                                            context,
                                            "Productos",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    OpcionMenuAnimado(
                                        texto = "Recomendaciones",
                                        modoOscuro = modoOscuro
                                    ) {
                                        mostrarMenu = false

                                        Toast.makeText(
                                            context,
                                            "Recomendaciones",
                                            Toast.LENGTH_SHORT
                                        ).show()
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
                                .heightIn(max = 520.dp)
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

@Composable
fun OpcionMenuAnimado(
    texto: String,
    modoOscuro: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                vertical = 12.dp,
                horizontal = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = texto,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            color =
                if (modoOscuro)
                    CremaOscuro
                else
                    Color(0xFF514C50)
        )

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint =
                if (modoOscuro)
                    CremaOscuro
                else
                    Color(0xFF202020),
            modifier = Modifier.size(26.dp)
        )
    }
}


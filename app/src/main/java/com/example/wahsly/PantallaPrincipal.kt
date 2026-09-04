package com.example.wahsly

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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    var busqueda by remember {
        mutableStateOf("")
    }

    var tabSeleccionado by remember {
        mutableIntStateOf(0)
    }

    var mostrarMenu by remember {
        mutableStateOf(false)
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
                                ),
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



                    // CONTENIDO CENTRAL: "Mis Rutinas"
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(top = 16.dp) // separación de la franja del menú
                    ) {

                        // Bloque de "Mis Rutinas"
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Mis Rutinas",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorTextoPrincipal,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Tarjeta 1
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .border(
                                        width = if (modoOscuro) 2.dp else 0.dp,
                                        color = colorBordeTarjeta,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorTarjetaRutina
                                ),
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
                                        contentDescription = "Imagen de rutina 1",
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    // Textos
                                    Column {
                                        Text(
                                            "100% Polyester",
                                            fontWeight = FontWeight.Medium,
                                            color = colorTextoPrincipal
                                        )
                                        Text(
                                            "100%",
                                            color = colorTextoSecundario,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            "TEXTO",
                                            color = colorTextoSecundario,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }

                            // Tarjeta 2
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .border(
                                        width = if (modoOscuro) 2.dp else 0.dp,
                                        color = colorBordeTarjeta,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorTarjetaRutina
                                ),
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
                                        contentDescription = "Imagen de rutina 2",
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    // Textos
                                    Column {
                                        Text(
                                            "Algodón 100%",
                                            fontWeight = FontWeight.Medium,
                                            color = colorTextoPrincipal
                                        )
                                        Text(
                                            "Lavado en frío",
                                            color = colorTextoSecundario,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            "Ejemplo de rutina",
                                            color = colorTextoSecundario,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }

                        // CONTENIDO ORIGINAL: historial (si existe)
                        if (BaseDatosHistorial.registros.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(colorFondo),
                                contentPadding = PaddingValues(
                                    start = 32.dp,
                                    end = 32.dp,
                                    top = 16.dp,
                                    bottom = 25.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(BaseDatosHistorial.registros) { registro ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        elevation = CardDefaults.cardElevation(4.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(16.dp)
                                        ) {
                                            Text(
                                                text = registro.nombre,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = colorTextoPrincipal
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = registro.fecha,
                                                fontSize = 14.sp,
                                                color = colorTextoSecundario
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = registro.informacion,
                                                fontSize = 14.sp,
                                                color = colorTextoPrincipal
                                            )
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


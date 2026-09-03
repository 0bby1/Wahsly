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

@Composable
fun PantallaPrincipal(
    usuario: Usuario?,
    modoOscuro: Boolean,
    onPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {

    val context = LocalContext.current
    var busqueda by remember {
        mutableStateOf("")
    }
    var mostrarMenu by remember {
        mutableStateOf(false)
    }

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
        if (modoOscuro) CremaOscuro else AzulTextoClaro

    val colorTextoSecundario =
        if (modoOscuro) CremaOscuro else Color.Gray

    val colorLupa =
        if (modoOscuro) RosaOscuro else RosaClaro

    val colorBuscador =
        if (modoOscuro) FondoOscuro else Color.White

    val colorTarjetaRutina =
        if (modoOscuro) FondoOscuro else TarjetaRutinaClaro

    val colorBordeTarjeta =
        if (modoOscuro) RosaOscuro else Color.Transparent

    val colorSeleccionado =
        if (modoOscuro) CremaOscuro else Color.White

    val colorIconosBarra =
        if (modoOscuro) CremaOscuro else IconoSecundarioClaro

    val colorTextoBarra =
        if (modoOscuro) CremaOscuro else Color.White

    // Altura donde Android muestra hora, batería, WiFi, etc.
    val alturaStatusBar = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()

    Scaffold(
        containerColor = colorFondo,
        contentWindowInsets = WindowInsets.systemBars,

        // BARRA INFERIOR
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
                        // ---------------- INICIO ----------------
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
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Inicio",
                                    tint = colorBarraInferior,
                                    modifier = Modifier.size(34.dp)
                                )

                                Text(
                                    text = "Inicio",
                                    fontSize = 12.sp,
                                    color = colorBarraInferior
                                )
                            }
                        }

                        // ---------------- ESCANEAR ----------------
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

                        // ---------------- CUENTA ----------------
                        Column(
                            modifier = Modifier
                                .width(105.dp)
                                .clickable {
                                    onPerfil()
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Cuenta",
                                tint = colorIconosBarra,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Cuenta",
                                fontSize = 12.sp,
                                color = colorTextoBarra
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
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
                    .height(alturaStatusBar + 92.dp)
                    .background(
                        color = colorEncabezado,
                        shape = RectangleShape
                    )
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
                    .background(colorFranjaMenu)
            ) {
                IconButton(
                    onClick = {
                        mostrarMenu = true
                    },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = if (modoOscuro){colorFondo} else {colorLupa},
                        modifier = Modifier.size(34.dp)
                    )
                }

                // MENÚ DESPLEGABLE
                DropdownMenu(
                    expanded = mostrarMenu,
                    onDismissRequest = {
                        mostrarMenu = false
                    }
                ) {
                    // Saludo con el nombre del usuario
                    Text(
                        text = "¡Hola, ${usuario?.nombre ?: "Usuario"}!",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // Encabezado "General"
                    Text(
                        text = "General",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // Opciones con flecha
                    DropdownMenuItem(
                        text = { Text("Tipos de lavado") },
                        trailingIcon = { Icon(Icons.Default.ArrowForward, contentDescription = null) },
                        onClick = {
                            mostrarMenu = false
                            Toast.makeText(context, "Tipos de lavado", Toast.LENGTH_SHORT).show()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Tipos de Tela") },
                        trailingIcon = { Icon(Icons.Default.ArrowForward, contentDescription = null) },
                        onClick = {
                            mostrarMenu = false
                            Toast.makeText(context, "Tipos de Tela", Toast.LENGTH_SHORT).show()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Productos") },
                        trailingIcon = { Icon(Icons.Default.ArrowForward, contentDescription = null) },
                        onClick = {
                            mostrarMenu = false
                            Toast.makeText(context, "Productos", Toast.LENGTH_SHORT).show()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Recomendaciones") },
                        trailingIcon = { Icon(Icons.Default.ArrowForward, contentDescription = null) },
                        onClick = {
                            mostrarMenu = false
                            Toast.makeText(context, "Recomendaciones", Toast.LENGTH_SHORT).show()
                        }
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
    }
}

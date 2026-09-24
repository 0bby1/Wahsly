package com.example.wahsly.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wahsly.auth.AutenticadorLocal
import com.example.wahsly.auth.CredencialesIncorrectasException
import com.example.wahsly.datos.database.AppDatabase
import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.datos.repository.UsuarioRepository
import com.example.wahsly.ui.componentes.BotonCrearCuenta
import com.example.wahsly.ui.componentes.BotonDegradado
import com.example.wahsly.ui.theme.AzulPrincipalClaro
import com.example.wahsly.ui.theme.AzulTextoClaro
import com.example.wahsly.ui.theme.CremaOscuro
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.theme.TextoBlancoClaro
import com.example.wahsly.ui.theme.TextoSecundarioClaro
import com.example.wahsly.ui.theme.TextoSecundarioOscuro
import kotlinx.coroutines.launch
import com.example.wahsly.AnimacionesVectoriales.WashlyBienvenidaAnimado
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.example.wahsly.utilidades.TipoPantalla
import com.example.wahsly.utilidades.obtenerTipoPantalla
import androidx.compose.ui.unit.Dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicioSesion(
    modoOscuro: Boolean,
    windowSizeClass: WindowSizeClass,
    onCrearCuenta: () -> Unit,
    onLoginExitoso: (Usuario) -> Unit
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val repository = remember { UsuarioRepository(db.usuarioDao()) }
    val autenticador = remember { AutenticadorLocal(repository) }
    val scope = rememberCoroutineScope()

    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }

    val tipoPantalla = obtenerTipoPantalla(windowSizeClass)

    val logoSize = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 330.dp
        TipoPantalla.TABLET_VERTICAL -> 600.dp
        TipoPantalla.TABLET_HORIZONTAL -> 500.dp
    }

    val anchoCampos = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 360.dp
        TipoPantalla.TABLET_VERTICAL -> 440.dp
        TipoPantalla.TABLET_HORIZONTAL -> 420.dp
    }

    val anchoBotonTelefono = 210.dp
    val anchoBotonTablet = 240.dp

    // Colores
    val colorFondo = if (modoOscuro) FondoOscuro else FondoClaro
    val colorCampo = if (modoOscuro) CremaOscuro else AzulPrincipalClaro
    val colorTextoPrincipal = if (modoOscuro) AzulTextoClaro else TextoBlancoClaro
    val colorTextoSecundario = if (modoOscuro) TextoSecundarioOscuro else TextoSecundarioClaro
    val colorIcono = if (modoOscuro) AzulTextoClaro else TextoBlancoClaro

    // Lógica de inicio de sesión
    val onIniciarSesionClick: () -> Unit = {
        scope.launch {
            try {
                val usuario = autenticador.iniciarSesion(correo, contrasena)
                Toast.makeText(context, "Bienvenido ${usuario.nombre}", Toast.LENGTH_LONG).show()
                onLoginExitoso(usuario)
            } catch (e: IllegalArgumentException) {
                Toast.makeText(context, e.message ?: "Datos inválidos", Toast.LENGTH_SHORT).show()
            } catch (e: CredencialesIncorrectasException) {
                Toast.makeText(context, e.message ?: "Error", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Ocurrió un error inesperado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
    ) {
        when (tipoPantalla) {
            // TELÉFONO
            TipoPantalla.TELEFONO -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(20.dp))

                    WashlyBienvenidaAnimado(
                        modoOscuro = modoOscuro,
                        tamano = logoSize
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    Column(
                        modifier = Modifier.width(anchoCampos)
                    ) {
                        CampoCorreo(
                            correo = correo,
                            onValueChange = { correo = it },
                            colorCampo = colorCampo,
                            colorTextoPrincipal = colorTextoPrincipal,
                            colorIcono = colorIcono
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CampoContrasena(
                            contrasena = contrasena,
                            onValueChange = { contrasena = it },
                            mostrarContrasena = mostrarContrasena,
                            onMostrarChange = { mostrarContrasena = it },
                            colorCampo = colorCampo,
                            colorTextoPrincipal = colorTextoPrincipal,
                            colorIcono = colorIcono
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable {
                                    Toast.makeText(
                                        context,
                                        "Recuperación de contraseña",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                            color = colorTextoSecundario,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    BotonesAccion(
                        onIniciarSesionClick = onIniciarSesionClick,
                        onCrearCuentaClick = onCrearCuenta,
                        modoOscuro = modoOscuro,
                        tipoPantalla = tipoPantalla,
                        colorTextoSecundario = colorTextoSecundario,
                        anchoBotonTelefono = anchoBotonTelefono,
                        anchoBotonTablet = anchoBotonTablet
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            // TABLET VERTICAL
            TipoPantalla.TABLET_VERTICAL -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 36.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(18.dp))

                    WashlyBienvenidaAnimado(
                        modoOscuro = modoOscuro,
                        tamano = logoSize
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Column(
                        modifier = Modifier.width(anchoCampos)
                    ) {
                        CampoCorreo(
                            correo = correo,
                            onValueChange = { correo = it },
                            colorCampo = colorCampo,
                            colorTextoPrincipal = colorTextoPrincipal,
                            colorIcono = colorIcono
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        CampoContrasena(
                            contrasena = contrasena,
                            onValueChange = { contrasena = it },
                            mostrarContrasena = mostrarContrasena,
                            onMostrarChange = { mostrarContrasena = it },
                            colorCampo = colorCampo,
                            colorTextoPrincipal = colorTextoPrincipal,
                            colorIcono = colorIcono
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            modifier = Modifier
                                .align(Alignment.Start)
                                .clickable {
                                    Toast.makeText(
                                        context,
                                        "Recuperación de contraseña",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                            color = colorTextoSecundario,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    BotonesAccion(
                        onIniciarSesionClick = onIniciarSesionClick,
                        onCrearCuentaClick = onCrearCuenta,
                        modoOscuro = modoOscuro,
                        tipoPantalla = tipoPantalla,
                        colorTextoSecundario = colorTextoSecundario,
                        anchoBotonTelefono = anchoBotonTelefono,
                        anchoBotonTablet = anchoBotonTablet
                    )
                }
            }


            // TABLET HORIZONTAL
            TipoPantalla.TABLET_HORIZONTAL -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = 56.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(42.dp)
                    ) {
                        Box(
                            modifier = Modifier.weight(0.9f),
                            contentAlignment = Alignment.Center
                        ) {
                            WashlyBienvenidaAnimado(
                                modoOscuro = modoOscuro,
                                tamano = logoSize
                            )
                        }
                        Column(
                            modifier = Modifier.weight(1.1f)
                        ) {
                            Column(
                                modifier = Modifier.width(anchoCampos)
                            ) {
                                CampoCorreo(
                                    correo = correo,
                                    onValueChange = { correo = it },
                                    colorCampo = colorCampo,
                                    colorTextoPrincipal = colorTextoPrincipal,
                                    colorIcono = colorIcono
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                CampoContrasena(
                                    contrasena = contrasena,
                                    onValueChange = { contrasena = it },
                                    mostrarContrasena = mostrarContrasena,
                                    onMostrarChange = { mostrarContrasena = it },
                                    colorCampo = colorCampo,
                                    colorTextoPrincipal = colorTextoPrincipal,
                                    colorIcono = colorIcono
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "¿Olvidaste tu contraseña?",
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .clickable {
                                            Toast.makeText(
                                                context,
                                                "Recuperación de contraseña",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                    color = colorTextoSecundario,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(34.dp))

                    BotonesAccion(
                        onIniciarSesionClick = onIniciarSesionClick,
                        onCrearCuentaClick = onCrearCuenta,
                        modoOscuro = modoOscuro,
                        tipoPantalla = tipoPantalla,
                        colorTextoSecundario = colorTextoSecundario,
                        anchoBotonTelefono = anchoBotonTelefono,
                        anchoBotonTablet = anchoBotonTablet
                    )
                }
            }
        }
    }
}

// COMPONENTES REUTILIZABLES
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoCorreo(
    correo: String,
    onValueChange: (String) -> Unit,
    colorCampo: Color,
    colorTextoPrincipal: Color,
    colorIcono: Color
) {
    OutlinedTextField(
        value = correo,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        placeholder = {
            Text("Correo electrónico", fontSize = 16.sp, color = colorTextoPrincipal)
        },
        leadingIcon = {
            Icon(
                Icons.Default.Email,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(22.dp)
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorCampo,
            unfocusedContainerColor = colorCampo,
            focusedBorderColor = colorCampo,
            unfocusedBorderColor = colorCampo,
            cursorColor = colorTextoPrincipal,
            focusedTextColor = colorTextoPrincipal,
            unfocusedTextColor = colorTextoPrincipal
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoContrasena(
    contrasena: String,
    onValueChange: (String) -> Unit,
    mostrarContrasena: Boolean,
    onMostrarChange: (Boolean) -> Unit,
    colorCampo: Color,
    colorTextoPrincipal: Color,
    colorIcono: Color
) {
    OutlinedTextField(
        value = contrasena,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        placeholder = {
            Text("Contraseña", fontSize = 16.sp, color = colorTextoPrincipal)
        },
        leadingIcon = {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(22.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = { onMostrarChange(!mostrarContrasena) }) {
                Icon(
                    imageVector = if (mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (mostrarContrasena) "Ocultar" else "Mostrar",
                    tint = colorIcono
                )
            }
        },
        visualTransformation = if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorCampo,
            unfocusedContainerColor = colorCampo,
            focusedBorderColor = colorCampo,
            unfocusedBorderColor = colorCampo,
            cursorColor = colorTextoPrincipal,
            focusedTextColor = colorTextoPrincipal,
            unfocusedTextColor = colorTextoPrincipal
        )
    )
}

@Composable
fun BotonesAccion(
    onIniciarSesionClick: () -> Unit,
    onCrearCuentaClick: () -> Unit,
    modoOscuro: Boolean,
    tipoPantalla: TipoPantalla,
    colorTextoSecundario: Color,
    anchoBotonTelefono: Dp,
    anchoBotonTablet: Dp
) {
    when (tipoPantalla) {

        // TELÉFONO
        TipoPantalla.TELEFONO -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.width(anchoBotonTelefono)) {
                    BotonDegradado(
                        texto = "Iniciar Sesión",
                        modoOscuro = modoOscuro,
                        onClick = onIniciarSesionClick
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "¿Aún no tienes una cuenta?",
                    color = colorTextoSecundario,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(modifier = Modifier.width(anchoBotonTelefono)) {
                    BotonCrearCuenta(
                        onClick = onCrearCuentaClick,
                        modoOscuro = modoOscuro
                    )
                }
            }
        }


        // TABLET VERTICAL / HORIZONTAL
        TipoPantalla.TABLET_VERTICAL,
        TipoPantalla.TABLET_HORIZONTAL -> {
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(26.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.width(anchoBotonTablet)) {
                        BotonDegradado(
                            texto = "Iniciar Sesión",
                            modoOscuro = modoOscuro,
                            onClick = onIniciarSesionClick
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.width(anchoBotonTablet)) {
                        BotonCrearCuenta(
                            onClick = onCrearCuentaClick,
                            modoOscuro = modoOscuro
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "¿Aún no tienes una cuenta?",
                        color = colorTextoSecundario,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
package com.example.wahsly.ui.pantallas

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicioSesion(
    modoOscuro: Boolean,
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

    // Detectar configuración de pantalla
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isPhone = configuration.screenWidthDp < 600 // Si el ancho es menor a 600dp, es un celular

    // Tamaños dinámicos según el dispositivo
    val paddingHorizontal = if (isPhone) 24.dp else 48.dp
    val logoSize = when {
        isPhone && isLandscape -> 180.dp // Celular acostado
        isPhone && !isLandscape -> 220.dp // Celular parado
        !isPhone && isLandscape -> 380.dp // Tablet acostada
        else -> 320.dp // Tablet parada
    }

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

    Box(modifier = Modifier.fillMaxSize().background(colorFondo)) {
        if (isLandscape) {
            // ==========================================
            // DISEÑO HORIZONTAL (Celular y Tablet acostados)
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Menos padding general para pantallas bajitas
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mitad Izquierda: Logo Washly
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    WashlyBienvenidaAnimado(
                        modifier = Modifier.size(logoSize),
                        modoOscuro = modoOscuro,
                        tamano = logoSize
                    )
                }

                // Mitad Derecha: Formulario
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = paddingHorizontal)
                        .verticalScroll(rememberScrollState()), // Vital para celular horizontal
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CampoCorreo(correo, { correo = it }, colorCampo, colorTextoPrincipal, colorIcono)
                    Spacer(modifier = Modifier.height(16.dp))
                    CampoContrasena(contrasena, { contrasena = it }, mostrarContrasena, { mostrarContrasena = it }, colorCampo, colorTextoPrincipal, colorIcono)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        modifier = Modifier.align(Alignment.Start).clickable {
                            Toast.makeText(context, "Recuperación de contraseña", Toast.LENGTH_SHORT).show()
                        },
                        color = colorTextoSecundario,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    BotonesAccion(onIniciarSesionClick, onCrearCuenta, modoOscuro)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¿Aún no tienes una cuenta?",
                        color = colorTextoSecundario,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            // ==========================================
            // DISEÑO VERTICAL (Celular y Tablet parados)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = paddingHorizontal, vertical = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                WashlyBienvenidaAnimado(
                    modifier = Modifier.size(logoSize),
                    modoOscuro = modoOscuro,
                    tamano = logoSize
                )
                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier.widthIn(max = 500.dp) // En celular no hace nada, en tablet limita el ancho
                ) {
                    CampoCorreo(correo, { correo = it }, colorCampo, colorTextoPrincipal, colorIcono)
                    Spacer(modifier = Modifier.height(16.dp))
                    CampoContrasena(contrasena, { contrasena = it }, mostrarContrasena, { mostrarContrasena = it }, colorCampo, colorTextoPrincipal, colorIcono)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        modifier = Modifier.align(Alignment.Start).clickable {
                            Toast.makeText(context, "Recuperación de contraseña", Toast.LENGTH_SHORT).show()
                        },
                        color = colorTextoSecundario,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    BotonesAccion(onIniciarSesionClick, onCrearCuenta, modoOscuro)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¿Aún no tienes una cuenta?",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = colorTextoSecundario,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// ==========================================
// COMPONENTES REUTILIZABLES
// ==========================================

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
            .height(60.dp), // Un poquito más bajito para que quepa mejor en celular
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
    modoOscuro: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp) // Menos espacio entre botones para celular
    ) {
        Box(modifier = Modifier.weight(1f)) {
            BotonDegradado(
                texto = "Iniciar Sesión",
                modoOscuro = modoOscuro,
                onClick = onIniciarSesionClick
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            BotonCrearCuenta(
                onClick = onCrearCuentaClick,
                modoOscuro = modoOscuro
            )
        }
    }
}
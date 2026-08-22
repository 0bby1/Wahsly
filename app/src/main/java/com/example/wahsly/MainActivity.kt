package com.example.wahsly

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.layout.statusBars
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


// cesar@gmail.com
// 1234

class MainActivity : ComponentActivity() {                      //  G

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() //R
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                AndroidColor.rgb(200, 217, 230)
            )
        )

        setContent {

            MaterialTheme {

                // Guarda qué pantalla se está mostrando actualmente
                var pantallaActual by remember {
                    mutableStateOf("LOGIN")
                }

                // Guarda qué usuario inició sesión
                var usuarioActual by remember {
                    mutableStateOf<Usuario?>(null)
                }

                when (pantallaActual) {

                    // ---------------- LOGIN ----------------
                    "LOGIN" -> {
                        PantallaInicioSesion(

                            // Si quiere crear una cuenta
                            onCrearCuenta = {
                                pantallaActual = "REGISTRO"
                            },

                            // Si inicia sesión correctamente
                            onLoginExitoso = { usuario ->
                                usuarioActual = usuario
                                pantallaActual = "INICIO"
                            }
                        )
                    }

                    // ---------------- REGISTRO ----------------
                    "REGISTRO" -> {
                        PantallaRegistro(

                            onRegistroExitoso = {

                                pantallaActual = "LOGIN"

                                Toast.makeText(
                                    this,
                                    "Cuenta creada. Inicia sesión.",
                                    Toast.LENGTH_LONG
                                ).show()
                            },

                            onVolverLogin = {
                                pantallaActual = "LOGIN"
                            }
                        )
                    }

                    // ---------------- PANTALLA PRINCIPAL ----------------
                    "INICIO" -> {
                        PantallaPrincipal(

                            usuario = usuarioActual,

                            // Abre la pantalla de historial
                            onHistorial = {
                                pantallaActual = "HISTORIAL"
                            },

                            // Abre la pantalla de perfil
                            onPerfil = {
                                pantallaActual = "PERFIL"
                            },

                            // Cerrar sesión
                            onCerrarSesion = {
                                usuarioActual = null
                                pantallaActual = "LOGIN"
                            }
                        )
                    }

                    // ---------------- HISTORIAL ----------------
                    "HISTORIAL" -> {

                        PantallaHistorial(

                            // Regresa a Inicio
                            onVolver = {
                                pantallaActual = "INICIO"
                            }
                        )
                    }

                    // ---------------- PERFIL ----------------
                    "PERFIL" -> {

                        PantallaPerfil(

                            usuario = usuarioActual,

                            onVolver = {
                                pantallaActual = "INICIO"
                            },

                            onCerrarSesion = {
                                usuarioActual = null
                                pantallaActual = "LOGIN"
                            }
                        )
                    }
                }
            }
        }
    } }                                                                     //

    // Clase del Usuario
    data class Usuario(
        val nombre: String,
        val apellido: String,
        val correo: String,
        val contrasena: String
    )

// Registro de escaneo

    data class RegistroEscaneo(
        val nombre: String,
        val fecha: String,
        val informacion: String
    )


// historial vacio porque no sabia que agregar jeje

    object BaseDatosHistorial {
        val registros = mutableStateListOf<RegistroEscaneo>()
    }

    // Usuarios de prueba (mutable)
    object base_de_datos_Usuarios {
        val usuarios = mutableListOf(
            Usuario("César", "Avalos", "cesar@gmail.com", "1234"),
            Usuario("María", "Gonzalez", "maria@gmail.com", "5678"),
            Usuario("Juan", "Gabriel", "juan@gmail.com", "abcd")
        )

        fun buscarUsuario(correo: String): Usuario? {
            return usuarios.find { it.correo.equals(correo.trim(), ignoreCase = true) }
        }

        fun agregarUsuario(nuevo: Usuario): Boolean {
            if (buscarUsuario(nuevo.correo) != null) return false
            usuarios.add(nuevo)
            return true
        }
    }

    interface Autenticador {
        fun iniciarSesion(correo: String, contrasena: String): Usuario
    }

    class CredencialesIncorrectasException(mensaje: String) : Exception(mensaje)

    class AutenticadorLocal : Autenticador {
        override fun iniciarSesion(correo: String, contrasena: String): Usuario {
            if (correo.isBlank()) throw IllegalArgumentException("Ingresa tu correo electrónico")
            if (contrasena.isBlank()) throw IllegalArgumentException("Ingresa tu contraseña")
            if (!correoValido(correo)) throw IllegalArgumentException("Ingresa un correo electrónico válido")

            val usuario = base_de_datos_Usuarios.buscarUsuario(correo)
            if (usuario?.contrasena == contrasena) return usuario
            throw CredencialesIncorrectasException("Correo o contraseña incorrectos")
        }
    }

    fun correoValido(correo: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()
    }

    // Pantalla inicio de sesion
    @Composable
    fun PantallaInicioSesion(       //
        onCrearCuenta: () -> Unit,   //Gabo//
        onLoginExitoso: (Usuario) -> Unit   //
    ) {                             //
        val context = LocalContext.current
        var correo by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var mostrarContrasena by remember { mutableStateOf(false) }
        val autenticador = remember { AutenticadorLocal() }

        val colorFondo = Color(0xFFC8D9E6)
        val colorTextoPrincipal = Color(0xFF334055)
        val colorTextoSecundario = Color(0xFFA5B1BD)
        val colorIcono = Color(0xFFA5A5A5)

        Box(modifier = Modifier.fillMaxSize().background(colorFondo)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, end = 40.dp, top = 95.dp, bottom = 45.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icono_washly),
                    contentDescription = "Logo de Wahsly",
                    modifier = Modifier.size(145.dp)
                )
                Spacer(modifier = Modifier.height(75.dp))

                // Correo
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    modifier = Modifier.fillMaxWidth().height(66.dp),
                    placeholder = {
                        Text(
                            "Correo electrónico",
                            fontSize = 19.sp,
                            color = Color(0xFFA5A5A5)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = colorIcono,
                            modifier = Modifier.size(25.dp)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFBFC7CD),
                        unfocusedBorderColor = Color(0xFFE8E8E8),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Contraseña
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    modifier = Modifier.fillMaxWidth().height(66.dp),
                    placeholder = {
                        Text(
                            "Contraseña",
                            fontSize = 19.sp,
                            color = Color(0xFFA5A5A5)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = colorIcono,
                            modifier = Modifier.size(25.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                            Icon(
                                imageVector = if (mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (mostrarContrasena) "Ocultar" else "Mostrar",
                                tint = Color(0xFF747474)
                            )
                        }
                    },
                    visualTransformation = if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFBFC7CD),
                        unfocusedBorderColor = Color(0xFFE8E8E8),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    modifier = Modifier.align(Alignment.End).clickable {
                        Toast.makeText(context, "Recuperación de contraseña", Toast.LENGTH_SHORT)
                            .show()
                    },
                    color = colorTextoSecundario,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(44.dp))

                BotonDegradado(
                    texto = "Iniciar Sesión",
                    onClick = {
                        try {
                            val usuario = autenticador.iniciarSesion(correo, contrasena)
                            Toast.makeText(
                                context,
                                "Bienvenido ${usuario.nombre}",
                                Toast.LENGTH_LONG
                            ).show()
                            // Después de iniciar sesión correctamente se envia al
                            // desgraciado usuario a la pantalla principal.
                            onLoginExitoso(usuario)
                        } catch (e: IllegalArgumentException) {
                            Toast.makeText(
                                context,
                                e.message ?: "Datos inválidos",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: CredencialesIncorrectasException) {
                            Toast.makeText(context, e.message ?: "Error", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Ocurrió un error inesperado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "¿Aún no tienes una cuenta?",
                    color = colorTextoSecundario,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                BotonCrearCuenta(onClick = onCrearCuenta)
            }
        }
    }

    // Pantalla de registro (logo arriba izquierda, enlace a login arriba derecha)
    @Composable
    fun PantallaRegistro(
        onRegistroExitoso: () -> Unit,
        onVolverLogin: () -> Unit
    ) {
        val context = LocalContext.current
        var nombre by remember { mutableStateOf("") }
        var apellido by remember { mutableStateOf("") }
        var correo by remember { mutableStateOf("") }
        var contrasena by remember { mutableStateOf("") }
        var confirmarContrasena by remember { mutableStateOf("") }
        var aceptaPolitica by remember { mutableStateOf(false) }
        var mostrarContrasena by remember { mutableStateOf(false) }
        var mostrarConfirmacion by remember { mutableStateOf(false) }

        val colorFondo = Color(0xFFC8D9E6)
        val colorTextoPrincipal = Color(0xFF334055)
        val colorTextoSecundario = Color(0xFFA5B1BD)
        val colorIcono = Color(0xFFA5A5A5)

        Box(modifier = Modifier.fillMaxSize().background(colorFondo)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, end = 40.dp, top = 45.dp, bottom = 45.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Fila superior: logo a la izquierda, textos a la derecha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icono_washly),
                        contentDescription = "Logo de Wahsly",
                        modifier = Modifier.size(80.dp)
                    )
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "¿Ya tienes una cuenta?",
                            color = colorTextoSecundario,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Iniciar sesión",
                            color = colorTextoPrincipal,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onVolverLogin() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Crea tu cuenta",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorTextoPrincipal,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    placeholder = { Text("Nombre", fontSize = 18.sp, color = Color(0xFFA5A5A5)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = colorIcono
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFBFC7CD),
                        unfocusedBorderColor = Color(0xFFE8E8E8),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Apellido
                OutlinedTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    placeholder = { Text("Apellido", fontSize = 18.sp, color = Color(0xFFA5A5A5)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = colorIcono
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFBFC7CD),
                        unfocusedBorderColor = Color(0xFFE8E8E8),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Correo
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    placeholder = {
                        Text(
                            "Correo electrónico",
                            fontSize = 18.sp,
                            color = Color(0xFFA5A5A5)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = colorIcono
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFBFC7CD),
                        unfocusedBorderColor = Color(0xFFE8E8E8),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Contraseña
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    placeholder = {
                        Text(
                            "Crea tu contraseña",
                            fontSize = 18.sp,
                            color = Color(0xFFA5A5A5)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = colorIcono
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                            Icon(
                                imageVector = if (mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color(0xFF747474)
                            )
                        }
                    },
                    visualTransformation = if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFBFC7CD),
                        unfocusedBorderColor = Color(0xFFE8E8E8),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Confirmar contraseña
                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = { confirmarContrasena = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    placeholder = {
                        Text(
                            "Confirma tu contraseña",
                            fontSize = 18.sp,
                            color = Color(0xFFA5A5A5)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = colorIcono
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { mostrarConfirmacion = !mostrarConfirmacion }) {
                            Icon(
                                imageVector = if (mostrarConfirmacion) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color(0xFF747474)
                            )
                        }
                    },
                    visualTransformation = if (mostrarConfirmacion) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFBFC7CD),
                        unfocusedBorderColor = Color(0xFFE8E8E8),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Checkbox política
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = aceptaPolitica,
                        onCheckedChange = { aceptaPolitica = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF334055),
                            uncheckedColor = Color.Gray
                        )
                    )
                    Text(
                        text = "Acepta la Política de Privacidad",
                        fontSize = 16.sp,
                        color = colorTextoPrincipal,
                        modifier = Modifier.clickable { aceptaPolitica = !aceptaPolitica }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botón Regístrate
                BotonDegradado(
                    texto = "Regístrate",
                    onClick = {
                        if (nombre.isBlank() || apellido.isBlank() || correo.isBlank() ||
                            contrasena.isBlank() || confirmarContrasena.isBlank()
                        ) {
                            Toast.makeText(
                                context,
                                "Todos los campos son obligatorios",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@BotonDegradado
                        }
                        if (!correoValido(correo)) {
                            Toast.makeText(
                                context,
                                "Correo electrónico no válido",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@BotonDegradado
                        }
                        if (contrasena != confirmarContrasena) {
                            Toast.makeText(
                                context,
                                "Las contraseñas no coinciden",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@BotonDegradado
                        }
                        if (!aceptaPolitica) {
                            Toast.makeText(
                                context,
                                "Debes aceptar la política de privacidad",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@BotonDegradado
                        }

                        val nuevoUsuario = Usuario(
                            nombre = nombre.trim(),
                            apellido = apellido.trim(),
                            correo = correo.trim(),
                            contrasena = contrasena
                        )
                        if (base_de_datos_Usuarios.agregarUsuario(nuevoUsuario)) {
                            onRegistroExitoso()
                        } else {
                            Toast.makeText(
                                context,
                                "El correo ya está registrado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )

                // Ya no hay "o regístrate con" ni botones sociales
            }
        }
    }

    // Botón Degradado (compartido)
    @Composable
    fun BotonDegradado(texto: String, onClick: () -> Unit) {
        val forma = RoundedCornerShape(50.dp)
        Box(
            modifier = Modifier
                .width(255.dp)
                .height(61.dp)
                .clip(forma)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF7B92A4), Color(0xFF334055))
                    )
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = texto,
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    // Botón Crear Cuenta (usado en login)
    @Composable
    fun BotonCrearCuenta(onClick: () -> Unit) {
        val forma = RoundedCornerShape(50.dp)
        Box(
            modifier = Modifier
                .width(230.dp)
                .height(61.dp)
                .clip(forma)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFD29FAD), Color(0xFFD9D9D9))
                    )
                )
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Crear Cuenta",
                color = Color(0xFF334055),
                fontSize = 23.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    @Composable
    fun PantallaPrincipal(
        usuario: Usuario?,
        onHistorial: () -> Unit,
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

        // COLORES
        val azulClaro = Color(0xFFC8D9E6)
        val azulPrincipal = Color(0xFF547D94)
        val azulTexto = Color(0xFF334055)
        val moradoLupa = Color(0xFF9D82D6)
        val fondo = Color(0xFFFCFCFC)

        // Altura donde Android muestra hora, batería, WiFi, etc.
        val alturaStatusBar = WindowInsets.statusBars
            .asPaddingValues()
            .calculateTopPadding()


        Scaffold(

            containerColor = fondo,

            // Evita que Android nos agregue otro espacio arriba
            contentWindowInsets = WindowInsets(
                left = 0,
                top = 0,
                right = 0,
                bottom = 0
            ),

            // ==================================================
            // BARRA INFERIOR
            // ==================================================
            bottomBar = {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(fondo)
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
                            Box(
                                modifier = Modifier
                                    .width(105.dp)
                                    .height(65.dp)
                                    .clip(RoundedCornerShape(38.dp))
                                    .background(azulClaro),

                                contentAlignment = Alignment.Center
                            ) {

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = "Inicio",
                                        tint = azulPrincipal,
                                        modifier = Modifier.size(34.dp)
                                    )

                                    Text(
                                        text = "Inicio",
                                        fontSize = 12.sp,
                                        color = azulPrincipal
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
                                    tint = Color(0xFFC5D7E0),
                                    modifier = Modifier.size(38.dp)
                                )

                                Text(
                                    text = "Escanear",
                                    fontSize = 12.sp,
                                    color = Color.White
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
                                    tint = Color(0xFFC5D7E0),
                                    modifier = Modifier.size(32.dp)
                                )

                                Text(
                                    text = "Cuenta",
                                    fontSize = 12.sp,
                                    color = Color.White
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
                    .background(fondo)
                    .padding(bottom = padding.calculateBottomPadding())
            ) {


                // CABECERA AZUL CLARO

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaStatusBar + 92.dp)
                        .background(
                            color = azulClaro,
                            shape = RoundedCornerShape(
                                bottomStart = 12.dp,
                                bottomEnd = 12.dp
                            )
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
                                tint = moradoLupa,
                                modifier = Modifier.size(28.dp)
                            )
                        },

                        singleLine = true,

                        shape = RoundedCornerShape(40.dp),

                        colors = OutlinedTextFieldDefaults.colors(

                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,

                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,

                            focusedTextColor = azulTexto,
                            unfocusedTextColor = azulTexto,

                            cursorColor = azulTexto
                        )
                    )
                }


                // FRANJA AZUL DEL MENÚ

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(azulPrincipal)
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
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }


                    // ---------------- MENÚ DESPLEGABLE ----------------
                    DropdownMenu(

                        expanded = mostrarMenu,

                        onDismissRequest = {
                            mostrarMenu = false
                        }

                    ) {

                        DropdownMenuItem(

                            text = {
                                Text("Historial")
                            },

                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null
                                )
                            },

                            onClick = {

                                mostrarMenu = false

                                onHistorial()
                            }
                        )


                        DropdownMenuItem(

                            text = {
                                Text("Cerrar sesión")
                            },

                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = null
                                )
                            },

                            onClick = {

                                mostrarMenu = false

                                onCerrarSesion()
                            }
                        )
                    }
                }


                // CONTENIDO CENTRAL


                if (BaseDatosHistorial.registros.isEmpty()) {

                    // Historial vacío.
                    // Dejamos esta zona limpia como en tu diseño.
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(fondo)
                    )

                } else {


                    // CUANDO EXISTAN ESCANEOS

                    LazyColumn(

                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(fondo),

                        contentPadding = PaddingValues(
                            start = 32.dp,
                            end = 32.dp,
                            top = 35.dp,
                            bottom = 25.dp
                        ),

                        verticalArrangement = Arrangement.spacedBy(30.dp)

                    ) {

                        items(
                            BaseDatosHistorial.registros
                        ) { registro ->


                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clickable {
                                        onHistorial()
                                    },

                                shape = RoundedCornerShape(26.dp),

                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 5.dp
                                ),

                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(23.dp)
                                ) {

                                    Text(
                                        text = registro.nombre,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = azulTexto
                                    )

                                    Spacer(
                                        modifier = Modifier.height(12.dp)
                                    )

                                    Text(
                                        text = registro.fecha,
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )

                                    Spacer(
                                        modifier = Modifier.height(18.dp)
                                    )

                                    Text(
                                        text = registro.informacion,
                                        fontSize = 16.sp,
                                        color = azulTexto
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

// Pantalla del historial

    @Composable
    fun PantallaHistorial(
        onVolver: () -> Unit
    ) {

        val colorFondo = Color(0xFFF9F9F9)
        val colorTexto = Color(0xFF334055)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondo)
                .padding(30.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Historial",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (BaseDatosHistorial.registros.isEmpty()) {

                Text(
                    text = "No hay escaneos guardados.",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    onVolver()
                }
            ) {
                Text("Volver")
            }
        }
    }

    // Pantalla de perfil de usuario
    @Composable
    fun PantallaPerfil(
        usuario: Usuario?,
        onVolver: () -> Unit,
        onCerrarSesion: () -> Unit
    ) {
        val colorFondo = Color(0xFFF9F9F9)
        val colorTexto = Color(0xFF334055)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondo)
                .padding(24.dp)
        ) {
            // Encabezado
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onVolver) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = colorTexto)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Mi Perfil",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorTexto
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta con info personal
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color(0xFF547D94)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    InfoPerfilItem(
                        etiqueta = "Nombre",
                        valor = "${usuario?.nombre ?: "-"} ${usuario?.apellido ?: ""}"
                    )
                    InfoPerfilItem(etiqueta = "Correo", valor = usuario?.correo ?: "-")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Opciones de configuración
            OpcionPerfil(
                icono = Icons.Default.Settings,
                texto = "Configuración",
                onClick = { }
            )
            OpcionPerfil(
                icono = Icons.Default.Notifications,
                texto = "Notificaciones",
                onClick = { }
            )
            OpcionPerfil(
                icono = Icons.Default.Lock,
                texto = "Privacidad",
                onClick = { }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Cerrar sesión
            OpcionPerfil(
                icono = Icons.Default.ExitToApp,
                texto = "Cerrar sesión",
                colorTexto = Color(0xFFD9534F),
                onClick = onCerrarSesion
            )
        }
    }

    @Composable
    fun InfoPerfilItem(etiqueta: String, valor: String) {
        Column(modifier = Modifier.padding(vertical = 6.dp)) {
            Text(etiqueta, fontSize = 13.sp, color = Color.Gray)
            Text(valor, fontSize = 18.sp, color = Color(0xFF334055), fontWeight = FontWeight.Medium)
        }
    }

    @Composable
    fun OpcionPerfil(
        icono: androidx.compose.ui.graphics.vector.ImageVector,
        texto: String,
        colorTexto: Color = Color(0xFF334055),
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, tint = colorTexto)
            Spacer(modifier = Modifier.width(14.dp))
            Text(texto, fontSize = 16.sp, color = colorTexto)
        }
    }


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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.delay
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.net.Uri
import android.widget.VideoView
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.border
import androidx.compose.ui.window.Dialog

// cesar@gmail.com
// 1234

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // Primera pantalla que se mostrará
                var pantallaActual by remember {
                    mutableStateOf("SPLASH")
                }

                // Guarda qué usuario inició sesión
                var usuarioActual by remember {
                    mutableStateOf<Usuario?>(null)
                }

                // chavoooos esto indica a qué pantalla ir después del video de carga
                var destinoDespuesCarga by remember {
                    mutableStateOf("LOGIN")
                }


                when (pantallaActual) {

                    // ---------------- SPLASH ----------------
                    "SPLASH" -> {

                        PantallaSplash(
                            onTerminar = {
                                pantallaActual = "LOGIN"
                            }
                        )
                    }

                    // ---------------- VIDEO DE CARGA ----------------
                    "CARGA_VIDEO" -> {

                        PantallaCargaVideo(
                            onTerminar = {
                                pantallaActual = destinoDespuesCarga
                            }
                        )
                    }


                    // ---------------- LOGIN ----------------
                    "LOGIN" -> {

                        PantallaInicioSesion(

                            onCrearCuenta = {
                                pantallaActual = "REGISTRO"
                            },

                            onLoginExitoso = { usuario ->

                                usuarioActual = usuario

                                destinoDespuesCarga = "INICIO"

                                pantallaActual = "CARGA_VIDEO"
                            }
                        )
                    }

                    // ---------------- REGISTRO ----------------
                    "REGISTRO" -> {

                        PantallaRegistro(

                            onRegistroExitoso = {

                                destinoDespuesCarga = "LOGIN"

                                pantallaActual = "CARGA_VIDEO"

                                Toast.makeText(
                                    this,
                                    "Cuenta creada correctamente",
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

                            onHistorial = {
                                pantallaActual = "HISTORIAL"
                            },

                            onPerfil = {
                                pantallaActual = "PERFIL"
                            },

                            onCerrarSesion = {

                                usuarioActual = null

                                pantallaActual = "LOGIN"
                            }
                        )
                    }

                    // ---------------- HISTORIAL ----------------
                    "HISTORIAL" -> {

                        PantallaHistorial(

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
    }                                                             //

    @Composable
    fun PantallaSplash(
        onTerminar: () -> Unit
    ) {

        LaunchedEffect(Unit) {
            delay(1500)
            onTerminar()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4EFEB)),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.washlylogo_inicio
                ),
                contentDescription = "Logo de Washly",
                modifier = Modifier.size(280.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
    @Composable
    fun PantallaCargaVideo(
        onTerminar: () -> Unit
    ) {

        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4EFEB)),
            contentAlignment = Alignment.Center
        ) {

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1422f / 2530f),

                factory = { contexto ->

                    VideoView(contexto).apply {

                        val videoUri = Uri.parse(
                            "android.resource://${context.packageName}/${R.raw.washly_carga}"
                        )

                        setVideoURI(videoUri)

                        setOnPreparedListener { mediaPlayer ->

                            mediaPlayer.isLooping = false
                            start()
                        }

                        setOnCompletionListener {
                            onTerminar()
                        }

                        setOnErrorListener { _, _, _ ->
                            onTerminar()
                            true
                        }
                    }
                }
            )
        }
    }

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

    inner class AutenticadorLocal : Autenticador {
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

        val colorFondo = Color(0xFFF4EFEB)
        val colorTextoPrincipal = Color(0xFFFFFFFF)
        val colorTextoSecundario = Color(0xFFA2B1BE)
        val colorIcono = Color(0xFFFFFFFF)

        Box(modifier = Modifier.fillMaxSize().background(colorFondo)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, end = 40.dp, top = 95.dp, bottom = 45.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.washly_inicio_sesion),
                    contentDescription = "Logo de Wahsly",
                    modifier = Modifier.size(225.dp)
                )
                Spacer(modifier = Modifier.height(55.dp))

                // Correo
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    modifier = Modifier.fillMaxWidth().height(66.dp),
                    placeholder = {
                        Text(
                            "Correo electrónico",
                            fontSize = 19.sp,
                            color = Color(0xFFFFFFFF)
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
                        focusedContainerColor = Color(0xFF2F4157),
                        unfocusedContainerColor = Color(0xFF2F4157),
                        focusedBorderColor = Color(0xFF2F4157),
                        unfocusedBorderColor = Color(0xFF2F4157),
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
                            color = Color(0xFFFFFFFF)
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
                                tint = Color(0xFFFFFFFF)
                            )
                        }
                    },
                    visualTransformation = if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF2F4157),
                        unfocusedContainerColor = Color(0xFF2F4157),
                        focusedBorderColor = Color(0xFF2F4157),
                        unfocusedBorderColor = Color(0xFF2F4157),
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

    // Pantalla de registro
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

        val colorFondo = Color(0xFFF4EFEB)
        val colorTextoPrincipal = Color(0xFFFFFFFF)
        val colorTextoSecundario = Color(0xFFA2B1BE)
        val colorIcono = Color(0xFFFFFFFF)

        Box(modifier = Modifier.fillMaxSize().background(colorFondo)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 40.dp, end = 40.dp, top = 45.dp, bottom = 45.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                // Fila superior: logo a la izquierda, textos a la derecha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icono_washly_crear_cuenta),
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
                            color = Color(0xFFDC9CAD),
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
                    color = Color(0xFF2F4157),
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    placeholder = { Text("Nombre", fontSize = 18.sp, color = Color(0xFFFFFFFF)) },
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
                        focusedContainerColor = Color(0xFF2F4157),
                        unfocusedContainerColor = Color(0xFF2F4157),
                        focusedBorderColor = Color(0xFF2F4157),
                        unfocusedBorderColor = Color(0xFF2F4157),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(18.dp))

                // Apellido
                OutlinedTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    placeholder = { Text("Apellido", fontSize = 18.sp, color = Color(0xFFFFFFFF)) },
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
                        focusedContainerColor = Color(0xFF2F4157),
                        unfocusedContainerColor = Color(0xFF2F4157),
                        focusedBorderColor = Color(0xFF2F4157),
                        unfocusedBorderColor = Color(0xFF2F4157),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(18.dp))

                // Correo
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    placeholder = {
                        Text(
                            "Correo electrónico",
                            fontSize = 18.sp,
                            color = Color(0xFFFFFFFF)
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
                        focusedContainerColor = Color(0xFF2F4157),
                        unfocusedContainerColor = Color(0xFF2F4157),
                        focusedBorderColor = Color(0xFF2F4157),
                        unfocusedBorderColor = Color(0xFF2F4157),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(18.dp))

                // Contraseña
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    placeholder = {
                        Text(
                            "Crea tu contraseña",
                            fontSize = 18.sp,
                            color = Color(0xFFFFFFFF)
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
                                tint = Color(0xFFFFFFFF)
                            )
                        }
                    },
                    visualTransformation = if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF2F4157),
                        unfocusedContainerColor = Color(0xFF2F4157),
                        focusedBorderColor = Color(0xFF2F4157),
                        unfocusedBorderColor = Color(0xFF2F4157),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(18.dp))

                // Confirmar contraseña
                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = { confirmarContrasena = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    placeholder = {
                        Text(
                            "Confirma tu contraseña",
                            fontSize = 18.sp,
                            color = Color(0xFFFFFFFF)
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
                                tint = Color(0xFFFFFFFF)
                            )
                        }
                    },
                    visualTransformation = if (mostrarConfirmacion) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF2F4157),
                        unfocusedContainerColor = Color(0xFF2F4157),
                        focusedBorderColor = Color(0xFF2F4157),
                        unfocusedBorderColor = Color(0xFF2F4157),
                        cursorColor = colorTextoPrincipal,
                        focusedTextColor = colorTextoPrincipal,
                        unfocusedTextColor = colorTextoPrincipal
                    )
                )
                Spacer(modifier = Modifier.height(18.dp))

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
                        color = Color(0xFF2F4157),
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
        val azulClaro = Color(0xFF2F4157)
        val azulPrincipal = Color(0xFF2F4157)
        val azulTexto = Color(0xFF334055)
        val moradoLupa = Color(0xFF9D82D6)
        val fondo = Color(0xFFF4EFEB)

        // Altura donde Android muestra hora, batería, WiFi, etc.
        val alturaStatusBar = WindowInsets.statusBars
            .asPaddingValues()
            .calculateTopPadding()

        Scaffold(
            containerColor = fondo,
            contentWindowInsets = WindowInsets.systemBars,

            // BARRA INFERIOR
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
                                        tint = Color(0xFFC5D7E0),
                                        modifier = Modifier.size(34.dp)
                                    )
                                    Text(
                                        text = "Inicio",
                                        fontSize = 12.sp,
                                        color = Color.White
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
                // CABECERA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaStatusBar + 92.dp)
                        .background(
                            color = azulClaro,
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

                // FRANJA DEL MENÚ
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
                            color = azulTexto,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        // Encabezado "General"
                        Text(
                            text = "General",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            color = Color.Gray,
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
                            color = azulTexto,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Tarjeta 1
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8ECEF)
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
                                        color = azulTexto
                                    )
                                    Text(
                                        "100%",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        "TEXTO",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        // Tarjeta 2
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8ECEF)
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
                                        color = azulTexto
                                    )
                                    Text(
                                        "Lavado en frío",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        "Ejemplo de rutina",
                                        color = Color.Gray,
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
                                .background(fondo),
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
                                        .height(150.dp)
                                        .clickable { onHistorial() },
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
                                            color = azulTexto
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = registro.fecha,
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = registro.informacion,
                                            fontSize = 14.sp,
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
        val context = LocalContext.current
        val colorFondo = Color(0xFFF4EFEB)
        val colorTexto = Color(0xFF334055)
        val azulClaro = Color(0xFF2F4157)
        val azulPrincipal = Color(0xFF2F4157)
        var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }

        Scaffold(
            containerColor = colorFondo,
            contentWindowInsets = WindowInsets.systemBars,

            // BARRA INFERIOR (igual que en PantallaPrincipal, pero con "Cuenta" activo)
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorFondo)
                        .padding(start = 10.dp, end = 10.dp, bottom = 8.dp)
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
                            Column(
                                modifier = Modifier
                                    .width(105.dp)
                                    .clickable { onVolver() },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Inicio",
                                    tint = Color(0xFFC5D7E0),
                                    modifier = Modifier.size(34.dp)
                                )
                                Text("Inicio", fontSize = 12.sp, color = Color.White)
                            }

                            // ---------------- ESCANEAR ----------------
                            Column(
                                modifier = Modifier
                                    .width(105.dp)
                                    .clickable {
                                        Toast.makeText(context, "Escáner próximamente", Toast.LENGTH_SHORT).show()
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Escanear",
                                    tint = Color(0xFFC5D7E0),
                                    modifier = Modifier.size(38.dp)
                                )
                                Text("Escanear", fontSize = 12.sp, color = Color.White)
                            }

                            // ---------------- CUENTA (activo) ----------------
                            Box(
                                modifier = Modifier
                                    .width(105.dp)
                                    .height(65.dp)
                                    .clip(RoundedCornerShape(38.dp))
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Cuenta",
                                        tint = azulPrincipal,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Text("Cuenta", fontSize = 12.sp, color = azulPrincipal)
                                }
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
                // ---------------- CABECERA CON DEGRADADO Y CURVA ----------------
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(bottomStart = 45.dp, bottomEnd = 45.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF4F7188), Color(0xFF2F4157))
                            )
                        )
                ) {
                    Text(
                        text = "Perfil",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 16.dp, start = 20.dp)
                    )
                }

                // ---------------- AVATAR (se monta sobre la curva) ----------------
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-70).dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0))
                            .border(4.dp, azulClaro, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto de perfil",
                            tint = Color(0xFFB0B0B0),
                            modifier = Modifier.size(90.dp)
                        )
                    }
                }

                // ---------------- TARJETA DE OPCIONES ----------------
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-50).dp)
                        .padding(horizontal = 24.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            OpcionPerfilConFlecha(
                                icono = Icons.Default.Settings,
                                texto = "Configuración",
                                onClick = { }
                            )
                            Divider(color = Color(0xFFF0F0F0), thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 16.dp))
                            OpcionPerfilConFlecha(
                                icono = Icons.Default.Info,
                                texto = "Ayuda y Soporte",
                                onClick = { }
                            )
                            Divider(color = Color(0xFFF0F0F0), thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 16.dp))
                            OpcionPerfilConFlecha(
                                icono = Icons.Default.ExitToApp,
                                texto = "Cerrar Sesión",
                                onClick =  { mostrarDialogoCerrarSesion = true }
                            )
                        }
                    }
                }

                //  POP-UP PARA CONFIRMAR CIERRE DE SESIÓN :P
                    Dialog(onDismissRequest = { mostrarDialogoCerrarSesion = false }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(28.dp))
                                .background(Color(0xFF2F4157))
                                .padding(24.dp)
                        ) {
                            IconButton(
                                onClick = { mostrarDialogoCerrarSesion = false },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cerrar",
                                    tint = Color.White
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp, bottom = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "¿Cerrar sesión?",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color(0xFFF4EFEB))
                                        .clickable {
                                            mostrarDialogoCerrarSesion = false
                                            onCerrarSesion()
                                        }
                                        .padding(horizontal = 40.dp, vertical = 12.dp)
                                ) {
                                    Text(
                                        text = "Aceptar",
                                        color = Color(0xFF2F4157),
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

    @Composable
    fun OpcionPerfilConFlecha(
        icono: androidx.compose.ui.graphics.vector.ImageVector,
        texto: String,
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, contentDescription = null, tint = Color(0xFF334055))
            Spacer(modifier = Modifier.width(16.dp))
            Text(texto, fontSize = 16.sp, color = Color(0xFF334055), modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFBBBBBB)
            )
        }
    }
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var mostrarRegistro by remember { mutableStateOf(false) }

                if (mostrarRegistro) {
                    PantallaRegistro(
                        onRegistroExitoso = {
                            mostrarRegistro = false
                            Toast.makeText(
                                this,
                                "Cuenta creada. Inicia sesión.",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onVolverLogin = {
                            mostrarRegistro = false
                        }
                    )
                } else {
                    PantallaInicioSesion(
                        onCrearCuenta = {
                            mostrarRegistro = true
                        }
                    )
                }
            }
        }
    }
}

// Clase del Usuario
data class Usuario(
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contrasena: String
)

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
fun PantallaInicioSesion(onCrearCuenta: () -> Unit) {
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
                placeholder = { Text("Correo electrónico", fontSize = 19.sp, color = Color(0xFFA5A5A5)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = colorIcono, modifier = Modifier.size(25.dp)) },
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
                placeholder = { Text("Contraseña", fontSize = 19.sp, color = Color(0xFFA5A5A5)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = colorIcono, modifier = Modifier.size(25.dp)) },
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
                    Toast.makeText(context, "Recuperación de contraseña", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(context, "Bienvenido ${usuario.nombre}", Toast.LENGTH_LONG).show()
                    } catch (e: IllegalArgumentException) {
                        Toast.makeText(context, e.message ?: "Datos inválidos", Toast.LENGTH_SHORT).show()
                    } catch (e: CredencialesIncorrectasException) {
                        Toast.makeText(context, e.message ?: "Error", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Ocurrió un error inesperado", Toast.LENGTH_SHORT).show()
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
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = colorIcono) },
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
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = colorIcono) },
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
                placeholder = { Text("Correo electrónico", fontSize = 18.sp, color = Color(0xFFA5A5A5)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = colorIcono) },
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
                placeholder = { Text("Crea tu contraseña", fontSize = 18.sp, color = Color(0xFFA5A5A5)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = colorIcono) },
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
                placeholder = { Text("Confirma tu contraseña", fontSize = 18.sp, color = Color(0xFFA5A5A5)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = colorIcono) },
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
                        Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                        return@BotonDegradado
                    }
                    if (!correoValido(correo)) {
                        Toast.makeText(context, "Correo electrónico no válido", Toast.LENGTH_SHORT).show()
                        return@BotonDegradado
                    }
                    if (contrasena != confirmarContrasena) {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        return@BotonDegradado
                    }
                    if (!aceptaPolitica) {
                        Toast.makeText(context, "Debes aceptar la política de privacidad", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(context, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
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
        Text(text = texto, color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.SemiBold)
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
        Text(text = "Crear Cuenta", color = Color(0xFF334055), fontSize = 23.sp, fontWeight = FontWeight.SemiBold)
    }
}
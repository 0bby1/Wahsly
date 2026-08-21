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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
                PantallaInicioSesion()
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


// Usuarios de prueba
object base_de_datos_Usuarios {
    val usuarios = listOf(
        Usuario(
            nombre = "César",
            apellido = "Avalos",
            correo = "cesar@gmail.com",
            contrasena = "1234"
        ),

        Usuario(
            nombre = "María",
            apellido = "Gonzalez",
            correo = "maria@gmail.com",
            contrasena = "5678"
        ),

        Usuario(
            nombre = "Juan",
            apellido = "Gabriel",
            correo = "juan@gmail.com",
            contrasena = "abcd"
        )
    )

    // Función con parámetros y retorno.
    // Además utiliza una lambda con find.
    fun buscarUsuario(correo: String): Usuario? {
        return usuarios.find { usuario ->
            usuario.correo.equals(
                correo.trim(),
                ignoreCase = true
            )
        }
    }
}

interface Autenticador {
    fun iniciarSesion(
        correo: String,
        contrasena: String
    ): Usuario
}

class CredencialesIncorrectasException(
    mensaje: String
) : Exception(mensaje)

class AutenticadorLocal : Autenticador {
    override fun iniciarSesion(
        correo: String,
        contrasena: String
    ): Usuario {

        if (correo.isBlank()) {
            throw IllegalArgumentException(
                "Ingresa tu correo electrónico"
            )
        }

        if (contrasena.isBlank()) {
            throw IllegalArgumentException(
                "Ingresa tu contraseña"
            )
        }

        if (!correoValido(correo)) {
            throw IllegalArgumentException(
                "Ingresa un correo electrónico válido"
            )
        }

        val usuario: Usuario? =
            base_de_datos_Usuarios.buscarUsuario(correo)

        if (usuario?.contrasena == contrasena) {
            return usuario
        }

        throw CredencialesIncorrectasException(
            "Correo o contraseña incorrectos"
        )
    }
}

fun correoValido(correo: String): Boolean {
    return Patterns.EMAIL_ADDRESS
        .matcher(correo.trim())
        .matches()
}


// Pantalla inicio de sesion
@Composable
fun PantallaInicioSesion() {
    val context = LocalContext.current
    var correo by remember {
        mutableStateOf("")
    }
    var contrasena by remember {
        mutableStateOf("")
    }
    var mostrarContrasena by remember {
        mutableStateOf(false)
    }

    // Llamamos al autenticador de inicio de sesion
    val autenticador = remember {
        AutenticadorLocal()
    }

    // Colores
    val colorFondo = Color(0xFFC8D9E6)
    val colorTextoPrincipal = Color(0xFF334055)
    val colorTextoSecundario = Color(0xFFA5B1BD)
    val colorIcono = Color(0xFFA5A5A5)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 40.dp,
                    end = 40.dp,
                    top = 95.dp,
                    bottom = 45.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo
            Image(
                painter = painterResource(id = R.drawable.icono_washly),
                contentDescription = "Logo de Wahsly",
                modifier = Modifier.size(145.dp)
            )

            Spacer(modifier = Modifier.height(75.dp))

            // Correo
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp),
                placeholder = {
                    Text(
                        text = "Correo electrónico",
                        fontSize = 19.sp,
                        color = Color(0xFFA5A5A5)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Correo",
                        tint = colorIcono,
                        modifier = Modifier.size(25.dp)
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),

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
                onValueChange = {
                    contrasena = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp),
                placeholder = {
                    Text(
                        text = "Contraseña",
                        fontSize = 19.sp,
                        color = Color(0xFFA5A5A5)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Contraseña",
                        tint = colorIcono,
                        modifier = Modifier.size(25.dp)
                    )
                },

                trailingIcon = {
                    IconButton(
                        onClick = {
                            mostrarContrasena =
                                !mostrarContrasena
                        }
                    ) {
                        Icon(
                            imageVector =
                                if (mostrarContrasena)
                                    Icons.Default.VisibilityOff
                                else
                                    Icons.Default.Visibility,

                            contentDescription =
                                if (mostrarContrasena)
                                    "Ocultar contraseña"
                                else
                                    "Mostrar contraseña",
                            tint = Color(0xFF747474)
                        )
                    }
                },

                visualTransformation =
                    if (mostrarContrasena)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),

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

            // Olvidaste contraseña
            Text(
                text = "¿Olvidaste tu contraseña?",
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable {

                        Toast
                            .makeText(
                                context,
                                "Recuperación de contraseña",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    },
                color = colorTextoSecundario,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(44.dp))

            // Boton iniciar sesion
            BotonDegradado(
                texto = "Iniciar Sesión",

                onClick = {
                    // Try
                    try {
                        val usuario =
                            autenticador.iniciarSesion(
                                correo = correo,
                                contrasena = contrasena
                            )

                        // Inicio correcto
                        Toast
                            .makeText(
                                context,
                                "Bienvenido ${usuario.nombre}",
                                Toast.LENGTH_LONG
                            )
                            .show()

                    } catch (
                        e: IllegalArgumentException
                    ) {

                        // Null seguro con ?: por si el mensaje
                        // de la excepción fuera null
                        val mensaje =
                            e.message
                                ?: "Datos inválidos"

                        Toast
                            .makeText(
                                context,
                                mensaje,
                                Toast.LENGTH_SHORT
                            )
                            .show()

                    } catch (
                        e: CredencialesIncorrectasException
                    ) {

                        val mensaje =
                            e.message
                                ?: "No fue posible iniciar sesión"

                        Toast
                            .makeText(
                                context,
                                mensaje,
                                Toast.LENGTH_SHORT
                            )
                            .show()

                    } catch (
                        e: Exception
                    ) {
                        Toast
                            .makeText(
                                context,
                                "Ocurrió un error inesperado",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(25.dp))

            // CREAR CUENTA
            Text(
                text = "¿Aún no tienes una cuenta?",
                color = colorTextoSecundario,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            BotonCrearCuenta(
                onClick = {
                    Toast
                        .makeText(
                            context,
                            "Crear una cuenta",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            )
        }
    }
}

// Boton Iniciar Sesion
@Composable
fun BotonDegradado(
    texto: String,
    onClick: () -> Unit
) {

    val forma = RoundedCornerShape(50.dp)

    Box(
        modifier = Modifier
            .width(255.dp)
            .height(61.dp)
            .clip(forma)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF7B92A4),
                        Color(0xFF334055)
                    )
                )
            )
            .clickable {
                onClick()
            },
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

// Boton crear cuenta
@Composable
fun BotonCrearCuenta(
    onClick: () -> Unit
) {

    val forma = RoundedCornerShape(50.dp)

    Box(
        modifier = Modifier
            .width(230.dp)
            .height(61.dp)
            .clip(forma)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFD29FAD),
                        Color(0xFFD9D9D9)
                    )
                )
            )
            .clickable {
                onClick()
            },
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

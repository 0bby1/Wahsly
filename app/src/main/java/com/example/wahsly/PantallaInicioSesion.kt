package com.example.wahsly

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import android.net.Uri
import android.widget.VideoView
import androidx.compose.ui.viewinterop.AndroidView

// Pantalla inicio de sesion
@Composable
fun PantallaInicioSesion(
    modoOscuro: Boolean,
    onCrearCuenta: () -> Unit,
    onLoginExitoso: (Usuario) -> Unit
) {
    val context = LocalContext.current
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    val autenticador = remember { AutenticadorLocal() }

    // Colores
    val colorFondo =
        if (modoOscuro) FondoOscuro else FondoClaro

    val colorCampo =
        if (modoOscuro) CremaOscuro else AzulPrincipalClaro

    val colorTextoPrincipal =
        if (modoOscuro) AzulTextoClaro else TextoBlancoClaro

    val colorTextoSecundario =
        if (modoOscuro) TextoSecundarioOscuro else TextoSecundarioClaro

    val colorIcono =
        if (modoOscuro) AzulTextoClaro else TextoBlancoClaro

    Box(modifier = Modifier.fillMaxSize().background(colorFondo)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp, end = 40.dp, top = 105.dp, bottom = 45.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(0.dp))
            Box(
                modifier = Modifier.size(300.dp)
                    .height(10.dp)
                    .offset(y = (-40).dp),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    factory = { contexto ->
                        VideoView(contexto).apply {
                            val videoUri = if (modoOscuro){
                                Uri.parse(
                                    "android.resource://${context.packageName}/${R.raw.washly_login_oscuro}"
                                )
                            } else
                            {Uri.parse(
                                "android.resource://${context.packageName}/${R.raw.washly_login_claro}"
                            )}
                            setVideoURI(videoUri)
                            setOnPreparedListener { mediaPlayer ->
                                // Hace que la animación se repita
                                mediaPlayer.isLooping = true
                                start()
                            }
                        }
                    }
                )
            }

            // Correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                modifier = Modifier.fillMaxWidth().height(66.dp),
                placeholder = {
                    Text(
                        "Correo electrónico",
                        fontSize = 19.sp,
                        color = colorTextoPrincipal
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
                    focusedContainerColor = colorCampo,
                    unfocusedContainerColor = colorCampo,
                    focusedBorderColor = colorCampo,
                    unfocusedBorderColor = colorCampo,
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
                        color = colorTextoPrincipal
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
                modoOscuro = modoOscuro,
                onClick = {
                    try {
                        val usuario = autenticador.iniciarSesion(correo, contrasena)
                        Toast.makeText(
                            context,
                            "Bienvenido ${usuario.nombre}",
                            Toast.LENGTH_LONG
                        ).show()
                        // Después de iniciar sesión correctamente se envia al
                        //  usuario a la pantalla principal.
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

            // Sección para crear cuenta (Accesible para TalkBack)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .semantics(mergeDescendants = true) {
                        role = Role.Button
                    }
                    .clickable(
                        onClickLabel = "Crear una cuenta nueva",
                        onClick = onCrearCuenta
                    )
            ) {
                Text(
                    text = "¿Aún no tienes una cuenta?",
                    color = colorTextoSecundario,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                BotonCrearCuenta(onClick = onCrearCuenta, modoOscuro = modoOscuro)
            }
        }
    }
}

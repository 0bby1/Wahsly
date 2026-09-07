package com.example.wahsly

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

// Pantalla de registro
@Composable
fun PantallaRegistro(
    modoOscuro: Boolean,
    onRegistroExitoso: (Usuario) -> Unit,
    onVolverLogin: () -> Unit,
    viewModel: RegistroViewModel = viewModel()
) {
    val context = LocalContext.current

    // Mostrar Toast cuando aparezca un mensaje de error
    LaunchedEffect(viewModel.mensajeError) {
        viewModel.mensajeError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.mensajeErrorMostrado()
        }
    }

    // Colores
    val colorFondo = if (modoOscuro) FondoOscuro else FondoClaro
    val colorCampo = if (modoOscuro) CremaOscuro else AzulPrincipalClaro
    val colorTextoCampo = if (modoOscuro) AzulTextoClaro else TextoBlancoClaro
    val colorTitulo = if (modoOscuro) CremaOscuro else AzulTextoClaro
    val colorTextoSecundario = if (modoOscuro) TextoSecundarioOscuro else TextoSecundarioClaro
    val colorRosa = if (modoOscuro) RosaOscuro else RosaClaro
    val colorIcono = colorTextoCampo

    Box(modifier = Modifier.fillMaxSize().background(colorFondo)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp, end = 40.dp, top = 45.dp, bottom = 45.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = if (modoOscuro) painterResource(id = R.drawable.icono_washly_crear_cuenta_oscuro)
                    else painterResource(id = R.drawable.icono_washly_crear_cuenta_claro),
                    contentDescription = "Logo de Wahsly",
                    modifier = Modifier.size(80.dp)
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "¿Ya tienes una cuenta?", color = colorTextoSecundario, fontSize = 16.sp)
                    Text(
                        text = "Iniciar sesión",
                        color = colorRosa,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable(role = Role.Button, onClick = onVolverLogin)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Crea tu cuenta",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = colorTitulo,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Nombre
            OutlinedTextField(
                value = viewModel.nombre,
                onValueChange = viewModel::onNombreChange,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                placeholder = { Text("Tu nombre", fontSize = 18.sp, color = colorTextoCampo) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = colorIcono) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorCampo,
                    unfocusedContainerColor = colorCampo,
                    focusedBorderColor = colorCampo,
                    unfocusedBorderColor = colorCampo,
                    focusedTextColor = colorTextoCampo,
                    unfocusedTextColor = colorTextoCampo,
                    focusedLabelColor = colorTextoCampo,
                    unfocusedLabelColor = colorTextoCampo,
                    focusedPlaceholderColor = colorTextoCampo,
                    unfocusedPlaceholderColor = colorTextoCampo,
                    focusedLeadingIconColor = colorIcono,
                    unfocusedLeadingIconColor = colorIcono,
                    focusedTrailingIconColor = colorIcono,
                    unfocusedTrailingIconColor = colorIcono,
                    cursorColor = colorTextoCampo
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Apellido
            OutlinedTextField(
                value = viewModel.apellido,
                onValueChange = viewModel::onApellidoChange,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                placeholder = { Text("Tu apellido", fontSize = 18.sp, color = colorTextoCampo) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = colorIcono) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorCampo,
                    unfocusedContainerColor = colorCampo,
                    focusedBorderColor = colorCampo,
                    unfocusedBorderColor = colorCampo,
                    focusedTextColor = colorTextoCampo,
                    unfocusedTextColor = colorTextoCampo,
                    focusedLabelColor = colorTextoCampo,
                    unfocusedLabelColor = colorTextoCampo,
                    focusedPlaceholderColor = colorTextoCampo,
                    unfocusedPlaceholderColor = colorTextoCampo,
                    focusedLeadingIconColor = colorIcono,
                    unfocusedLeadingIconColor = colorIcono,
                    focusedTrailingIconColor = colorIcono,
                    unfocusedTrailingIconColor = colorIcono,
                    cursorColor = colorTextoCampo
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Correo
            OutlinedTextField(
                value = viewModel.correo,
                onValueChange = viewModel::onCorreoChange,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                placeholder = { Text("ejemplo@correo.com", fontSize = 18.sp, color = colorTextoCampo) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = colorIcono) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorCampo,
                    unfocusedContainerColor = colorCampo,
                    focusedBorderColor = colorCampo,
                    unfocusedBorderColor = colorCampo,
                    focusedTextColor = colorTextoCampo,
                    unfocusedTextColor = colorTextoCampo,
                    focusedLabelColor = colorTextoCampo,
                    unfocusedLabelColor = colorTextoCampo,
                    focusedPlaceholderColor = colorTextoCampo,
                    unfocusedPlaceholderColor = colorTextoCampo,
                    focusedLeadingIconColor = colorIcono,
                    unfocusedLeadingIconColor = colorIcono,
                    focusedTrailingIconColor = colorIcono,
                    unfocusedTrailingIconColor = colorIcono,
                    cursorColor = colorTextoCampo
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Contraseña
            OutlinedTextField(
                value = viewModel.contrasena,
                onValueChange = viewModel::onContrasenaChange,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                placeholder = { Text("Crea tu contraseña", fontSize = 18.sp, color = colorTextoCampo) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = colorIcono) },
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleMostrarContrasena() }) {
                        Icon(
                            imageVector = if (viewModel.mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = colorTextoCampo
                        )
                    }
                },
                visualTransformation = if (viewModel.mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorCampo,
                    unfocusedContainerColor = colorCampo,
                    focusedBorderColor = colorCampo,
                    unfocusedBorderColor = colorCampo,
                    focusedTextColor = colorTextoCampo,
                    unfocusedTextColor = colorTextoCampo,
                    focusedLabelColor = colorTextoCampo,
                    unfocusedLabelColor = colorTextoCampo,
                    focusedPlaceholderColor = colorTextoCampo,
                    unfocusedPlaceholderColor = colorTextoCampo,
                    focusedLeadingIconColor = colorIcono,
                    unfocusedLeadingIconColor = colorIcono,
                    focusedTrailingIconColor = colorIcono,
                    unfocusedTrailingIconColor = colorIcono,
                    cursorColor = colorTextoCampo
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Confirmar contraseña
            OutlinedTextField(
                value = viewModel.confirmarContrasena,
                onValueChange = viewModel::onConfirmarContrasenaChange,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                placeholder = { Text("Confirma tu contraseña", fontSize = 18.sp, color = colorTextoCampo) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = colorIcono) },
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleMostrarConfirmacion() }) {
                        Icon(
                            imageVector = if (viewModel.mostrarConfirmacion) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = colorTextoCampo
                        )
                    }
                },
                visualTransformation = if (viewModel.mostrarConfirmacion) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorCampo,
                    unfocusedContainerColor = colorCampo,
                    focusedBorderColor = colorCampo,
                    unfocusedBorderColor = colorCampo,
                    focusedTextColor = colorTextoCampo,
                    unfocusedTextColor = colorTextoCampo,
                    focusedLabelColor = colorTextoCampo,
                    unfocusedLabelColor = colorTextoCampo,
                    focusedPlaceholderColor = colorTextoCampo,
                    unfocusedPlaceholderColor = colorTextoCampo,
                    focusedLeadingIconColor = colorIcono,
                    unfocusedLeadingIconColor = colorIcono,
                    focusedTrailingIconColor = colorIcono,
                    unfocusedTrailingIconColor = colorIcono,
                    cursorColor = colorTextoCampo
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Checkbox política
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics(mergeDescendants = true) { }
                    .clickable(role = Role.Checkbox, onClick = { viewModel.toggleAceptaPolitica() })
            ) {
                Switch(
                    checked = viewModel.aceptaPolitica,
                    onCheckedChange = null,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF487A32)
                    )
                )
                Text(text = "  Acepta la Política de Privacidad", fontSize = 16.sp, color = colorCampo)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón Regístrate
            Box(modifier = Modifier.semantics { role = Role.Button }) {
                BotonDegradado(
                    texto = "Regístrate",
                    modoOscuro = modoOscuro,
                    onClick = { viewModel.registrar() }
                )
            }
        }

        if (viewModel.mostrarRegistroExitoso) {
            val coloresRegistroExitoso =
                if (modoOscuro) listOf(RosaOscuro, FondoClaro)
                else listOf(AzulGradienteOscuro, FondoOscuro)

            Dialog(onDismissRequest = { }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(45.dp))
                        .background(brush = Brush.verticalGradient(colors = coloresRegistroExitoso))
                        .semantics { liveRegion = LiveRegionMode.Polite },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tu registro fue\nexitoso",
                        color = if (modoOscuro) AzulTextoClaro else CremaOscuro,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 30.sp
                    )
                }
            }

            LaunchedEffect(viewModel.mostrarRegistroExitoso) {
                delay(1000)
                viewModel.registroExitosoManejado()
                viewModel.usuarioRegistrado?.let { usuario ->
                    onRegistroExitoso(usuario)
                }
            }
        }
    }
}
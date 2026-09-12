package com.example.wahsly.ui.pantallas

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wahsly.R
import com.example.wahsly.datos.database.AppDatabase
import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.datos.repository.UsuarioRepository
import com.example.wahsly.ui.componentes.BotonDegradado
import com.example.wahsly.ui.theme.AzulGradienteOscuro
import com.example.wahsly.ui.theme.AzulPrincipalClaro
import com.example.wahsly.ui.theme.AzulTextoClaro
import com.example.wahsly.ui.theme.CremaOscuro
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.theme.RosaClaro
import com.example.wahsly.ui.theme.RosaOscuro
import com.example.wahsly.ui.theme.TextoBlancoClaro
import com.example.wahsly.ui.theme.TextoSecundarioClaro
import com.example.wahsly.ui.theme.TextoSecundarioOscuro
import com.example.wahsly.viewmodels.RegistroViewModel
import com.example.wahsly.viewmodels.ViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun PantallaRegistro(
    modoOscuro: Boolean,
    onRegistroExitoso: (Usuario) -> Unit,
    onVolverLogin: () -> Unit
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val repository = remember { UsuarioRepository(db.usuarioDao()) }
    val viewModel: RegistroViewModel = viewModel(factory = ViewModelFactory(repository))

    // Mostrar Toast cuando aparezca mensaje de error
    LaunchedEffect(viewModel.mensajeError) {
        viewModel.mensajeError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.mensajeErrorMostrado()
        }
    }

    // ============================================
    // RESPONSIVE: DETECCIÓN DE DISPOSITIVO
    // ============================================
    val configuration = LocalConfiguration.current
    val anchoDp = configuration.screenWidthDp
    val altoDp = configuration.screenHeightDp
    val esTablet = anchoDp >= 600
    val esHorizontal = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val esCelularHorizontal = esHorizontal && !esTablet

    // ---- TAMAÑOS DINÁMICOS ----
    val paddingLateral = when {
        esTablet -> 40.dp
        esCelularHorizontal -> 48.dp
        else -> 24.dp
    }

    val paddingVertical = when {
        esCelularHorizontal -> 20.dp
        esTablet -> 45.dp
        else -> 32.dp
    }

    val tamanoLogo = when {
        esCelularHorizontal -> 60.dp
        esTablet -> 80.dp
        else -> 70.dp
    }

    val fontSizeTitulo = when {
        esCelularHorizontal -> 22.sp
        esTablet -> 26.sp
        else -> 24.sp
    }

    val fontSizeTexto = when {
        esCelularHorizontal -> 15.sp
        esTablet -> 18.sp
        else -> 16.sp
    }

    val fontSizeTextoGrande = when {
        esCelularHorizontal -> 17.sp
        esTablet -> 20.sp
        else -> 18.sp
    }

    val fontSizePlaceholder = when {
        esCelularHorizontal -> 16.sp
        esTablet -> 19.sp
        else -> 17.sp
    }

    val alturaCampo = when {
        esCelularHorizontal -> 52.dp
        esTablet -> 60.dp
        else -> 56.dp
    }

    val espacioEntreCampos = when {
        esCelularHorizontal -> 10.dp
        esTablet -> 18.dp
        else -> 14.dp
    }

    val maxAnchoContenido: Dp = if (esTablet) 500.dp else Dp.Unspecified

    val altoDialogo = when {
        esCelularHorizontal -> 140.dp
        esTablet -> 200.dp
        else -> 180.dp
    }

    val fontSizeDialogo = when {
        esCelularHorizontal -> 22.sp
        esTablet -> 30.sp
        else -> 26.sp
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
                .verticalScroll(rememberScrollState())
                .padding(
                    start = paddingLateral,
                    end = paddingLateral,
                    top = paddingVertical,
                    bottom = paddingVertical
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Contenedor para limitar el ancho en tablets
            Column(
                modifier = Modifier
                    .then(
                        if (maxAnchoContenido != Dp.Unspecified) {
                            Modifier.widthIn(max = maxAnchoContenido)
                        } else {
                            Modifier
                        }
                    )
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = if (modoOscuro) painterResource(id = R.drawable.icono_washly_crear_cuenta_oscuro)
                        else painterResource(id = R.drawable.icono_washly_crear_cuenta_claro),
                        contentDescription = "Logo de Wahsly",
                        modifier = Modifier.size(tamanoLogo)
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "¿Ya tienes una cuenta?",
                            color = colorTextoSecundario,
                            fontSize = fontSizeTexto
                        )
                        Text(
                            text = "Iniciar sesión",
                            color = colorRosa,
                            fontSize = fontSizeTextoGrande,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable(role = Role.Button, onClick = onVolverLogin)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Crea tu cuenta",
                    fontSize = fontSizeTitulo,
                    fontWeight = FontWeight.Bold,
                    color = colorTitulo,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Nombre
                OutlinedTextField(
                    value = viewModel.nombre,
                    onValueChange = viewModel::onNombreChange,
                    modifier = Modifier.fillMaxWidth().height(alturaCampo),
                    placeholder = { Text("Tu nombre", fontSize = fontSizePlaceholder, color = colorTextoCampo) },
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

                Spacer(modifier = Modifier.height(espacioEntreCampos))

                // Apellido
                OutlinedTextField(
                    value = viewModel.apellido,
                    onValueChange = viewModel::onApellidoChange,
                    modifier = Modifier.fillMaxWidth().height(alturaCampo),
                    placeholder = { Text("Tu apellido", fontSize = fontSizePlaceholder, color = colorTextoCampo) },
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

                Spacer(modifier = Modifier.height(espacioEntreCampos))

                // Correo
                OutlinedTextField(
                    value = viewModel.correo,
                    onValueChange = viewModel::onCorreoChange,
                    modifier = Modifier.fillMaxWidth().height(alturaCampo),
                    placeholder = { Text("ejemplo@correo.com", fontSize = fontSizePlaceholder, color = colorTextoCampo) },
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

                Spacer(modifier = Modifier.height(espacioEntreCampos))

                // Contraseña
                OutlinedTextField(
                    value = viewModel.contrasena,
                    onValueChange = viewModel::onContrasenaChange,
                    modifier = Modifier.fillMaxWidth().height(alturaCampo),
                    placeholder = { Text("Crea tu contraseña", fontSize = fontSizePlaceholder, color = colorTextoCampo) },
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

                Spacer(modifier = Modifier.height(espacioEntreCampos))

                // Confirmar contraseña
                OutlinedTextField(
                    value = viewModel.confirmarContrasena,
                    onValueChange = viewModel::onConfirmarContrasenaChange,
                    modifier = Modifier.fillMaxWidth().height(alturaCampo),
                    placeholder = { Text("Confirma tu contraseña", fontSize = fontSizePlaceholder, color = colorTextoCampo) },
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

                Spacer(modifier = Modifier.height(espacioEntreCampos))

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
                    Text(
                        text = "  Acepta la Política de Privacidad",
                        fontSize = fontSizeTexto,
                        color = colorCampo
                    )
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

                Spacer(modifier = Modifier.height(20.dp))
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
                        .height(altoDialogo)
                        .clip(RoundedCornerShape(45.dp))
                        .background(brush = Brush.verticalGradient(colors = coloresRegistroExitoso))
                        .semantics { liveRegion = LiveRegionMode.Polite },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tu registro fue\nexitoso",
                        color = if (modoOscuro) AzulTextoClaro else CremaOscuro,
                        fontSize = fontSizeDialogo,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = fontSizeDialogo
                    )
                }
            }

            LaunchedEffect(Unit) {
                delay(1000)
                viewModel.registroExitosoManejado()
                viewModel.usuarioRegistrado?.let { usuario ->
                    onRegistroExitoso(usuario)
                }
            }
        }
    }
}
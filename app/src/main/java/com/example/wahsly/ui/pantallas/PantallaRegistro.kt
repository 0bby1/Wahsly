package com.example.wahsly.ui.pantallas

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.example.wahsly.R
import com.example.wahsly.datos.model.Usuario
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
import com.example.wahsly.utilidades.TipoPantalla
import com.example.wahsly.utilidades.obtenerTipoPantalla
import com.example.wahsly.utilidades.vibrar

@Composable
fun PantallaRegistro(
    modoOscuro: Boolean,
    windowSizeClass: WindowSizeClass,
    onRegistroExitoso: (Usuario) -> Unit,
    onVolverLogin: () -> Unit
) {
    val context = LocalContext.current

    val viewModel: RegistroViewModel = viewModel(
        factory = ViewModelFactory()
    )

    // Mostrar Toast cuando aparezca mensaje de error
    LaunchedEffect(viewModel.mensajeError) {
        viewModel.mensajeError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.mensajeErrorMostrado()
        }
    }

    val tipoPantalla = obtenerTipoPantalla(windowSizeClass)

    val paddingHorizontal = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 30.dp
        TipoPantalla.TABLET_VERTICAL -> 50.dp
        TipoPantalla.TABLET_HORIZONTAL -> 60.dp
    }

    val paddingSuperior = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 20.dp
        TipoPantalla.TABLET_VERTICAL -> 30.dp
        TipoPantalla.TABLET_HORIZONTAL -> 18.dp
    }

    val tamanoLogo = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 52.dp
        TipoPantalla.TABLET_VERTICAL -> 58.dp
        TipoPantalla.TABLET_HORIZONTAL -> 58.dp
    }

    val anchoFormulario = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 390.dp
        TipoPantalla.TABLET_VERTICAL -> 600.dp
        TipoPantalla.TABLET_HORIZONTAL -> 515.dp
    }

    val alturaCampo = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 58.dp
        TipoPantalla.TABLET_VERTICAL -> 80.dp
        TipoPantalla.TABLET_HORIZONTAL -> 62.dp
    }

    val espacioEntreCampos = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 16.dp
        TipoPantalla.TABLET_VERTICAL -> 40.dp
        TipoPantalla.TABLET_HORIZONTAL -> 28.dp
    }

    val fontSizeTitulo = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 30.sp
        TipoPantalla.TABLET_VERTICAL -> 34.sp
        TipoPantalla.TABLET_HORIZONTAL -> 36.sp
    }

    val fontSizePlaceholder = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 18.sp
        TipoPantalla.TABLET_VERTICAL -> 20.sp
        TipoPantalla.TABLET_HORIZONTAL -> 18.sp
    }

    val anchoBoton = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 230.dp
        TipoPantalla.TABLET_VERTICAL -> 260.dp
        TipoPantalla.TABLET_HORIZONTAL -> 360.dp
    }

    val altoBoton = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 56.dp
        TipoPantalla.TABLET_VERTICAL -> 68.dp
        TipoPantalla.TABLET_HORIZONTAL -> 68.dp
    }

    val altoDialogo = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 180.dp
        TipoPantalla.TABLET_VERTICAL -> 200.dp
        TipoPantalla.TABLET_HORIZONTAL -> 180.dp
    }

    val fontSizeDialogo = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 26.sp
        TipoPantalla.TABLET_VERTICAL -> 30.sp
        TipoPantalla.TABLET_HORIZONTAL -> 28.sp
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
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = paddingHorizontal,
                    end = paddingHorizontal,
                    top = paddingSuperior,
                    bottom = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // CABECERA
            if (tipoPantalla == TipoPantalla.TABLET_HORIZONTAL) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                ) {
                    // Logo a la izquierda
                    Image(
                        painter = if (modoOscuro) {
                            painterResource(
                                id = R.drawable.icono_washly_crear_cuenta_oscuro
                            )
                        } else {
                            painterResource(
                                id = R.drawable.icono_washly_crear_cuenta_claro
                            )
                        },
                        contentDescription = "Logo de Wahsly",
                        modifier = Modifier
                            .size(tamanoLogo)
                            .align(Alignment.CenterStart)
                    )

                    // Título centrado
                    Text(
                        text = "Crea tu cuenta",
                        fontSize = fontSizeTitulo,
                        fontWeight = FontWeight.Bold,
                        color = colorTitulo,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // Ya tienes cuenta / Iniciar sesión
                    Column(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "¿Ya tienes una cuenta?",
                            color = colorTextoSecundario,
                            fontSize = 15.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Iniciar Sesión",
                            color = colorRosa,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable(
                                role = Role.Button,
                                onClick = onVolverLogin
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            } else {
                // Teléfono y tablet vertical:
                // logo izquierda + iniciar sesión derecha.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = if (modoOscuro) {
                            painterResource(
                                id = R.drawable.icono_washly_crear_cuenta_oscuro
                            )
                        } else {
                            painterResource(
                                id = R.drawable.icono_washly_crear_cuenta_claro
                            )
                        },
                        contentDescription = "Logo de Wahsly",
                        modifier = Modifier.size(tamanoLogo)
                    )
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "¿Ya tienes una cuenta?",
                            color = colorTextoSecundario,
                            fontSize = if (
                                tipoPantalla == TipoPantalla.TELEFONO
                            ) 13.sp else 15.sp
                        )

                        Text(
                            text = "Iniciar Sesión",
                            color = colorRosa,
                            fontSize = if (
                                tipoPantalla == TipoPantalla.TELEFONO
                            ) 16.sp else 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable(
                                role = Role.Button,
                                onClick = onVolverLogin
                            )
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(
                        if (tipoPantalla == TipoPantalla.TELEFONO) {
                            28.dp
                        } else {
                            42.dp
                        }
                    )
                )

                Text(
                    text = "Crea tu cuenta",
                    fontSize = fontSizeTitulo,
                    fontWeight = FontWeight.Bold,
                    color = colorTitulo,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(
                        if (tipoPantalla == TipoPantalla.TELEFONO) {
                            28.dp
                        } else {
                            38.dp
                        }
                    )
                )
            }


            // FORMULARIO
            Column(
                modifier = Modifier
                    .widthIn(max = anchoFormulario)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // NOMBRE
                OutlinedTextField(
                    value = viewModel.nombre,
                    onValueChange = viewModel::onNombreChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaCampo),
                    placeholder = {
                        Text(
                            text = "Nombre",
                            fontSize = fontSizePlaceholder,
                            color = colorTextoCampo
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = colorIcono
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorCampo,
                        unfocusedContainerColor = colorCampo,
                        focusedBorderColor = colorCampo,
                        unfocusedBorderColor = colorCampo,
                        focusedTextColor = colorTextoCampo,
                        unfocusedTextColor = colorTextoCampo,
                        focusedPlaceholderColor = colorTextoCampo,
                        unfocusedPlaceholderColor = colorTextoCampo,
                        focusedLeadingIconColor = colorIcono,
                        unfocusedLeadingIconColor = colorIcono,
                        cursorColor = colorTextoCampo
                    )
                )

                Spacer(modifier = Modifier.height(espacioEntreCampos))

                // APELLIDO
                OutlinedTextField(
                    value = viewModel.apellido,
                    onValueChange = viewModel::onApellidoChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaCampo),
                    placeholder = {
                        Text(
                            text = "Apellido",
                            fontSize = fontSizePlaceholder,
                            color = colorTextoCampo
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = colorIcono
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorCampo,
                        unfocusedContainerColor = colorCampo,
                        focusedBorderColor = colorCampo,
                        unfocusedBorderColor = colorCampo,
                        focusedTextColor = colorTextoCampo,
                        unfocusedTextColor = colorTextoCampo,
                        focusedPlaceholderColor = colorTextoCampo,
                        unfocusedPlaceholderColor = colorTextoCampo,
                        focusedLeadingIconColor = colorIcono,
                        unfocusedLeadingIconColor = colorIcono,
                        cursorColor = colorTextoCampo
                    )
                )

                Spacer(modifier = Modifier.height(espacioEntreCampos))

                // CORREO
                OutlinedTextField(
                    value = viewModel.correo,
                    onValueChange = viewModel::onCorreoChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaCampo),
                    placeholder = {
                        Text(
                            text = "Correo electrónico",
                            fontSize = fontSizePlaceholder,
                            color = colorTextoCampo
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorCampo,
                        unfocusedContainerColor = colorCampo,
                        focusedBorderColor = colorCampo,
                        unfocusedBorderColor = colorCampo,
                        focusedTextColor = colorTextoCampo,
                        unfocusedTextColor = colorTextoCampo,
                        focusedPlaceholderColor = colorTextoCampo,
                        unfocusedPlaceholderColor = colorTextoCampo,
                        focusedLeadingIconColor = colorIcono,
                        unfocusedLeadingIconColor = colorIcono,
                        cursorColor = colorTextoCampo
                    )
                )

                Spacer(modifier = Modifier.height(espacioEntreCampos))


                // CONTRASEÑA
                OutlinedTextField(
                    value = viewModel.contrasena,
                    onValueChange = viewModel::onContrasenaChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaCampo),
                    placeholder = {
                        Text(
                            text = "Crea tu contraseña",
                            fontSize = fontSizePlaceholder,
                            color = colorTextoCampo
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
                        IconButton(
                            onClick = {
                                viewModel.toggleMostrarContrasena()
                            }
                        ) {
                            Icon(
                                imageVector =
                                    if (viewModel.mostrarContrasena) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                contentDescription =
                                    if (viewModel.mostrarContrasena) {
                                        "Ocultar contraseña"
                                    } else {
                                        "Mostrar contraseña"
                                    },
                                tint = colorTextoCampo
                            )
                        }
                    },
                    visualTransformation =
                        if (viewModel.mostrarContrasena) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorCampo,
                        unfocusedContainerColor = colorCampo,
                        focusedBorderColor = colorCampo,
                        unfocusedBorderColor = colorCampo,
                        focusedTextColor = colorTextoCampo,
                        unfocusedTextColor = colorTextoCampo,
                        focusedPlaceholderColor = colorTextoCampo,
                        unfocusedPlaceholderColor = colorTextoCampo,
                        focusedLeadingIconColor = colorIcono,
                        unfocusedLeadingIconColor = colorIcono,
                        focusedTrailingIconColor = colorIcono,
                        unfocusedTrailingIconColor = colorIcono,
                        cursorColor = colorTextoCampo
                    )
                )



                // REQUISITOS DE CONTRASEÑA

                if (viewModel.contrasena.isNotEmpty()) {

                    val pendientes = buildList {

                        if (viewModel.contrasena.length < 8) {
                            add("Mínimo 8 caracteres")
                        }

                        if (!Regex("[A-Z]").containsMatchIn(
                                viewModel.contrasena
                            )
                        ) {
                            add("Una letra mayúscula")
                        }

                        if (!Regex("[0-9]").containsMatchIn(
                                viewModel.contrasena
                            )
                        ) {
                            add("Un número")
                        }

                        if (!Regex("[^A-Za-z0-9]").containsMatchIn(
                                viewModel.contrasena
                            )
                        ) {
                            add("Un símbolo especial (!, @, #)")
                        }

                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (pendientes.isEmpty()) {
                                    Color(0xFF2E7D32).copy(
                                        alpha = 0.12f
                                    )
                                } else if (modoOscuro) {
                                    Color.White.copy(alpha = 0.08f)
                                } else {
                                    AzulPrincipalClaro.copy(
                                        alpha = 0.10f
                                    )
                                },
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(
                                horizontal = 12.dp,
                                vertical = 10.dp
                            ),
                        verticalArrangement =
                            Arrangement.spacedBy(5.dp)
                    ) {

                        if (pendientes.isEmpty()) {

                            Text(
                                text = "✓ Contraseña válida",
                                fontSize = 13.sp,
                                color = if (modoOscuro) {
                                    Color(0xFF81C784)
                                } else {
                                    Color(0xFF2E7D32)
                                }
                            )

                        } else {

                            Text(
                                text = "A tu contraseña le falta:",
                                fontSize = 13.sp,
                                color = colorTitulo
                            )

                            pendientes.forEach { requisito ->

                                Text(
                                    text = "• $requisito",
                                    fontSize = 12.sp,
                                    color = colorTitulo.copy(
                                        alpha = 0.85f
                                    )
                                )

                            }

                        }

                    }

                }

                Spacer(
                    modifier = Modifier.height(
                        espacioEntreCampos
                    )
                )


                // CONFIRMAR CONTRASEÑA
                OutlinedTextField(
                    value = viewModel.confirmarContrasena,
                    onValueChange = viewModel::onConfirmarContrasenaChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(alturaCampo),
                    placeholder = {
                        Text(
                            text = "Confirma tu contraseña",
                            fontSize = fontSizePlaceholder,
                            color = colorTextoCampo
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
                        IconButton(
                            onClick = {
                                viewModel.toggleMostrarConfirmacion()
                            }
                        ) {
                            Icon(
                                imageVector =
                                    if (viewModel.mostrarConfirmacion) {
                                        Icons.Default.VisibilityOff
                                    } else {
                                        Icons.Default.Visibility
                                    },
                                contentDescription = null,
                                tint = colorTextoCampo
                            )
                        }
                    },
                    visualTransformation =
                        if (viewModel.mostrarConfirmacion) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorCampo,
                        unfocusedContainerColor = colorCampo,
                        focusedBorderColor = colorCampo,
                        unfocusedBorderColor = colorCampo,
                        focusedTextColor = colorTextoCampo,
                        unfocusedTextColor = colorTextoCampo,
                        focusedPlaceholderColor = colorTextoCampo,
                        unfocusedPlaceholderColor = colorTextoCampo,
                        focusedLeadingIconColor = colorIcono,
                        unfocusedLeadingIconColor = colorIcono,
                        focusedTrailingIconColor = colorIcono,
                        unfocusedTrailingIconColor = colorIcono,
                        cursorColor = colorTextoCampo
                    )
                )
            }

            // PARTE INFERIOR
            if (tipoPantalla == TipoPantalla.TELEFONO) {
                Spacer(modifier = Modifier.height(22.dp))
                PoliticaPrivacidadRegistro(
                    aceptada = viewModel.aceptaPolitica,
                    onClick = {
                        viewModel.toggleAceptaPolitica()
                    },
                    colorRosa = colorRosa,
                    colorTexto = colorCampo
                )

                Spacer(modifier = Modifier.height(26.dp))

                BotonRegistroResponsivo(
                    texto = "Regístrate",
                    modoOscuro = modoOscuro,
                    modifier = Modifier
                        .width(anchoBoton)
                        .height(altoBoton),
                    onClick = {
                        viewModel.registrar()
                    }
                )

            } else {
                Spacer(
                    modifier = Modifier.height(
                        if (tipoPantalla == TipoPantalla.TABLET_VERTICAL) {
                            32.dp
                        } else {
                            26.dp
                        }
                    )
                )

                BotonRegistroResponsivo(
                    texto = "Crear Cuenta",
                    modoOscuro = modoOscuro,
                    modifier = Modifier
                        .width(anchoBoton)
                        .height(altoBoton),
                    onClick = {
                        viewModel.registrar()
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                PoliticaPrivacidadRegistro(
                    aceptada = viewModel.aceptaPolitica,
                    onClick = {
                        viewModel.toggleAceptaPolitica()
                    },
                    colorRosa = colorRosa,
                    colorTexto = colorCampo
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
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
                vibrar(context)
                delay(1000)
                viewModel.registroExitosoManejado()
                viewModel.usuarioRegistrado?.let { usuario ->
                    onRegistroExitoso(usuario)
                }
            }
        }
    }
}

@Composable
private fun PoliticaPrivacidadRegistro(
    aceptada: Boolean,
    onClick: () -> Unit,
    colorRosa: Color,
    colorTexto: Color
) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .semantics(mergeDescendants = true) { }
            .clickable(
                role = Role.Checkbox,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Switch(
            checked = aceptada,
            onCheckedChange = null,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                uncheckedThumbColor = Color.White,
                checkedTrackColor = colorRosa,
                checkedBorderColor = Color.Transparent,
                uncheckedBorderColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Acepto la Política de Privacidad",
            fontSize = 16.sp,
            color = colorTexto
        )
    }
}

@Composable
private fun BotonRegistroResponsivo(
    texto: String,
    modoOscuro: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val coloresBoton = if (modoOscuro) {
        listOf(
            RosaOscuro,
            CremaOscuro
        )
    } else {
        listOf(
            Color(0xFF8AA6B8),
            Color(0xFF4F73A0)
        )
    }

    val colorTexto = if (modoOscuro) {
        FondoOscuro
    } else {
        Color.White
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = coloresBoton
                )
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = texto,
            color = colorTexto,
            fontSize = 21.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun RequisitoContrasena(
    texto: String,
    cumplido: Boolean,
    colorTexto: Color
) {

    val colorRequisito =
        if (cumplido) {
            Color(0xFF2E7D32)
        } else {
            colorTexto.copy(alpha = 0.75f)
        }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = if (cumplido) "✓" else "○",
            color = colorRequisito,
            fontSize = 15.sp
        )

        Text(
            text = texto,
            color = colorRequisito,
            fontSize = 13.sp
        )

    }

}

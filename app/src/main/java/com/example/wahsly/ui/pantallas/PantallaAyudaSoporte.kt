
package com.example.wahsly.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.datos.repository.FirebaseSoporteRepository
import com.example.wahsly.ui.theme.AzulPrincipalClaro
import com.example.wahsly.ui.theme.AzulTextoClaro
import com.example.wahsly.ui.theme.CremaOscuro
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.theme.RosaOscuro
import com.example.wahsly.ui.theme.TarjetaPerfilOscuro
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@Composable
fun PantallaAyudaSoporte(
    usuario: Usuario?,
    modoOscuro: Boolean,
    onVolver: () -> Unit
) {

    val soporteRepository = remember {
        FirebaseSoporteRepository()
    }

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    var categoriaSeleccionada by rememberSaveable {
        mutableStateOf("")
    }

    var mensaje by rememberSaveable {
        mutableStateOf("")
    }

    var enviando by remember {
        mutableStateOf(false)
    }

    val categorias = listOf(
        "Reportar un error",
        "Enviar una sugerencia",
        "Presentar una queja",
        "Otro"
    )

    val colorFondo =
        if (modoOscuro) FondoOscuro else FondoClaro

    val colorTexto =
        if (modoOscuro) CremaOscuro else AzulTextoClaro

    val colorTarjeta =
        if (modoOscuro) TarjetaPerfilOscuro else Color.White

    val colorAcento =
        if (modoOscuro) RosaOscuro else AzulPrincipalClaro

    val colorSecundario =
        colorTexto.copy(alpha = 0.7f)

    val colorCampo =
        if (modoOscuro) FondoOscuro else Color(0xFFF7F7F7)

    val puedeEnviar =
        usuario != null &&
                categoriaSeleccionada.isNotBlank() &&
                mensaje.trim().isNotBlank() &&
                !enviando

    Scaffold(
        containerColor = colorFondo,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { padding ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(colorFondo)
                .padding(padding)
                .imePadding(),
            contentAlignment = Alignment.TopCenter
        ) {

            val esHorizontal = maxWidth > maxHeight

            val esTablet =
                minOf(maxWidth, maxHeight) >= 600.dp

            val anchoMaximo = when {
                esTablet && esHorizontal -> 750.dp
                esTablet -> 600.dp
                esHorizontal -> 620.dp
                else -> 550.dp
            }

            val paddingLateral =
                if (esTablet) 32.dp else 20.dp

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = anchoMaximo)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = paddingLateral,
                        vertical = 16.dp
                    )
            ) {

                // ENCABEZADO

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(
                        enabled = !enviando,
                        onClick = onVolver
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = colorTexto
                        )

                    }

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = "Ayuda y Soporte",
                        color = colorTexto,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // PRESENTACIÓN

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Surface(
                        modifier = Modifier.size(66.dp),
                        shape = CircleShape,
                        color = colorAcento.copy(alpha = 0.15f)
                    ) {

                        Box(
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = colorAcento,
                                modifier = Modifier.size(34.dp)
                            )

                        }

                    }

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Text(
                        text = "¿En qué podemos ayudarte?",
                        color = colorTexto,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Tu opinión nos ayuda a mejorar Wahsly. " +
                                    "Cuéntanos qué ocurrió o qué te gustaría mejorar.",
                        color = colorSecundario,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )

                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                // CATEGORÍAS

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = colorTarjeta,
                    shadowElevation = 3.dp
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Motivo del mensaje",
                            color = colorTexto,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                "Selecciona la opción que mejor " +
                                        "describa tu situación.",
                            color = colorSecundario,
                            fontSize = 13.sp
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        categorias.forEach { categoria ->

                            FilterChip(
                                selected =
                                    categoriaSeleccionada == categoria,
                                onClick = {
                                    categoriaSeleccionada = categoria
                                },
                                enabled = !enviando,
                                modifier = Modifier.fillMaxWidth(),
                                label = {

                                    Text(
                                        text = categoria,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(
                                            vertical = 5.dp
                                        )
                                    )

                                },
                                shape = RoundedCornerShape(14.dp),
                                colors =
                                    FilterChipDefaults.filterChipColors(
                                        containerColor = colorCampo,
                                        labelColor = colorTexto,
                                        selectedContainerColor =
                                            colorAcento.copy(
                                                alpha = 0.18f
                                            ),
                                        selectedLabelColor = colorTexto
                                    )
                            )

                            Spacer(
                                modifier = Modifier.height(3.dp)
                            )

                        }

                    }

                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                // MENSAJE

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = colorTarjeta,
                    shadowElevation = 3.dp
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Describe tu situación",
                            color = colorTexto,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                "Explícanos qué ocurrió o qué " +
                                        "te gustaría mejorar.",
                            color = colorSecundario,
                            fontSize = 13.sp
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        OutlinedTextField(
                            value = mensaje,
                            onValueChange = {
                                if (it.length <= 2000) {
                                    mensaje = it
                                }
                            },
                            enabled = !enviando,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 170.dp),
                            placeholder = {

                                Text(
                                    text =
                                        "Escribe aquí tu mensaje...",
                                    color = colorSecundario
                                )

                            },
                            minLines = 5,
                            maxLines = 10,
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization =
                                    KeyboardCapitalization.Sentences
                            ),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor =
                                        colorCampo,
                                    unfocusedContainerColor =
                                        colorCampo,
                                    focusedTextColor =
                                        colorTexto,
                                    unfocusedTextColor =
                                        colorTexto,
                                    focusedBorderColor =
                                        colorAcento,
                                    unfocusedBorderColor =
                                        colorTexto.copy(
                                            alpha = 0.25f
                                        ),
                                    cursorColor = colorAcento
                                )
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "${mensaje.length}/2000 caracteres",
                            color = colorSecundario,
                            fontSize = 12.sp,
                            modifier = Modifier.align(
                                Alignment.End
                            )
                        )

                    }

                }

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                // INFORMACIÓN DEL USUARIO

                if (usuario != null) {

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = colorTarjeta,
                        shadowElevation = 3.dp
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                text = "Información de contacto",
                                color = colorTexto,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )

                            Text(
                                text = usuario.nombre,
                                color = colorTexto,
                                fontSize = 16.sp
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = usuario.correo,
                                color = colorSecundario,
                                fontSize = 14.sp
                            )

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )

                            Text(
                                text =
                                    "Utilizaremos estos datos " +
                                            "para identificar tu solicitud.",
                                color = colorSecundario,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )

                        }

                    }

                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                // BOTÓN ENVIAR

                Button(
                    onClick = {

                        if (enviando) {
                            return@Button
                        }

                        val categoriaEnviar =
                            categoriaSeleccionada

                        val mensajeEnviar =
                            mensaje.trim()

                        enviando = true

                        scope.launch {

                            try {

                                soporteRepository.enviarSolicitud(
                                    nombre =
                                        usuario?.nombre.orEmpty(),
                                    categoria = categoriaEnviar,
                                    mensaje = mensajeEnviar
                                )

                                categoriaSeleccionada = ""
                                mensaje = ""

                                snackbarHostState.showSnackbar(
                                    "Tu solicitud se guardó correctamente."
                                )

                            } catch (
                                e: CancellationException
                            ) {

                                throw e

                            } catch (e: Exception) {

                                android.util.Log.e(
                                    "WAHSLY_SOPORTE",
                                    "Error al guardar solicitud",
                                    e
                                )

                                snackbarHostState.showSnackbar(
                                    e.message
                                        ?: "No se pudo guardar tu solicitud."
                                )

                            } finally {

                                enviando = false

                            }

                        }

                    },
                    enabled = puedeEnviar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorAcento,
                        disabledContainerColor =
                            colorAcento.copy(alpha = 0.35f)
                    )
                ) {

                    if (enviando) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Text(
                            text = "Enviando...",
                            fontSize = 16.sp
                        )

                    } else {

                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Text(
                            text = "Enviar mensaje",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                    }

                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text =
                        "Tu mensaje quedará registrado " +
                                "para que podamos darle seguimiento.",
                    color = colorSecundario,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(26.dp)
                )

            }

        }

    }

}

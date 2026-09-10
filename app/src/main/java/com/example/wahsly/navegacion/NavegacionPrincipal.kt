package com.example.wahsly.navegacion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.wahsly.ui.componentes.BarraInferiorAnimada
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.pantallas.PantallaPerfil
import com.example.wahsly.ui.pantallas.PantallaPrincipal
import com.example.wahsly.datos.model.Usuario
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.wahsly.ui.pantallas.PantallaEscaner
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.wahsly.ia.GeminiLavado
import com.example.wahsly.ia.ResultadoLavado
import com.example.wahsly.ui.theme.AzulPrincipalClaro
import com.example.wahsly.ui.theme.RosaOscuro
import com.example.wahsly.ui.theme.TarjetaPerfilOscuro
import kotlinx.coroutines.launch
import com.example.wahsly.datos.database.AppDatabase
import com.example.wahsly.datos.model.RegistroEscaneo
import com.example.wahsly.datos.repository.HistorialRepository
import com.example.wahsly.ia.aJson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NavegacionPrincipal(
    seccionActual: String,
    usuario: Usuario?,
    modoOscuro: Boolean,
    onCambiarSeccion: (String) -> Unit,
    onConfiguracion: () -> Unit,
    onCerrarSesion: () -> Unit
) {

    val context = LocalContext.current

    val db = remember { AppDatabase.getDatabase(context) }
    val historialRepository = remember { HistorialRepository(db.historialDao()) }

    val scope = rememberCoroutineScope()
    var analizandoEtiqueta by remember { mutableStateOf(false) }
    var resultadoLavado by remember { mutableStateOf<ResultadoLavado?>(null) }
    var errorGemini by remember { mutableStateOf<String?>(null) }
    var primeraCarga by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        primeraCarga = false
    }

    val animarCambioCabecera = !primeraCarga

    val seleccionado = when (seccionActual) {
        "INICIO" -> 0
        "ESCANER" -> 1
        "PERFIL" -> 2
        else -> 0
    }

    val colorFondo = if (modoOscuro) FondoOscuro else FondoClaro

    Scaffold(
        containerColor = colorFondo,
        bottomBar = {
            BarraInferiorAnimada(
                seleccionado = seleccionado,
                modoOscuro = modoOscuro,
                onInicio = {
                    if (seccionActual != "INICIO") {
                        onCambiarSeccion("INICIO")
                    }
                },
                onEscanear = {
                    if (seccionActual != "ESCANER") {
                        onCambiarSeccion("ESCANER")
                    }
                },
                onCuenta = {
                    if (seccionActual != "PERFIL") {
                        onCambiarSeccion("PERFIL")
                    }
                }
            )
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = padding.calculateBottomPadding()
                )
        ) {

            when (seccionActual) {
                "INICIO" -> {
                    PantallaPrincipal(
                        usuario = usuario,
                        modoOscuro = modoOscuro,
                        mostrarBarraInferior = false,
                        animarCabecera = animarCambioCabecera,
                        onPerfil = {
                            onCambiarSeccion("PERFIL")
                        },
                        onCerrarSesion = onCerrarSesion
                    )
                }

                "ESCANER" -> {
                    PantallaEscaner(
                        modoOscuro = modoOscuro,
                        onDatosConfirmados = { datos ->
                            if (!analizandoEtiqueta) {
                                scope.launch {
                                    analizandoEtiqueta = true
                                    resultadoLavado = null
                                    errorGemini = null
                                    try {
                                        val resultado =
                                            GeminiLavado.analizarEtiqueta(
                                                context = context,
                                                datos = datos
                                            )

                                        // SOLO GUARDAMOS SI GEMINI
                                        // PUDO LEER LA ETIQUETA
                                        if (resultado.etiquetaLegible) {
                                            val nombreRutina =
                                                if (resultado.composicion.isNotBlank() && !resultado.composicion.equals(
                                                        "No visible",
                                                        ignoreCase = true
                                                    )
                                                ) {
                                                    resultado.composicion
                                                } else {
                                                    "Rutina de lavado"
                                                }

                                            val fechaActual = SimpleDateFormat(
                                                    "dd/MM/yyyy HH:mm",
                                                    Locale.getDefault()
                                                ).format(Date())

                                            val correoUsuario = usuario?.correo

                                            if (correoUsuario.isNullOrBlank()) {
                                                throw IllegalStateException(
                                                    "No hay un usuario activo."
                                                )
                                            }

                                            val registro =
                                                RegistroEscaneo(
                                                    correoUsuario = correoUsuario,
                                                    nombre = nombreRutina,
                                                    fecha = fechaActual,
                                                    informacion = resultado.aJson()
                                                )
                                            historialRepository
                                                .agregarRegistro(registro)
                                        }

                                        resultadoLavado =
                                            resultado
                                    } catch (e: Exception) {
                                        errorGemini =
                                            e.message
                                                ?: "No se pudo analizar la etiqueta."
                                    } finally {
                                        analizandoEtiqueta = false
                                    }
                                }
                            }
                        }
                    )
                }

                "PERFIL" -> {
                    PantallaPerfil(
                        usuario = usuario,
                        modoOscuro = modoOscuro,
                        mostrarBarraInferior = false,
                        animarCabecera = animarCambioCabecera,
                        onConfiguracion = onConfiguracion,
                        onVolver = {
                            onCambiarSeccion("INICIO")
                        },
                        onCerrarSesion = onCerrarSesion
                    )
                }
            }
        }
    }

    // CARGANDO - GEMINI
    if (analizandoEtiqueta) {
        val colorDialogo = if (modoOscuro) { TarjetaPerfilOscuro } else { FondoClaro }
        val colorTexto = if (modoOscuro) { RosaOscuro } else { AzulPrincipalClaro }

        AlertDialog(
            onDismissRequest = {
                // No se puede cerrar mientras analiza
            },
            containerColor = colorDialogo,

            title = {
                Text(
                    text = "Analizando etiqueta",
                    color = colorTexto
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = colorTexto
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Washly está leyendo las instrucciones del fabricante...",
                        color = colorTexto
                    )
                }
            },
            confirmButton = {}
        )
    }

    // RESULTADO DEL ANALISIS
    resultadoLavado?.let { resultado ->
        val colorDialogo = if (modoOscuro) { TarjetaPerfilOscuro } else { FondoClaro }
        val colorTexto = if (modoOscuro) { RosaOscuro } else { AzulPrincipalClaro }


        AlertDialog(
            onDismissRequest = {
                val fueLegible = resultado.etiquetaLegible
                resultadoLavado = null
                if (fueLegible) {
                    onCambiarSeccion(
                        "INICIO"
                    )
                }
            },
            containerColor = colorDialogo,

            title = {
                Text(
                    text =
                        if (resultado.etiquetaLegible) {
                            "Recomendación de lavado:"
                        } else {
                            "No se pudo leer la etiqueta"
                        },
                    color = colorTexto
                )
            },
            text = {
                if (!resultado.etiquetaLegible) {
                    Text(
                        text = resultado.mensaje,
                        color = colorTexto
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(
                                rememberScrollState()
                            )
                    ) {
                        Text(
                            text = "Prendas: ${resultado.cantidad}",
                            color = colorTexto
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Composición:",
                            color = colorTexto
                        )

                        Text(
                            text = resultado.composicion,
                            color = colorTexto
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Lavado:",
                            color = colorTexto
                        )

                        Text(
                            text = resultado.lavado,
                            color = colorTexto
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Temperatura:",
                            color = colorTexto
                        )

                        Text(
                            text = resultado.temperatura,
                            color = colorTexto
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Blanqueador:",
                            color = colorTexto
                        )

                        Text(
                            text = resultado.blanqueador,
                            color = colorTexto
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Secado:",
                            color = colorTexto
                        )

                        Text(
                            text = resultado.secado,
                            color = colorTexto
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Planchado:",
                            color = colorTexto
                        )

                        Text(
                            text = resultado.planchado,
                            color = colorTexto
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Limpieza profesional:",
                            color = colorTexto
                        )

                        Text(
                            text = resultado.limpiezaProfesional,
                            color = colorTexto
                        )


                        if (resultado.tieneMancha) {
                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Tratamiento de la mancha:",
                                color = colorTexto
                            )

                            Text(
                                text = resultado.tratamientoMancha,
                                color = colorTexto
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "⚠️ Precauciones",
                            color = colorTexto
                        )

                        Text(
                            text = resultado.precauciones,
                            color = colorTexto
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val fueLegible = resultado.etiquetaLegible
                        resultadoLavado = null

                        if (fueLegible) {
                            onCambiarSeccion(
                                "INICIO"
                            )
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }

    // ERROR DE GEMINI
    errorGemini?.let { mensaje ->
        val colorDialogo = if (modoOscuro) { TarjetaPerfilOscuro } else { FondoClaro }
        val colorTexto = if (modoOscuro) { RosaOscuro } else { AzulPrincipalClaro }

        AlertDialog(
            onDismissRequest = {
                errorGemini = null
            },

            containerColor = colorDialogo,

            title = {
                Text(
                    text = "No se pudo analizar",
                    color = colorTexto
                )
            },
            text = {
                Text(
                    text = mensaje,
                    color = colorTexto
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        errorGemini = null
                    }
                ) {
                    Text(
                        text = "Aceptar",
                        color = colorTexto
                    )
                }
            }
        )
    }
}
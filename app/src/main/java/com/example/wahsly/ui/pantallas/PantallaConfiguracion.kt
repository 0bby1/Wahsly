package com.example.wahsly.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wahsly.datos.database.AppDatabase
import com.example.wahsly.datos.model.Usuario
import com.example.wahsly.datos.repository.HistorialRepository
import com.example.wahsly.datos.repository.UsuarioRepository
import com.example.wahsly.ui.theme.AzulPrincipalClaro
import com.example.wahsly.ui.theme.AzulTextoClaro
import com.example.wahsly.ui.theme.CremaOscuro
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.theme.RosaOscuro
import com.example.wahsly.ui.theme.TarjetaPerfilOscuro
import com.example.wahsly.viewmodels.CuentaViewModel
import com.example.wahsly.viewmodels.ViewModelFactory

// COLOR DE ACCIONES PELIGROSAS (ELIMINAR CUENTA)
private val RojoPeligro = Color(0xFFD64545)

@Composable
fun PantallaConfiguracion(
    modoOscuro: Boolean,
    onCambiarModoOscuro: (Boolean) -> Unit,
    tamanoTexto: Float,
    onCambiarTamanoTexto: (Float) -> Unit,
    onVolver: () -> Unit,
    usuario: Usuario? = null,
    onCuentaEliminada: () -> Unit = {}
) {

    val context = LocalContext.current

    // DEPENDENCIAS PARA CAMBIAR CONTRASEÑA / ELIMINAR CUENTA
    val db = remember { AppDatabase.getDatabase(context) }
    val usuarioRepository = remember { UsuarioRepository(db.usuarioDao()) }
    val historialRepository = remember { HistorialRepository(db.historialDao()) }
    val cuentaViewModel: CuentaViewModel = viewModel(
        factory = ViewModelFactory(usuarioRepository, historialRepository)
    )

    // COLORES
    val colorFondo =
        if (modoOscuro) FondoOscuro else FondoClaro

    val colorTexto =
        if (modoOscuro) CremaOscuro else AzulTextoClaro

    val colorTarjeta =
        if (modoOscuro) TarjetaPerfilOscuro else Color.White

    val colorAcento =
        if (modoOscuro) RosaOscuro else AzulPrincipalClaro


    // G DIALOGO
    var mostrarDialogo by remember {
        mutableStateOf(false)
    }

    var seleccion by remember {
        mutableStateOf(tamanoTexto)
    }


    // G NOMBRE
    val nombreTamano = when {
        tamanoTexto < 0.95f -> "Pequeño"
        tamanoTexto > 1.10f -> "Grande"
        else -> "Mediano"
    }

    // G CUENTA: DIÁLOGOS
    var mostrarDialogoContrasena by remember { mutableStateOf(false) }
    var mostrarDialogoEliminarCuenta by remember { mutableStateOf(false) }

    // AVISO DE CONTRASEÑA CAMBIADA CON ÉXITO
    LaunchedEffect(cuentaViewModel.contrasenaCambiada) {
        if (cuentaViewModel.contrasenaCambiada) {
            mostrarDialogoContrasena = false
            Toast.makeText(
                context,
                "Contraseña actualizada correctamente",
                Toast.LENGTH_SHORT
            ).show()
            cuentaViewModel.contrasenaCambiadaManejada()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(24.dp)
    ) {

        // BOTÓN VOLVER + TÍTULO
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 6.dp)
        ) {

            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = colorTexto
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "Configuración",
                color = colorTexto,
                fontSize = 26.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            // TARJETA MODO OSCURO
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = colorTarjeta,
                shadowElevation = 4.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 18.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Modo oscuro",
                        color = colorTexto,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )

                    Switch(
                        checked = modoOscuro,

                        onCheckedChange = { activado ->
                            onCambiarModoOscuro(activado)
                        },

                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CremaOscuro,
                            checkedTrackColor = RosaOscuro,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFD9D9D9)
                        )
                    )
                }
            }


            // G ACCESIBILIDAD
            Text(
                text = "Accesibilidad",
                color = colorTexto,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 30.dp, bottom = 12.dp)
            )


            // G TAMAÑO DE TEXTO
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        seleccion = tamanoTexto
                        mostrarDialogo = true
                    },

                shape = RoundedCornerShape(18.dp),
                color = colorTarjeta,
                shadowElevation = 4.dp
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 18.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Tamaño del texto",
                            color = colorTexto,
                            fontSize = 18.sp
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = nombreTamano,
                            color = colorTexto.copy(
                                alpha = 0.65f
                            ),
                            fontSize = 14.sp
                        )
                    }

                    Icon(
                        imageVector =
                            Icons.Default.KeyboardArrowRight,

                        contentDescription =
                            "Tamaño del texto",

                        tint = colorTexto
                    )
                }
            }

            // G CUENTA
            if (usuario != null) {

                Text(
                    text = "Cuenta",
                    color = colorTexto,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 30.dp, bottom = 12.dp)
                )

                // CAMBIAR CONTRASEÑA
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            cuentaViewModel.mensajeErrorMostrado()
                            mostrarDialogoContrasena = true
                        },

                    shape = RoundedCornerShape(18.dp),
                    color = colorTarjeta,
                    shadowElevation = 4.dp
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 20.dp,
                                vertical = 18.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = colorTexto
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(
                            text = "Cambiar contraseña",
                            color = colorTexto,
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Cambiar contraseña",
                            tint = colorTexto
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ELIMINAR CUENTA
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            cuentaViewModel.mensajeErrorMostrado()
                            mostrarDialogoEliminarCuenta = true
                        },

                    shape = RoundedCornerShape(18.dp),
                    color = colorTarjeta,
                    shadowElevation = 4.dp
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 20.dp,
                                vertical = 18.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = null,
                            tint = RojoPeligro
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(
                            text = "Eliminar cuenta",
                            color = RojoPeligro,
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Eliminar cuenta",
                            tint = RojoPeligro
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }


    // G DIALOGO TAMAÑO DE TEXTO
    if (mostrarDialogo) {

        val densidadActual =
            LocalDensity.current

        val ajuste =
            seleccion / tamanoTexto

        val densidadPrevia = Density(
            density = densidadActual.density,
            fontScale =
                densidadActual.fontScale * ajuste
        )

        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
            },

            containerColor = colorTarjeta,

            title = {
                Text(
                    text = "Tamaño del texto",
                    color = colorTexto
                )
            },

            text = {

                Column {

                    Text(
                        text = "Vista previa",
                        color = colorTexto.copy(
                            alpha = 0.65f
                        ),
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    CompositionLocalProvider(
                        LocalDensity provides densidadPrevia
                    ) {

                        Text(
                            text = "Así se verá el texto dentro de Wahsly",
                            color = colorTexto,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )


                    OpcionTamano(
                        texto = "Pequeño",
                        escala = 0.85f,
                        seleccion = seleccion,
                        colorTexto = colorTexto,
                        colorAcento = colorAcento
                    ) {
                        seleccion = 0.85f
                    }


                    OpcionTamano(
                        texto = "Mediano",
                        escala = 1.0f,
                        seleccion = seleccion,
                        colorTexto = colorTexto,
                        colorAcento = colorAcento
                    ) {
                        seleccion = 1.0f
                    }


                    OpcionTamano(
                        texto = "Grande",
                        escala = 1.25f,
                        seleccion = seleccion,
                        colorTexto = colorTexto,
                        colorAcento = colorAcento
                    ) {
                        seleccion = 1.25f
                    }
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        onCambiarTamanoTexto(seleccion)
                        mostrarDialogo = false
                    }
                ) {
                    Text(
                        text = "Aceptar",
                        color = colorAcento
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        seleccion = tamanoTexto
                        mostrarDialogo = false
                    }
                ) {
                    Text(
                        text = "Cancelar",
                        color = colorTexto
                    )
                }
            }
        )
    }

    // G DIALOGO CAMBIAR CONTRASEÑA
    if (mostrarDialogoContrasena && usuario != null) {

        var contrasenaActual by remember { mutableStateOf("") }
        var contrasenaNueva by remember { mutableStateOf("") }
        var confirmarContrasenaNueva by remember { mutableStateOf("") }

        var mostrarActual by remember { mutableStateOf(false) }
        var mostrarNueva by remember { mutableStateOf(false) }
        var mostrarConfirmacion by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = {
                if (!cuentaViewModel.cargando) {
                    mostrarDialogoContrasena = false
                    cuentaViewModel.mensajeErrorMostrado()
                }
            },

            containerColor = colorTarjeta,

            title = {
                Text(
                    text = "Cambiar contraseña",
                    color = colorTexto
                )
            },

            text = {
                Column {

                    OutlinedTextField(
                        value = contrasenaActual,
                        onValueChange = { contrasenaActual = it },
                        label = { Text("Contraseña actual") },
                        singleLine = true,
                        visualTransformation =
                            if (mostrarActual) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { mostrarActual = !mostrarActual }) {
                                Icon(
                                    imageVector = if (mostrarActual) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = contrasenaNueva,
                        onValueChange = { contrasenaNueva = it },
                        label = { Text("Nueva contraseña") },
                        singleLine = true,
                        visualTransformation =
                            if (mostrarNueva) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { mostrarNueva = !mostrarNueva }) {
                                Icon(
                                    imageVector = if (mostrarNueva) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = confirmarContrasenaNueva,
                        onValueChange = { confirmarContrasenaNueva = it },
                        label = { Text("Confirmar nueva contraseña") },
                        singleLine = true,
                        visualTransformation =
                            if (mostrarConfirmacion) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { mostrarConfirmacion = !mostrarConfirmacion }) {
                                Icon(
                                    imageVector = if (mostrarConfirmacion) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    cuentaViewModel.mensajeError?.let { mensaje ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = mensaje,
                            color = RojoPeligro,
                            fontSize = 13.sp
                        )
                    }

                    if (cuentaViewModel.cargando) {
                        Spacer(modifier = Modifier.height(10.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = colorAcento
                        )
                    }
                }
            },

            confirmButton = {
                TextButton(
                    enabled = !cuentaViewModel.cargando,
                    onClick = {
                        cuentaViewModel.cambiarContrasena(
                            usuario = usuario,
                            contrasenaActual = contrasenaActual,
                            contrasenaNueva = contrasenaNueva,
                            confirmarContrasenaNueva = confirmarContrasenaNueva
                        )
                    }
                ) {
                    Text(text = "Guardar", color = colorAcento)
                }
            },

            dismissButton = {
                TextButton(
                    enabled = !cuentaViewModel.cargando,
                    onClick = {
                        mostrarDialogoContrasena = false
                        cuentaViewModel.mensajeErrorMostrado()
                    }
                ) {
                    Text(text = "Cancelar", color = colorTexto)
                }
            }
        )
    }

    // G DIALOGO ELIMINAR CUENTA
    if (mostrarDialogoEliminarCuenta && usuario != null) {

        var contrasenaConfirmacion by remember { mutableStateOf("") }
        var mostrarContrasena by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = {
                if (!cuentaViewModel.cargando) {
                    mostrarDialogoEliminarCuenta = false
                    cuentaViewModel.mensajeErrorMostrado()
                }
            },

            containerColor = colorTarjeta,

            title = {
                Text(
                    text = "Eliminar cuenta",
                    color = colorTexto
                )
            },

            text = {
                Column {

                    Text(
                        text = "Esta acción eliminará tu cuenta y tu historial de forma permanente. " +
                            "Ingresa tu contraseña para confirmar.",
                        color = colorTexto
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = contrasenaConfirmacion,
                        onValueChange = { contrasenaConfirmacion = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation =
                            if (mostrarContrasena) VisualTransformation.None
                            else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                                Icon(
                                    imageVector = if (mostrarContrasena) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    cuentaViewModel.mensajeError?.let { mensaje ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = mensaje,
                            color = RojoPeligro,
                            fontSize = 13.sp
                        )
                    }

                    if (cuentaViewModel.cargando) {
                        Spacer(modifier = Modifier.height(10.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = RojoPeligro
                        )
                    }
                }
            },

            confirmButton = {
                TextButton(
                    enabled = !cuentaViewModel.cargando,
                    onClick = {
                        cuentaViewModel.eliminarCuenta(
                            usuario = usuario,
                            contrasena = contrasenaConfirmacion,
                            onEliminada = {
                                mostrarDialogoEliminarCuenta = false
                                onCuentaEliminada()
                            }
                        )
                    }
                ) {
                    Text(text = "Eliminar cuenta", color = RojoPeligro)
                }
            },

            dismissButton = {
                TextButton(
                    enabled = !cuentaViewModel.cargando,
                    onClick = {
                        mostrarDialogoEliminarCuenta = false
                        cuentaViewModel.mensajeErrorMostrado()
                    }
                ) {
                    Text(text = "Cancelar", color = colorTexto)
                }
            }
        )
    }
}


// G OPCION
@Composable
private fun OpcionTamano(
    texto: String,
    escala: Float,
    seleccion: Float,
    colorTexto: Color,
    colorAcento: Color,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 6.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        RadioButton(
            selected = seleccion == escala,

            onClick = {
                onClick()
            },

            colors = RadioButtonDefaults.colors(
                selectedColor = colorAcento,
                unselectedColor =
                    colorTexto.copy(alpha = 0.60f)
            )
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = texto,
            color = colorTexto,
            fontSize = 16.sp
        )
    }
}

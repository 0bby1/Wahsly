package com.example.wahsly.ui.pantallas

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.wahsly.ui.theme.AzulPrincipalClaro
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.theme.RosaOscuro
import java.io.File
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.wahsly.ui.theme.TarjetaPerfilOscuro
import com.example.wahsly.ui.theme.TextoBlancoClaro
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.camera.core.FocusMeteringAction
import androidx.compose.foundation.gestures.detectTapGestures
import java.util.concurrent.TimeUnit
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import kotlinx.coroutines.delay
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import com.example.wahsly.utilidades.TipoPantalla
import com.example.wahsly.utilidades.obtenerTipoPantalla
import com.example.wahsly.utilidades.vibrar
import androidx.compose.foundation.clickable

data class DatosEscaneo(
    val fotoUri: Uri,
    val cantidad: Int,
    val tieneMancha: Boolean,
    val tipoMancha: String,
    val informacionAdicional: String
)

@Composable
fun PantallaEscaner(
    modoOscuro: Boolean,
    windowSizeClass: WindowSizeClass,
    onDatosConfirmados: (DatosEscaneo) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val tipoPantalla = obtenerTipoPantalla(windowSizeClass)
    val esTabletVertical =
        tipoPantalla == TipoPantalla.TABLET_VERTICAL
    val esTabletHorizontal =
        tipoPantalla == TipoPantalla.TABLET_HORIZONTAL
    val esTablet =
        esTabletVertical || esTabletHorizontal
    val esCelularHorizontal =
        tipoPantalla == TipoPantalla.TELEFONO &&
                windowSizeClass.heightSizeClass ==
                WindowHeightSizeClass.Compact
    val esHorizontal =
        esTabletHorizontal || esCelularHorizontal

    // DATOS DEL FORMULARIO
    var mostrarDialogoDatos by remember { mutableStateOf(false) }
    var fotoPendiente by remember { mutableStateOf<Uri?>(null) }
    var cantidadTexto by remember { mutableStateOf("1") }
    var tieneMancha by remember { mutableStateOf(false) }
    var tipoMancha by remember { mutableStateOf("") }
    var informacionAdicional by remember { mutableStateOf("") }

    fun abrirFormulario(uri: Uri) {
        fotoPendiente = uri
        cantidadTexto = "1"
        tieneMancha = false
        tipoMancha = ""
        informacionAdicional = ""
        mostrarDialogoDatos = true
    }

    // COLORES
    val colorFondo = if (modoOscuro) { FondoOscuro } else { FondoClaro }
    val colorIconos = if (modoOscuro) { RosaOscuro } else { AzulPrincipalClaro }

    // MEDIDAS RESPONSIVAS DEL ESCÁNER
    val paddingSuperiorIconos = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 22.dp
        TipoPantalla.TABLET_VERTICAL -> 24.dp
        TipoPantalla.TABLET_HORIZONTAL -> 18.dp
    }

    val paddingHorizontalIconos = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 62.dp
        TipoPantalla.TABLET_VERTICAL -> 120.dp
        TipoPantalla.TABLET_HORIZONTAL -> 180.dp
    }

    val tamanoIconosSuperiores = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 34.dp
        TipoPantalla.TABLET_VERTICAL -> 38.dp
        TipoPantalla.TABLET_HORIZONTAL -> 28.dp
    }

    val fraccionMarco = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 0.58f
        TipoPantalla.TABLET_VERTICAL -> 0.52f
        TipoPantalla.TABLET_HORIZONTAL -> 0.48f
    }

    val desplazamientoMarcoY = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> (-15).dp
        TipoPantalla.TABLET_VERTICAL -> (-10).dp
        TipoPantalla.TABLET_HORIZONTAL -> (-8).dp
    }

    val tamanoBotonFoto = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 74.dp
        TipoPantalla.TABLET_VERTICAL -> 78.dp
        TipoPantalla.TABLET_HORIZONTAL -> 70.dp
    }

    val tamanoIconosZoom = when (tipoPantalla) {
        TipoPantalla.TELEFONO -> 30.dp
        TipoPantalla.TABLET_VERTICAL -> 32.dp
        TipoPantalla.TABLET_HORIZONTAL -> 24.dp
    }

    // PERMISO DE CAMARA
    var tienePermisoCamara by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcherPermisoCamara =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) { permisoAceptado ->
            tienePermisoCamara =
                permisoAceptado
        }

    LaunchedEffect(Unit) {
        if (!tienePermisoCamara) {
            launcherPermisoCamara.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    // CAMERAX
    val previewView = remember {
        PreviewView(context).apply {
            scaleType =
                PreviewView.ScaleType.FILL_CENTER
            implementationMode =
                PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var imagenCongelada by remember { mutableStateOf<ImageBitmap?>(null) }
    var puntoEnfoque by remember { mutableStateOf<Offset?>(null) }
    var idEnfoque by remember { mutableIntStateOf(0) }
    val escalaEnfoque = remember { Animatable(1f) }
    val alphaEnfoque = remember { Animatable(0f) }
    var camara by remember { mutableStateOf<Camera?>(null) }
    var linternaEncendida by remember { mutableStateOf(false) }
    var zoomActual by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(idEnfoque) {
        if (idEnfoque == 0) {
            return@LaunchedEffect
        }
        // Empieza un poco más grande
        escalaEnfoque.snapTo(1.35f)

        // Aparece
        alphaEnfoque.snapTo(1f)

        // Se contrae como una cámara real
        escalaEnfoque.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 180
            )
        )
        // Se queda visible un momento
        delay(450)

        // Desaparece suavemente
        alphaEnfoque.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 250
            )
        )
        puntoEnfoque = null
    }

    DisposableEffect(
        tienePermisoCamara,
        lifecycleOwner
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        if (tienePermisoCamara) {
            cameraProviderFuture.addListener(
                {
                    try {
                        val cameraProvider =
                            cameraProviderFuture.get()
                        // VISTA PREVIA
                        val preview =
                            Preview.Builder()
                                .build()
                                .also {
                                    it.surfaceProvider =
                                        previewView.surfaceProvider
                                }

                        // CAPTURA DE FOTO
                        val captura =
                            ImageCapture.Builder()
                                .setCaptureMode(
                                    ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
                                )
                                .build()
                        imageCapture = captura

                        // CAMARA TRASERA
                        val selector =
                            CameraSelector.DEFAULT_BACK_CAMERA
                        cameraProvider.unbindAll()
                        camara =
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                selector,
                                preview,
                                captura
                            )

                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "No se pudo iniciar la cámara",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                ContextCompat.getMainExecutor(
                    context
                )
            )
        }

        onDispose {
            if (cameraProviderFuture.isDone) {
                try {
                    cameraProviderFuture
                        .get()
                        .unbindAll()
                } catch (_: Exception) {
                }
            }
        }
    }

    // FUNCION TOMAR FOTO
    fun tomarFoto() {
        vibrar(context)
        val captura = imageCapture
        if (captura == null) {
            Toast.makeText(
                context,
                "La cámara todavía está cargando",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        previewView.bitmap?.let { bitmap ->
            imagenCongelada =
                bitmap.copy(
                    bitmap.config ?: android.graphics.Bitmap.Config.ARGB_8888,
                    false
                ).asImageBitmap()
        }

        val archivo = File(
            context.cacheDir,
            "washly_${System.currentTimeMillis()}.jpg"
        )

        val opciones =
            ImageCapture.OutputFileOptions
                .Builder(archivo)
                .build()

        captura.takePicture(
            opciones,
            ContextCompat.getMainExecutor(
                context
            ),
            object :
                ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(
                    outputFileResults:
                    ImageCapture.OutputFileResults
                ) {
                    val uri = Uri.fromFile(archivo)
                    abrirFormulario(uri)
                }
                override fun onError(
                    exception:
                    ImageCaptureException
                ) {
                    imagenCongelada = null
                    Toast.makeText(
                        context,
                        "No se pudo tomar la foto",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    // GALERIA
    val launcherGaleria =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                abrirFormulario(uri)
            }
        }

    // INTERFAZ
    val modificadorGestosCamara =
        if (
            tienePermisoCamara &&
            imagenCongelada == null
        ) {
            Modifier
                // ZOOM CON GESTO
                .pointerInput(camara, imagenCongelada) {
                    detectTransformGestures { _, _, zoomCambio, _ ->
                        val camera = camara ?: return@detectTransformGestures
                        val zoomState =
                            camera.cameraInfo.zoomState.value ?: return@detectTransformGestures
                        val nuevoZoomRatio =
                            (
                                    zoomState.zoomRatio * zoomCambio
                                    ).coerceIn(
                                    zoomState.minZoomRatio,
                                    zoomState.maxZoomRatio
                                )
                        camera.cameraControl.setZoomRatio(nuevoZoomRatio)
                        val rango =
                            zoomState.maxZoomRatio - zoomState.minZoomRatio

                        if (rango > 0f) {
                            zoomActual = ((nuevoZoomRatio - zoomState.minZoomRatio) / rango).coerceIn(0f, 1f)
                        }
                    }
                }

                // TOCAR PARA ENFOCAR
                .pointerInput(camara, imagenCongelada) {
                    detectTapGestures(
                        onTap = { posicion ->
                            val camera = camara ?: return@detectTapGestures
                            puntoEnfoque = posicion
                            idEnfoque++
                            val punto =
                                previewView.meteringPointFactory
                                    .createPoint(
                                        posicion.x,
                                        posicion.y
                                    )
                            val accionEnfoque =
                                FocusMeteringAction.Builder(
                                    punto,
                                    FocusMeteringAction.FLAG_AF or
                                            FocusMeteringAction.FLAG_AE
                                )
                                    .setAutoCancelDuration(3, TimeUnit.SECONDS)
                                    .build()

                            camera.cameraControl.startFocusAndMetering(accionEnfoque)
                        }
                    )
                }
        } else {
            Modifier
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (tienePermisoCamara) {
                    Color.Black
                } else {
                    colorFondo
                }
            )
            .then(modificadorGestosCamara)
    ) {

        // FONDO DE CÁMARA
        if (tienePermisoCamara) {
            if (imagenCongelada != null) {
                Image(
                    bitmap = imagenCongelada!!,
                    contentDescription = "Foto de la etiqueta",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // COLOR DE CONTROLES
        val colorControles =
            if (tienePermisoCamara) {
                Color.White
            } else {
                colorIconos
            }

        // INDICADOR ANIMADO DE ENFOQUE
        puntoEnfoque?.let { punto ->
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val lado = 62.dp.toPx() * escalaEnfoque.value
                val mitad = lado / 2f
                val longitudEsquina = 17.dp.toPx() * escalaEnfoque.value
                val grosor = 2.5.dp.toPx()
                val color = colorControles.copy(alpha = alphaEnfoque.value)
                val izquierda = punto.x - mitad
                val derecha = punto.x + mitad
                val arriba = punto.y - mitad
                val abajo = punto.y + mitad

                // ARRIBA IZQUIERDA
                drawLine(
                    color = color,
                    start = Offset(izquierda, arriba),
                    end = Offset(izquierda + longitudEsquina, arriba),
                    strokeWidth = grosor
                )

                drawLine(
                    color = color,
                    start = Offset(izquierda, arriba),
                    end = Offset(izquierda, arriba + longitudEsquina),
                    strokeWidth = grosor
                )

                // ARRIBA DERECHA
                drawLine(
                    color = color,
                    start = Offset(derecha, arriba),
                    end = Offset(derecha - longitudEsquina, arriba),
                    strokeWidth = grosor
                )

                drawLine(
                    color = color,
                    start = Offset(derecha, arriba),
                    end = Offset(derecha, arriba + longitudEsquina),
                    strokeWidth = grosor
                )

                // ABAJO IZQUIERDA
                drawLine(
                    color = color,
                    start = Offset(izquierda, abajo),
                    end = Offset(izquierda + longitudEsquina, abajo),
                    strokeWidth = grosor
                )

                drawLine(
                    color = color,
                    start = Offset(izquierda, abajo),
                    end = Offset(izquierda, abajo - longitudEsquina),
                    strokeWidth = grosor
                )

                // ABAJO DERECHA
                drawLine(
                    color = color,
                    start = Offset(derecha, abajo),
                    end = Offset(derecha - longitudEsquina, abajo),
                    strokeWidth = grosor
                )

                drawLine(
                    color = color,
                    start = Offset(derecha, abajo),
                    end = Offset(derecha, abajo - longitudEsquina),
                    strokeWidth = grosor
                )

                // PUNTO CENTRAL
                drawCircle(
                    color = color,
                    radius = 3.dp.toPx(),
                    center = punto
                )
            }
        }

        // ICONOS SUPERIORES
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(
                    top = paddingSuperiorIconos,
                    start = paddingHorizontalIconos,
                    end = paddingHorizontalIconos
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // FLASH
            IconButton(
                onClick = {
                    val camera = camara
                    if (camera?.cameraInfo?.hasFlashUnit() == true) {
                        linternaEncendida = !linternaEncendida
                        camera.cameraControl.enableTorch(
                            linternaEncendida
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Este dispositivo no tiene flash",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Icon(
                    imageVector =
                        if (linternaEncendida) {
                            Icons.Default.FlashOn
                        } else {
                            Icons.Default.FlashOff
                        },
                    contentDescription = "Flash",
                    tint = colorControles,
                    modifier = Modifier.size(tamanoIconosSuperiores)
                )
            }

            // GALERÍA
            IconButton(
                onClick = {
                    launcherGaleria.launch("image/*")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = "Galería",
                    tint = colorControles,
                    modifier = Modifier.size(tamanoIconosSuperiores)
                )
            }

            // AYUDA
            IconButton(
                onClick = {
                    Toast.makeText(
                        context,
                        "Coloca toda la etiqueta de lavado dentro del marco y asegúrate de que los símbolos y el texto se vean claramente.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "Ayuda",
                    tint = colorControles,
                    modifier = Modifier.size(tamanoIconosSuperiores)
                )
            }
        }

        // MARCO DE ESCANEO
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {

            val anchoMarco =
                maxWidth * fraccionMarco

            val altoMarco =
                anchoMarco * 0.75f

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = desplazamientoMarcoY)
                    .width(anchoMarco)
                    .height(altoMarco)
                    .then(
                        if (!tienePermisoCamara) {
                            Modifier.clickable {
                                launcherPermisoCamara.launch(
                                    Manifest.permission.CAMERA
                                )
                            }
                        } else {
                            Modifier
                        }
                    )
            ) {
                MarcoEscaneo(
                    color = colorControles,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // BOTÓN DE FOTO
        if (tipoPantalla != TipoPantalla.TABLET_HORIZONTAL) {
            IconButton(
                onClick = {
                    if (tienePermisoCamara) {
                        tomarFoto()
                    } else {
                        launcherPermisoCamara.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(
                        bottom = when (tipoPantalla) {
                            TipoPantalla.TELEFONO -> 92.dp
                            TipoPantalla.TABLET_VERTICAL -> 105.dp
                            TipoPantalla.TABLET_HORIZONTAL -> 80.dp
                        }
                    )
                    .size(tamanoBotonFoto)
                    .background(
                        color = FondoClaro,
                        shape = CircleShape
                    )
                    .border(
                        width = 3.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
            ) {
                // botón vacío a propósito
            }
        }

        // ZOOM SOLO EN HORIZONTAL
        if (tipoPantalla == TipoPantalla.TABLET_HORIZONTAL) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 74.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = {
                        val nuevoZoom =
                            (zoomActual - 0.10f)
                                .coerceIn(0f, 1f)
                        zoomActual = nuevoZoom
                        camara
                            ?.cameraControl
                            ?.setLinearZoom(nuevoZoom)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ZoomOut,
                        contentDescription = "Alejar",
                        tint = colorControles,
                        modifier = Modifier.size(tamanoIconosZoom)
                    )
                }

                IconButton(
                    onClick = {
                        val nuevoZoom =
                            (zoomActual + 0.10f)
                                .coerceIn(0f, 1f)
                        zoomActual = nuevoZoom
                        camara
                            ?.cameraControl
                            ?.setLinearZoom(nuevoZoom)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ZoomIn,
                        contentDescription = "Acercar",
                        tint = colorControles,
                        modifier = Modifier.size(tamanoIconosZoom)
                    )
                }
            }
        }
    }

        // POPUP DATOS DE LAS PRENDAS
        if (
            mostrarDialogoDatos &&
            fotoPendiente != null
        ) {
            val colorDialogo = if (modoOscuro) { TarjetaPerfilOscuro } else { FondoClaro }
            val colorTextoDialogo = if (modoOscuro) { RosaOscuro } else { AzulPrincipalClaro }
            val colorBoton = if (modoOscuro) { RosaOscuro } else { AzulPrincipalClaro }
            val colorTextoBoton = if (modoOscuro) { FondoOscuro } else { TextoBlancoClaro }

            AlertDialog(
                onDismissRequest = {
                    mostrarDialogoDatos = false
                    fotoPendiente = null
                    imagenCongelada = null
                },
                containerColor = colorDialogo,

                // TITULO
                title = {
                    Text(
                        text = "Información de las prendas",
                        color = colorTextoDialogo,
                        fontWeight = FontWeight.Bold
                    )
                },

                // CONTENIDO
                text = {
                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text =
                                "Indica cuántas prendas tienen estas mismas instrucciones de lavado.",
                            color = colorTextoDialogo
                        )

                        // CANTIDAD
                        OutlinedTextField(
                            value = cantidadTexto,
                            onValueChange = { nuevoValor ->
                                if (
                                    nuevoValor.all {
                                            caracter ->
                                        caracter.isDigit()
                                    }
                                ) {
                                    cantidadTexto =
                                        nuevoValor
                                }
                            },
                            label = {
                                Text("Prendas con esta misma etiqueta")
                            },
                            placeholder = { Text("Ej. 3") },
                            singleLine = true,
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Number
                                ),
                            modifier =
                                Modifier.fillMaxWidth(),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = colorTextoDialogo,
                                    unfocusedTextColor = colorTextoDialogo,
                                    focusedBorderColor = colorTextoDialogo,
                                    unfocusedBorderColor = colorTextoDialogo.copy(alpha = 0.5f),
                                    focusedLabelColor = colorTextoDialogo,
                                    unfocusedLabelColor = colorTextoDialogo.copy(alpha = 0.7f),
                                    cursorColor = colorTextoDialogo
                                )
                        )

                        // MANCHAS
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "¿Tiene alguna mancha?",
                                color = colorTextoDialogo,
                                fontWeight = FontWeight.SemiBold
                            )
                            Switch(
                                checked = tieneMancha,
                                onCheckedChange = {
                                    tieneMancha = it
                                    if (!it) {
                                        tipoMancha = ""
                                    }
                                }
                            )
                        }

                        // TIPO DE MANCHA
                        if (tieneMancha) {
                            OutlinedTextField(
                                value = tipoMancha,
                                onValueChange = {
                                    tipoMancha = it
                                },
                                label = {
                                    Text("Tipo de mancha")
                                },
                                placeholder = {
                                    Text("Ej. café, grasa, tinta...")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors =
                                    OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = colorTextoDialogo,
                                        unfocusedTextColor = colorTextoDialogo,
                                        focusedBorderColor = colorTextoDialogo,
                                        unfocusedBorderColor = colorTextoDialogo.copy(alpha = 0.5f),
                                        focusedLabelColor = colorTextoDialogo,
                                        unfocusedLabelColor = colorTextoDialogo.copy(alpha = 0.7f),
                                        cursorColor = colorTextoDialogo
                                    )
                            )
                        }

                        // INFORMACIÓN EXTRA
                        OutlinedTextField(
                            value = informacionAdicional,
                            onValueChange = {
                                informacionAdicional = it
                            },
                            label = {
                                Text("Información adicional")
                            },
                            placeholder = {
                                Text("Ej. No quiero usar secadora")
                            },
                            minLines = 2,
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = colorTextoDialogo,
                                    unfocusedTextColor = colorTextoDialogo,
                                    focusedBorderColor = colorTextoDialogo,
                                    unfocusedBorderColor = colorTextoDialogo.copy(alpha = 0.5f),
                                    focusedLabelColor = colorTextoDialogo,
                                    unfocusedLabelColor = colorTextoDialogo.copy(alpha = 0.7f),
                                    cursorColor = colorTextoDialogo
                                )
                        )
                    }
                },

                // CONTINUAR
                confirmButton = {
                    Button(
                        onClick = {
                            val cantidad = cantidadTexto.toIntOrNull()

                            if (cantidad == null || cantidad <= 0) {
                                Toast.makeText(
                                    context,
                                    "Ingresa una cantidad válida",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            if (tieneMancha && tipoMancha.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Indica qué tipo de mancha tiene",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }


                            val datos = DatosEscaneo(
                                fotoUri = fotoPendiente!!,
                                cantidad = cantidad,
                                tieneMancha = tieneMancha,
                                tipoMancha = tipoMancha.trim(),
                                informacionAdicional = informacionAdicional.trim()
                            )
                            mostrarDialogoDatos = false
                            fotoPendiente = null
                            onDatosConfirmados(datos)
                        },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = colorBoton,
                                contentColor = colorTextoBoton
                            )
                    ) {
                        Text(
                            text = "Continuar"
                        )
                    }
                },

                // CANCELAR
                dismissButton = {
                    TextButton(
                        onClick = {
                            mostrarDialogoDatos = false
                            fotoPendiente = null
                            imagenCongelada = null
                        }
                    ) {

                        Text(
                            text = "Cancelar",
                            color = colorTextoDialogo
                        )
                    }
                }
            )
        }
    }


// ESQUINAS DEL ESCANER
@Composable
private fun MarcoEscaneo(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .padding(3.dp)
    ) {
        val longitud = 24.dp.toPx()
        val grosor = 4.dp.toPx()

        // ARRIBA IZQUIERDA
        drawLine(
            color = color,
            start = Offset(
                0f,
                0f
            ),
            end = Offset(
                longitud,
                0f
            ),
            strokeWidth = grosor,
            cap = StrokeCap.Square
        )

        drawLine(
            color = color,
            start = Offset(
                0f,
                0f
            ),
            end = Offset(
                0f,
                longitud
            ),
            strokeWidth = grosor,
            cap = StrokeCap.Square
        )

        // ARRIBA DERECHA
        drawLine(
            color = color,
            start = Offset(
                size.width,
                0f
            ),
            end = Offset(
                size.width - longitud,
                0f
            ),
            strokeWidth = grosor,
            cap = StrokeCap.Square
        )

        drawLine(
            color = color,
            start = Offset(
                size.width,
                0f
            ),
            end = Offset(
                size.width,
                longitud
            ),
            strokeWidth = grosor,
            cap = StrokeCap.Square
        )

        // ABAJO IZQUIERDA
        drawLine(
            color = color,
            start = Offset(
                0f,
                size.height
            ),
            end = Offset(
                longitud,
                size.height
            ),
            strokeWidth = grosor,
            cap = StrokeCap.Square
        )

        drawLine(
            color = color,
            start = Offset(
                0f,
                size.height
            ),
            end = Offset(
                0f,
                size.height - longitud
            ),
            strokeWidth = grosor,
            cap = StrokeCap.Square
        )

        // ABAJO DERECHA
        drawLine(
            color = color,
            start = Offset(
                size.width,
                size.height
            ),
            end = Offset(
                size.width - longitud,
                size.height
            ),
            strokeWidth = grosor,
            cap = StrokeCap.Square
        )

        drawLine(
            color = color,
            start = Offset(
                size.width,
                size.height
            ),
            end = Offset(
                size.width,
                size.height - longitud
            ),
            strokeWidth = grosor,
            cap = StrokeCap.Square
        )
    }
}
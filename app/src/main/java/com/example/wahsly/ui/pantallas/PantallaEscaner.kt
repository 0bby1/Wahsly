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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.wahsly.ui.theme.AzulPrincipalClaro
import com.example.wahsly.ui.theme.CremaOscuro
import com.example.wahsly.ui.theme.FondoClaro
import com.example.wahsly.ui.theme.FondoOscuro
import com.example.wahsly.ui.theme.RosaClaro
import com.example.wahsly.ui.theme.RosaOscuro
import java.io.File
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.CameraAlt
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
    onDatosConfirmados: (DatosEscaneo) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // RESPONSIVE PARA TABLETAS Y ORIENTACION
    val configuration = LocalConfiguration.current
    val anchoPantalla = configuration.screenWidthDp
    val altoPantalla = configuration.screenHeightDp
    val esTablet = anchoPantalla >= 600
    val esHorizontal = anchoPantalla > altoPantalla
    val esCelularHorizontal = esHorizontal && !esTablet

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
    val colorCabecera = if (modoOscuro) { RosaOscuro } else { AzulPrincipalClaro }
    val colorBuscador = if (modoOscuro) { FondoOscuro } else { FondoClaro }
    val colorIconos = if (modoOscuro) { RosaOscuro } else { AzulPrincipalClaro }
    val colorBusqueda = if (modoOscuro) { CremaOscuro } else { RosaClaro }

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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // CABECERA RESPONSIVA
            val alturaCabecera = when {
                esCelularHorizontal -> 70.dp
                esHorizontal -> 100.dp
                else -> 125.dp
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(alturaCabecera)
                    .clip(RoundedCornerShape(
                        bottomStart = 14.dp,
                        bottomEnd = 14.dp
                    )
                    )
                    .background(colorCabecera)
                    .statusBarsPadding()
            ) {

                // BUSCADOR
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(
                            start = if (esHorizontal) 32.dp else 16.dp,
                            end = if (esHorizontal) 32.dp else 16.dp,
                            bottom = if (esHorizontal) 10.dp else 20.dp
                        )
                        .fillMaxWidth(if (esHorizontal) 0.6f else 1f)
                        .height(42.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(colorBuscador)
                ) {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = colorBusqueda,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 12.dp)
                            .size(23.dp)
                    )
                }
            }

            // CUERPO DEL ESCANER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // BOTONES SUPERIORES
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(
                            top = if (esHorizontal) 8.dp else if (esTablet) 24.dp else 38.dp,
                            start = if (esTablet) 48.dp else 16.dp,
                            end = if (esTablet) 48.dp else 16.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // FLASH
                    IconButton(
                        onClick = {
                            val camera = camara
                            if ( camera
                                    ?.cameraInfo
                                    ?.hasFlashUnit() == true
                            ) {
                                linternaEncendida =
                                    !linternaEncendida
                                camera
                                    .cameraControl
                                    .enableTorch(
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
                            tint = colorIconos,
                            modifier =
                                Modifier.size(30.dp)
                        )
                    }

                    // GALERIA
                    IconButton(
                        onClick = {
                            launcherGaleria.launch(
                                "image/*"
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Galería",
                            tint = colorIconos,
                            modifier =
                                Modifier.size(30.dp)
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
                            tint = colorIconos,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                // CAMARA
                if (tienePermisoCamara) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // INSTRUCCIONES
                        Text(
                            text = "Fotografía la etiqueta de lavado",
                            color = colorIconos,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Asegúrate de que los símbolos y el texto sean visibles",
                            color = colorIconos.copy(alpha = 0.75f),
                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // CAMARA RESPONSIVA
                        Box(
                            modifier = Modifier
                                .then(
                                    when {
                                        // Tablet Horizontal
                                        esTablet && esHorizontal -> {
                                            Modifier
                                                .fillMaxWidth(0.45f)
                                                .aspectRatio(4f / 3f)
                                        }

                                        // Tablet Vertical
                                        esTablet -> {
                                            Modifier
                                                .fillMaxWidth(0.60f)
                                                .aspectRatio(3f / 4f)
                                        }

                                        // Celular Horizontal (Evita que se salga)
                                        esCelularHorizontal -> {
                                            Modifier
                                                .fillMaxHeight(0.65f)
                                                .aspectRatio(4f / 3f)
                                        }

                                        // Celular Vertical
                                        else -> {
                                            Modifier
                                                .fillMaxWidth(0.70f)
                                                .aspectRatio(3f / 4f)
                                        }
                                    }
                                )
                                .pointerInput(camara, imagenCongelada) {
                                    if (imagenCongelada == null) {
                                        detectTransformGestures { _, _, zoomCambio, _ ->
                                            val camera = camara ?: return@detectTransformGestures

                                            val zoomState =
                                                camera.cameraInfo
                                                    .zoomState
                                                    .value
                                                    ?: return@detectTransformGestures

                                            val nuevoZoomRatio =
                                                (
                                                        zoomState.zoomRatio *
                                                                zoomCambio
                                                        ).coerceIn(
                                                        zoomState.minZoomRatio,
                                                        zoomState.maxZoomRatio
                                                    )
                                            camera
                                                .cameraControl
                                                .setZoomRatio(
                                                    nuevoZoomRatio
                                                )

                                            val rango = zoomState.maxZoomRatio - zoomState.minZoomRatio

                                            if (rango > 0f) {
                                                zoomActual = ((nuevoZoomRatio - zoomState.minZoomRatio) / rango).coerceIn(0f, 1f)
                                            }
                                        }
                                    }
                                }
                                .pointerInput(camara, imagenCongelada) {
                                    if (imagenCongelada == null) {
                                        detectTapGestures(
                                            onTap = { posicion ->
                                                val camera = camara ?: return@detectTapGestures

                                                // MOSTRAR CUADRO DE ENFOQUE
                                                puntoEnfoque = posicion
                                                idEnfoque++

                                                // ENFOCAR CAMARA
                                                val punto = previewView
                                                    .meteringPointFactory
                                                    .createPoint(
                                                        posicion.x,
                                                        posicion.y
                                                    )

                                                val accionEnfoque =
                                                    FocusMeteringAction
                                                        .Builder(
                                                            punto,
                                                            FocusMeteringAction.FLAG_AF or
                                                                    FocusMeteringAction.FLAG_AE
                                                        )
                                                        .setAutoCancelDuration(3, TimeUnit.SECONDS)
                                                        .build()
                                                camera
                                                    .cameraControl
                                                    .startFocusAndMetering(
                                                        accionEnfoque
                                                    )
                                            }
                                        )
                                    }
                                }
                        ) {
                            if (imagenCongelada != null) {
                                // FOTO CONGELADA
                                Image(
                                    bitmap = imagenCongelada!!,
                                    contentDescription = "Foto de la etiqueta",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(4.dp))
                                )
                            } else {
                                AndroidView(
                                    factory = {
                                        previewView
                                    },
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(4.dp))
                                )
                            }

                            // =========================
                            // INDICADOR DE ENFOQUE
                            // =========================
                            puntoEnfoque?.let { punto ->

                                Canvas(
                                    modifier = Modifier.fillMaxSize()
                                ) {

                                    val lado =
                                        62.dp.toPx() *
                                                escalaEnfoque.value

                                    val mitad =
                                        lado / 2f

                                    val longitudEsquina =
                                        17.dp.toPx() *
                                                escalaEnfoque.value

                                    val grosor =
                                        2.5.dp.toPx()

                                    val color =
                                        colorIconos.copy(
                                            alpha = alphaEnfoque.value
                                        )


                                    val izquierda =
                                        punto.x - mitad

                                    val derecha =
                                        punto.x + mitad

                                    val arriba =
                                        punto.y - mitad

                                    val abajo =
                                        punto.y + mitad


                                    // ARRIBA IZQUIERDA
                                    drawLine(
                                        color = color,
                                        start = Offset(
                                            izquierda,
                                            arriba
                                        ),
                                        end = Offset(
                                            izquierda + longitudEsquina,
                                            arriba
                                        ),
                                        strokeWidth = grosor
                                    )

                                    drawLine(
                                        color = color,
                                        start = Offset(
                                            izquierda,
                                            arriba
                                        ),
                                        end = Offset(
                                            izquierda,
                                            arriba + longitudEsquina
                                        ),
                                        strokeWidth = grosor
                                    )


                                    // ARRIBA DERECHA
                                    drawLine(
                                        color = color,
                                        start = Offset(
                                            derecha,
                                            arriba
                                        ),
                                        end = Offset(
                                            derecha - longitudEsquina,
                                            arriba
                                        ),
                                        strokeWidth = grosor
                                    )

                                    drawLine(
                                        color = color,
                                        start = Offset(
                                            derecha,
                                            arriba
                                        ),
                                        end = Offset(
                                            derecha,
                                            arriba + longitudEsquina
                                        ),
                                        strokeWidth = grosor
                                    )


                                    // ABAJO IZQUIERDA
                                    drawLine(
                                        color = color,
                                        start = Offset(
                                            izquierda,
                                            abajo
                                        ),
                                        end = Offset(
                                            izquierda + longitudEsquina,
                                            abajo
                                        ),
                                        strokeWidth = grosor
                                    )

                                    drawLine(
                                        color = color,
                                        start = Offset(
                                            izquierda,
                                            abajo
                                        ),
                                        end = Offset(
                                            izquierda,
                                            abajo - longitudEsquina
                                        ),
                                        strokeWidth = grosor
                                    )


                                    // ABAJO DERECHA
                                    drawLine(
                                        color = color,
                                        start = Offset(
                                            derecha,
                                            abajo
                                        ),
                                        end = Offset(
                                            derecha - longitudEsquina,
                                            abajo
                                        ),
                                        strokeWidth = grosor
                                    )

                                    drawLine(
                                        color = color,
                                        start = Offset(
                                            derecha,
                                            abajo
                                        ),
                                        end = Offset(
                                            derecha,
                                            abajo - longitudEsquina
                                        ),
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

                            // MARCO DE ESCANEO
                            MarcoEscaneo(
                                color = colorIconos,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                } else {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Washly necesita permiso para utilizar la cámara.",
                            color = colorIconos
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                launcherPermisoCamara
                                    .launch(
                                        Manifest.permission.CAMERA
                                    )
                            }
                        ) {
                            Text(
                                text = "Permitir cámara"
                            )
                        }
                    }
                }

                // BOTON TOMAR FOTO
                IconButton(
                    onClick = {
                        if (tienePermisoCamara) {
                            tomarFoto()
                        } else {
                            Toast.makeText(
                                context,
                                "Necesitas permitir el acceso a la cámara",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(
                            bottom = if (esHorizontal) 60.dp else 80.dp
                        )
                        .size(if (esTablet) 76.dp else 68.dp)
                        .background(
                            color = colorFondo,
                            shape = CircleShape
                        )
                        .border(
                            width = 4.dp,
                            color = colorIconos,
                            shape = CircleShape
                        )
                ) {

                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Tomar foto",
                        tint = colorIconos,
                        modifier = Modifier.size(34.dp)
                    )
                }

                // ZOOM
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = if (esHorizontal) 8.dp else 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // ALEJAR
                    IconButton(
                        onClick = {
                            val nuevoZoom =
                                (zoomActual - 0.10f)
                                    .coerceIn(
                                        0f,
                                        1f
                                    )
                            zoomActual =
                                nuevoZoom
                            camara
                                ?.cameraControl
                                ?.setLinearZoom(
                                    nuevoZoom
                                )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ZoomOut,
                            contentDescription = "Alejar",
                            tint = colorIconos,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // ACERCAR
                    IconButton(
                        onClick = {
                            val nuevoZoom =
                                (zoomActual + 0.10f)
                                    .coerceIn(
                                        0f,
                                        1f
                                    )
                            zoomActual =
                                nuevoZoom
                            camara
                                ?.cameraControl
                                ?.setLinearZoom(
                                    nuevoZoom
                                )
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.ZoomIn,
                            contentDescription = "Acercar",
                            tint = colorIconos,
                            modifier = Modifier.size(32.dp)
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

                        // INFORMACION EXTRA
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
                            color =
                                colorTextoDialogo
                        )
                    }
                }
            )
        }
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

        val longitud =
            24.dp.toPx()
        val grosor =
            4.dp.toPx()

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
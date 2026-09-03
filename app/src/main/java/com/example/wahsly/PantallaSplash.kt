package com.example.wahsly

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PantallaSplash(
    modoOscuro: Boolean,
    onTerminar: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (modoOscuro) FondoOscuro else FondoClaro),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { contexto ->
                VideoView(contexto).apply {
                    val videoUri = Uri.parse(
                        "android.resource://${context.packageName}/${R.raw.washly}"
                    )
                    setVideoURI(videoUri)
                    setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.isLooping = false
                        start()
                    }
                    setOnCompletionListener {
                        onTerminar()
                    }
                    setOnErrorListener { _, _, _ ->
                        onTerminar()
                        true
                    }
                }
            }
        )
    }
}
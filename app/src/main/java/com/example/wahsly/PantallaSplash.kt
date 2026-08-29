package com.example.wahsly

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun PantallaSplash(
    onTerminar: () -> Unit
) {

    LaunchedEffect(Unit) {
        delay(1500)
        onTerminar()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4EFEB)),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.washlylogo_inicio
            ),
            contentDescription = "Logo de Washly",
            modifier = Modifier.size(280.dp),
            contentScale = ContentScale.Fit
        )
    }
}

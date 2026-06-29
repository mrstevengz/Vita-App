package com.example.vita_app.ui.components

// Proposito: Componente reutilizable que aplica el fondo visual comun de la aplicacion.

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vita_app.ui.theme.BackgroundLight
import com.example.vita_app.ui.theme.MutedOlive
import com.example.vita_app.ui.theme.PineBlue

@Composable
// Envuelve pantallas con un fondo consistente y recibe contenido como slot.
fun AppBackground(content: @Composable () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(top = 10.dp)
    ) {

        // Mancha 1
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-120).dp, y = (-100).dp)
                .background(
                    color = PineBlue.copy(alpha = 0.08f),
                    shape = CircleShape
                )
        )

        // Mancha 2
        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = 300.dp, y = 400.dp)
                .background(
                    color = PineBlue.copy(alpha = 0.08f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = (-100).dp, y = 800.dp)
                .background(
                    color = PineBlue.copy(alpha = 0.08f),
                    shape = CircleShape
                )
        )

        // Contenido segun la pagina
        content()
    }
}
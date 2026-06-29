package com.example.vita_app.ui.theme

// Proposito: Configuracion de MaterialTheme para colores, tipografia y modo claro/oscuro.

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = PineBlue,                    // buttons, cursors, selected chips, indicators
    onPrimary = White,                     // text/icons on top of primary
    secondary = CharcoalBrown,
    onSecondary = White,
    secondaryContainer = MutedOlive,       // selected FilterChip background (light teal)
    onSecondaryContainer = PineBlue,       // selected FilterChip text
    background = BackgroundLight,          // your cream page background
    onBackground = CarbonBlack,
    surface = White,                       // cards/boxes — pure white, visible on the cream
    onSurface = CarbonBlack,               // text inside boxes
    surfaceVariant = BackgroundAccent,
    onSurfaceVariant = CharcoalBrown
)

@Composable
// Aplica el tema Material 3 de la app a todo el arbol de composables.
fun VitaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
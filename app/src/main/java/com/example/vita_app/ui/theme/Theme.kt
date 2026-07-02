package com.example.vita_app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = Color(0xFF0F7C7C),            // teal del logo: botones, chips activos, indicadores
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFA0F0E6),
    onPrimaryContainer = Color(0xFF00201F),
    secondary = Color(0xFF4A6360),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCCE8E3), // fondo de chip seleccionado
    onSecondaryContainer = Color(0xFF051F1D),
    tertiary = Color(0xFF43617A),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFF4FAF8),          // página: blanco con un toque verde
    onBackground = Color(0xFF171D1C),
    surface = Color(0xFFFFFFFF),            // tarjetas: blancas, resaltan sobre el fondo
    onSurface = Color(0xFF171D1C),
    surfaceVariant = Color(0xFFDAE5E1),
    onSurfaceVariant = Color(0xFF3F4946),   // texto secundario / iconos
    outline = Color(0xFF6F7976),            // bordes hairline
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF84D3C4),            // teal más claro para contraste sobre oscuro
    onPrimary = Color(0xFF00382F),
    primaryContainer = Color(0xFF005048),
    onPrimaryContainer = Color(0xFFA0F0E6),
    secondary = Color(0xFFB1CCC6),
    onSecondary = Color(0xFF1C3531),
    secondaryContainer = Color(0xFF334B47),
    onSecondaryContainer = Color(0xFFCCE8E3),
    tertiary = Color(0xFFAAC9E5),
    onTertiary = Color(0xFF0C3349),
    background = Color(0xFF0E1513),
    onBackground = Color(0xFFDDE4E0),
    surface = Color(0xFF0E1513),            // las tarjetas se elevan con el overlay tonal de M3
    onSurface = Color(0xFFDDE4E0),
    surfaceVariant = Color(0xFF3F4946),
    onSurfaceVariant = Color(0xFFBEC9C5),
    outline = Color(0xFF889390),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun VitaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
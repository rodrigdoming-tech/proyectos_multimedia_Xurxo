package com.example.diarideviatges.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF00639B),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFCCE5FF),
    onPrimaryContainer = Color(0xFF001D33),
    secondary = Color(0xFF516069),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF006A60)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF97CBFF),
    onPrimary = Color(0xFF003352),
    primaryContainer = Color(0xFF004B77),
    onPrimaryContainer = Color(0xFFCCE5FF),
    secondary = Color(0xFFB8C8D2),
    tertiary = Color(0xFF4DDACB)
)

@Composable
fun DiariDeViatgesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}

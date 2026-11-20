package ru.bokach.plugins.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF3253DC),
    onPrimary = Color(0xFFFFFFFF),
    background = Color(0xFFF4F5FA),
    surface = Color(0xFFFCFCFF),
    surfaceVariant = Color(0xFFE2E4EF),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB3C2FF),
    onPrimary = Color(0xFF001B9B),
    background = Color(0xFF121317),
    surface = Color(0xFF1B1C20),
    surfaceVariant = Color(0xFF44464F),
)

@Composable
fun AppTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}

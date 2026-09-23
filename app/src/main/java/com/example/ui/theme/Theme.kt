package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DomesColorScheme = lightColorScheme(
    primary = Color(0xFF121619),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF3F4F6),
    onPrimaryContainer = Color(0xFF121619),
    secondary = Color(0xFF4B5563),
    onSecondary = Color.White,
    background = Color.White,
    onBackground = Color(0xFF121619),
    surface = Color.White,
    onSurface = Color(0xFF121619),
    surfaceVariant = Color(0xFFF9FAFB),
    onSurfaceVariant = Color(0xFF6B7280),
    outline = Color(0xFFE5E7EB)
)

@Composable
fun DomesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DomesColorScheme,
        typography = Typography,
        content = content
    )
}

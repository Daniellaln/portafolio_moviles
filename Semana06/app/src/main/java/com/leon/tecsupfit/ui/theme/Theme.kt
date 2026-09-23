package com.leon.tecsupfit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColorScheme = lightColorScheme(
    primary = VerdeTecsup,
    secondary = VerdeBadge,
    background = FondoGris,
    surface = TarjetaGris
)

@Composable
fun TecsupFitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}

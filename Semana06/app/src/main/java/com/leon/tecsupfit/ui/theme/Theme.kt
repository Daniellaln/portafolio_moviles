package com.leon.tecsupfit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme =
    lightColorScheme(
        primary = AzulProfundo,
        onPrimary = Color.White,
        primaryContainer = LuzAqua,
        onPrimaryContainer = AzulProfundo,
        secondary = ReflejoLila,
        onSecondary = Color.White,
        background = BasePorcelana,
        onBackground = TintaMarina,
        surface = BasePorcelana,
        onSurface = TintaMarina,
        outline = GlassBorder,
    )

@Composable
fun TecsupFitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
    )
}

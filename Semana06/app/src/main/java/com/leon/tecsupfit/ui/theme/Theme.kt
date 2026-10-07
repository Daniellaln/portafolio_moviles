package com.leon.tecsupfit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
<<<<<<< HEAD
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
=======

private val ColorScheme = lightColorScheme(
    primary = VerdeTecsup,
    secondary = VerdeBadge,
    background = FondoGris,
    surface = TarjetaGris
)
>>>>>>> sinia

@Composable
fun TecsupFitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
<<<<<<< HEAD
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
=======
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
>>>>>>> sinia
    )
}

package com.leon.lab04carritotecsup_leon.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Sin colores dinámicos: así el morado se ve igual en cualquier celular
private val EsquemaClaro = lightColorScheme(
    primary = MoradoTecsup,
    onPrimary = Color.White,
    secondary = MoradoTecsup,
    background = Color.White,
    surface = Color.White,
    onBackground = TextoOscuro,
    onSurface = TextoOscuro,
    onSurfaceVariant = TextoGris,
    outline = BordeSuave,
    error = RojoEliminar
)

@Composable
fun Lab04CarritoTecsupLeonTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography, // viene de Type.kt (no lo borres)
        content = content
    )
}

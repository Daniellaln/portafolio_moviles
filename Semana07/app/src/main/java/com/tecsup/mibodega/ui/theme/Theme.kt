package com.tecsup.mibodega.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
private val colores = lightColorScheme(primary = VerdeBodega, onPrimary = Blanco,
 secondary = VerdeOscuro, secondaryContainer = VerdeSuave, onSecondaryContainer = VerdeOscuro, background = FondoClaro, onBackground = AzulTexto,
 surface = Blanco, onSurface = AzulTexto, surfaceVariant = GrisClaro,
 onSurfaceVariant = GrisTexto, outline = GrisBorde, error = RojoPrecio,
 primaryContainer = VerdeSuave, onPrimaryContainer = VerdeOscuro)
@Composable fun BodegaTheme(content: @Composable () -> Unit) {
 MaterialTheme(colorScheme = colores, typography = BodegaTypography, content = content)
}
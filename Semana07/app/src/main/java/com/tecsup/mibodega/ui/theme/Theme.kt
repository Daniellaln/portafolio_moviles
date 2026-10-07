package com.tecsup.mibodega.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
private val claro = lightColorScheme(primary = VerdeBodega, onPrimary = Blanco,
 secondary = VerdeOscuro, secondaryContainer = VerdeSuave, onSecondaryContainer = VerdeOscuro, background = FondoClaro, onBackground = AzulTexto,
 surface = Color(0xFFF5F5ED), onSurface = AzulTexto, surfaceVariant = GrisClaro,
 onSurfaceVariant = GrisTexto, outline = GrisBorde, error = Color(0xFFB3261E),
 primaryContainer = VerdeSuave, onPrimaryContainer = VerdeOscuro)
private val oscuro = darkColorScheme(primary = Color(0xFFA7D8B7), onPrimary = Color(0xFF123A29),
 secondary = Color(0xFFA7D8B7), secondaryContainer = Color(0xFF31493A), onSecondaryContainer = Color(0xFFD9E9D9),
 background = Color(0xFF171E1B), onBackground = Color(0xFFE4EBE5), surface = Color(0xFF242D28), onSurface = Color(0xFFE4EBE5),
 surfaceVariant = Color(0xFF303D34), onSurfaceVariant = Color(0xFFBAC9BF), outline = Color(0xFF75887B),
 primaryContainer = Color(0xFF314D3D), onPrimaryContainer = Color(0xFFD8F0DF), error = Color(0xFFFFB4AB))
@Composable fun BodegaTheme(oscuroActivo: Boolean = false, content: @Composable () -> Unit) {
 val objetivo = if(oscuroActivo) oscuro else claro
 val colores = objetivo.copy(
  background = animateColorAsState(objetivo.background, tween(180), label = "Fondo").value,
  surface = animateColorAsState(objetivo.surface, tween(180), label = "Tarjetas").value,
  onSurface = animateColorAsState(objetivo.onSurface, tween(180), label = "Texto").value,
  onBackground = animateColorAsState(objetivo.onBackground, tween(180), label = "Texto fondo").value,
  primary = animateColorAsState(objetivo.primary, tween(180), label = "Verde").value,
  onPrimary = animateColorAsState(objetivo.onPrimary, tween(180), label = "Texto botón").value,
  primaryContainer = animateColorAsState(objetivo.primaryContainer, tween(180), label = "Selección").value,
  surfaceVariant = animateColorAsState(objetivo.surfaceVariant, tween(180), label = "Imagen").value,
  onSurfaceVariant = animateColorAsState(objetivo.onSurfaceVariant, tween(180), label = "Texto secundario").value
 )
 MaterialTheme(colorScheme = colores, typography = BodegaTypography, content = content)
}

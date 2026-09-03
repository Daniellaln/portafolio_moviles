package pe.edu.tecsup.registronotas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val EsquemaClaro = lightColorScheme(
    primary = MoradoPrimario,
    onPrimary = Color.White,
    primaryContainer = MoradoSuave,
    onPrimaryContainer = MoradoOscuro,
    secondary = MoradoPrimario,
    onSecondary = Color.White,
    background = FondoDegradadoBottom,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    surfaceVariant = MoradoSuave,
    onSurfaceVariant = TextoSecundario,
    outline = MoradoDeshabilitado
)

/**
 * Tema de la app. Se fuerza el esquema claro para que la pantalla se vea
 * igual que las figuras del enunciado, tenga o no el telefono el modo oscuro
 * activado.
 */
@Composable
fun RegistroNotasTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as android.app.Activity).window
            window.statusBarColor = MoradoPrimario.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Tipografia,
        content = content
    )
}

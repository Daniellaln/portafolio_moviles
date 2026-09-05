package pe.edu.tecsup.registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import pe.edu.tecsup.registronotas.ui.screens.RegistroNotasScreen
import pe.edu.tecsup.registronotas.ui.theme.RegistroNotasTheme

/**
 * Punto de entrada de la app. Solo monta el tema y la pantalla principal:
 * toda la logica de estado vive dentro de RegistroNotasScreen.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistroNotasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    RegistroNotasScreen()
                }
            }
        }
    }
}

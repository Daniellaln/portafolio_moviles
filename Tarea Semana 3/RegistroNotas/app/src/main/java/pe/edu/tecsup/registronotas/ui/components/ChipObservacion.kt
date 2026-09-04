package pe.edu.tecsup.registronotas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.tecsup.registronotas.logic.Observacion
import pe.edu.tecsup.registronotas.ui.theme.AmbarFondo
import pe.edu.tecsup.registronotas.ui.theme.AmbarTexto
import pe.edu.tecsup.registronotas.ui.theme.RojoFondo
import pe.edu.tecsup.registronotas.ui.theme.RojoTexto
import pe.edu.tecsup.registronotas.ui.theme.VerdeFondo
import pe.edu.tecsup.registronotas.ui.theme.VerdeOscuroFondo
import pe.edu.tecsup.registronotas.ui.theme.VerdeOscuroTexto
import pe.edu.tecsup.registronotas.ui.theme.VerdeTexto

/**
 * Par de colores (fondo del chip, color del texto) para cada observacion.
 * El when devuelve el par completo, asi el color se decide en un solo lugar.
 */
private fun coloresDe(observacion: Observacion): Pair<Color, Color> = when (observacion) {
    Observacion.EXCELENTE -> VerdeOscuroFondo to VerdeOscuroTexto
    Observacion.APROBADO -> VerdeFondo to VerdeTexto
    Observacion.EN_RECUPERACION -> AmbarFondo to AmbarTexto
    Observacion.DESAPROBADO -> RojoFondo to RojoTexto
}

/**
 * Chip de color que muestra la observacion: verde oscuro, verde, ambar o rojo
 * segun el rango del promedio final.
 */
@Composable
fun ChipObservacion(
    observacion: Observacion,
    modifier: Modifier = Modifier
) {
    val (fondo, texto) = coloresDe(observacion)

    Text(
        text = observacion.etiqueta,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = texto,
        modifier = modifier
            .background(fondo, RoundedCornerShape(percent = 50))
            .padding(horizontal = 18.dp, vertical = 10.dp)
    )
}

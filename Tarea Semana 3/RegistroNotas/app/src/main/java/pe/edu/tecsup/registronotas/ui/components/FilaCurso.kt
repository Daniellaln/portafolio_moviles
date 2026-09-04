package pe.edu.tecsup.registronotas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.tecsup.registronotas.model.Curso
import pe.edu.tecsup.registronotas.model.NOTA_MAXIMA
import pe.edu.tecsup.registronotas.model.NOTA_MINIMA
import pe.edu.tecsup.registronotas.ui.theme.MoradoPrimario
import pe.edu.tecsup.registronotas.ui.theme.MoradoSuave
import pe.edu.tecsup.registronotas.ui.theme.TextoPrincipal
import pe.edu.tecsup.registronotas.ui.theme.TextoSecundario

/**
 * Una fila de curso: nombre + peso, badge con la nota en vivo y el Slider.
 *
 * El Slider sigue el mismo patron estado + duo que el OutlinedTextField:
 * recibe el valor actual (value) y avisa cada cambio (onValueChange). Como el
 * estado vive en la pantalla, cada arrastre dispara una recomposicion y el
 * badge se actualiza al instante.
 */
@Composable
fun FilaCurso(
    curso: Curso,
    nota: Float,
    onNotaChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nombre del curso + peso entre parentesis
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = curso.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "(${curso.pesoTexto})",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextoSecundario,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            // Badge con la nota elegida: se refresca en vivo al mover el Slider
            Text(
                text = nota.toInt().toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MoradoPrimario,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .widthIn(min = 46.dp)
                    .background(MoradoSuave, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // Slider de 0 a 20 con valores enteros:
        // 21 posiciones posibles = 19 pasos intermedios + los dos extremos.
        Slider(
            value = nota,
            onValueChange = onNotaChange,
            valueRange = NOTA_MINIMA.toFloat()..NOTA_MAXIMA.toFloat(),
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = MoradoPrimario,
                activeTrackColor = MoradoPrimario,
                inactiveTrackColor = MoradoSuave,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

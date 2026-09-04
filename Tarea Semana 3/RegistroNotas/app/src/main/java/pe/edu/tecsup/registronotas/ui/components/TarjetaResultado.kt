package pe.edu.tecsup.registronotas.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.tecsup.registronotas.logic.ResultadoNotas
import pe.edu.tecsup.registronotas.ui.theme.MoradoPrimario
import pe.edu.tecsup.registronotas.ui.theme.MoradoSuave
import pe.edu.tecsup.registronotas.ui.theme.TextoPrincipal
import pe.edu.tecsup.registronotas.ui.theme.TextoSecundario

/**
 * Tarjeta blanca con el resultado del calculo:
 * promedio ponderado (2 decimales), promedio final (redondeado si el Switch
 * estaba activo) y el chip de la observacion.
 */
@Composable
fun TarjetaResultado(
    resultado: ResultadoNotas,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, MoradoSuave),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Promedio ponderado, siempre con 2 decimales
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Promedio ponderado:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextoPrincipal
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = resultado.ponderadoTexto,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextoPrincipal
                )
            }

            Spacer(Modifier.height(12.dp))

            // Promedio final, destacado en morado
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Promedio final:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MoradoPrimario
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = resultado.finalTexto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MoradoPrimario
                )
            }

            // La aclaracion solo aparece cuando el Switch estaba activo
            if (resultado.fueRedondeado) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "(redondeado)",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextoSecundario
                )
            }

            Spacer(Modifier.height(16.dp))

            ChipObservacion(observacion = resultado.observacion)
        }
    }
}

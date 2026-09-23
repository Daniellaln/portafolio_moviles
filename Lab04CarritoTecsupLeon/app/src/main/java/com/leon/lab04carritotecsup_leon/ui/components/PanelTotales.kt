package com.leon.lab04carritotecsup_leon.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.lab04carritotecsup_leon.ui.theme.FondoPanel
import com.leon.lab04carritotecsup_leon.util.formatoSoles

// Panel fijo abajo: Productos, Subtotal, IGV, (Descuento) y TOTAL
@Composable
fun PanelTotales(
    cantidadProductos: Int,
    subtotal: Double,
    igv: Double,
    porcentajeDescuento: Double,
    descuento: Double,
    total: Double
) {
    Surface(
        color = FondoPanel,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Productos: $cantidadProductos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (cantidadProductos > 0) {
                FilaTotal("Subtotal", formatoSoles(subtotal))
                FilaTotal("IGV (18%)", formatoSoles(igv))
                // Reto opcional: solo aparece cuando corresponde
                if (descuento > 0) {
                    FilaTotal(
                        "Descuento (${(porcentajeDescuento * 100).toInt()}%)",
                        "- " + formatoSoles(descuento)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TOTAL",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatoSoles(total),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// SpaceBetween: etiqueta a la izquierda, monto a la derecha
@Composable
private fun FilaTotal(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(etiqueta, style = MaterialTheme.typography.bodyMedium)
        Text(valor, style = MaterialTheme.typography.bodyMedium)
    }
}

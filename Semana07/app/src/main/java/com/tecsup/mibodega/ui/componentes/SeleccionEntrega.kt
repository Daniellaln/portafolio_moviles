package com.tecsup.mibodega.ui.componentes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.DIRECCION_TIENDA
@Composable fun SeleccionEntrega(recojo: Boolean, onRecojo: (Boolean) -> Unit) {
 Tarjeta {
  Text("¿Cómo lo recibes?", style = MaterialTheme.typography.titleMedium)
  Column(Modifier.selectableGroup()) {
   listOf(false to "Delivery · S/ 4.00", true to "Recojo en tienda · Gratis").forEach { (opcion, texto) ->
    Row(Modifier.fillMaxWidth().selectable(selected = recojo == opcion, onClick = { onRecojo(opcion) }, role = Role.RadioButton).heightIn(min = 56.dp), verticalAlignment = Alignment.CenterVertically) {
     RadioButton(recojo == opcion, onClick = null); Text(texto, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
    }
   }
  }
  Text(if(recojo) DIRECCION_TIENDA else "Lo llevamos a la dirección de tu pedido.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
 }
}

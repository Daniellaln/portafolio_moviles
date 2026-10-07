package com.tecsup.mibodega.ui.cliente.screens.confirmacion
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.*
import com.tecsup.mibodega.ui.cliente.modelo.*
@Composable fun ConfirmacionScreen(pedido: Pedido?, onInicio: () -> Unit) {
 var verEstado by rememberSaveable { mutableStateOf(false) }
 Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
  Column(Modifier.safeDrawingPadding().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
   Text("PASO 3 DE 3", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 24.dp))
   Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) { Icon(Icons.Default.Check, null, Modifier.padding(28.dp).size(48.dp), tint = MaterialTheme.colorScheme.primary) }
   Text("¡Pedido realizado!", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
   Text("Nos encargamos del resto. Tu pedido ya está en preparación.", textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
   if(pedido != null) Tarjeta {
    Text("Pedido #${pedido.numero}", style = MaterialTheme.typography.titleLarge)
    Text("Total: ${dinero(pedido.total)}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = .4f))
    Text(if(pedido.recojo) "Recojo en tienda" else "Dirección de entrega", style = MaterialTheme.typography.labelLarge)
    Text(if(pedido.recojo) DIRECCION_TIENDA else pedido.cliente.direccion); if(!pedido.recojo && pedido.cliente.referencia.isNotBlank()) Text(pedido.cliente.referencia, style = MaterialTheme.typography.bodySmall)
    Text("Pago: ${pedido.metodoPago}", style = MaterialTheme.typography.bodySmall)
   }
   if(verEstado) Tarjeta { Text("En preparación", style = MaterialTheme.typography.titleMedium); Text("Tu bodega está preparando los productos. El estado es de demostración y no se actualiza por Internet.", style = MaterialTheme.typography.bodyMedium) }
   BotonSecundario(if(verEstado) "Ocultar estado" else "Ver estado del pedido", { verEstado = !verEstado })
   BotonPrimario("Volver al inicio", onInicio)
  }
 }
}
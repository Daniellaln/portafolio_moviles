package com.tecsup.mibodega.ui.cliente.screens.entrega
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.*
import com.tecsup.mibodega.ui.cliente.modelo.*
@Composable fun DatosEntregaScreen(cliente: DatosCliente, total: Double, onVolver: () -> Unit, onConfirmar: (DatosCliente, String) -> Unit) {
 var nombre by rememberSaveable { mutableStateOf(cliente.nombre) }; var telefono by rememberSaveable { mutableStateOf(cliente.telefono) }
 var direccion by rememberSaveable { mutableStateOf(cliente.direccion) }; var referencia by rememberSaveable { mutableStateOf(cliente.referencia) }
 var pago by rememberSaveable { mutableStateOf("Efectivo al entregar") }
 Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
  Column(Modifier.safeDrawingPadding().imePadding().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
   Encabezado("Datos de entrega", "Paso 2 de 3 · Ya casi está", onVolver)
   Text("¿Dónde lo llevamos?", style = MaterialTheme.typography.headlineMedium)
   CampoTexto("Nombre", nombre, { nombre = it })
   CampoTexto("Teléfono", telefono, { telefono = it.filter(Char::isDigit).take(9) }, teclado = KeyboardType.Phone,
    error = if(telefono.isNotEmpty() && telefono.length != 9) "Escribe los 9 números de tu teléfono" else null)
   CampoTexto("Dirección", direccion, { direccion = it })
   CampoTexto("Referencia (opcional)", referencia, { referencia = it })
   Text("¿Cómo prefieres pagar?", style = MaterialTheme.typography.titleLarge)
   Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
    listOf("Efectivo al entregar", "Yape", "Plin").forEach { metodo ->
     Surface(modifier = Modifier.clay(base = if(pago == metodo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background, hundido = pago == metodo, radio = 18f), shape = RoundedCornerShape(18.dp), color = if(pago == metodo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface) {
      Row(Modifier.fillMaxWidth().selectable(pago == metodo, onClick = { pago = metodo }, role = Role.RadioButton).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
       RadioButton(pago == metodo, onClick = null); Spacer(Modifier.width(12.dp)); Text(metodo)
      }
     }
    }
   }
   Text("Demostración: no se realizará ningún cobro.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
   Tarjeta { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Total con delivery", style = MaterialTheme.typography.titleMedium); Text(dinero(total), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary) } }
   BotonPrimario("Confirmar pedido", { onConfirmar(DatosCliente(nombre.trim(), telefono, direccion.trim(), referencia.trim()), pago) }, habilitado = nombre.isNotBlank() && telefono.length == 9 && direccion.isNotBlank() && total > 0)
  }
 }
}
package com.tecsup.mibodega.ui.cliente.screens.entrega
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.*
import com.tecsup.mibodega.ui.componentes.*
@Composable
fun DatosEntregaScreen(cliente: DatosCliente, total: Double, onVolver: () -> Unit, onConfirmar: (DatosCliente, String) -> Unit) {
    var nombre by rememberSaveable { mutableStateOf(cliente.nombre) }
    var telefono by rememberSaveable { mutableStateOf(cliente.telefono) }
    var direccion by rememberSaveable { mutableStateOf(cliente.direccion) }
    var referencia by rememberSaveable { mutableStateOf(cliente.referencia) }
    var pago by rememberSaveable { mutableStateOf("Efectivo al entregar") }
    Column(Modifier.fillMaxSize().safeDrawingPadding().imePadding().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextButton(onClick = onVolver) { Text("Volver al carrito") }
        Text("Datos de entrega", style = MaterialTheme.typography.titleLarge)
        CampoTexto("Nombre", nombre, { nombre = it })
        CampoTexto("Teléfono", telefono, { telefono = it.filter(Char::isDigit).take(9) }, teclado = androidx.compose.ui.text.input.KeyboardType.Phone)
        CampoTexto("Dirección", direccion, { direccion = it })
        CampoTexto("Referencia (opcional)", referencia, { referencia = it })
        Text("Método de pago", style = MaterialTheme.typography.titleMedium)
        listOf("Efectivo al entregar", "Yape", "Plin").forEach { metodo ->
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(selected = pago == metodo, onClick = { pago = metodo })
                TextButton(onClick = { pago = metodo }) { Text(metodo) }
            }
        }
        Text("Total: S/ %.2f".format(java.util.Locale.US, total), style = MaterialTheme.typography.titleMedium)
        Text("Pago simulado para esta demostración.", style = MaterialTheme.typography.bodySmall)
        BotonPrimario("Confirmar pedido", onClick = {
            onConfirmar(DatosCliente(nombre.trim(), telefono, direccion.trim(), referencia.trim()), pago)
        }, habilitado = nombre.isNotBlank() && telefono.length == 9 && direccion.isNotBlank() && total > 0)
    }
}
package com.tecsup.mibodega.ui.cliente.screens.confirmacion
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
fun ConfirmacionScreen(pedido: Pedido?, onInicio: () -> Unit) {
    Column(Modifier.fillMaxSize().safeDrawingPadding().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
        Text("✓", style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.primary)
        Text("¡Pedido realizado!", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        Text("Tu pedido está siendo preparado y será entregado pronto.")
        if (pedido != null) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Pedido #${pedido.numero}", style = MaterialTheme.typography.titleMedium)
                    Text("Total: S/ %.2f".format(java.util.Locale.US, pedido.total))
                    Text("Dirección: ${pedido.cliente.direccion}")
                    if (pedido.cliente.referencia.isNotBlank()) Text(pedido.cliente.referencia)
                    Text("Pago: ${pedido.metodoPago}")
                }
            }
        }
        BotonSecundario("Volver al inicio", onInicio)
    }
}
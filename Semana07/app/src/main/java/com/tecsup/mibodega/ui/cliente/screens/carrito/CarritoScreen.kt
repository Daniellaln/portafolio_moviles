package com.tecsup.mibodega.ui.cliente.screens.carrito
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.*
import com.tecsup.mibodega.ui.cliente.modelo.*
@Composable fun CarritoScreen(carrito: List<ItemCarrito>, onVolver: () -> Unit, onIncrementar: (Producto) -> Unit,
 onDecrementar: (Producto) -> Unit, onEliminar: (Producto) -> Unit, onContinuarPedido: () -> Unit) {
 Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
  Column(Modifier.safeDrawingPadding()) {
   Box(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) { Encabezado("Mi carrito", "Paso 1 de 3 · Revisa tu compra", onVolver) }
   LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    if(carrito.isEmpty()) item { EstadoVacio(Icons.Default.ShoppingBag, "Tu carrito está vacío", "Elige tus productos y vuelve por aquí."); BotonSecundario("Seguir comprando", onVolver) }
    items(carrito, key = { it.producto.id }) { item -> Tarjeta {
     Row(verticalAlignment = Alignment.CenterVertically) {
      ImagenProducto(item.producto, Modifier.size(66.dp))
      Column(Modifier.weight(1f).padding(start = 12.dp)) { Text(item.producto.nombre, style = MaterialTheme.typography.titleMedium); Text("${unidad(item.producto)} · ${dinero(item.producto.precio)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
      IconButton({ onEliminar(item.producto) }) { Icon(Icons.Default.DeleteOutline, "Eliminar ${item.producto.nombre}", tint = MaterialTheme.colorScheme.onSurfaceVariant) }
     }
     Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
      SelectorCantidad(item.cantidad, { onIncrementar(item.producto) }, { onDecrementar(item.producto) }, minimo = 0)
      Text(dinero(item.producto.precio * item.cantidad), style = MaterialTheme.typography.titleMedium)
     }
    } }
   }
   Surface(shadowElevation = 6.dp) {
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
     Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant); Text(dinero(subtotalCarrito(carrito))) }
     Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Delivery", color = MaterialTheme.colorScheme.onSurfaceVariant); Text(dinero(deliveryCarrito(carrito))) }
     Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Total", style = MaterialTheme.typography.titleLarge); Text(dinero(totalCarrito(carrito)), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary) }
     BotonPrimario("Continuar pedido", onContinuarPedido, habilitado = carrito.isNotEmpty())
    }
   }
  }
 }
}
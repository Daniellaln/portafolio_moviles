package com.tecsup.mibodega.ui.cliente.screens.detalle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.*
import com.tecsup.mibodega.ui.cliente.modelo.Producto
@Composable fun DetalleProductoScreen(producto: Producto, onVolver: () -> Unit, onAgregarAlCarrito: (Producto, Int) -> Unit) {
 var cantidad by rememberSaveable(producto.id) { mutableIntStateOf(1) }
 Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
  Column(Modifier.safeDrawingPadding()) {
   Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    Encabezado("Detalle del producto", "Elige la cantidad que necesitas", onVolver)
    Surface(Modifier.fillMaxWidth().clay(MaterialTheme.colorScheme.primaryContainer, radio = 32f), shape = RoundedCornerShape(32.dp), color = MaterialTheme.colorScheme.primaryContainer) {
     ImagenProducto(producto, Modifier.fillMaxWidth().height(240.dp).padding(24.dp))
    }
    Text(producto.categoria.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
    Text(producto.nombre, style = MaterialTheme.typography.headlineMedium)
    Text(unidad(producto), color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(dinero(producto.precio), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
    Text(producto.descripcion, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
     Text("Cantidad", style = MaterialTheme.typography.titleMedium)
     SelectorCantidad(cantidad, { cantidad++ }, { if(cantidad > 1) cantidad-- })
    }
   }
   Surface(shadowElevation = 6.dp) {
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
     Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Total del producto"); Text(dinero(producto.precio * cantidad), style = MaterialTheme.typography.titleMedium) }
     BotonPrimario("Agregar al carrito", { onAgregarAlCarrito(producto, cantidad) })
    }
   }
  }
 }
}
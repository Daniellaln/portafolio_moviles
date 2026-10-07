package com.tecsup.mibodega.ui.componentes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
@Composable fun ProductoCard(producto: Producto, onClick: () -> Unit, onAgregar: () -> Unit, modifier: Modifier = Modifier, favorito: Boolean = false, onFavorito: () -> Unit = {}) {
 Card(onClick, modifier.fillMaxWidth().clay(), shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.Transparent)) {
  Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
   Surface(shape = RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surfaceVariant) { ImagenProducto(producto, Modifier.size(94.dp).padding(6.dp)) }
   Column(Modifier.weight(1f).padding(horizontal = 14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
    Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
    Text(unidad(producto), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
    Text(dinero(producto.precio), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
   }
   Column(horizontalAlignment = Alignment.CenterHorizontally) {
    IconButton(onFavorito) { Icon(if(favorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder, if(favorito) "Quitar favorito ${producto.nombre}" else "Guardar favorito ${producto.nombre}", tint = if(favorito) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) }
   FilledIconButton(onAgregar, Modifier.size(48.dp).clay(MaterialTheme.colorScheme.primary, radio = 16f), colors = IconButtonDefaults.filledIconButtonColors(containerColor = androidx.compose.ui.graphics.Color.Transparent, contentColor = MaterialTheme.colorScheme.onPrimary), shape = RoundedCornerShape(16.dp)) { Icon(Icons.Default.Add, "Agregar ${producto.nombre}") }
   }
  }
 }
}
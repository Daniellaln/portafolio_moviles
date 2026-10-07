package com.tecsup.mibodega.ui.componentes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun SelectorCantidad(cantidad: Int, onIncrementar: () -> Unit, onDecrementar: () -> Unit, modifier: Modifier = Modifier, minimo: Int = 1) {
 Surface(modifier.clay(hundido = true, radio = 18f), shape = RoundedCornerShape(16.dp), color = androidx.compose.ui.graphics.Color.Transparent) {
  Row(verticalAlignment = Alignment.CenterVertically) {
   IconButton(onDecrementar, enabled = cantidad > minimo, modifier = Modifier.size(48.dp)) { Icon(Icons.Default.Remove, "Disminuir cantidad") }
   Text("$cantidad", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 6.dp))
   IconButton(onIncrementar, modifier = Modifier.size(48.dp)) { Icon(Icons.Default.Add, "Aumentar cantidad", tint = MaterialTheme.colorScheme.primary) }
  }
 }
}
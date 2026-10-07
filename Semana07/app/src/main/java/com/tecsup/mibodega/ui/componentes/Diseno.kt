package com.tecsup.mibodega.ui.componentes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.util.Locale
fun dinero(valor: Double) = "S/ %.2f".format(Locale.US, valor)
@Composable fun Encabezado(titulo: String, subtitulo: String, onVolver: () -> Unit) {
 Row(Modifier.fillMaxWidth().padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically) {
  IconButton(onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
  Column { Text(titulo, style = MaterialTheme.typography.titleLarge); Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
 }
}
@Composable fun Tarjeta(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
 Surface(modifier.fillMaxWidth().clay(), shape = RoundedCornerShape(26.dp), color = androidx.compose.ui.graphics.Color.Transparent) {
  Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
 }
}
@Composable fun EstadoVacio(icono: ImageVector, titulo: String, detalle: String) {
 Column(Modifier.fillMaxWidth().padding(vertical = 32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
  Icon(icono, null, Modifier.size(44.dp), tint = MaterialTheme.colorScheme.primary)
  Text(titulo, style = MaterialTheme.typography.titleMedium)
  Text(detalle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
 }
}
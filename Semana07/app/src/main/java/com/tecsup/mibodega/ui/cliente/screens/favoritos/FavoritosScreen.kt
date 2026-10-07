package com.tecsup.mibodega.ui.cliente.screens.favoritos
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.*
import com.tecsup.mibodega.ui.componentes.*
@Composable fun FavoritosScreen(ids: List<Int>, onVolver: () -> Unit, onProducto: (Producto) -> Unit, onAgregar: (Producto) -> Unit, onFavorito: (Producto) -> Unit) {
 Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
  LazyColumn(Modifier.safeDrawingPadding(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
   item { Encabezado("Mis favoritos", "Los productos que quieres tener cerca", onVolver) }
   val favoritos = listaProductosFake.filter { it.id in ids }
   if(favoritos.isEmpty()) item { EstadoVacio(Icons.Default.FavoriteBorder, "Aún no tienes favoritos", "Toca el corazón de un producto para guardarlo."); BotonSecundario("Explorar productos", onVolver) }
   items(favoritos, key = { it.id }) { p -> ProductoCard(p, { onProducto(p) }, { onAgregar(p) }, favorito = true, onFavorito = { onFavorito(p) }) }
  }
 }
}

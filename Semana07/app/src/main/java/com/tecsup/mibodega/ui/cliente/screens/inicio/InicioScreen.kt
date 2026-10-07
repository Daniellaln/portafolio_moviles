package com.tecsup.mibodega.ui.cliente.screens.inicio
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.tecsup.mibodega.R
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.*
import com.tecsup.mibodega.ui.componentes.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(productos: List<Producto> = listaProductosFake, cantidadCarrito: Int, onVerCarrito: () -> Unit,
 onProductoClick: (Producto) -> Unit, onAgregarProducto: (Producto) -> Unit,
 cliente: DatosCliente = DatosCliente(), pedidos: List<Pedido> = emptyList(), favoritos: List<Int> = emptyList(), onFavorito: (Producto) -> Unit = {}, onVerFavoritos: () -> Unit = {}, oscuro: Boolean = false, onCambiarTema: (Boolean) -> Unit = {}) {
 var destino by rememberSaveable { mutableIntStateOf(0) }
 var categoria by rememberSaveable { mutableStateOf("Todos") }
 var busqueda by rememberSaveable { mutableStateOf("") }
 var orden by rememberSaveable { mutableStateOf("Recomendados") }
 var menuOrden by remember { mutableStateOf(false) }
 val estadoLista = rememberLazyListState()
 LaunchedEffect(orden, categoria, busqueda) { estadoLista.scrollToItem(0) }
 val lista = ordenarProductos(filtrarProductos(productos, categoria, busqueda), orden)
 val snackbar = remember { SnackbarHostState() }
 val scope = rememberCoroutineScope()
 val teclado = LocalSoftwareKeyboardController.current
 Scaffold(snackbarHost = { SnackbarHost(snackbar) }, topBar = {
  TopAppBar(title = { Column { Text("Mi Bodega", style = MaterialTheme.typography.titleLarge); Text("Cerca de ti, siempre", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) } },
   colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
   actions = { IconButton(onVerFavoritos) { Icon(Icons.Default.FavoriteBorder, "Mis favoritos") }; IconButton(onVerCarrito) { BadgedBox(badge = { if(cantidadCarrito > 0) Badge { Text("$cantidadCarrito") } }) { Icon(Icons.Default.ShoppingBag, "Carrito") } } })
 }, bottomBar = {
  Box(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp)) {
   Surface(Modifier.fillMaxWidth().cristal(), shape = RoundedCornerShape(32.dp), color = androidx.compose.ui.graphics.Color.Transparent) {
    NavigationBar(containerColor = androidx.compose.ui.graphics.Color.Transparent, tonalElevation = 0.dp, windowInsets = WindowInsets(0, 0, 0, 0)) {
   listOf("Inicio" to Icons.Default.Home, "Categorías" to Icons.Default.GridView, "Pedidos" to Icons.Default.ReceiptLong, "Perfil" to Icons.Default.PersonOutline).forEachIndexed { index, item ->
    NavigationBarItem(destino == index, { teclado?.hide(); destino = index }, icon = { Icon(item.second, item.first) }, label = { Text(item.first) })
   }
  }
  }
  }
 }) { padding ->
  AnimatedContent(targetState = destino, modifier = Modifier.fillMaxSize(), transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(120)) }, label = "Sección principal") { seccion ->
  if(seccion == 2) {
   LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
    item { Text("Mis pedidos", style = MaterialTheme.typography.headlineMedium); Text("Tus compras, en un solo lugar.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
    if(pedidos.isEmpty()) item { EstadoVacio(Icons.Default.ReceiptLong, "Aún no tienes pedidos.", "Tu primera compra aparecerá aquí.") }
    items(pedidos.reversed(), key = { it.numero }) { pedido -> Tarjeta {
     Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Pedido #${pedido.numero}", style = MaterialTheme.typography.titleMedium); Text(dinero(pedido.total), color = MaterialTheme.colorScheme.primary) }
     Text("En preparación", color = MaterialTheme.colorScheme.primary)
     Text(pedido.items.joinToString { "${it.cantidad} \u00d7 ${it.producto.nombre}" }, style = MaterialTheme.typography.bodySmall)
     Text(if(pedido.recojo) "Recojo · $DIRECCION_TIENDA" else "Delivery · ${pedido.cliente.direccion}"); Text(pedido.metodoPago, style = MaterialTheme.typography.bodySmall)
    } }
   }
  } else if(seccion == 3) {
   LazyColumn(Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
    item { Text("Mi perfil", style = MaterialTheme.typography.headlineMedium) }
    item { Tarjeta { Image(painterResource(R.drawable.perfil_daniella), "Foto de perfil de Daniella", Modifier.size(88.dp).clip(CircleShape), contentScale = ContentScale.Crop); Text(cliente.nombre.ifBlank { "Cliente de demostración" }, style = MaterialTheme.typography.titleLarge); Text(cliente.telefono.ifBlank { "Sin teléfono registrado" }) } }
    item { Tarjeta {
     Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
      Column(Modifier.weight(1f)) { Text("Modo oscuro", style = MaterialTheme.typography.titleMedium); Text("Un descanso para tus ojos", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
      Switch(checked = oscuro, onCheckedChange = onCambiarTema, modifier = Modifier.semantics { contentDescription = "Modo oscuro" })
     }
     BotonSecundario("Mis favoritos (${favoritos.size})", onVerFavoritos)
    } }
    item { Tarjeta { Text("Dirección de entrega", style = MaterialTheme.typography.titleMedium); Text(cliente.direccion.ifBlank { "La puedes completar al hacer tu pedido." }); if(cliente.referencia.isNotBlank()) Text(cliente.referencia, style = MaterialTheme.typography.bodySmall) } }
   }
  } else {
   Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
    Text(if(seccion == 1) "Elige tu categoría" else "¿Qué necesitas hoy?", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp, bottom = 12.dp))
    OutlinedTextField(busqueda, { busqueda = it }, Modifier.fillMaxWidth().clay(base = MaterialTheme.colorScheme.background, hundido = true, radio = 18f), placeholder = { Text("Buscar productos...") },
     leadingIcon = { Icon(Icons.Default.Search, null) }, trailingIcon = { if(busqueda.isNotEmpty()) IconButton({ busqueda = "" }) { Icon(Icons.Default.Close, "Limpiar búsqueda") } },
     singleLine = true, shape = RoundedCornerShape(18.dp), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
     keyboardActions = androidx.compose.foundation.text.KeyboardActions(onSearch = { teclado?.hide() }),
     colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent, focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(vertical = 12.dp)) {
     items(listaCategorias) { nombre -> FilterChip(categoria == nombre, { categoria = nombre }, label = { Text(nombre) }, modifier = Modifier.heightIn(min = 48.dp).padding(vertical = 3.dp).clay(base = if(categoria == nombre) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background, hundido = categoria == nombre, radio = 16f), shape = RoundedCornerShape(16.dp)) }
    }
    Row(Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
     Text(if(busqueda.isBlank() && categoria == "Todos") "Tus favoritos de siempre" else "Resultados", style = MaterialTheme.typography.titleMedium)
     Text("${lista.size} productos", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Box {
     TextButton({ menuOrden = true }) { Icon(Icons.Default.Sort, null); Spacer(Modifier.width(8.dp)); Text("Orden: $orden") }
     DropdownMenu(expanded = menuOrden, onDismissRequest = { menuOrden = false }) {
      listOf("Recomendados", "Menor precio", "Mayor precio").forEach { opcion -> DropdownMenuItem(text = { Text(opcion) }, onClick = { orden = opcion; menuOrden = false }) }
     }
    }
    LazyColumn(Modifier.weight(1f), state = estadoLista, verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
     if(lista.isEmpty()) item {
      EstadoVacio(Icons.Default.SearchOff, "No encontramos productos", "Prueba otro nombre o cambia la categoría.")
      BotonSecundario("Quitar filtros", { busqueda = ""; categoria = "Todos"; teclado?.hide() })
     }
     items(lista, key = { it.id }) { producto -> ProductoCard(producto, { teclado?.hide(); onProductoClick(producto) }, {
      onAgregarProducto(producto)
      scope.launch { snackbar.currentSnackbarData?.dismiss(); snackbar.showSnackbar("${producto.nombre} agregado", duration = SnackbarDuration.Short) }
     }, favorito = producto.id in favoritos, onFavorito = { onFavorito(producto) }) }
    }
   }
  }
 }
}
}

package com.tecsup.mibodega.ui.cliente.modelo
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import org.json.JSONArray
import org.json.JSONObject

// Estado de la sesión, guardado por Android al recrear la actividad. No es una base de datos.
val clienteSaver = listSaver<DatosCliente, String>(
 save = { listOf(it.nombre, it.telefono, it.direccion, it.referencia) },
 restore = { DatosCliente(it[0], it[1], it[2], it[3]) })
val carritoSaver = listSaver<List<ItemCarrito>, Int>(
 save = { it.flatMap { item -> listOf(item.producto.id, item.cantidad) } },
 restore = { it.chunked(2).mapNotNull { par -> listaProductosFake.find { p -> p.id == par[0] }?.let { p -> ItemCarrito(p, par[1]) } } })
val pedidosSaver = Saver<List<Pedido>, String>(
 save = { pedidos -> JSONArray().apply { pedidos.forEach { pedido ->
  put(JSONObject().apply {
   put("recojo", pedido.recojo); put("numero", pedido.numero); put("total", pedido.total); put("pago", pedido.metodoPago)
   put("nombre", pedido.cliente.nombre); put("telefono", pedido.cliente.telefono)
   put("direccion", pedido.cliente.direccion); put("referencia", pedido.cliente.referencia)
   put("items", JSONArray().apply { pedido.items.forEach { put(JSONObject().put("id", it.producto.id).put("cantidad", it.cantidad)) } })
  })
 } }.toString() },
 restore = { texto -> val array = JSONArray(texto); (0 until array.length()).map { index ->
  val p = array.getJSONObject(index); val items = p.getJSONArray("items")
  Pedido(p.getInt("numero"), DatosCliente(p.getString("nombre"), p.getString("telefono"), p.getString("direccion"), p.getString("referencia")), p.getString("pago"),
   (0 until items.length()).mapNotNull { i -> val item = items.getJSONObject(i); listaProductosFake.find { it.id == item.getInt("id") }?.let { ItemCarrito(it, item.getInt("cantidad")) } }, p.getDouble("total"), p.optBoolean("recojo", false))
 } })
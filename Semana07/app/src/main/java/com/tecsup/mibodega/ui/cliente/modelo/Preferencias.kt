package com.tecsup.mibodega.ui.cliente.modelo
const val USUARIO_FIJO = "daniella leon"
const val CLAVE_FIJA = "Daniella123"
const val DIRECCION_TIENDA = "Mi Bodega · Av. Los Olivos 123"
fun credencialesValidas(usuario: String, clave: String) = usuario.trim().lowercase() == USUARIO_FIJO && clave == CLAVE_FIJA
fun ordenarProductos(productos: List<Producto>, orden: String) = when (orden) {
 "Menor precio" -> productos.sortedBy { it.precio }
 "Mayor precio" -> productos.sortedByDescending { it.precio }
 else -> productos
}
fun errorRequerido(texto: String, mostrar: Boolean) = if (mostrar && texto.isBlank()) "Completa este campo" else null
fun errorTelefono(texto: String, mostrar: Boolean): String? = when {
 mostrar && texto.isBlank() -> "Completa este campo"
 texto.isNotEmpty() && texto.length != 9 -> "Escribe los 9 números de tu teléfono"
 else -> null
}

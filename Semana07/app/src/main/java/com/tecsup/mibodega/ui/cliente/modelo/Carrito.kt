package com.tecsup.mibodega.ui.cliente.modelo

const val COSTO_DELIVERY = 4.00
fun subtotalCarrito(items: List<ItemCarrito>) = items.sumOf { it.producto.precio * it.cantidad }
fun deliveryCarrito(items: List<ItemCarrito>) = if (items.isEmpty()) 0.0 else COSTO_DELIVERY
fun totalCarrito(items: List<ItemCarrito>) = subtotalCarrito(items) + deliveryCarrito(items)
fun agregarProducto(items: List<ItemCarrito>, producto: Producto, cantidad: Int): List<ItemCarrito> {
    if (cantidad <= 0) return items
    return if (items.any { it.producto.id == producto.id }) items.map {
        if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
    } else items + ItemCarrito(producto, cantidad)
}
fun cambiarCantidad(items: List<ItemCarrito>, producto: Producto, cambio: Int) = items.mapNotNull {
    if (it.producto.id != producto.id) it else (it.cantidad + cambio).let { cantidad ->
        if (cantidad <= 0) null else it.copy(cantidad = cantidad)
    }
}
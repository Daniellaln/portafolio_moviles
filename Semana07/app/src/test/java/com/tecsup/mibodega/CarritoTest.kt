package com.tecsup.mibodega

import com.tecsup.mibodega.ui.cliente.modelo.*
import org.junit.Assert.*
import org.junit.Test

class CarritoTest {
    @Test fun totalDelEjemploYCarritoVacio() {
        val items = listOf(ItemCarrito(listaProductosFake[4], 1), ItemCarrito(listaProductosFake[0], 2), ItemCarrito(listaProductosFake[2], 1))
        assertEquals(20.70, subtotalCarrito(items), 0.001)
        assertEquals(24.70, totalCarrito(items), 0.001)
        assertEquals(0.0, totalCarrito(emptyList()), 0.001)
    }
    @Test fun sumarSinDuplicarYEliminarAlLlegarACero() {
        val producto = listaProductosFake[0]
        val items = agregarProducto(agregarProducto(emptyList(), producto, 1), producto, 2)
        assertEquals(1, items.size)
        assertEquals(3, items.single().cantidad)
        assertEquals(13.50, subtotalCarrito(items), 0.001)
        assertTrue(cambiarCantidad(items, producto, -3).isEmpty())
        assertEquals(items, agregarProducto(items, producto, 0))
    }
    @Test fun categoriasCubrenCatalogo() {
        assertTrue(listaProductosFake.any { it.categoria == "Bebidas" })
        assertTrue(listaProductosFake.any { it.categoria == "Abarrotes" })
        assertTrue(listaProductosFake.any { it.categoria == "Snacks" })
        assertEquals(listaProductosFake.size, listaProductosFake.map { it.id }.distinct().size)
    }
}
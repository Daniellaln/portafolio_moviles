package com.tecsup.mibodega
import com.tecsup.mibodega.ui.cliente.modelo.*
import org.junit.Assert.*
import org.junit.Test
class BusquedaTest {
 @Test fun combinaCategoriaYTexto() {
  assertEquals(listOf(1), filtrarProductos(listaProductosFake, "Abarrotes", "arroz").map { it.id })
  assertTrue(filtrarProductos(listaProductosFake, "Bebidas", "arroz").isEmpty())
  assertEquals(5, filtrarProductos(listaProductosFake, "Todos", "").size)
 }
 @Test fun ignoraTildesMayusculasYEspacios() {
  assertEquals(listOf(1), filtrarProductos(listaProductosFake, "Todos", "  COSTENO  ARROZ ").map { it.id })
  assertEquals(3, filtrarProductos(listaProductosFake, "Abarrotes", "   ").size)
  assertTrue(filtrarProductos(listaProductosFake, "Todos", "inexistente").isEmpty())
 }
}
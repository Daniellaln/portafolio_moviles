package com.tecsup.mibodega
import com.tecsup.mibodega.ui.cliente.modelo.*
import org.junit.Assert.*
import org.junit.Test
class CorreccionTest {
 @Test fun loginRechazaCredencialesYCamposVacios() {
  assertTrue(credencialesValidas("daniella leon", "Daniella123"))
  assertFalse(credencialesValidas("daniella leon", "otra"))
  assertFalse(credencialesValidas("", ""))
  assertNotNull(errorRequerido("   ", true))
  assertNull(errorRequerido("Daniella", true))
  assertNotNull(errorTelefono("", true))
  assertNotNull(errorTelefono("123", true))
 }
 @Test fun entregaEsGratisAlRecogerYVaciaNoCobra() {
  val lista = listOf(ItemCarrito(listaProductosFake.first(), 2))
  assertEquals(subtotalCarrito(lista), totalCarrito(lista, true), .001)
  assertEquals(subtotalCarrito(lista) + 4, totalCarrito(lista, false), .001)
  assertEquals(0.0, totalCarrito(emptyList(), false), .001)
 }
 @Test fun ordenPorPrecioConservaProductos() {
  val asc = ordenarProductos(listaProductosFake, "Menor precio")
  val desc = ordenarProductos(listaProductosFake, "Mayor precio")
  assertEquals(listaProductosFake.size, asc.size)
  assertTrue(asc.zipWithNext().all { (a,b) -> a.precio <= b.precio })
  assertTrue(desc.zipWithNext().all { (a,b) -> a.precio >= b.precio })
 }
}

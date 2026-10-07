package com.tecsup.mibodega
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
class BusquedaUiTest {
 @get:Rule val compose = createAndroidComposeRule<MainActivity>()
 private fun cerrarTeclado() { compose.activityRule.scenario.onActivity {
  (it.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager).hideSoftInputFromWindow(it.window.decorView.windowToken, 0)
 } }
 @Test fun busquedaYCategoriaSeCombinanYSePuedenLimpiar() {
  compose.entrar()
  compose.onNode(hasText("Bebidas") and hasClickAction()).performClick()
  compose.onNode(hasSetTextAction()).performTextInput("arroz")
  compose.onNodeWithText("No encontramos productos").assertExists()
  compose.onNodeWithText("Coca-Cola Original").assertDoesNotExist()
  compose.onNode(hasText("Todos") and hasClickAction()).performClick()
  compose.onNodeWithText("Arroz Costeño").assertExists()
  compose.onNodeWithContentDescription("Limpiar búsqueda").performClick()
  compose.onNode(hasSetTextAction()).performTextInput("  COSTENO ")
  compose.onNodeWithText("Arroz Costeño").assertExists()
  compose.onNodeWithText("Aceite Primor").assertDoesNotExist()
  cerrarTeclado()
  compose.activityRule.scenario.recreate()
  compose.onNodeWithText("Arroz Costeño").assertExists()
  compose.onNode(hasSetTextAction()).assertTextContains("  COSTENO ")
  compose.onNodeWithContentDescription("Limpiar búsqueda").performClick()
  compose.onNode(hasSetTextAction()).performTextInput("zzzz")
  cerrarTeclado()
  compose.onNodeWithText("Quitar filtros").performScrollTo().performClick()
  compose.onNodeWithText("5 productos").assertExists()
 }
 @Test fun carritoSobreviveARecrearLaActividad() {
  compose.entrar()
  compose.onNodeWithContentDescription("Agregar Arroz Costeño").performClick()
  compose.onNodeWithContentDescription("Carrito").performClick()
  compose.onNodeWithText("S/ 8.50").assertExists()
  compose.activityRule.scenario.recreate()
  compose.onNodeWithText("S/ 8.50").assertExists()
  compose.onNodeWithText("Continuar pedido").assertIsEnabled()
 }
}
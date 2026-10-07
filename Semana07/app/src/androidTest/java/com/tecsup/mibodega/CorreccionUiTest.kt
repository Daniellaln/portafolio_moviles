package com.tecsup.mibodega
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.graphics.asAndroidBitmap
import org.junit.Rule
import org.junit.Test
import java.io.File
class CorreccionUiTest {
 @get:Rule val compose = createAndroidComposeRule<MainActivity>()
 private fun tecladoFuera() { compose.activityRule.scenario.onActivity { (it.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager).hideSoftInputFromWindow(it.window.decorView.windowToken, 0) } }
 private fun captura(nombre: String) {
  compose.mainClock.advanceTimeBy(500); compose.waitForIdle(); android.os.SystemClock.sleep(200)
  File(compose.activity.getExternalFilesDir(null), "$nombre.png").outputStream().use { androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it) }
 }
 @Test fun loginIncorrectoNoAvanzaYSePuedeCorregir() {
  compose.onNodeWithText("Iniciar sesión").performClick()
  compose.onNodeWithText("Entrar").performClick()
  compose.onAllNodesWithText("Completa este campo").assertCountEquals(2)
  compose.onNode(hasSetTextAction() and hasText("Usuario")).performTextInput("daniella leon")
  compose.onNode(hasSetTextAction() and hasText("Contraseña")).performTextInput("incorrecta")
  tecladoFuera(); compose.onNodeWithText("Entrar").performScrollTo().performClick()
  compose.onNodeWithText("El usuario o la contraseña no coinciden. Inténtalo otra vez.").assertExists()
  captura("08_login_error")
  compose.onNode(hasSetTextAction() and hasText("Contraseña")).performTextReplacement("Daniella123")
  tecladoFuera(); compose.onNodeWithText("Entrar").performScrollTo().performClick()
  compose.onNodeWithContentDescription("Carrito").assertExists()
 }
 @Test fun favoritosOrdenModoOscuroYRotacion() {
  compose.entrar()
  compose.onNodeWithContentDescription("Guardar favorito Arroz Costeño").performClick()
  compose.onNodeWithContentDescription("Mis favoritos").performClick()
  compose.onNodeWithText("Arroz Costeño").performClick()
  compose.onNodeWithContentDescription("Quitar favorito Arroz Costeño").assertExists()
  compose.onNodeWithContentDescription("Volver").performClick(); captura("09_favoritos")
  compose.onNodeWithContentDescription("Volver").performClick()
  compose.onNodeWithText("Orden: Recomendados").performClick()
  compose.onNodeWithText("Mayor precio").performClick(); compose.mainClock.advanceTimeBy(300); compose.waitForIdle()
  compose.onNodeWithText("Aceite Primor").assertIsDisplayed()
  compose.onNodeWithText("Orden: Mayor precio").performClick()
  compose.onNodeWithText("Menor precio").performClick(); compose.mainClock.advanceTimeBy(300); compose.waitForIdle()
  compose.onNodeWithText("Galleta Oreo").assertIsDisplayed()
  compose.onNodeWithText("Perfil").performClick()
  compose.onNodeWithText("Daniella Leon").assertIsDisplayed()
  compose.onNodeWithContentDescription("Foto de perfil de Daniella").assertExists()
  compose.onNodeWithContentDescription("Modo oscuro").performScrollTo().performClick().assertIsOn()
  captura("10_perfil_oscuro")
  compose.activityRule.scenario.recreate()
  compose.onNodeWithContentDescription("Modo oscuro").assertIsOn()
  compose.onNodeWithText("Inicio").performClick(); captura("11_inicio_oscuro")
  compose.onNodeWithText("Orden: Menor precio").assertExists()
  compose.onNodeWithContentDescription("Mis favoritos").performClick()
  compose.onNodeWithContentDescription("Quitar favorito Arroz Costeño").performClick()
  compose.onNodeWithText("Aún no tienes favoritos").assertExists()
 }
 @Test fun eliminarPideConfirmacionInclusoAlBajarAZero() {
  compose.entrar()
  compose.onNodeWithContentDescription("Agregar Arroz Costeño").performClick()
  compose.onNodeWithContentDescription("Carrito").performClick()
  compose.onNodeWithContentDescription("Eliminar Arroz Costeño").performClick()
  compose.onNodeWithText("¿Eliminar producto?").assertExists(); captura("12_eliminar")
  compose.onNodeWithText("Conservar").performClick()
  compose.onNodeWithText("S/ 8.50").assertExists()
  compose.onNodeWithContentDescription("Disminuir cantidad").performClick()
  compose.onNodeWithText("Eliminar", substring = false).performClick()
  compose.onNodeWithText("Tu carrito está vacío").assertExists()
  compose.onNodeWithText("Continuar pedido").assertIsNotEnabled()
 }
 @Test fun recojoGratisYDeliveryGuardanHistorialYTotales() {
  compose.entrar()
  compose.onNodeWithContentDescription("Agregar Arroz Costeño").performClick()
  compose.onNodeWithContentDescription("Carrito").performClick()
  compose.onNodeWithText("Recojo en tienda · Gratis").performClick()
  compose.onAllNodesWithText("S/ 4.50")[0].assertExists()
  compose.onNodeWithText("Continuar pedido").performClick()
  compose.onNodeWithText("Confirmar pedido").performScrollTo().performClick()
  compose.onAllNodesWithText("Completa este campo")[0].assertExists()
  compose.onNode(hasSetTextAction() and hasText("Teléfono")).performScrollTo().performTextInput("987654321")
  tecladoFuera(); captura("13_recojo")
  compose.onNodeWithText("Confirmar pedido").performScrollTo().performClick()
  compose.onNodeWithText("Total: S/ 4.50").assertExists()
  compose.onNodeWithText("Recojo en tienda").assertExists()
  compose.onNodeWithText("Volver al inicio").performClick()
  compose.onNodeWithContentDescription("Agregar Arroz Costeño").performClick()
  compose.onNodeWithContentDescription("Carrito").performClick()
  compose.onNodeWithText("Delivery · S/ 4.00").performClick()
  compose.onNodeWithText("S/ 8.50").assertExists()
  compose.onNodeWithText("Continuar pedido").performClick()
  compose.onNodeWithText("Confirmar pedido").performScrollTo().performClick()
  compose.onAllNodesWithText("Completa este campo")[0].assertExists()
  compose.onNode(hasSetTextAction() and hasText("Dirección", substring = false)).performScrollTo().performTextInput("Calle Lima 100")
  tecladoFuera(); compose.onNodeWithText("Confirmar pedido").performScrollTo().performClick()
  compose.onNodeWithText("Total: S/ 8.50").assertExists()
  compose.onNodeWithText("Volver al inicio").performClick()
  compose.onNodeWithText("Pedidos").performClick()
  compose.onNodeWithText("Pedido #1024").assertExists()
  compose.onNodeWithText("Pedido #1025").assertExists()
  captura("14_historial")
  compose.activityRule.scenario.recreate()
  compose.onNodeWithText("Pedido #1024").assertExists()
  compose.onNodeWithText("Pedido #1025").assertExists()
 }
}

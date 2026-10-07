package com.tecsup.mibodega
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.graphics.asAndroidBitmap
import org.junit.Rule
import org.junit.Test
import java.io.File
class VistasTest {
 @get:Rule val compose = createAndroidComposeRule<MainActivity>()
 private fun captura(nombre: String) {
  compose.mainClock.advanceTimeBy(500)
  compose.waitForIdle()
  android.os.SystemClock.sleep(350)
  val file = File(compose.activity.getExternalFilesDir(null), "$nombre.png")
  file.outputStream().use { compose.onRoot().captureToImage().asAndroidBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it) }
 }
 @Test fun revisarLasSietePantallas() {
  captura("01_bienvenida")
  compose.onNodeWithText("Registrarme").performClick(); captura("02_registro")
  compose.onNodeWithContentDescription("Volver").performClick()
  compose.onNodeWithText("Iniciar sesión (demo)").performClick(); captura("03_inicio")
  compose.onNode(hasText("Bebidas") and hasClickAction()).performClick()
  compose.onNodeWithText("Coca-Cola Original").performClick(); captura("04_detalle")
  compose.onNodeWithText("Agregar al carrito").performClick(); captura("05_carrito")
  compose.onNodeWithText("Continuar pedido").performClick(); captura("06_entrega")
  compose.onAllNodes(hasSetTextAction())[0].performScrollTo().performTextInput("Juan Perez")
  compose.onAllNodes(hasSetTextAction())[1].performScrollTo().performTextInput("987654321")
  compose.onAllNodes(hasSetTextAction())[2].performScrollTo().performTextInput("Av. Los Olivos 123")
  compose.activityRule.scenario.onActivity { (it.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager).hideSoftInputFromWindow(it.window.decorView.windowToken, 0) }
  compose.onNodeWithText("Confirmar pedido").performScrollTo().performClick(); captura("07_confirmacion")
 }
}

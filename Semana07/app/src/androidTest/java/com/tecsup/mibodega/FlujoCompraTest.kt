package com.tecsup.mibodega

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class FlujoCompraTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun compraCompletaConMenuFiltroCantidadesYPopUpTo() {
        compose.onNodeWithText("Iniciar sesión (demo)").performClick()
        compose.onNodeWithText("Perfil").performClick()
        compose.onNodeWithText("Cliente de demostración").assertIsDisplayed()
        compose.onNodeWithText("Pedidos").performClick()
        compose.onNodeWithText("Aún no tienes pedidos.").assertIsDisplayed()
        compose.onNodeWithText("Categorías").performClick()
        compose.onNode(hasText("Bebidas") and hasClickAction()).performClick()
        compose.onNodeWithText("Arroz Costeño").assertDoesNotExist()
        compose.onNodeWithText("Coca-Cola Original").performClick()
        compose.onNodeWithContentDescription("Aumentar cantidad").performClick()
        compose.onNodeWithText("Agregar al carrito").performClick()
        compose.onNodeWithText("S/ 17.00").assertIsDisplayed()
        compose.onNodeWithContentDescription("Disminuir cantidad").performClick()
        compose.onNodeWithText("S/ 10.50").assertIsDisplayed()
        compose.onNodeWithContentDescription("Eliminar Coca-Cola Original").performClick()
        compose.onNodeWithText("Continuar pedido").assertIsNotEnabled()
        compose.onAllNodesWithText("S/ 0.00")[0].assertExists()
        compose.onNodeWithContentDescription("Volver").performClick()
        compose.onNode(hasText("Bebidas") and hasClickAction()).performClick()
        compose.onNodeWithContentDescription("Agregar Coca-Cola Original").performClick()
        compose.onNodeWithContentDescription("Carrito").performClick()
        compose.onNodeWithText("Continuar pedido").performClick()
        compose.onNodeWithText("Confirmar pedido").performScrollTo().assertIsNotEnabled()
        compose.onAllNodes(hasSetTextAction())[0].performScrollTo().performTextInput("Juan Perez")
        compose.onAllNodes(hasSetTextAction())[1].performScrollTo().performTextInput("987654321")
        compose.onAllNodes(hasSetTextAction())[2].performScrollTo().performTextInput("Av. Los Olivos 123")
        compose.onAllNodes(hasSetTextAction())[3].performScrollTo().performTextInput("Frente al parque")
        compose.activityRule.scenario.onActivity { activity ->
            val manager = activity.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            manager.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
        compose.onNodeWithText("Yape").performScrollTo().performClick()
        compose.onNodeWithText("Confirmar pedido").performScrollTo().performClick()
        compose.onNodeWithText("¡Pedido realizado!").assertIsDisplayed()
        compose.onNodeWithText("Pedido #1024").assertIsDisplayed()
        compose.onNodeWithText("Total: S/ 10.50").assertIsDisplayed()
        compose.activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
        compose.onNodeWithContentDescription("Carrito").assertIsDisplayed()
        compose.onNodeWithText("Datos de entrega").assertDoesNotExist()
        compose.onNodeWithText("Pedidos").performClick()
        compose.onNodeWithText("Pedido #1024").assertIsDisplayed()
        compose.onNodeWithText("Perfil").performClick()
        compose.onNodeWithText("Juan Perez").assertIsDisplayed()
        compose.onNodeWithText("Inicio").performClick()
        compose.onNodeWithContentDescription("Carrito").performClick()
        compose.onNodeWithText("Continuar pedido").assertIsNotEnabled()
    }

    @Test fun registroValidaYConservaDatos() {
        compose.onNodeWithText("Registrarme").performClick()
        compose.onNodeWithText("Crear cuenta", substring = false).let {
            compose.onAllNodesWithText("Crear cuenta")[1].assertIsNotEnabled()
        }
        compose.onAllNodes(hasSetTextAction())[0].performScrollTo().performTextInput("Ana")
        compose.onAllNodes(hasSetTextAction())[1].performScrollTo().performTextInput("912345678")
        compose.onAllNodes(hasSetTextAction())[2].performScrollTo().performTextInput("Calle Lima 100")
        compose.activityRule.scenario.onActivity { activity ->
            val manager = activity.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            manager.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
        compose.onAllNodesWithText("Crear cuenta")[1].performScrollTo().performClick()
        compose.onNodeWithText("Perfil").performClick()
        compose.onNodeWithText("Ana").assertIsDisplayed()
        compose.onNodeWithText("Calle Lima 100").assertIsDisplayed()
    }
}

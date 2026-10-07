package com.tecsup.mibodega
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
fun AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.entrar() {
 onNodeWithText("Iniciar sesión", useUnmergedTree = true).performClick()
 onNode(hasSetTextAction() and hasText("Usuario")).performTextInput("daniella leon")
 onNode(hasSetTextAction() and hasText("Contraseña")).performTextInput("Daniella123")
 activityRule.scenario.onActivity { (it.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager).hideSoftInputFromWindow(it.window.decorView.windowToken, 0) }
 onNodeWithText("Entrar").performScrollTo().performClick()
}

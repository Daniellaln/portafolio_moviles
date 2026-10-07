package com.tecsup.mibodega.ui.componentes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
@Composable
fun CampoTexto(etiqueta: String, valor: String, onValorCambia: (String) -> Unit, modifier: Modifier = Modifier,
 placeholder: String? = null, teclado: KeyboardType = KeyboardType.Text, error: String? = null) {
 OutlinedTextField(valor, onValorCambia, modifier.fillMaxWidth().clay(base = MaterialTheme.colorScheme.background, hundido = true, radio = 18f), label = { Text(etiqueta) },
  placeholder = placeholder?.let { { Text(it) } }, singleLine = true, isError = error != null,
  supportingText = error?.let { { Text(it) } }, shape = RoundedCornerShape(16.dp),
  keyboardOptions = KeyboardOptions(keyboardType = teclado),
  colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
   focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent, unfocusedBorderColor = MaterialTheme.colorScheme.outline))
}
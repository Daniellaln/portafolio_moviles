package com.tecsup.mibodega.ui.componentes
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
@Composable
fun BotonPrimario(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, subtexto: String? = null, icono: Painter? = null, habilitado: Boolean = true) {
 val interaction = remember { MutableInteractionSource() }
 val pressed by interaction.collectIsPressedAsState()
 val scale by animateFloatAsState(if (pressed) .98f else 1f, spring(dampingRatio = 1f, stiffness = 900f), label = "presion")
 Button(onClick, modifier.fillMaxWidth().heightIn(min = 56.dp).clay(if(habilitado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, radio = 22f).graphicsLayer { scaleX = scale; scaleY = scale },
  colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Transparent, disabledContainerColor = androidx.compose.ui.graphics.Color.Transparent), enabled = habilitado, interactionSource = interaction, shape = RoundedCornerShape(18.dp), contentPadding = PaddingValues(16.dp)) {
  if (icono != null) { Icon(icono, null, Modifier.size(22.dp)); Spacer(Modifier.width(10.dp)) }
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
   Text(texto, style = MaterialTheme.typography.labelLarge)
   if (subtexto != null) Text(subtexto, style = MaterialTheme.typography.bodySmall)
  }
 }
}
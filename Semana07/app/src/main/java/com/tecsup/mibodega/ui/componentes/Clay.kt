package com.tecsup.mibodega.ui.componentes
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp

// Luz arriba a la izquierda y sombra opuesta. Se dibuja sin librerías adicionales.
@Composable
fun Modifier.clay(base: Color = MaterialTheme.colorScheme.surface, hundido: Boolean = false, radio: Float = 26f): Modifier {
 val oscuro = MaterialTheme.colorScheme.background.luminance() < .2f
 return drawBehind {
 val radius = radio.dp.toPx()
 val offset = 4.dp.toPx()
 drawIntoCanvas { canvas ->
  val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  paint.color = base.toArgb()
  paint.setShadowLayer(10.dp.toPx(), -offset, -offset, Color.White.copy(alpha = if (oscuro) .035f else .95f).toArgb())
  canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radius, radius, paint)
  paint.setShadowLayer(10.dp.toPx(), offset, offset, (if(oscuro) Color.Black.copy(alpha = .4f) else Color(0xFF98AA96).copy(alpha = .35f)).toArgb())
  canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radius, radius, paint)
 }
 drawRoundRect(Brush.linearGradient(listOf(if(hundido) base.copy(red = base.red * .91f, green = base.green * .93f, blue = base.blue * .91f) else base,
  if(hundido) base else Color.White.copy(alpha = if(oscuro) .035f else .38f).compositeOver(base)), start = Offset.Zero, end = Offset(size.width, size.height)), cornerRadius = CornerRadius(radius))
 if(hundido) {
  drawRoundRect(Color(0xFF536E52).copy(alpha = .12f), topLeft = Offset(1.dp.toPx(), 1.dp.toPx()), size = Size(size.width - 2.dp.toPx(), size.height - 2.dp.toPx()), cornerRadius = CornerRadius(radius), style = androidx.compose.ui.graphics.drawscope.Stroke(1.5.dp.toPx()))
 }
}
}
@Composable
fun Modifier.cristal(): Modifier {
 val oscuro = MaterialTheme.colorScheme.background.luminance() < .2f
 return drawBehind {
 val radio = 32.dp.toPx()
 drawIntoCanvas { canvas ->
  val p = Paint(Paint.ANTI_ALIAS_FLAG)
  p.color = (if(oscuro) Color(0xE62C3C33) else Color(0xC8F3FAF7)).toArgb()
  p.setShadowLayer(16.dp.toPx(), 0f, 8.dp.toPx(), Color(0xFF637F74).copy(alpha = .23f).toArgb())
  canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radio, radio, p)
 }
 drawRoundRect(Brush.linearGradient(if(oscuro) listOf(Color(0xF034463A), Color(0xDC25382E), Color(0xEF304136)) else listOf(Color.White.copy(alpha = .95f), Color(0xFFDFF2E8).copy(alpha = .68f), Color.White.copy(alpha = .82f)), end = Offset(size.width, size.height)), cornerRadius = CornerRadius(radio))
 drawRoundRect(Color.White.copy(alpha = if(oscuro) .18f else .95f), topLeft = Offset(1.dp.toPx(), 1.dp.toPx()), size = Size(size.width-2.dp.toPx(), size.height-2.dp.toPx()), cornerRadius = CornerRadius(radio), style = androidx.compose.ui.graphics.drawscope.Stroke(1.5.dp.toPx()))
 drawLine(Color.White.copy(alpha = if(oscuro) .12f else .65f), Offset(radio, 4.dp.toPx()), Offset(size.width-radio, 4.dp.toPx()), strokeWidth = 2.dp.toPx())
}

}

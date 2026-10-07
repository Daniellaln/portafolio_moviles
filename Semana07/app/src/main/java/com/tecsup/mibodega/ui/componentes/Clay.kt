package com.tecsup.mibodega.ui.componentes
import android.graphics.Paint
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp

// Luz arriba a la izquierda y sombra opuesta. Se dibuja sin librerías adicionales.
fun Modifier.clay(base: Color = Color(0xFFF5F5ED), hundido: Boolean = false, radio: Float = 26f) = drawBehind {
 val radius = radio.dp.toPx()
 val offset = 4.dp.toPx()
 drawIntoCanvas { canvas ->
  val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  paint.color = base.toArgb()
  paint.setShadowLayer(10.dp.toPx(), -offset, -offset, Color.White.copy(alpha = .95f).toArgb())
  canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radius, radius, paint)
  paint.setShadowLayer(10.dp.toPx(), offset, offset, Color(0xFF98AA96).copy(alpha = .35f).toArgb())
  canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radius, radius, paint)
 }
 drawRoundRect(Brush.linearGradient(listOf(if(hundido) base.copy(red = base.red * .91f, green = base.green * .93f, blue = base.blue * .91f) else base,
  if(hundido) base else Color.White.copy(alpha = .38f).compositeOver(base)), start = Offset.Zero, end = Offset(size.width, size.height)), cornerRadius = CornerRadius(radius))
 if(hundido) {
  drawRoundRect(Color(0xFF536E52).copy(alpha = .12f), topLeft = Offset(1.dp.toPx(), 1.dp.toPx()), size = Size(size.width - 2.dp.toPx(), size.height - 2.dp.toPx()), cornerRadius = CornerRadius(radius), style = androidx.compose.ui.graphics.drawscope.Stroke(1.5.dp.toPx()))
 }
}
fun Modifier.cristal() = drawBehind {
 val radio = 32.dp.toPx()
 drawIntoCanvas { canvas ->
  val p = Paint(Paint.ANTI_ALIAS_FLAG)
  p.color = Color(0xC8F3FAF7).toArgb()
  p.setShadowLayer(16.dp.toPx(), 0f, 8.dp.toPx(), Color(0xFF637F74).copy(alpha = .23f).toArgb())
  canvas.nativeCanvas.drawRoundRect(0f, 0f, size.width, size.height, radio, radio, p)
 }
 drawRoundRect(Brush.linearGradient(listOf(Color.White.copy(alpha = .95f), Color(0xFFDFF2E8).copy(alpha = .68f), Color.White.copy(alpha = .82f)), end = Offset(size.width, size.height)), cornerRadius = CornerRadius(radio))
 drawRoundRect(Color.White.copy(alpha = .95f), topLeft = Offset(1.dp.toPx(), 1.dp.toPx()), size = Size(size.width-2.dp.toPx(), size.height-2.dp.toPx()), cornerRadius = CornerRadius(radio), style = androidx.compose.ui.graphics.drawscope.Stroke(1.5.dp.toPx()))
 drawLine(Color.White.copy(alpha = .65f), Offset(radio, 4.dp.toPx()), Offset(size.width-radio, 4.dp.toPx()), strokeWidth = 2.dp.toPx())
}

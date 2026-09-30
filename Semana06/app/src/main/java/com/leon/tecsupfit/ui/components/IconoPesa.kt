package com.leon.tecsupfit.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

/**
 * Dibuja una pesa horizontal simple (barra + dos discos), como en la maqueta.
 */
@Composable
fun IconoPesa(modifier: Modifier = Modifier, color: Color = Color.Black) {
    Canvas(modifier = modifier) {
        val discoAncho = size.width * 0.16f
        val discoAlto = size.height * 0.6f
        val barraAlto = size.height * 0.16f

        // Barra central
        drawRoundRect(
            color = color,
            topLeft = Offset(discoAncho, size.height / 2 - barraAlto / 2),
            size = Size(size.width - discoAncho * 2, barraAlto),
            cornerRadius = CornerRadius(barraAlto / 2)
        )
        // Disco izquierdo
        drawRoundRect(
            color = color,
            topLeft = Offset(0f, size.height / 2 - discoAlto / 2),
            size = Size(discoAncho, discoAlto),
            cornerRadius = CornerRadius(discoAncho / 3)
        )
        // Disco derecho
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width - discoAncho, size.height / 2 - discoAlto / 2),
            size = Size(discoAncho, discoAlto),
            cornerRadius = CornerRadius(discoAncho / 3)
        )
    }
}
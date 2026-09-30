package com.leon.tecsupfit.ui.components

import android.provider.Settings
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext

/**
 * Escala táctil breve y regreso elástico discreto para CTAs.
 */
fun Modifier.botonClickAnimado(onClick: () -> Unit = {}) = composed {
    val context = LocalContext.current
    // Detección básica de "Reducir movimiento" del sistema
    val animatorScale = try {
        Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1.0f)
    } catch (e: Exception) { 1.0f }
    val reduceMotion = animatorScale == 0f

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed && !reduceMotion) 0.95f else 1f,
        animationSpec = if (reduceMotion) {
            tween(durationMillis = 0)
        } else {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        },
        label = "EscalaBoton"
    )

    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }.clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
    )
}

// Constantes de tiempo solicitadas
const val DurationScreen = 280 // 220-320ms
const val DurationSheet = 310  // 260-360ms
const val DurationFast = 150

fun <T> springLowBouncy() = spring<T>(
    dampingRatio = Spring.DampingRatioLowBouncy,
    stiffness = Spring.StiffnessLow
)

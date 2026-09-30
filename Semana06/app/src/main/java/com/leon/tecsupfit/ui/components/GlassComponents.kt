package com.leon.tecsupfit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.GlassSurface
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina

@Composable
fun GlassContainer(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    blurAmount: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(cornerRadius), ambientColor = Color.Black.copy(0.1f))
            .clip(RoundedCornerShape(cornerRadius))
            .background(GlassSurface)
            .border(0.5.dp, GlassBorder, RoundedCornerShape(cornerRadius))
            .blur(blurAmount) // This blurs the content inside, but for glass we usually want blur behind. 
            // In Compose, blur(blurAmount) blurs the node's content. 
            // Real glass effect (blur behind) is harder in standard Compose without RenderEffect on Android 12+.
            // For now, we use a translucent layer with a subtle gradient to simulate refraction.
            .drawBehind {
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.1f),
                            Color.White.copy(alpha = 0.02f)
                        )
                    )
                )
            }
    ) {
        content()
    }
}

@Composable
fun BotonVolver(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(52.dp)
            .shadow(8.dp, CircleShape)
            .clip(CircleShape)
            .background(GlassSurface)
            .border(1.dp, GlassBorder, CircleShape)
            .clickable { onVolver() },
        contentAlignment = Alignment.Center
    ) {
        // Reflejo aqua desplazado
        Box(
            modifier = Modifier
                .size(20.dp)
                .offset(x = (-8).dp, y = (-8).dp)
                .blur(10.dp)
                .background(LuzAqua.copy(alpha = 0.3f), CircleShape)
        )
        
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Volver",
            tint = TintaMarina,
            modifier = Modifier.size(24.dp)
        )
    }
}

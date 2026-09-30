package com.leon.tecsupfit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.leon.tecsupfit.R
import com.leon.tecsupfit.ui.theme.BasePorcelana

/**
 * Layout base espacioso con fondo prismático claro y soporte para dock flotante.
 */
@Composable
fun LayoutBase(
    rutaActual: String? = null,
    onNavegar: ((String) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            if (rutaActual != null && onNavegar != null) {
                TecsupFitBottomBar(rutaActual = rutaActual, onNavegar = onNavegar)
            }
        },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BasePorcelana)
        ) {
            // Fondo prisma claro (baja opacidad para profundidad sutil)
            Image(
                painter = painterResource(id = R.drawable.fondo_prisma),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.25f
            )
            
            // Capa porcelana translúcida para legibilidad
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BasePorcelana.copy(alpha = 0.7f))
            )

            Box(modifier = Modifier.padding(padding)) {
                content()
            }
        }
    }
}

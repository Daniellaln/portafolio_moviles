package com.leon.tecsupfit.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.leon.tecsupfit.navigation.Pantallas
import com.leon.tecsupfit.ui.theme.AzulProfundo
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.GlassSurface
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina

data class ItemBottomBar(
    val ruta: String,
    val etiqueta: String,
    val icono: ImageVector
)

private val itemsBottomBar = listOf(
    ItemBottomBar(Pantallas.INICIO, "Inicio", Icons.Filled.Home),
    ItemBottomBar(Pantallas.RESERVAS, "Reservas", Icons.Filled.List),
    ItemBottomBar(Pantallas.RUTINAS, "Rutinas", Icons.Filled.FitnessCenter),
    ItemBottomBar(Pantallas.PERFIL, "Perfil", Icons.Filled.Person)
)

@Composable
fun TecsupFitBottomBar(rutaActual: String, onNavegar: (String) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val barPadding = 24.dp
    val contentWidth = screenWidth - (barPadding * 2) - 16.dp
    val itemWidth = contentWidth / 4

    val currentIndex = itemsBottomBar.indexOfFirst { it.ruta == rutaActual }.coerceAtLeast(0)
    
    // Animación suave del desplazamiento del dock
    val indicatorOffset by animateDpAsState(
        targetValue = itemWidth * currentIndex,
        animationSpec = springLowBouncy(),
        label = "DockIndicator"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp, start = barPadding, end = barPadding),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Dock Background
        Box(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(32.dp), ambientColor = Color.Black.copy(0.08f))
                .clip(RoundedCornerShape(32.dp))
                .background(GlassSurface)
                .border(1.dp, GlassBorder, RoundedCornerShape(32.dp))
        )

        Box(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            // Indicador activo deslizante
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(itemWidth)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(56.dp)
                        .height(36.dp)
                        .clip(CircleShape)
                        .background(AzulProfundo),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .align(Alignment.TopEnd)
                            .padding(top = 4.dp, end = 4.dp)
                            .blur(4.dp)
                            .background(LuzAqua.copy(alpha = 0.5f), CircleShape)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsBottomBar.forEach { item ->
                    val seleccionado = rutaActual == item.ruta
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .botonClickAnimado { onNavegar(item.ruta) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icono,
                            contentDescription = item.etiqueta,
                            tint = if (seleccionado) Color.White else TintaMarina.copy(alpha = 0.5f),
                            modifier = Modifier.size(if (seleccionado) 20.dp else 24.dp)
                        )
                    }
                }
            }
        }
    }
}

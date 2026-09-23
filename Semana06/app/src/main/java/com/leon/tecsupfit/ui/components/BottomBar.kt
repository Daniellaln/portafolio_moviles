package com.leon.tecsupfit.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.leon.tecsupfit.navigation.Pantallas
import com.leon.tecsupfit.ui.theme.VerdeTecsup

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

/**
 * Barra inferior con las 4 pestañas. Resalta el ícono de la pantalla actual.
 */
@Composable
fun TecsupFitBottomBar(rutaActual: String, onNavegar: (String) -> Unit) {
    NavigationBar {
        itemsBottomBar.forEach { item ->
            val seleccionado = rutaActual == item.ruta
            NavigationBarItem(
                selected = seleccionado,
                onClick = { onNavegar(item.ruta) },
                icon = {
                    Icon(
                        imageVector = item.icono,
                        contentDescription = item.etiqueta,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(item.etiqueta) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VerdeTecsup,
                    selectedTextColor = VerdeTecsup,
                    indicatorColor = Color(0xFFDCEFE4)
                )
            )
        }
    }
}

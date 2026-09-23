package com.leon.tecsupfit.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leon.tecsupfit.ui.components.TecsupFitBottomBar

/**
 * Pantalla simple de Rutinas, pendiente de desarrollar en una próxima semana.
 */
@Composable
fun PantallaRutinas(rutaActual: String, onNavegar: (String) -> Unit) {
    Scaffold(
        bottomBar = { TecsupFitBottomBar(rutaActual = rutaActual, onNavegar = onNavegar) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Rutinas próximamente")
        }
    }
}

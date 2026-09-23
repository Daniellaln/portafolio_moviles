package com.leon.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.leon.tecsupfit.ui.components.TecsupFitBottomBar
import com.leon.tecsupfit.ui.theme.VerdeClaro
import com.leon.tecsupfit.ui.theme.VerdeTecsup

@Composable
fun PantallaPerfil(rutaActual: String, onNavegar: (String) -> Unit) {
    Scaffold(
        bottomBar = { TecsupFitBottomBar(rutaActual = rutaActual, onNavegar = onNavegar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mi perfil",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .size(72.dp)
                    .background(VerdeClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "DR", color = VerdeTecsup, fontWeight = FontWeight.Bold)
            }

            Text(
                text = "Diego Ramos",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = "Plan Premium",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EstadisticaCard(valor = "14", etiqueta = "Clases")
                EstadisticaCard(valor = "3", etiqueta = "Rachas")
            }
        }
    }
}

@Composable
private fun EstadisticaCard(valor: String, etiqueta: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFFEDEDED)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .size(width = 90.dp, height = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = valor, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.titleMedium.fontSize)
            Text(text = etiqueta, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

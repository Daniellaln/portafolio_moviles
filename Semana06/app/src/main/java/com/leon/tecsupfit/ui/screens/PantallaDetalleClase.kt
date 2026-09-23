package com.leon.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.tecsupfit.data.ClaseFitness
import com.leon.tecsupfit.ui.theme.VerdeClaro
import com.leon.tecsupfit.ui.theme.VerdeTecsup

/**
 * Pantalla de detalle. Recibe la clase elegida por navegación (por id, ver NavGraph)
 * y muestra el botón para reservar el cupo.
 */
@Composable
fun PantallaDetalleClase(
    clase: ClaseFitness,
    onVolver: () -> Unit,
    onReservar: () -> Unit
) {
    Scaffold(
        bottomBar = {
            Button(
                onClick = onReservar,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeTecsup),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Reservar cupo", color = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "← Detalle de clase",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onVolver() }
                    .padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(VerdeClaro, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.FitnessCenter,
                    contentDescription = null,
                    tint = VerdeTecsup,
                    modifier = Modifier.height(48.dp)
                )
            }

            Column(modifier = Modifier.padding(top = 20.dp)) {
                Text(
                    text = clase.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
                Text(
                    text = "${clase.hora} · ${clase.sala} · ${clase.duracionMin} min",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = clase.descripcion,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Text(
                    text = "${clase.cuposDisponibles} de ${clase.cuposTotales} cupos disponibles",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }
    }
}

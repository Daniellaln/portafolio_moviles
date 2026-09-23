package com.leon.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.tecsupfit.data.DatosClases
import com.leon.tecsupfit.data.EstadoReserva
import com.leon.tecsupfit.data.Reserva
import com.leon.tecsupfit.ui.components.TecsupFitBottomBar
import com.leon.tecsupfit.ui.theme.VerdeBadge

@Composable
fun PantallaReservas(rutaActual: String, onNavegar: (String) -> Unit) {
    Scaffold(
        bottomBar = { TecsupFitBottomBar(rutaActual = rutaActual, onNavegar = onNavegar) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Text(
                text = "Mis reservas",
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(DatosClases.reservas) { reserva ->
                    TarjetaReserva(reserva)
                }
            }
        }
    }
}

@Composable
private fun TarjetaReserva(reserva: Reserva) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = reserva.clase.nombre, fontWeight = FontWeight.Bold)
            Text(text = reserva.fecha, color = MaterialTheme.colorScheme.onSurfaceVariant)

            val esConfirmada = reserva.estado == EstadoReserva.CONFIRMADA
            Text(
                text = if (esConfirmada) "Confirmada" else "Completada",
                color = if (esConfirmada) VerdeBadge else Color.Gray,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

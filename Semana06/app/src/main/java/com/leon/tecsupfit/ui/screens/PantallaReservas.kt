package com.leon.tecsupfit.ui.screens

<<<<<<< HEAD
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.*

@Composable
fun PantallaReservas(
    clases: List<ClaseFitness>,
    reservas: List<Reserva>,
    ahora: Long,
    foto: String?,
    onProfile: () -> Unit,
    abrir: (String) -> Unit,
    explorar: () -> Unit,
) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val todas = unirReservas(reservas, clases)
    val proximas = todas.filter { it.proxima(ahora) }.sortedBy { it.clase.inicio }
    val curso = todas.filter { it.enCurso(ahora) }.sortedBy { it.clase.inicio }
    val historial =
        todas
            .filter { it.reserva.estado == "CANCELADA" || it.clase.fin <= ahora }
            .sortedByDescending { it.clase.inicio }
    Page(
        "Mis reservas",
        if (tab == 0) "Gestiona tus próximas clases." else "Clases anteriores y canceladas.",
        foto,
        onProfile,
    ) {
        Segments(listOf("Próximas", "Historial"), tab) { tab = it }
        if (tab == 0) {
            curso.forEach { r ->
                PhotoRow(
                    r.clase.nombre,
                    "${hora(r.clase.inicio)} · ${r.clase.sala}",
                    "En curso",
                    r.clase.imageKey,
                ) {
                    abrir(r.reserva.id)
                }
            }
            val primera = proximas.firstOrNull()
            if (primera == null && curso.isEmpty()) {
                InfoCard(
                    "Tu próximo momento empieza aquí",
                    "Elige una clase y guarda tu lugar.",
                    true,
                )
                PrimaryButton("Explorar clases", onClick = explorar)
            }
            if (primera != null) {
                val c = primera.clase
                PhotoHero(
                    c.nombre,
                    c.imageKey,
                    "CONFIRMADA",
                    "${fechaBreve(c.inicio,ahora)} · ${hora(c.inicio)} · ${c.sala}",
                    onClick = { abrir(primera.reserva.id) },
                )
                if (proximas.size > 1) SectionTitle("Después")
                proximas.drop(1).forEach { r ->
                    PhotoRow(
                        r.clase.nombre,
                        "${fechaBreve(r.clase.inicio,ahora)} · ${hora(r.clase.inicio)} · ${r.clase.sala}",
                        "Confirmada",
                        r.clase.imageKey,
                    ) {
                        abrir(r.reserva.id)
                    }
                }
                InfoCard("Abre la reserva para ver sus detalles o cancelar.")
            }
        } else {
            if (historial.isEmpty())
                InfoCard(
                    "Todavía no hay historial",
                    "Aquí aparecerán las clases finalizadas y las reservas que canceles.",
                )
            historial.forEach { r ->
                PhotoRow(
                    r.clase.nombre,
                    "${fechaBreve(r.clase.inicio,ahora)} · ${hora(r.clase.inicio)}",
                    r.etiqueta(ahora),
                    r.clase.imageKey,
                ) {
                    abrir(r.reserva.id)
                }
            }
            if (historial.isNotEmpty())
                InfoCard(
                    "Tu actividad, en un lugar",
                    "Una clase finalizada indica que su horario terminó; no confirma asistencia.",
                )
=======
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
>>>>>>> sinia
        }
    }
}

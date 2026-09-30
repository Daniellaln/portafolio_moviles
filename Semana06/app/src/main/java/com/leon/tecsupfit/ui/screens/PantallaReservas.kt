package com.leon.tecsupfit.ui.screens

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
        }
    }
}

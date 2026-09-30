package com.leon.tecsupfit.ui.screens

import com.leon.tecsupfit.data.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale

val espanol: Locale = Locale.forLanguageTag("es-PE")

fun fecha(ms: Long): LocalDate = Instant.ofEpochMilli(ms).atZone(DatosClases.zona).toLocalDate()

fun hora(ms: Long): String =
    DateTimeFormatter.ofPattern("HH:mm", espanol)
        .format(Instant.ofEpochMilli(ms).atZone(DatosClases.zona))

fun fechaLarga(ms: Long) =
    fecha(ms).format(DateTimeFormatter.ofPattern("EEEE d 'de' MMMM", espanol)).replaceFirstChar {
        it.titlecase(espanol)
    }

fun fechaBreve(ms: Long, ahora: Long) =
    when (fecha(ms)) {
        fecha(ahora) -> "Hoy"
        fecha(ahora).plusDays(1) -> "Mañana"
        else -> fecha(ms).format(DateTimeFormatter.ofPattern("EEE d MMM", espanol))
    }

fun cupos(c: ClaseFitness, reservas: List<Reserva>) =
    (c.cuposTotales -
            c.ocupadosIniciales -
            reservas.count { it.claseId == c.id && it.estado == "CONFIRMADA" })
        .coerceAtLeast(0)

fun unirReservas(reservas: List<Reserva>, clases: List<ClaseFitness>): List<ReservaConClase> {
    val mapa = clases.associateBy { it.id }
    return reservas.mapNotNull { r -> mapa[r.claseId]?.let { ReservaConClase(r, it) } }
}

fun cronometro(ms: Long): String {
    val s = (ms.coerceAtLeast(0) + 999) / 1000
    return "%02d:%02d".format(s / 60, s % 60)
}

package com.leon.tecsupfit.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.*
import com.leon.tecsupfit.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PantallaInicio(
    clases: List<ClaseFitness>,
    reservas: List<Reserva>,
    ahora: Long,
    foto: String?,
    onProfile: () -> Unit,
    abrir: (String) -> Unit,
) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var dia by rememberSaveable { mutableStateOf(fecha(ahora).toString()) }
    var filtro by rememberSaveable { mutableIntStateOf(0) }
    val hoy = fecha(ahora)
    LaunchedEffect(hoy) { if (LocalDate.parse(dia) < hoy) dia = hoy.toString() }
    Page(
        if (tab == 0) "Hoy, a tu ritmo."
        else if (tab == 1) "Elige tu momento" else "Elige una clase",
        if (tab == 0) fechaLarga(ahora).uppercase(espanol)
        else "Un espacio para moverte a tu manera.",
        foto,
        onProfile,
        home = tab == 0,
    ) {
        Segments(listOf("Hoy", "Semana", "Explorar"), tab) { tab = it }
        if (tab == 1) {
            Glass(Modifier.fillMaxWidth(), radius = 25.dp) {
                Row(
                    Modifier.horizontalScroll(rememberScrollState()).padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    repeat(14) { i ->
                        val d = hoy.plusDays(i.toLong())
                        val selected = d.toString() == dia
                        Column(
                            Modifier.width(58.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (selected) AzulProfundo else Color.Transparent)
                                .clickable { dia = d.toString() }
                                .padding(vertical = 13.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                d.format(DateTimeFormatter.ofPattern("EEE", espanol)).uppercase(),
                                fontSize = 10.sp,
                                color = if (selected) Color.White else TextoSecundario,
                            )
                            Text(
                                d.dayOfMonth.toString(),
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) Color.White else TintaMarina,
                            )
                        }
                    }
                }
            }
            Text(
                LocalDate.parse(dia)
                    .format(DateTimeFormatter.ofPattern("EEEE d", espanol))
                    .replaceFirstChar { it.titlecase(espanol) },
                style = MaterialTheme.typography.titleMedium,
            )
        }
        if (tab == 2) Segments(listOf("Todas", "Mañana", "Tarde"), filtro) { filtro = it }
        val visibles =
            clases
                .filter { c ->
                    c.inicio > ahora &&
                        when (tab) {
                            0 -> fecha(c.inicio) == hoy
                            1 -> fecha(c.inicio).toString() == dia
                            else ->
                                fecha(c.inicio) <= hoy.plusDays(13) &&
                                    when (filtro) {
                                        1 -> hora(c.inicio) < "12:00"
                                        2 -> hora(c.inicio) >= "12:00"
                                        else -> true
                                    }
                        }
                }
                .sortedBy { it.inicio }
        if (visibles.isEmpty()) {
            InfoCard(
                "Por hoy, todo listo",
                if (tab == 0)
                    "No quedan clases por comenzar hoy. Descubre los próximos horarios en Semana."
                else "No hay clases para este día o filtro.",
                true,
            )
            if (tab == 0)
                SecondaryButton("Ver los próximos días") {
                    tab = 1
                    dia = hoy.plusDays(1).toString()
                }
        } else {
            val hero =
                if (tab == 0)
                    visibles.firstOrNull {
                        cupos(it, reservas) > 0 ||
                            reservas.any { r -> r.claseId == it.id && r.estado == "CONFIRMADA" }
                    } ?: visibles.first()
                else null
            if (hero != null) {
                PhotoHero(
                    hero.nombre,
                    hero.imageKey,
                    "TU PRÓXIMA CLASE",
                    "${hora(hero.inicio)} · ${hero.sala} · ${hero.duracionMin} min",
                    onClick = { abrir(hero.id) },
                )
                SectionTitle("Después", "Ver todas") { tab = 2 }
            }
            val filas = if (tab == 0) visibles.filter { it.id != hero?.id }.take(2) else visibles
            filas.forEach { c ->
                val reservada = reservas.any { it.claseId == c.id && it.estado == "CONFIRMADA" }
                PhotoRow(
                    c.nombre,
                    "${if(tab==2) fechaBreve(c.inicio,ahora)+" · " else ""}${hora(c.inicio)} · ${c.sala}",
                    if (reservada) "Tu cupo está reservado"
                    else if (cupos(c, reservas) == 0) "Sin cupos" else "${cupos(c,reservas)} cupos",
                    c.imageKey,
                ) {
                    abrir(c.id)
                }
            }
        }
        if (tab != 0)
            InfoCard(
                "La agenda se actualiza al reservar.",
                "Horarios de Lima · Próximas dos semanas.",
            )
    }
}

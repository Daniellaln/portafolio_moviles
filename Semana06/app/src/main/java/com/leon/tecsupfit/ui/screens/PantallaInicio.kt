package com.leon.tecsupfit.ui.screens

<<<<<<< HEAD
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
=======
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import com.leon.tecsupfit.ui.components.IconoPesa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.tecsupfit.data.ClaseFitness
import com.leon.tecsupfit.data.DatosClases
import com.leon.tecsupfit.ui.components.TecsupFitBottomBar
import com.leon.tecsupfit.ui.theme.VerdeTecsup
import com.leon.tecsupfit.ui.theme.VerdeClaro

private val filtros = listOf("Hoy", "Esta semana")

@Composable
fun PantallaInicio(
    nombreUsuario: String,
    rutaActual: String,
    onNavegar: (String) -> Unit,
    onClaseSeleccionada: (ClaseFitness) -> Unit
) {
    var filtroSeleccionado by remember { mutableStateOf(filtros.first()) }

    Scaffold(
        bottomBar = { TecsupFitBottomBar(rutaActual = rutaActual, onNavegar = onNavegar) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Encabezado verde
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VerdeTecsup)
                    .padding(20.dp)
            ) {
                Text(
                    text = "TECSUP Fit",
                    color = Color.White,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hola, $nombreUsuario",
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            // Chips de filtro
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtros) { filtro ->
                    FilterChip(
                        selected = filtro == filtroSeleccionado,
                        onClick = { filtroSeleccionado = filtro },
                        label = { Text(filtro) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = VerdeTecsup,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Text(
                text = "Clases disponibles",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            // Lista de clases
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(DatosClases.clases) { clase ->
                    TarjetaClase(clase = clase, onClick = { onClaseSeleccionada(clase) })
                }
            }
        }
    }
}

@Composable
private fun TarjetaClase(clase: ClaseFitness, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(VerdeClaro, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                IconoPesa(
                    modifier = Modifier.size(22.dp),
                    color = VerdeTecsup
                )
            }
            Column {
                Text(text = clase.nombre, fontWeight = FontWeight.Bold)
                Text(
                    text = "${clase.hora} · ${clase.sala}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
>>>>>>> sinia
    }
}

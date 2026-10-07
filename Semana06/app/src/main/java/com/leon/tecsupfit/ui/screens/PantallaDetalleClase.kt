package com.leon.tecsupfit.ui.screens

<<<<<<< HEAD
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleClase(
    clase: ClaseFitness,
    reserva: Reserva?,
    reservas: List<Reserva>,
    ahora: Long,
    foto: String?,
    onProfile: () -> Unit,
    back: () -> Unit,
    busy: Boolean,
    reservar: () -> Unit,
    cancelar: () -> Unit,
    verReserva: () -> Unit,
    modoReserva: Boolean = false,
) {
    var hoja by rememberSaveable { mutableStateOf(false) }
    BackHandler(hoja) { hoja = false }
    val vigente = reserva?.estado == "CONFIRMADA"
    val pasada = clase.inicio <= ahora
    Page(
        if (modoReserva) "Mi reserva" else "Clase",
        foto = foto,
        onProfile = onProfile,
        onBack = { if (hoja) hoja = false else back() },
    ) {
        PhotoHero(
            clase.nombre,
            clase.imageKey,
            reserva?.let { ReservaConClase(it, clase).etiqueta(ahora) } ?: "MUÉVETE A TU RITMO",
            if (modoReserva)
                "${fechaBreve(clase.inicio,ahora)} · ${hora(clase.inicio)} – ${hora(clase.fin)}"
            else "${clase.duracionMin} minutos para ti",
            detail = true,
        )
        Text(fechaLarga(clase.inicio), style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.weight(1f)) {
                InfoCard("${hora(clase.inicio)} – ${hora(clase.fin)}", "Horario · Lima")
            }
            Box(Modifier.weight(1f)) { InfoCard(clase.sala, "${clase.duracionMin} minutos") }
        }
        InfoCard(
            if (vigente) "Tu cupo está reservado."
            else if (pasada) "Esta clase ya comenzó."
            else "${cupos(clase,reservas)} de ${clase.cuposTotales} cupos disponibles",
            aqua = true,
        )
        if (!modoReserva) Text(clase.descripcion, style = MaterialTheme.typography.bodyMedium)
        if (modoReserva) {
            if (vigente && !pasada) SecondaryButton("Cancelar reserva") { hoja = true }
            else SecondaryButton("Volver a mis reservas", back)
        } else if (vigente) PrimaryButton("Ver mi reserva", onClick = verReserva)
        else
            PrimaryButton(
                if (pasada) "Horario no disponible"
                else if (cupos(clase, reservas) == 0) "Sin cupos" else "Reservar mi cupo",
                enabled = !pasada && cupos(clase, reservas) > 0,
                busy = busy,
                onClick = reservar,
            )
    }
    if (hoja)
        ModalBottomSheet(
            onDismissRequest = { if (!busy) hoja = false },
            containerColor = Color.Transparent,
            dragHandle = null,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            Glass(Modifier.fillMaxWidth(), radius = 32.dp, opacity = .84f) {
                Column(
                    Modifier.padding(24.dp).navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    Text("¿Cancelar la reserva?", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "Liberaremos tu cupo de ${clase.nombre}. Puedes volver a reservar si todavía hay espacio."
                    )
                    PrimaryButton(
                        "Sí, cancelar",
                        busy = busy,
                        onClick = {
                            cancelar()
                            hoja = false
                        },
                    )
                    SecondaryButton("Conservar mi cupo") { hoja = false }
                }
            }
        }
=======
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
import androidx.compose.foundation.layout.width
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
import com.leon.tecsupfit.ui.components.IconoPesa
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
                IconoPesa(
                    modifier = Modifier
                        .height(70.dp)
                        .width(100.dp),
                    color = VerdeTecsup
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
>>>>>>> sinia
}

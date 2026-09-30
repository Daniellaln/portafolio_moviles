package com.leon.tecsupfit.ui.screens

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
}

package com.leon.tecsupfit.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leon.tecsupfit.data.ClaseFitness
import com.leon.tecsupfit.data.DatosClases
import com.leon.tecsupfit.data.EstadoReserva
import com.leon.tecsupfit.data.Reserva
import com.leon.tecsupfit.navigation.Pantallas
import com.leon.tecsupfit.ui.components.DurationFast
import com.leon.tecsupfit.ui.components.ImageUtils
import com.leon.tecsupfit.ui.components.LayoutBase
import com.leon.tecsupfit.ui.components.botonClickAnimado
import com.leon.tecsupfit.ui.theme.AzulProfundo
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina
import com.leon.tecsupfit.ui.theme.TextoSecundario

enum class ModoReservas { PROXIMAS, HISTORIAL }

@Composable
fun PantallaReservas(
    rutaActual: String,
    onNavegar: (String) -> Unit
) {
    var modoSeleccionado by rememberSaveable { mutableStateOf(ModoReservas.PROXIMAS) }

    LayoutBase(
        rutaActual = rutaActual,
        onNavegar = onNavegar
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Mis reservas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = TintaMarina,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp)
            )

            // Tabs con mayor área de toque y contraste
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
            ) {
                TabReservas(
                    text = "Próximas",
                    selected = modoSeleccionado == ModoReservas.PROXIMAS,
                    onClick = { modoSeleccionado = ModoReservas.PROXIMAS },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                TabReservas(
                    text = "Historial",
                    selected = modoSeleccionado == ModoReservas.HISTORIAL,
                    onClick = { modoSeleccionado = ModoReservas.HISTORIAL },
                    modifier = Modifier.weight(1f)
                )
            }

            AnimatedContent(
                targetState = modoSeleccionado,
                transitionSpec = {
                    fadeIn(animationSpec = tween(DurationFast)) togetherWith fadeOut(animationSpec = tween(DurationFast))
                },
                label = "CambioTabReservas"
            ) { modo ->
                when (modo) {
                    ModoReservas.PROXIMAS -> ListaProximas(
                        onExplorar = { onNavegar(Pantallas.INICIO) },
                        onClaseClick = { clase -> onNavegar(Pantallas.detalleRuta(clase.id)) }
                    )
                    ModoReservas.HISTORIAL -> ListaHistorial(
                        onClaseClick = { clase -> onNavegar(Pantallas.detalleRuta(clase.id)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TabReservas(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp) // Accesibilidad: >= 48dp
            .clip(RoundedCornerShape(24.dp))
            .background(if (selected) AzulProfundo else Color.White.copy(alpha = 0.3f))
            .border(0.5.dp, GlassBorder, RoundedCornerShape(24.dp))
            .botonClickAnimado(onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else TintaMarina,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ListaProximas(onExplorar: () -> Unit, onClaseClick: (ClaseFitness) -> Unit) {
    val proximas = DatosClases.reservas.filter { it.estado == EstadoReserva.CONFIRMADA }
    
    if (proximas.isEmpty()) {
        EmptyStateReservas(
            mensaje = "No tienes clases reservadas",
            onAction = onExplorar
        )
    } else {
        val protagonista = proximas.first()
        val resto = proximas.drop(1)

        LazyColumn(
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 120.dp), // Espacio para dock
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                ReservaProtagonista(reserva = protagonista, onClick = { onClaseClick(protagonista.clase) })
            }
            if (resto.isNotEmpty()) {
                item {
                    Text(
                        text = "Otras reservas", 
                        style = MaterialTheme.typography.titleMedium, 
                        fontWeight = FontWeight.Bold, 
                        color = TintaMarina,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                items(resto) { reserva ->
                    TarjetaReservaCompacta(reserva = reserva, onClick = { onClaseClick(reserva.clase) })
                }
            }
        }
    }
}

@Composable
private fun ListaHistorial(onClaseClick: (ClaseFitness) -> Unit) {
    val historial = DatosClases.reservas.filter { 
        it.estado != EstadoReserva.CONFIRMADA
    }.sortedByDescending { it.timestamp }

    if (historial.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Historial vacío", color = TextoSecundario, fontWeight = FontWeight.Medium)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(historial) { reserva ->
                TarjetaReservaCompacta(reserva = reserva, onClick = { onClaseClick(reserva.clase) })
            }
        }
    }
}

@Composable
private fun ReservaProtagonista(reserva: Reserva, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .shadow(16.dp, RoundedCornerShape(32.dp), ambientColor = Color.Black.copy(0.1f))
            .clip(RoundedCornerShape(32.dp))
            .botonClickAnimado(onClick)
    ) {
        Image(
            painter = painterResource(id = ImageUtils.getDrawableForNombre(reserva.clase.nombre, true, reserva.clase.imageKey)),
            contentDescription = "Clase ${reserva.clase.nombre}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Estado en vidrio dinámico
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.85f))
                .border(0.5.dp, GlassBorder, CircleShape)
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(text = "Confirmada", color = LuzAqua, fontWeight = FontWeight.Black, fontSize = 12.sp)
        }

        // Lámina informativa inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(20.dp)
        ) {
            Column {
                Text(text = reserva.clase.nombre, color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
                Text(text = "${reserva.fecha} · ${reserva.clase.sala}", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun TarjetaReservaCompacta(reserva: Reserva, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.55f))
            .border(0.5.dp, GlassBorder, RoundedCornerShape(24.dp))
            .botonClickAnimado(onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
        ) {
            Image(
                painter = painterResource(id = ImageUtils.getDrawableForNombre(reserva.clase.nombre, true, reserva.clase.imageKey)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = reserva.clase.nombre, fontWeight = FontWeight.Bold, color = TintaMarina, fontSize = 15.sp)
            Text(text = reserva.fecha, color = TextoSecundario, fontSize = 13.sp)
        }
        Text(
            text = when(reserva.estado) {
                EstadoReserva.CONFIRMADA -> "Activa"
                EstadoReserva.COMPLETADA -> "Hecha"
                EstadoReserva.CANCELADA -> "Cancelada"
                EstadoReserva.FINALIZADA -> "Pasada"
            },
            color = when(reserva.estado) {
                EstadoReserva.CONFIRMADA -> LuzAqua
                EstadoReserva.CANCELADA -> Color.Red.copy(0.8f)
                else -> TextoSecundario
            },
            fontWeight = FontWeight.Black,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun EmptyStateReservas(mensaje: String, onAction: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = mensaje, color = TintaMarina, fontWeight = FontWeight.Black, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAction,
            modifier = Modifier.height(56.dp).padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AzulProfundo),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Explorar clases disponibles", fontWeight = FontWeight.Bold)
        }
    }
}

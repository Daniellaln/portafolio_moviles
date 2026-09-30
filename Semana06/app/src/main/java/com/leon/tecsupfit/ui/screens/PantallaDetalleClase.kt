package com.leon.tecsupfit.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.leon.tecsupfit.ui.components.BotonVolver
import com.leon.tecsupfit.ui.components.ImageUtils
import com.leon.tecsupfit.ui.components.LayoutBase
import com.leon.tecsupfit.ui.components.botonClickAnimado
import com.leon.tecsupfit.ui.theme.AzulProfundo
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina
import com.leon.tecsupfit.ui.theme.TextoSecundario
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun PantallaDetalleClase(
    clase: ClaseFitness,
    onVolver: () -> Unit,
    onReservar: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var showConfirmationSheet by remember { mutableStateOf(false) }
    var showCancelSheet by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    
    val imageHeight by animateDpAsState(
        targetValue = if (expanded) 320.dp else 240.dp,
        animationSpec = tween(durationMillis = 600),
        label = "ExpansionFoto"
    )

    LaunchedEffect(Unit) {
        expanded = true
    }

    val reservaActiva = DatosClases.obtenerReservaPorClase(clase.id)
    val estaReservada = reservaActiva != null
    val esLlena = clase.cuposDisponibles <= 0
    
    val calendar = Calendar.getInstance()
    val diaActual = calendar.get(Calendar.DAY_OF_WEEK).let { if (it == 1) 7 else it - 1 }
    val horaActualMinutos = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    
    val partesHora = clase.hora.split(":")
    val horaClaseMinutos = if (partesHora.size == 2) {
        partesHora[0].toInt() * 60 + partesHora[1].toInt()
    } else 0
    
    val esPasada = if (clase.diaSemana < diaActual) true 
                   else if (clase.diaSemana == diaActual) horaClaseMinutos < horaActualMinutos 
                   else false

    LayoutBase {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                ) {
                    Image(
                        painter = painterResource(id = ImageUtils.getDrawableForNombre(clase.nombre, true, clase.imageKey)),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    BotonVolver(onVolver = onVolver, modifier = Modifier.padding(24.dp))
                }

                Column(modifier = Modifier.padding(24.dp)) {
                    val dias = listOf("", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
                    Text(
                        text = dias.getOrElse(clase.diaSemana) { "" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = LuzAqua,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = clase.nombre,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = TintaMarina
                    )
                    
                    Row(modifier = Modifier.padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        InfoCapsule(text = clase.hora)
                        Spacer(modifier = Modifier.size(8.dp))
                        InfoCapsule(text = "${clase.duracionMin} min")
                        Spacer(modifier = Modifier.size(8.dp))
                        InfoCapsule(text = clase.sala)
                    }

                    Text(text = "Sobre esta clase", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TintaMarina, modifier = Modifier.padding(top = 32.dp))
                    Text(text = clase.descripcion, style = MaterialTheme.typography.bodyLarge, color = TextoSecundario, modifier = Modifier.padding(top = 8.dp), lineHeight = 24.sp)

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = "${clase.cuposDisponibles} cupos disponibles de ${clase.cuposTotales}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = if (esLlena) Color.Red else AzulProfundo)
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }

            // CTA Flotante
            Box(modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp).fillMaxWidth()) {
                val buttonText = when {
                    estaReservada -> "Cancelar reserva"
                    esPasada -> "Clase finalizada"
                    esLlena -> "Clase llena"
                    else -> if (isSaving) "Reservando..." else "Reservar cupo"
                }
                
                val enabled = !isSaving && !esPasada && (!esLlena || estaReservada)

                Button(
                    onClick = {
                        if (estaReservada) {
                            showCancelSheet = true
                        } else {
                            isSaving = true
                            scope.launch {
                                delay(1000) // Simular red
                                val exito = DatosClases.reservarClase(clase.id)
                                isSaving = false
                                if (exito) {
                                    showConfirmationSheet = true
                                    delay(2000)
                                    showConfirmationSheet = false
                                    onReservar()
                                }
                            }
                        }
                    },
                    enabled = enabled,
                    modifier = Modifier.fillMaxWidth().height(60.dp).botonClickAnimado(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (estaReservada) Color.White.copy(0.8f) else AzulProfundo,
                        contentColor = if (estaReservada) Color.Red else Color.White,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    border = if (estaReservada) androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(0.3f)) else null
                ) {
                    Text(text = buttonText, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                }
            }

            // Hoja de cancelación (Mockup 10)
            AnimatedVisibility(
                visible = showCancelSheet,
                enter = slideInVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)) { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.4f))
                        .clickable { showCancelSheet = false },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                            .background(Color.White)
                            .padding(24.dp)
                            .clickable(enabled = false) {}
                    ) {
                        Text(text = "¿Cancelar reserva?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = TintaMarina)
                        Text(text = "Podrás recuperar tu cupo más tarde si aún hay disponibilidad.", style = MaterialTheme.typography.bodyMedium, color = TextoSecundario, modifier = Modifier.padding(top = 8.dp))
                        
                        Button(
                            onClick = {
                                reservaActiva?.let { DatosClases.cancelarReserva(it.id) }
                                showCancelSheet = false
                                onVolver()
                            },
                            modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(0.1f), contentColor = Color.Red),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Sí, cancelar reserva", fontWeight = FontWeight.Bold)
                        }
                        
                        Button(
                            onClick = { showCancelSheet = false },
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = TintaMarina),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Conservar mi cupo")
                        }
                    }
                }
            }

            // Feedback de confirmación (Check de luz)
            AnimatedVisibility(
                visible = showConfirmationSheet,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(0.9f)), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier.size(100.dp).clip(CircleShape).background(LuzAqua.copy(0.2f)).border(2.dp, LuzAqua, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = LuzAqua, modifier = Modifier.size(60.dp))
                        }
                        Text(text = "¡Reservado!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = TintaMarina, modifier = Modifier.padding(top = 24.dp))
                        Text(text = "${clase.nombre} confirmada", color = TextoSecundario)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCapsule(text: String) {
    Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.5f)).border(0.5.dp, GlassBorder, RoundedCornerShape(12.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = TintaMarina)
    }
}

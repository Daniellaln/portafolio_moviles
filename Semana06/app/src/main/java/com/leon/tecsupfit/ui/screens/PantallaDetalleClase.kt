package com.leon.tecsupfit.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.leon.tecsupfit.ui.components.DurationScreen
import com.leon.tecsupfit.ui.components.DurationSheet
import com.leon.tecsupfit.ui.components.ImageUtils
import com.leon.tecsupfit.ui.components.LayoutBase
import com.leon.tecsupfit.ui.components.botonClickAnimado
import com.leon.tecsupfit.ui.components.springLowBouncy
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
    var showCancelSheet by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    
    // Foto expandiéndose al abrir (220-320ms)
    val imageHeight by animateDpAsState(
        targetValue = if (expanded) 340.dp else 260.dp,
        animationSpec = tween(durationMillis = DurationScreen),
        label = "ExpansionFoto"
    )

    LaunchedEffect(Unit) {
        expanded = true
    }

    val reservaActiva = DatosClases.obtenerReservaPorClase(clase.id)
    val estaReservada = reservaActiva != null
    val esLlena = clase.cuposDisponibles <= 0
    
    // BackHandler para cerrar la hoja antes de volver
    BackHandler(enabled = showCancelSheet) {
        showCancelSheet = false
    }

    LayoutBase {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .clip(RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp))
                ) {
                    Image(
                        painter = painterResource(id = ImageUtils.getDrawableForNombre(clase.nombre, true, clase.imageKey)),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    BotonVolver(
                        onVolver = {
                            if (showCancelSheet) showCancelSheet = false else onVolver()
                        },
                        modifier = Modifier.padding(24.dp)
                    )
                }

                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = clase.nombre,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = TintaMarina
                    )
                    
                    Row(
                        modifier = Modifier.padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoCapsule(text = clase.hora)
                        Spacer(modifier = Modifier.size(8.dp))
                        InfoCapsule(text = clase.sala)
                    }

                    Text(
                        text = clase.descripcion,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextoSecundario,
                        modifier = Modifier.padding(top = 24.dp),
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "${clase.cuposDisponibles} de ${clase.cuposTotales} cupos",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = if (esLlena) Color.Red else AzulProfundo
                    )
                    Spacer(modifier = Modifier.height(140.dp))
                }
            }

            // CTA Flotante
            Box(modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp).fillMaxWidth()) {
                val buttonText = when {
                    estaReservada -> "Gestionar reserva"
                    esLlena -> "Clase llena"
                    else -> if (isSaving) "Procesando..." else "Reservar cupo"
                }
                
                Button(
                    onClick = {
                        if (estaReservada) {
                            showCancelSheet = true
                        } else {
                            isSaving = true
                            scope.launch {
                                delay(600)
                                if (DatosClases.reservarClase(clase.id)) onReservar()
                                isSaving = false
                            }
                        }
                    },
                    enabled = !isSaving && (!esLlena || estaReservada),
                    modifier = Modifier.fillMaxWidth().height(64.dp).botonClickAnimado(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (estaReservada) Color.White.copy(0.9f) else AzulProfundo,
                        contentColor = if (estaReservada) TintaMarina else Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    border = if (estaReservada) borderStroke(1.dp, GlassBorder) else null
                ) {
                    Text(text = buttonText, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                }
            }

            // Hoja flotante de cancelación (260-360ms)
            AnimatedVisibility(
                visible = showCancelSheet,
                enter = slideInVertically(animationSpec = springLowBouncy()) { it } + fadeIn(),
                exit = slideOutVertically(animationSpec = tween(DurationSheet)) { it } + fadeOut()
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
                            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                            .background(Color.White)
                            .padding(32.dp)
                            .clickable(enabled = false) { }
                    ) {
                        Text(text = "¿Cancelar cupo?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = {
                                DatosClases.obtenerReservaPorClase(clase.id)?.let { DatosClases.cancelarReserva(it.id) }
                                showCancelSheet = false
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp).botonClickAnimado(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(0.1f), contentColor = Color.Red),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Sí, cancelar reserva", fontWeight = FontWeight.Bold)
                        }
                        
                        Button(
                            onClick = { showCancelSheet = false },
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = TintaMarina),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Mantener mi lugar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCapsule(text: String) {
    Box(modifier = Modifier.clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.6f)).border(0.5.dp, GlassBorder, RoundedCornerShape(14.dp)).padding(horizontal = 14.dp, vertical = 8.dp)) {
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = TintaMarina, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)

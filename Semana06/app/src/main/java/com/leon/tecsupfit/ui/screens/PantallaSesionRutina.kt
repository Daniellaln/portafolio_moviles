package com.leon.tecsupfit.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leon.tecsupfit.data.DatosRutinas
import com.leon.tecsupfit.data.Rutina
import com.leon.tecsupfit.ui.components.LayoutBase
import com.leon.tecsupfit.ui.components.botonClickAnimado
import com.leon.tecsupfit.ui.theme.AzulProfundo
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina
import com.leon.tecsupfit.ui.theme.TextoSecundario
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun PantallaSesionRutina(
    rutina: Rutina,
    onFinalizar: () -> Unit,
    onAbandonar: () -> Unit
) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var currentLap by rememberSaveable { mutableIntStateOf(1) }
    var isTransition by rememberSaveable { mutableStateOf(false) }
    var secondsRemaining by rememberSaveable { mutableIntStateOf(rutina.ejercicios[0].segundosActivos) }
    var isPaused by rememberSaveable { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    val ejercicioActual = rutina.ejercicios[currentIndex]
    val totalEjercicios = rutina.ejercicios.size
    
    // Animar el progreso circular aqua suavemente
    val targetProgress = if (isTransition) {
        secondsRemaining.toFloat() / ejercicioActual.segundosTransicion
    } else {
        secondsRemaining.toFloat() / ejercicioActual.segundosActivos
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000, easing = androidx.compose.animation.core.LinearEasing),
        label = "AquaRingProgress"
    )

    LaunchedEffect(isPaused, secondsRemaining, isTransition) {
        if (!isPaused && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining -= 1
        } else if (secondsRemaining == 0) {
            if (!isTransition) {
                isTransition = true
                secondsRemaining = ejercicioActual.segundosTransicion
            } else {
                isTransition = false
                if (currentIndex < totalEjercicios - 1) {
                    currentIndex += 1
                    secondsRemaining = rutina.ejercicios[currentIndex].segundosActivos
                } else if (currentLap < rutina.vueltas) {
                    currentLap += 1
                    currentIndex = 0
                    secondsRemaining = rutina.ejercicios[0].segundosActivos
                } else {
                    DatosRutinas.registrarSesion(rutina)
                    onFinalizar()
                }
            }
        }
    }

    BackHandler {
        showExitDialog = true
    }

    LayoutBase {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TintaMarina)
                    }
                    Text(
                        text = "Lap $currentLap / ${rutina.vueltas}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoSecundario
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = if (isTransition) "Transición" else ejercicioActual.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = TintaMarina,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(60.dp))

                // Anillo Aqua dinámico
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(280.dp),
                        color = LuzAqua,
                        strokeWidth = 10.dp,
                        trackColor = Color.White.copy(0.2f),
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = formatTime(secondsRemaining),
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Black,
                            color = TintaMarina
                        )
                        Text(
                            text = if (isTransition) "Prepárate" else "Sigue así",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario
                        )
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))

                if (!isTransition) {
                    val nextEj = if (currentIndex < totalEjercicios - 1) rutina.ejercicios[currentIndex + 1] else if (currentLap < rutina.vueltas) rutina.ejercicios[0] else null
                    if (nextEj != null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Siguiente:", style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                            Text(text = nextEj.nombre, fontWeight = FontWeight.Bold, color = TintaMarina)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Controles circulares flotantes
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .shadow(12.dp, CircleShape, ambientColor = AzulProfundo.copy(0.1f))
                            .clip(CircleShape)
                            .background(if (isPaused) LuzAqua else Color.White.copy(0.6f))
                            .border(1.dp, GlassBorder, CircleShape)
                            .botonClickAnimado { isPaused = !isPaused },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = null,
                            tint = if (isPaused) Color.White else AzulProfundo,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(32.dp))
                    
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .shadow(8.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color.White.copy(0.4f))
                            .border(1.dp, GlassBorder, CircleShape)
                            .botonClickAnimado { 
                                secondsRemaining = 0
                                isPaused = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Saltar",
                            tint = TintaMarina,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("¿Deseas salir?") },
            text = { Text("El progreso de esta sesión no se guardará.") },
            confirmButton = {
                TextButton(onClick = onAbandonar) {
                    Text("Abandonar", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Continuar", color = AzulProfundo, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = Color.White
        )
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", mins, secs)
}

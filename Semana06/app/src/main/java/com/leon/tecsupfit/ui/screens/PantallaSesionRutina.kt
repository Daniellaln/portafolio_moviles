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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leon.tecsupfit.data.DatosRutinas
import com.leon.tecsupfit.data.Rutina
import com.leon.tecsupfit.ui.components.LayoutBase
import com.leon.tecsupfit.ui.theme.AzulProfundo
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina
import com.leon.tecsupfit.ui.theme.TextoSecundario
import kotlinx.coroutines.delay

@Composable
fun PantallaSesionRutina(
    rutina: Rutina,
    onFinalizar: () -> Unit,
    onAbandonar: () -> Unit
) {
    // Estado persistente para sobrevivir a recreaciones
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var currentLap by rememberSaveable { mutableIntStateOf(1) }
    var isTransition by rememberSaveable { mutableStateOf(false) }
    var secondsRemaining by rememberSaveable { mutableIntStateOf(rutina.ejercicios[0].segundosActivos) }
    var isPaused by rememberSaveable { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    val ejercicioActual = rutina.ejercicios[currentIndex]
    val totalEjercicios = rutina.ejercicios.size
    
    // Timer Effect
    LaunchedEffect(isPaused, secondsRemaining, isTransition) {
        if (!isPaused && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining -= 1
        } else if (secondsRemaining == 0) {
            if (!isTransition) {
                // Termina ejercicio, empieza transición
                isTransition = true
                secondsRemaining = ejercicioActual.segundosTransicion
            } else {
                // Termina transición, siguiente ejercicio
                isTransition = false
                if (currentIndex < totalEjercicios - 1) {
                    currentIndex += 1
                    secondsRemaining = rutina.ejercicios[currentIndex].segundosActivos
                } else if (currentLap < rutina.vueltas) {
                    // Siguiente vuelta
                    currentLap += 1
                    currentIndex = 0
                    secondsRemaining = rutina.ejercicios[0].segundosActivos
                } else {
                    // Rutina completada
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
                // Header con salida
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(Icons.Default.Close, contentDescription = "Salir", tint = TintaMarina)
                    }
                    Text(
                        text = "Vuelta $currentLap de ${rutina.vueltas}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoSecundario
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Nombre del ejercicio
                Text(
                    text = if (isTransition) "Prepárate" else ejercicioActual.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = TintaMarina
                )
                
                Spacer(modifier = Modifier.height(60.dp))

                // Progreso Circular Aqua
                Box(contentAlignment = Alignment.Center) {
                    val progressMax = if (isTransition) ejercicioActual.segundosTransicion else ejercicioActual.segundosActivos
                    val progress = secondsRemaining.toFloat() / progressMax
                    
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(240.dp),
                        color = LuzAqua,
                        strokeWidth = 12.dp,
                        trackColor = Color.White.copy(0.3f),
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = formatTime(secondsRemaining),
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Black,
                            color = TintaMarina
                        )
                        Text(
                            text = if (isTransition) "Transición" else "Segundos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario
                        )
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))

                // Próxima acción
                if (!isTransition && (currentIndex < totalEjercicios - 1 || currentLap < rutina.vueltas)) {
                    val nextEj = if (currentIndex < totalEjercicios - 1) rutina.ejercicios[currentIndex + 1] else rutina.ejercicios[0]
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Siguiente:", style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
                        Text(text = nextEj.nombre, fontWeight = FontWeight.Bold, color = TintaMarina)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Controles grandes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón Pausar/Continuar
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(if (isPaused) LuzAqua else Color.White.copy(0.5f))
                            .border(1.dp, GlassBorder, CircleShape)
                            .clickable { isPaused = !isPaused },
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
                    
                    // Botón Saltar
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(0.3f))
                            .border(1.dp, GlassBorder, CircleShape)
                            .clickable { 
                                secondsRemaining = 0
                                isPaused = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Saltar",
                            tint = AzulProfundo,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("¿Abandonar sesión?") },
            text = { Text("Tu progreso actual no se guardará en el historial si abandonas ahora.") },
            confirmButton = {
                TextButton(onClick = onAbandonar) {
                    Text("Abandonar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Continuar", color = AzulProfundo)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}

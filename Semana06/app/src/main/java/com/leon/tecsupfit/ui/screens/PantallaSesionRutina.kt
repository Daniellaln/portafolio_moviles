package com.leon.tecsupfit.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.*
import com.leon.tecsupfit.R
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.*
import com.leon.tecsupfit.ui.theme.*

@Composable
fun PantallaSesionRutina(
    sesion: SesionRutina,
    ahora: Long,
    foto: String?,
    onProfile: () -> Unit,
    busy: Boolean,
    accion: (String) -> Unit,
    guardarSalir: () -> Unit,
    abandonar: () -> Unit,
    completada: () -> Unit,
) {
    var salir by rememberSaveable { mutableStateOf(false) }
    val actual = MotorRutina.actualizar(sesion, ahora)
    val fase = MotorRutina.fase(actual)
    LaunchedEffect(actual.estado, busy) { if (actual.estado == "COMPLETADA" && !busy) completada() }
    BackHandler {
        accion("pausa")
        salir = true
    }
    Page(
        "Sesión en curso",
        foto = foto,
        onProfile = {
            accion("pausa")
            onProfile()
        },
        onBack = {
            accion("pausa")
            salir = true
        },
    ) {
        if (fase != null) {
            Text(
                "VUELTA ${fase.vuelta+1} DE ${sesion.vueltas} · MOVIMIENTO ${fase.indice+1} DE ${sesion.ejercicios.size}",
                fontSize = 11.sp,
                color = TextoSecundario,
            )
            Glass(Modifier.fillMaxWidth(), radius = 29.dp, elevation = 14.dp, opacity = .62f) {
                Column(
                    Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        if (fase.descanso) "Toma un respiro" else fase.ejercicio.nombre,
                        Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        if (fase.descanso) "Suelta la tensión y prepara el siguiente movimiento."
                        else
                            fase.ejercicio.instruccion.ifBlank {
                                "Muévete con control y a tu ritmo."
                            },
                        Modifier.fillMaxWidth(),
                        color = TextoSecundario,
                        fontSize = 14.sp,
                    )
                    val restante =
                        (fase.inicioMs + fase.duracionMs - actual.progresoMs).coerceAtLeast(0)
                    val progreso = (1f - restante.toFloat() / fase.duracionMs).coerceIn(0f, 1f)
                    Box(
                        Modifier.fillMaxWidth().aspectRatio(1f).semantics {
                            contentDescription =
                                "${if(fase.descanso) "Descanso" else fase.ejercicio.nombre}: ${restante/1000} segundos restantes"
                        },
                        contentAlignment = Alignment.Center,
                    ) {
                        Canvas(Modifier.fillMaxSize().padding(13.dp)) {
                            drawArc(
                                Color(0xFFDCE7F0),
                                -90f,
                                360f,
                                false,
                                style = Stroke(10.dp.toPx(), cap = StrokeCap.Round),
                            )
                            drawArc(
                                LuzAqua,
                                -90f,
                                360f * progreso,
                                false,
                                style = Stroke(7.dp.toPx(), cap = StrokeCap.Round),
                            )
                            drawArc(
                                Color.White.copy(alpha = .8f),
                                -90f,
                                360f * progreso,
                                false,
                                style = Stroke(1.5.dp.toPx(), cap = StrokeCap.Round),
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Text(
                                cronometro(restante),
                                fontSize = 42.sp,
                                style = MaterialTheme.typography.headlineLarge,
                            )
                            Text(
                                if (!sesion.corriendo) "En pausa"
                                else "de ${fase.duracionMs/1000} s",
                                color = TextoSecundario,
                                fontSize = 13.sp,
                            )
                        }
                    }
                    val next =
                        MotorRutina.fases(actual).firstOrNull {
                            it.inicioMs >= fase.inicioMs + fase.duracionMs
                        }
                    Text(
                        next?.let {
                            "Sigue: ${if(it.descanso) "descanso" else it.ejercicio.nombre}"
                        } ?: "Último movimiento. ¡Ya casi!",
                        fontSize = 13.sp,
                        color = TextoSecundario,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(22.dp)) {
                        CircleControl(
                            if (sesion.corriendo) R.drawable.icon_pause else R.drawable.icon_play,
                            if (sesion.corriendo) "Pausar" else "Continuar",
                        ) {
                            accion(if (sesion.corriendo) "pausa" else "continuar")
                        }
                        CircleControl(R.drawable.icon_arrow, "Saltar al siguiente movimiento") {
                            accion("saltar")
                        }
                    }
                }
            }
        }
        SecondaryButton("Guardar y salir", guardarSalir)
        TextButton(
            onClick = {
                accion("pausa")
                salir = true
            }
        ) {
            Text("Terminar sesión")
        }
    }
    if (salir)
        AlertDialog(
            onDismissRequest = { salir = false },
            title = { Text("Tu sesión está en pausa") },
            text = {
                Text(
                    "Puedes guardarla para continuar después o abandonarla sin marcarla como completada."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        salir = false
                        guardarSalir()
                    }
                ) {
                    Text("Guardar y salir")
                }
            },
            dismissButton = {
                Column {
                    TextButton(
                        onClick = {
                            salir = false
                            abandonar()
                        }
                    ) {
                        Text("Abandonar sesión")
                    }
                    TextButton(onClick = { salir = false }) { Text("Seguir aquí") }
                }
            },
        )
}

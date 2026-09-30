package com.leon.tecsupfit.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.*
import com.leon.tecsupfit.ui.theme.*

@Composable
fun SuccessMark() {
    val reduced = LocalMovimientoReducido.current
    val progress = remember { Animatable(if (reduced) 1f else 0f) }
    LaunchedEffect(Unit) { if (!reduced) progress.animateTo(1f, tween(480)) }
    Glass(
        Modifier.size(116.dp).semantics { contentDescription = "Completado" },
        radius = 60.dp,
        elevation = 20.dp,
    ) {
        Canvas(Modifier.fillMaxSize().padding(31.dp)) {
            drawCircle(LuzAqua.copy(alpha = .2f), radius = size.minDimension * .65f)
            val p =
                Path().apply {
                    moveTo(size.width * .12f, size.height * .52f)
                    lineTo(size.width * .4f, size.height * .78f)
                    lineTo(size.width * .91f, size.height * .18f)
                }
            val measure = PathMeasure()
            measure.setPath(p, false)
            val shown = Path()
            measure.getSegment(0f, measure.length * progress.value, shown, true)
            drawPath(
                shown,
                Color(0xFF147D90),
                style = Stroke(3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
        }
    }
}

@Composable
fun PantallaConfirmacion(
    clase: ClaseFitness,
    foto: String?,
    onProfile: () -> Unit,
    ver: () -> Unit,
    inicio: () -> Unit,
) {
    Page("Tu reserva", foto = foto, onProfile = onProfile, onBack = inicio) {
        Spacer(Modifier.height(24.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { SuccessMark() }
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            Text("Reserva confirmada", style = MaterialTheme.typography.headlineMedium)
            Text("Tu lugar ya está listo.", color = TextoSecundario)
        }
        Glass(Modifier.fillMaxWidth(), radius = 30.dp, elevation = 14.dp) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    clase.nombre.uppercase(),
                    color = Color(0xFF216B7D),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(fechaLarga(clase.inicio), style = MaterialTheme.typography.titleLarge)
                HorizontalDivider(color = TextoSecundario.copy(alpha = .2f))
                Text("${hora(clase.inicio)} – ${hora(clase.fin)}", fontWeight = FontWeight.SemiBold)
                Text(clase.sala, color = TextoSecundario)
            }
        }
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Ver mi reserva", onClick = ver)
        SecondaryButton("Volver al inicio", inicio)
    }
}

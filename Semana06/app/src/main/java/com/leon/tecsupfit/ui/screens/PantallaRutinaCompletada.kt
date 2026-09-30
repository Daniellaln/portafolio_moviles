package com.leon.tecsupfit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import com.leon.tecsupfit.data.SesionRutina
import com.leon.tecsupfit.ui.components.*
import com.leon.tecsupfit.ui.theme.TextoSecundario

@Composable
fun PantallaRutinaCompletada(
    s: SesionRutina,
    foto: String?,
    onProfile: () -> Unit,
    rutinas: () -> Unit,
    inicio: () -> Unit,
) {
    Page("Sesión terminada", foto = foto, onProfile = onProfile, onBack = rutinas) {
        Spacer(Modifier.height(25.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { SuccessMark() }
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("¡Rutina completada!", style = MaterialTheme.typography.headlineMedium)
            Text("Bien hecho, Daniella.", color = TextoSecundario)
        }
        InfoCard(
            s.nombre,
            "${cronometro(s.realizadoMs)} de sesión · ${s.vueltas} vueltas\nTu actividad quedó guardada en este dispositivo.",
            true,
        )
        Spacer(Modifier.height(20.dp))
        PrimaryButton("Ver más rutinas", onClick = rutinas)
        SecondaryButton("Volver al inicio", inicio)
    }
}

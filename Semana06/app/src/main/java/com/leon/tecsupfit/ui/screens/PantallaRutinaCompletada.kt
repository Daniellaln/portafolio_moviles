package com.leon.tecsupfit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.tecsupfit.data.Rutina
import com.leon.tecsupfit.ui.components.LayoutBase
import com.leon.tecsupfit.ui.theme.AzulProfundo
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina
import com.leon.tecsupfit.ui.theme.TextoSecundario

@Composable
fun PantallaRutinaCompletada(
    rutina: Rutina,
    onVolver: () -> Unit
) {
    LayoutBase {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 80.dp)
                    .size(120.dp)
                    .shadow(16.dp, CircleShape, ambientColor = LuzAqua.copy(alpha = 0.4f))
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, GlassBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = LuzAqua,
                    modifier = Modifier.size(60.dp)
                )
            }

            Text(
                text = "¡Rutina finalizada!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = TintaMarina,
                modifier = Modifier.padding(top = 32.dp)
            )
            
            Text(
                text = "Has completado ${rutina.nombre}",
                style = MaterialTheme.typography.bodyLarge,
                color = TextoSecundario,
                modifier = Modifier.padding(top = 8.dp)
            )

            Column(
                modifier = Modifier.padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ResumenDato(valor = "${rutina.duracionTotalMin} min", etiqueta = "Tiempo total")
                SpacerPadding()
                ResumenDato(valor = "${rutina.ejercicios.size}", etiqueta = "Ejercicios")
            }

            Box(modifier = Modifier.weight(1f))

            Button(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulProfundo),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "Volver a rutinas",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ResumenDato(valor: String, etiqueta: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = AzulProfundo)
        Text(text = etiqueta, style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
    }
}

@Composable
private fun SpacerPadding() {
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(24.dp))
}

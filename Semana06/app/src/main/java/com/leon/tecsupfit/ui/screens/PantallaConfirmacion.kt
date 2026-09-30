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
import androidx.compose.material.icons.filled.Check
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
import com.leon.tecsupfit.data.ClaseFitness
import com.leon.tecsupfit.ui.components.LayoutBase
import com.leon.tecsupfit.ui.theme.AzulProfundo
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina
import com.leon.tecsupfit.ui.theme.TextoSecundario

@Composable
fun PantallaConfirmacion(
    clase: ClaseFitness,
    onVerReservas: () -> Unit
) {
    LayoutBase {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono de check con estilo Glass/Clay
            Box(
                modifier = Modifier
                    .padding(top = 80.dp)
                    .size(100.dp)
                    .shadow(16.dp, CircleShape, ambientColor = LuzAqua.copy(alpha = 0.4f))
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, GlassBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = LuzAqua,
                    modifier = Modifier.size(48.dp)
                )
            }

            Text(
                text = "¡Cupo reservado!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = TintaMarina,
                modifier = Modifier.padding(top = 32.dp)
            )
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = clase.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TintaMarina,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hoy, ${clase.hora} · ${clase.sala}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }

            Box(modifier = Modifier.weight(1f))

            Button(
                onClick = onVerReservas,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.6f),
                    contentColor = TintaMarina
                ),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, GlassBorder)
            ) {
                Text(
                    "Ver mis reservas",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

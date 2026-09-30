package com.leon.tecsupfit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.leon.tecsupfit.data.Rutina
import com.leon.tecsupfit.ui.components.BotonVolver
import com.leon.tecsupfit.ui.components.ImageUtils
import com.leon.tecsupfit.ui.components.LayoutBase
import com.leon.tecsupfit.ui.components.botonClickAnimado
import com.leon.tecsupfit.ui.theme.AzulProfundo
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina
import com.leon.tecsupfit.ui.theme.TextoSecundario

@Composable
fun PantallaDetalleRutina(
    rutina: Rutina,
    onVolver: () -> Unit,
    onEmpezar: () -> Unit
) {
    LayoutBase {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen de cabecera
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                ) {
                    Image(
                        painter = painterResource(id = ImageUtils.getDrawableForNombre(rutina.nombre, false, rutina.imageKey)),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    BotonVolver(onVolver = onVolver, modifier = Modifier.padding(24.dp))
                }

                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Rutina",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LuzAqua,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = rutina.nombre,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = TintaMarina
                    )

                    Row(
                        modifier = Modifier.padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoCapsule(text = "${rutina.duracionTotalMin} min")
                        Spacer(modifier = Modifier.size(8.dp))
                        InfoCapsule(text = rutina.equipo)
                        Spacer(modifier = Modifier.size(8.dp))
                        InfoCapsule(text = "${rutina.ejercicios.size} ejercicios")
                    }

                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TintaMarina,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    Text(
                        text = rutina.descripcion,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextoSecundario,
                        modifier = Modifier.padding(top = 8.dp),
                        lineHeight = 24.sp
                    )

                    Text(
                        text = "Secuencia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TintaMarina,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    
                    rutina.ejercicios.forEachIndexed { index, ejercicio ->
                        FilaEjercicio(index + 1, ejercicio)
                        if (index < rutina.ejercicios.size - 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(120.dp))
                }
            }

            // CTA Empezar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = onEmpezar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .botonClickAnimado(),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulProfundo),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Empezar sesión",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun FilaEjercicio(num: Int, ejercicio: com.leon.tecsupfit.data.Ejercicio) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(0.4f))
            .border(0.5.dp, GlassBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(LuzAqua.copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = num.toString(), color = LuzAqua, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = ejercicio.nombre, fontWeight = FontWeight.Bold, color = TintaMarina)
            Text(text = "${ejercicio.segundosActivos} seg", style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
        }
    }
}

@Composable
private fun InfoCapsule(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.5f))
            .border(0.5.dp, GlassBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = TintaMarina)
    }
}

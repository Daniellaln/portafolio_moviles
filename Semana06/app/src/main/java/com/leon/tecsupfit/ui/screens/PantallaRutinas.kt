package com.leon.tecsupfit.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.leon.tecsupfit.data.DatosRutinas
import com.leon.tecsupfit.data.Intensidad
import com.leon.tecsupfit.data.Rutina
import com.leon.tecsupfit.ui.components.DurationFast
import com.leon.tecsupfit.ui.components.ImageUtils
import com.leon.tecsupfit.ui.components.LayoutBase
import com.leon.tecsupfit.ui.components.botonClickAnimado
import com.leon.tecsupfit.ui.theme.AzulProfundo
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina
import com.leon.tecsupfit.ui.theme.TextoSecundario

@Composable
fun PantallaRutinas(
    rutaActual: String,
    onNavegar: (String) -> Unit,
    onRutinaSeleccionada: (Rutina) -> Unit
) {
    var filtroDuracion by rememberSaveable { mutableStateOf<Int?>(null) }
    var filtroIntensidad by rememberSaveable { mutableStateOf<Intensidad?>(null) }

    val rutinaEnCurso = DatosRutinas.rutinaEnCursoId?.let { DatosRutinas.obtenerRutinaPorId(it) }
    
    val protagonista = rutinaEnCurso ?: DatosRutinas.rutinas.find { 
        it.intensidad == DatosRutinas.ultimaPreferenciaIntensidad 
    } ?: DatosRutinas.rutinas.first()

    val rutinasFiltradas = DatosRutinas.rutinas.filter { rutina ->
        val matchDuracion = filtroDuracion == null || 
            (filtroDuracion == 10 && rutina.duracionTotalMin <= 10) ||
            (filtroDuracion == 15 && rutina.duracionTotalMin in 11..15) ||
            (filtroDuracion == 20 && rutina.duracionTotalMin > 15)
        
        val matchIntensidad = filtroIntensidad == null || rutina.intensidad == filtroIntensidad
        
        matchDuracion && matchIntensidad
    }

    LayoutBase(
        rutaActual = rutaActual,
        onNavegar = onNavegar
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Rutinas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = TintaMarina,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item { FiltroChip("≤ 10 min", filtroDuracion == 10) { filtroDuracion = if (filtroDuracion == 10) null else 10 } }
                item { FiltroChip("11-15 min", filtroDuracion == 15) { filtroDuracion = if (filtroDuracion == 15) null else 15 } }
                item { FiltroChip("Baja", filtroIntensidad == Intensidad.BAJA) { filtroIntensidad = if (filtroIntensidad == Intensidad.BAJA) null else Intensidad.BAJA } }
                item { FiltroChip("Media", filtroIntensidad == Intensidad.MEDIA) { filtroIntensidad = if (filtroIntensidad == Intensidad.MEDIA) null else Intensidad.MEDIA } }
            }

            LazyColumn(
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    RutinaProtagonista(
                        rutina = protagonista, 
                        isContinuing = rutinaEnCurso != null,
                        onClick = { onRutinaSeleccionada(protagonista) }
                    )
                }

                item {
                    Text(
                        text = "Explorar catálogo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TintaMarina,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                items(rutinasFiltradas.filter { it.id != protagonista.id }) { rutina ->
                    TarjetaRutinaCompacta(rutina = rutina, onClick = { onRutinaSeleccionada(rutina) })
                }
            }
        }
    }
}

@Composable
private fun FiltroChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (selected) AzulProfundo else Color.White.copy(alpha = 0.45f))
            .border(0.5.dp, GlassBorder, CircleShape)
            .botonClickAnimado(onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else TintaMarina,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun RutinaProtagonista(rutina: Rutina, isContinuing: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .shadow(16.dp, RoundedCornerShape(32.dp), ambientColor = Color.Black.copy(0.08f))
            .clip(RoundedCornerShape(32.dp))
            .botonClickAnimado(onClick)
    ) {
        Image(
            painter = painterResource(id = ImageUtils.getDrawableForNombre(rutina.nombre, false, rutina.imageKey)),
            contentDescription = "Rutina ${rutina.nombre}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.94f))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isContinuing) "Retomar: ${rutina.nombre}" else rutina.nombre, 
                        fontWeight = FontWeight.Black, 
                        fontSize = 19.sp, 
                        color = TintaMarina
                    )
                    Text(text = "${rutina.duracionTotalMin} min · Intensidad ${rutina.intensidad}", color = TextoSecundario, fontSize = 14.sp)
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isContinuing) LuzAqua else AzulProfundo),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = com.leon.tecsupfit.R.drawable.icon_play),
                        contentDescription = "Iniciar",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaRutinaCompacta(rutina: Rutina, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.55f))
            .border(0.5.dp, GlassBorder, RoundedCornerShape(24.dp))
            .botonClickAnimado(onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(id = ImageUtils.getDrawableForNombre(rutina.nombre, false, rutina.imageKey)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = rutina.nombre, fontWeight = FontWeight.Bold, color = TintaMarina, fontSize = 16.sp)
            Text(text = "${rutina.duracionTotalMin} min · ${rutina.equipo}", style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
        }
        Text(
            text = "Ver",
            color = AzulProfundo,
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(end = 4.dp)
        )
    }
}

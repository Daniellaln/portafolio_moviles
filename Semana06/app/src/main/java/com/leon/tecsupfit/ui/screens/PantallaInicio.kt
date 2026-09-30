package com.leon.tecsupfit.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import coil.compose.AsyncImage
import com.leon.tecsupfit.R
import com.leon.tecsupfit.data.ClaseFitness
import com.leon.tecsupfit.data.DatosClases
import com.leon.tecsupfit.data.UsuarioManager
import com.leon.tecsupfit.ui.components.DurationScreen
import com.leon.tecsupfit.ui.components.ImageUtils
import com.leon.tecsupfit.ui.components.LayoutBase
import com.leon.tecsupfit.ui.components.botonClickAnimado
import com.leon.tecsupfit.ui.theme.AzulProfundo
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina
import com.leon.tecsupfit.ui.theme.TextoSecundario
import java.util.Calendar

enum class ModoInicio { HOY, SEMANA, EXPLORAR }

@Composable
fun PantallaInicio(
    nombreUsuario: String,
    rutaActual: String,
    onNavegar: (String) -> Unit,
    onClaseSeleccionada: (ClaseFitness) -> Unit
) {
    var modoSeleccionado by rememberSaveable { mutableStateOf(ModoInicio.HOY) }
    var diaSeleccionado by rememberSaveable { 
        mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK).let { if (it == 1) 7 else it - 1 }) 
    }

    LayoutBase(
        rutaActual = rutaActual,
        onNavegar = onNavegar
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderInicio()

            SelectorModo(
                modoActual = modoSeleccionado,
                onModoSelected = { modoSeleccionado = it }
            )

            AnimatedContent(
                targetState = modoSeleccionado,
                transitionSpec = {
                    if (targetState.ordinal > initialState.ordinal) {
                        (slideInHorizontally(animationSpec = tween(DurationScreen)) { it } + fadeIn(animationSpec = tween(DurationScreen)))
                            .togetherWith(slideOutHorizontally(animationSpec = tween(DurationScreen)) { -it } + fadeOut(animationSpec = tween(DurationScreen)))
                    } else {
                        (slideInHorizontally(animationSpec = tween(DurationScreen)) { -it } + fadeIn(animationSpec = tween(DurationScreen)))
                            .togetherWith(slideOutHorizontally(animationSpec = tween(DurationScreen)) { it } + fadeOut(animationSpec = tween(DurationScreen)))
                    }
                },
                label = "CambioModo"
            ) { modo ->
                when (modo) {
                    ModoInicio.HOY -> ContenidoHoy(onClaseSeleccionada)
                    ModoInicio.SEMANA -> ContenidoSemana(
                        diaSeleccionado = diaSeleccionado,
                        onDiaSelected = { diaSeleccionado = it },
                        onClaseSeleccionada = onClaseSeleccionada
                    )
                    ModoInicio.EXPLORAR -> ContenidoExplorar(onClaseSeleccionada)
                }
            }
        }
    }
}

@Composable
private fun HeaderInicio() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hola, ${UsuarioManager.nombreReal}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = TintaMarina
            )
            Text(
                text = "Lunes, 12 de Junio",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )
        }
        
        Box(
            modifier = Modifier
                .size(44.dp)
                .shadow(8.dp, CircleShape, ambientColor = Color.Black.copy(0.05f))
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, GlassBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(targetState = UsuarioManager.fotoPerfilUri, label = "HeaderProfile") { uri ->
                if (uri != null) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectorModo(
    modoActual: ModoInicio,
    onModoSelected: (ModoInicio) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Color.White.copy(alpha = 0.4f))
            .border(0.5.dp, GlassBorder, RoundedCornerShape(26.dp))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            ModoInicio.values().forEach { modo ->
                val seleccionado = modo == modoActual
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .botonClickAnimado { onModoSelected(modo) },
                    contentAlignment = Alignment.Center
                ) {
                    if (seleccionado) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxSize()
                                .clip(RoundedCornerShape(22.dp))
                                .background(AzulProfundo)
                        )
                    }
                    Text(
                        text = when(modo) {
                            ModoInicio.HOY -> "Hoy"
                            ModoInicio.SEMANA -> "Semana"
                            ModoInicio.EXPLORAR -> "Explorar"
                        },
                        color = if (seleccionado) Color.White else TintaMarina.copy(0.6f),
                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ContenidoHoy(onClaseSeleccionada: (ClaseFitness) -> Unit) {
    val diaHoy = Calendar.getInstance().get(Calendar.DAY_OF_WEEK).let { if (it == 1) 7 else it - 1 }
    val clasesHoy = DatosClases.clases.filter { it.diaSemana == diaHoy }
    val claseProtagonista = clasesHoy.firstOrNull { it.cuposDisponibles > 0 }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        if (claseProtagonista != null) {
            item {
                ProtagonistCard(clase = claseProtagonista, onClick = { onClaseSeleccionada(claseProtagonista) })
            }
        }

        item {
            Text(
                text = "Más actividades",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TintaMarina,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
            )
        }

        items(clasesHoy.filter { it.id != claseProtagonista?.id }) { clase ->
            TarjetaClaseCompacta(clase = clase, onClick = { onClaseSeleccionada(clase) })
        }
    }
}

@Composable
private fun ProtagonistCard(clase: ClaseFitness, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .fillMaxWidth()
            .height(300.dp)
            .shadow(16.dp, RoundedCornerShape(36.dp), ambientColor = Color.Black.copy(0.1f))
            .clip(RoundedCornerShape(36.dp))
            .botonClickAnimado(onClick)
    ) {
        Image(
            painter = painterResource(id = ImageUtils.getDrawableForNombre(clase.nombre, true, clase.imageKey)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Lámina flotante Glass
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White.copy(alpha = 0.92f))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = clase.nombre, fontWeight = FontWeight.Black, fontSize = 20.sp, color = TintaMarina)
                    Text(text = "${clase.hora} · ${clase.sala}", color = TextoSecundario, fontSize = 14.sp)
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(LuzAqua),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaClaseCompacta(clase: ClaseFitness, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp), ambientColor = Color.Black.copy(0.02f))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.5f))
            .border(0.5.dp, GlassBorder, RoundedCornerShape(24.dp))
            .botonClickAnimado(onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = ImageUtils.getDrawableForNombre(clase.nombre, true, clase.imageKey)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(14.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = clase.nombre, fontWeight = FontWeight.Bold, color = TintaMarina, fontSize = 16.sp)
            Text(text = "${clase.hora} · ${clase.sala}", style = MaterialTheme.typography.bodySmall, color = TextoSecundario)
        }
        Text(
            text = "Ver",
            color = AzulProfundo,
            fontWeight = FontWeight.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ContenidoSemana(
    diaSeleccionado: Int,
    onDiaSelected: (Int) -> Unit,
    onClaseSeleccionada: (ClaseFitness) -> Unit
) {
    val dias = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
    val clasesDia = DatosClases.clases.filter { it.diaSemana == diaSeleccionado }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(7) { index ->
                val diaNum = index + 1
                val seleccionado = diaNum == diaSeleccionado
                Column(
                    modifier = Modifier
                        .width(54.dp)
                        .height(80.dp)
                        .shadow(if (seleccionado) 8.dp else 0.dp, RoundedCornerShape(18.dp), ambientColor = LuzAqua.copy(0.2f))
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (seleccionado) LuzAqua else Color.White.copy(alpha = 0.3f))
                        .clickable { onDiaSelected(diaNum) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = dias[index], color = if (seleccionado) Color.White else TintaMarina, fontSize = 12.sp, fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal)
                    Text(text = "${12 + index}", fontWeight = FontWeight.Black, color = if (seleccionado) Color.White else TintaMarina, fontSize = 18.sp)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(clasesDia) { clase ->
                TarjetaClaseCompacta(clase = clase, onClick = { onClaseSeleccionada(clase) })
            }
        }
    }
}

@Composable
private fun ContenidoExplorar(onClaseSeleccionada: (ClaseFitness) -> Unit) {
    val filtros = listOf("Pilates", "Baile Fit", "Boxeo")
    var filtroActivo by rememberSaveable { mutableStateOf<String?>(null) }
    val clasesFiltradas = if (filtroActivo == null) DatosClases.clases else DatosClases.clases.filter { it.nombre.contains(filtroActivo!!, ignoreCase = true) }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtros) { filtro ->
                val seleccionado = filtro == filtroActivo
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (seleccionado) AzulProfundo else Color.White.copy(alpha = 0.5f))
                        .border(0.5.dp, GlassBorder, CircleShape)
                        .botonClickAnimado { filtroActivo = if (seleccionado) null else filtro }
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Text(text = filtro, color = if (seleccionado) Color.White else TintaMarina, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            items(clasesFiltradas) { clase ->
                TarjetaClaseCompacta(clase = clase, onClick = { onClaseSeleccionada(clase) })
            }
        }
    }
}

@Composable
private fun EmptyStateHoy() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No hay clases próximas", color = TintaMarina, fontWeight = FontWeight.Black, fontSize = 18.sp)
        Text("Revisa la agenda semanal", color = TextoSecundario, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

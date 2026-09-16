package com.leon.lab04

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leon.lab04.ui.theme.Lab04Theme

data class Tarea(
    val id: Int,
    val nombre: String,
    val completada: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab04Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay suave para mejorar contraste
        Box(
            modifier = Modifier
                .fillMaxSize()
        )

        PantallaTareas()
    }
}

@Composable
fun PantallaTareas() {
    var textoTarea by remember { mutableStateOf("") }
    var contadorId by remember { mutableStateOf(1) }
    val listaTareas = remember { mutableStateListOf<Tarea>() }

    // Colores para las tarjetas
    val cardColors = listOf(
        Color(0xFFE3F2FD), Color(0xFFF1F8E9), Color(0xFFFFF3E0), 
        Color(0xFFFCE4EC), Color(0xFFF3E5F5), Color(0xFFE0F2F1)
    )
    val stripColors = listOf(
        Color(0xFF2196F3), Color(0xFF4CAF50), Color(0xFFFF9800), 
        Color(0xFFE91E63), Color(0xFF9C27B0), Color(0xFF009688)
    )

    val completadas = listaTareas.count { it.completada }
    val total = listaTareas.size
    val progreso = if (total > 0) completadas.toFloat() / total else 0f
    val progressAnimated by animateFloatAsState(targetValue = progreso, label = "progress")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Encabezado
        Text(
            text = "Lista de tareas",
            color = Color(0xFF0D47A1),
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black)
        )
        Text(
            text = "Tecsup",
            color = Color(0xFF81D4FA),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "pequeños pasos, grandes metas",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "¡¡tú puedes!!",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input y Botón
        OutlinedTextField(
            value = textoTarea,
            onValueChange = { textoTarea = it },
            placeholder = { Text("Escribe tu próxima tarea...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF0D47A1),
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (textoTarea.isNotBlank()) {
                    listaTareas.add(Tarea(id = contadorId, nombre = textoTarea))
                    contadorId++
                    textoTarea = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Agregar tarea", style = MaterialTheme.typography.titleMedium, color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card de progreso (Indigo)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF3F51B5)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(0.3f)) {
                    Text("Total", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text("$total", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                }
                
                Column(modifier = Modifier.weight(0.7f)) {
                    LinearProgressIndicator(
                        progress = { progressAnimated },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = Color(0xFF80CBC4),
                        trackColor = Color.White.copy(alpha = 0.2f),
                        strokeCap = StrokeCap.Round
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Completadas $completadas • Pendientes ${total - completadas}",
                        color = Color.White,
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lista
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(listaTareas, key = { _, t -> t.id }) { index, tarea ->
                ItemTarea(
                    tarea = tarea,
                    bgColor = cardColors[index % cardColors.size],
                    stripColor = stripColors[index % stripColors.size],
                    onEliminar = { listaTareas.remove(tarea) },
                    onCambiarEstado = { status ->
                        val idx = listaTareas.indexOf(tarea)
                        if (idx != -1) {
                            listaTareas[idx] = listaTareas[idx].copy(completada = status)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ItemTarea(
    tarea: Tarea,
    bgColor: Color,
    stripColor: Color,
    onEliminar: () -> Unit,
    onCambiarEstado: (Boolean) -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (tarea.completada) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.5f), label = "scale"
    )
    val alpha by animateFloatAsState(targetValue = if (tarea.completada) 0.6f else 1f, label = "alpha")
    val animatedColor by animateColorAsState(
        targetValue = if (tarea.completada) Color.LightGray.copy(alpha = 0.3f) else bgColor, 
        label = "color"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = animatedColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Franja de color
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(stripColor)
            )

            Checkbox(
                checked = tarea.completada,
                onCheckedChange = onCambiarEstado,
                modifier = Modifier.padding(start = 12.dp),
                colors = CheckboxDefaults.colors(checkedColor = stripColor)
            )

            Text(
                text = tarea.nombre,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                ),
                color = if (tarea.completada) Color.Gray else Color(0xFF0D47A1),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            )

            IconButton(
                onClick = onEliminar,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

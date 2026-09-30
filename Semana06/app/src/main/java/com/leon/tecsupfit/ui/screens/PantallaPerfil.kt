package com.leon.tecsupfit.ui.screens

import android.net.Uri
import androidx.activity.compose.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.leon.tecsupfit.R
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.*
import com.leon.tecsupfit.ui.theme.*
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(
    foto: String?,
    reservas: List<ReservaConClase>,
    sesiones: List<SesionRutina>,
    ahora: Long,
    busy: Boolean,
    guardarFoto: (Uri, () -> Unit) -> Unit,
    error: (String) -> Unit,
    back: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    var hoja by rememberSaveable { mutableStateOf(false) }
    var actividad by rememberSaveable { mutableStateOf(false) }
    var pendiente by rememberSaveable { mutableStateOf<String?>(null) }
    val galeria =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) guardarFoto(uri) { hoja = false }
        }
    val camara =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
            pendiente?.let { path ->
                val f = File(path)
                if (ok)
                    guardarFoto(Uri.fromFile(f)) {
                        f.delete()
                        hoja = false
                        pendiente = null
                    }
                else {
                    f.delete()
                    pendiente = null
                }
            }
        }
    BackHandler(hoja || actividad) { if (hoja) hoja = false else actividad = false }
    val completadas = sesiones.filter { it.estado == "COMPLETADA" }
    val finalizadas = reservas.count { it.reserva.estado == "CONFIRMADA" && it.clase.fin <= ahora }
    Page("Mi perfil", foto = foto, onProfile = {}, profile = true, onBack = back) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Glass(
                Modifier.size(132.dp),
                radius = 70.dp,
                elevation = 14.dp,
                onClick = { hoja = true },
            ) {
                AsyncImage(
                    foto ?: R.drawable.perfil,
                    "Cambiar mi foto de perfil",
                    Modifier.padding(7.dp).fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.perfil),
                    error = painterResource(R.drawable.perfil),
                )
            }
            Text("Daniella Leon", style = MaterialTheme.typography.titleLarge)
            Text("Tu espacio de bienestar", color = TextoSecundario, fontSize = 13.sp)
        }
        Glass(Modifier.fillMaxWidth(), radius = 29.dp, elevation = 12.dp) {
            Row(
                Modifier.fillMaxWidth().padding(22.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$finalizadas", fontSize = 29.sp, fontWeight = FontWeight.Bold)
                    Text("Clases finalizadas", fontSize = 12.sp, color = TextoSecundario)
                }
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${completadas.size}", fontSize = 29.sp, fontWeight = FontWeight.Bold)
                    Text("Rutinas completadas", fontSize = 12.sp, color = TextoSecundario)
                }
            }
        }
        SecondaryButton("Cambiar foto") { hoja = true }
        SecondaryButton(if (actividad) "Ocultar mi actividad" else "Mi actividad") {
            actividad = !actividad
        }
        if (actividad) {
            SectionTitle("Tus sesiones")
            if (completadas.isEmpty())
                InfoCard(
                    "Aún no hay sesiones completadas",
                    "Termina una rutina para ver aquí tu progreso.",
                )
            completadas.forEach { s ->
                InfoCard(
                    s.nombre,
                    "${fechaBreve(s.completadaEn?:s.creadaEn,ahora)} · ${cronometro(s.realizadoMs)} de sesión",
                )
            }
        }
        InfoCard(
            "Tu progreso queda guardado",
            "Las reservas, rutinas y tu foto se guardan en este dispositivo. Las clases finalizadas no equivalen a una asistencia verificada.",
        )
    }
    if (hoja)
        ModalBottomSheet(
            onDismissRequest = { if (!busy) hoja = false },
            containerColor = Color.Transparent,
            dragHandle = null,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        ) {
            Glass(Modifier.fillMaxWidth(), radius = 32.dp, opacity = .87f) {
                Column(
                    Modifier.padding(24.dp).navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text("Cambiar foto", style = MaterialTheme.typography.titleLarge)
                    SecondaryButton("Tomar una foto") {
                        if (!busy)
                            runCatching {
                                val dir = File(context.cacheDir, "images").apply { mkdirs() }
                                val f = File.createTempFile("camera_", ".jpg", dir)
                                pendiente = f.absolutePath
                                camara.launch(
                                    FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        f,
                                    )
                                )
                            }
                                .onFailure {
                                    error(
                                        "No hay una cámara disponible. Puedes elegir una foto de la galería."
                                    )
                                }
                    }
                    SecondaryButton("Elegir de la galería") {
                        if (!busy)
                            runCatching { galeria.launch(arrayOf("image/*")) }
                                .onFailure { error("No se pudo abrir la galería.") }
                    }
                    if (busy) LinearProgressIndicator(Modifier.fillMaxWidth())
                    SecondaryButton("Cancelar") { hoja = false }
                }
            }
        }
}

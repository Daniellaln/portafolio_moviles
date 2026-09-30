package com.leon.tecsupfit.ui.screens

import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.leon.tecsupfit.R
import com.leon.tecsupfit.data.UsuarioManager
import com.leon.tecsupfit.ui.components.LayoutBase
import com.leon.tecsupfit.ui.theme.AzulProfundo
import com.leon.tecsupfit.ui.theme.GlassBorder
import com.leon.tecsupfit.ui.theme.LuzAqua
import com.leon.tecsupfit.ui.theme.TintaMarina
import com.leon.tecsupfit.ui.theme.TextoSecundario
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaPerfil(nombreUsuario: String, rutaActual: String, onNavegar: (String) -> Unit) {
    val context = LocalContext.current
    var showPhotoSheet by remember { mutableStateOf(false) }
    
    // Camera Logic
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            UsuarioManager.guardarFoto(context, uri)
            showPhotoSheet = false
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            UsuarioManager.guardarFoto(context, tempPhotoUri)
            showPhotoSheet = false
        }
    }

    LayoutBase(
        rutaActual = rutaActual,
        onNavegar = onNavegar
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Mi perfil",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = TintaMarina,
                    modifier = Modifier.fillMaxWidth()
                )

                // Avatar Circular
                Box(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .size(120.dp)
                        .shadow(12.dp, CircleShape, ambientColor = Color.Black.copy(0.1f))
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, GlassBorder, CircleShape)
                        .clickable { showPhotoSheet = true },
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(targetState = UsuarioManager.fotoPerfilUri, label = "ProfileImageDissolve") { uri ->
                        if (uri != null) {
                            AsyncImage(
                                model = uri,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.perfil),
                                contentDescription = "Foto de perfil predeterminada",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Text(
                    text = UsuarioManager.nombreReal,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TintaMarina,
                    modifier = Modifier.padding(top = 16.dp)
                )
                
                Text(
                    text = "Cambiar foto",
                    style = MaterialTheme.typography.bodySmall,
                    color = LuzAqua,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { showPhotoSheet = true }
                        .padding(8.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    EstadisticaCard(
                        valor = UsuarioManager.getClasesReservadas().toString(), 
                        etiqueta = "Reservas"
                    )
                    EstadisticaCard(
                        valor = UsuarioManager.getSesionesCompletadas().toString(), 
                        etiqueta = "Sesiones"
                    )
                }
            }

            // Hoja de Vidrio para Cambio de Foto (Mockup 14)
            AnimatedVisibility(
                visible = showPhotoSheet,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable { showPhotoSheet = false },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                            .background(Color.White)
                            .padding(24.dp)
                            .clickable(enabled = false) { }
                    ) {
                        Text(
                            text = "Foto de perfil",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = TintaMarina
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        AccionFoto(
                            text = "Tomar una foto",
                            onClick = {
                                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
                                val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                                val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
                                tempPhotoUri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    file
                                )
                                cameraLauncher.launch(tempPhotoUri!!)
                            }
                        )
                        
                        AccionFoto(
                            text = "Elegir de la galería",
                            onClick = {
                                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                        )
                        
                        if (UsuarioManager.fotoPerfilUri != null) {
                            AccionFoto(
                                text = "Restaurar foto inicial",
                                color = LuzAqua,
                                onClick = {
                                    UsuarioManager.guardarFoto(context, null)
                                    showPhotoSheet = false
                                }
                            )
                        }

                        Button(
                            onClick = { showPhotoSheet = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = TextoSecundario),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Cancelar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccionFoto(
    text: String,
    onClick: () -> Unit,
    color: Color = TintaMarina
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun EstadisticaCard(valor: String, etiqueta: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, GlassBorder),
        modifier = Modifier.shadow(8.dp, RoundedCornerShape(24.dp), ambientColor = Color.Black.copy(0.05f))
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .size(width = 80.dp, height = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = valor, 
                fontWeight = FontWeight.Black, 
                style = MaterialTheme.typography.titleLarge,
                color = AzulProfundo
            )
            Text(
                text = etiqueta, 
                textAlign = TextAlign.Center, 
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )
        }
    }
}

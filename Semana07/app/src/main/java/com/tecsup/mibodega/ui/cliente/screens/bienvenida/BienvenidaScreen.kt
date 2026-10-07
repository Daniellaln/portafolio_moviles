package com.tecsup.mibodega.ui.cliente.screens.bienvenida
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.R
import com.tecsup.mibodega.ui.componentes.*
@Composable fun BienvenidaScreen(onRegistrarse: () -> Unit, onIniciarSesion: () -> Unit, onTerminos: () -> Unit) {
 Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
  Column(Modifier.safeDrawingPadding().verticalScroll(rememberScrollState()).padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(20.dp)) {
   Text("TU BODEGA DE CONFIANZA", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 20.dp))
   Image(painterResource(R.drawable.bodega_clay), null, Modifier.fillMaxWidth().height(280.dp))
   Text("Lo de siempre.\nMás cerca de ti.", style = MaterialTheme.typography.displayMedium, textAlign = TextAlign.Center)
   Text("Mi Bodega", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
   Text("Todo lo que necesitas para tu día,\nsin salir de casa.", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
   BotonPrimario("Registrarme", onRegistrarse, subtexto = "Empieza con tu teléfono")
   BotonSecundario("Iniciar sesión (demo)", onIniciarSesion)
   TextButton(onTerminos) { Text("Términos y condiciones") }
  }
 }
}
package com.tecsup.mibodega.ui.cliente.screens.login
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.*
import com.tecsup.mibodega.ui.cliente.modelo.*
@Composable fun LoginScreen(onVolver: () -> Unit, onEntrar: () -> Unit) {
 var usuario by rememberSaveable { mutableStateOf("") }
 // La contraseña no se guarda al recrear la actividad.
 var clave by remember { mutableStateOf("") }
 var intento by rememberSaveable { mutableStateOf(false) }
 var incorrecto by remember { mutableStateOf(false) }
 Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
  Column(Modifier.safeDrawingPadding().imePadding().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
   Encabezado("Iniciar sesión", "Qué bueno tenerte de vuelta", onVolver)
   Text("Hola de nuevo", style = MaterialTheme.typography.headlineLarge)
   Text("Entra para guardar tus favoritos y preparar tu pedido.", color = MaterialTheme.colorScheme.onSurfaceVariant)
   CampoTexto("Usuario", usuario, { usuario = it; incorrecto = false }, error = errorRequerido(usuario, intento))
   CampoTexto("Contraseña", clave, { clave = it; incorrecto = false }, teclado = KeyboardType.Password, contrasena = true, error = errorRequerido(clave, intento))
   if(incorrecto) Text("El usuario o la contraseña no coinciden. Inténtalo otra vez.", color = MaterialTheme.colorScheme.error)
   BotonPrimario("Entrar", {
    intento = true
    if(credencialesValidas(usuario, clave)) onEntrar() else incorrecto = usuario.isNotBlank() && clave.isNotBlank()
   })
  }
 }
}

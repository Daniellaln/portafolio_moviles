package com.tecsup.mibodega.ui.cliente.screens.registro
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAddAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.*
@Composable fun RegistroScreen(onVolver: () -> Unit, onCrearCuenta: (String, String, String, String) -> Unit) {
 var nombre by rememberSaveable { mutableStateOf("") }; var telefono by rememberSaveable { mutableStateOf("") }
 var direccion by rememberSaveable { mutableStateOf("") }; var referencia by rememberSaveable { mutableStateOf("") }
 Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
  Column(Modifier.safeDrawingPadding().imePadding().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
   Encabezado("Crear cuenta", "Unos datos y estamos listos", onVolver)
   Icon(Icons.Default.PersonAddAlt, null, Modifier.size(52.dp), tint = MaterialTheme.colorScheme.primary)
   Text("Empecemos por ti", style = MaterialTheme.typography.headlineMedium)
   Text("Usaremos estos datos para llevar tu pedido a la dirección correcta.", color = MaterialTheme.colorScheme.onSurfaceVariant)
   CampoTexto("Nombre completo", nombre, { nombre = it }, placeholder = "Juan Pérez")
   CampoTexto("Teléfono", telefono, { telefono = it.filter(Char::isDigit).take(9) }, teclado = KeyboardType.Phone,
    error = if(telefono.isNotEmpty() && telefono.length != 9) "Escribe los 9 números de tu teléfono" else null)
   CampoTexto("Dirección de entrega", direccion, { direccion = it }, placeholder = "Av. Los Olivos 123")
   CampoTexto("Referencia (opcional)", referencia, { referencia = it }, placeholder = "Frente al parque")
   BotonPrimario("Crear cuenta", { onCrearCuenta(nombre.trim(), telefono, direccion.trim(), referencia.trim()) },
    habilitado = nombre.isNotBlank() && telefono.length == 9 && direccion.isNotBlank())
  }
 }
}
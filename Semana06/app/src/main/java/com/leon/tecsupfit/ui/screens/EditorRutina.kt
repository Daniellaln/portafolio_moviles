package com.leon.tecsupfit.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.*
import java.util.UUID

@Composable
fun EditorRutina(
    original: Rutina?,
    duplicar: Boolean,
    foto: String?,
    onProfile: () -> Unit,
    back: () -> Unit,
    busy: Boolean,
    guardar: (Rutina) -> Unit,
) {
    val focus = LocalFocusManager.current
    val id = rememberSaveable {
        if (original != null && !duplicar) original.id else UUID.randomUUID().toString()
    }
    val inicialNombre =
        if (duplicar && original != null) "${original.nombre} (copia)"
        else original?.nombre.orEmpty()
    var nombre by rememberSaveable { mutableStateOf(inicialNombre) }
    var descripcion by rememberSaveable { mutableStateOf(original?.descripcion.orEmpty()) }
    var equipo by rememberSaveable { mutableStateOf(original?.equipo ?: "Sin equipo") }
    var intensidad by rememberSaveable { mutableIntStateOf(original?.intensidad?.ordinal ?: 0) }
    var vueltas by rememberSaveable { mutableStateOf((original?.vueltas ?: 1).toString()) }
    val codec = remember { Conversores() }
    val inicialJson = remember { codec.guardarEjercicios(original?.ejercicios ?: emptyList()) }
    var json by rememberSaveable { mutableStateOf(inicialJson) }
    val ejercicios = remember(json) { codec.leerEjercicios(json) }
    var editando by rememberSaveable { mutableIntStateOf(-2) }
    var confirmarSalida by rememberSaveable { mutableStateOf(false) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    val cambiado =
        nombre != inicialNombre ||
            descripcion != original?.descripcion.orEmpty() ||
            equipo != (original?.equipo ?: "Sin equipo") ||
            intensidad != (original?.intensidad?.ordinal ?: 0) ||
            vueltas != (original?.vueltas ?: 1).toString() ||
            json != inicialJson
    fun salir() {
        if (cambiado) confirmarSalida = true else back()
    }
    BackHandler { if (editando >= -1) editando = -2 else salir() }
    if (editando < -1)
        Page(
            if (original == null || duplicar) "Crear rutina" else "Editar rutina",
            foto = foto,
            onProfile = onProfile,
            onBack = { salir() },
        ) {
            InfoCard(
                "Hazla tuya",
                "La foto se elige según el nombre: fuerza, yoga, baile, cardio y más.",
                true,
            )
            OutlinedTextField(
                nombre,
                { nombre = it.take(70) },
                Modifier.fillMaxWidth(),
                label = { Text("Nombre de la rutina") },
                singleLine = true,
            )
            if (nombre.isNotBlank())
                PhotoRow(nombre, "Vista previa de la foto", tag = "Asignación automática")
            OutlinedTextField(
                descripcion,
                { descripcion = it.take(300) },
                Modifier.fillMaxWidth(),
                label = { Text("Descripción") },
                minLines = 2,
            )
            OutlinedTextField(
                equipo,
                { equipo = it.take(80) },
                Modifier.fillMaxWidth(),
                label = { Text("Equipo necesario") },
                singleLine = true,
            )
            SectionTitle("Intensidad")
            Segments(listOf("Baja", "Media", "Alta"), intensidad) { intensidad = it }
            OutlinedTextField(
                vueltas,
                { vueltas = it.filter(Char::isDigit).take(2) },
                Modifier.fillMaxWidth(),
                label = { Text("Vueltas (1–10)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            SectionTitle("Ejercicios")
            ejercicios.forEachIndexed { i, e ->
                Glass(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("${i+1}. ${e.nombre}", style = MaterialTheme.typography.titleMedium)
                        Text("${e.segundosActivos} s · ${e.segundosTransicion} s de descanso")
                        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                            TextButton(
                                onClick = {
                                    focus.clearFocus()
                                    editando = i
                                }
                            ) {
                                Text("Editar")
                            }
                            TextButton(
                                enabled = i > 0,
                                onClick = {
                                    val l = ejercicios.toMutableList()
                                    l.add(i - 1, l.removeAt(i))
                                    json = codec.guardarEjercicios(l)
                                },
                            ) {
                                Text("Subir")
                            }
                            TextButton(
                                enabled = i < ejercicios.lastIndex,
                                onClick = {
                                    val l = ejercicios.toMutableList()
                                    l.add(i + 1, l.removeAt(i))
                                    json = codec.guardarEjercicios(l)
                                },
                            ) {
                                Text("Bajar")
                            }
                            TextButton(
                                onClick = {
                                    json =
                                        codec.guardarEjercicios(
                                            ejercicios.filterIndexed { j, _ -> i != j }
                                        )
                                }
                            ) {
                                Text("Quitar")
                            }
                        }
                    }
                }
            }
            if (ejercicios.size < 30)
                SecondaryButton("+ Añadir ejercicio") {
                    focus.clearFocus()
                    editando = -1
                }
            val v = vueltas.toIntOrNull() ?: 0
            val segundos = ejercicios.sumOf { it.segundosActivos + it.segundosTransicion } * v
            InfoCard(
                "Duración: ${segundos/60} min ${segundos%60} s",
                "Calculada con los tiempos y descansos de cada vuelta.",
            )
            if (error != null) Text(error!!, color = MaterialTheme.colorScheme.error)
            PrimaryButton(
                "Guardar rutina",
                busy = busy,
                onClick = {
                    if (nombre.isBlank() || ejercicios.isEmpty() || v !in 1..10)
                        error =
                            "Escribe un nombre, añade un ejercicio y elige entre 1 y 10 vueltas."
                    else
                        guardar(
                            Rutina(
                                id,
                                nombre.trim(),
                                descripcion.trim(),
                                equipo.ifBlank { "Sin equipo" },
                                Intensidad.entries[intensidad],
                                v,
                                ejercicios,
                                propia = true,
                            )
                        )
                },
            )
        }
    if (editando >= -1)
        key(editando) {
            val actual = ejercicios.getOrNull(editando)
            var n by rememberSaveable { mutableStateOf(actual?.nombre.orEmpty()) }
            var instrucciones by rememberSaveable { mutableStateOf(actual?.instruccion.orEmpty()) }
            var activo by rememberSaveable {
                mutableStateOf((actual?.segundosActivos ?: 45).toString())
            }
            var descanso by rememberSaveable {
                mutableStateOf((actual?.segundosTransicion ?: 15).toString())
            }
            var aviso by rememberSaveable { mutableStateOf<String?>(null) }
            Page(
                if (actual == null) "Nuevo ejercicio" else "Editar ejercicio",
                foto = foto,
                onProfile = onProfile,
                onBack = { editando = -2 },
            ) {
                OutlinedTextField(
                    n,
                    { n = it.take(70) },
                    Modifier.fillMaxWidth(),
                    label = { Text("Nombre") },
                    singleLine = true,
                )
                OutlinedTextField(
                    instrucciones,
                    { instrucciones = it.take(250) },
                    Modifier.fillMaxWidth(),
                    label = { Text("Cómo hacerlo") },
                    minLines = 3,
                )
                OutlinedTextField(
                    activo,
                    { activo = it.filter(Char::isDigit).take(3) },
                    Modifier.fillMaxWidth(),
                    label = { Text("Actividad (5–600 s)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                OutlinedTextField(
                    descanso,
                    { descanso = it.filter(Char::isDigit).take(3) },
                    Modifier.fillMaxWidth(),
                    label = { Text("Descanso (0–300 s)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                if (aviso != null) Text(aviso!!, color = MaterialTheme.colorScheme.error)
                PrimaryButton(
                    "Guardar ejercicio",
                    onClick = {
                        val a = activo.toIntOrNull()
                        val d = descanso.toIntOrNull()
                        if (n.isBlank() || a == null || a !in 5..600 || d == null || d !in 0..300)
                            aviso = "Revisa el nombre y los rangos de tiempo."
                        else {
                            focus.clearFocus()
                            val l = ejercicios.toMutableList()
                            val e = Ejercicio(n.trim(), instrucciones.trim(), a, d)
                            if (editando < 0) l.add(e) else l[editando] = e
                            json = codec.guardarEjercicios(l)
                            editando = -2
                        }
                    },
                )
                SecondaryButton("Cancelar") { editando = -2 }
            }
        }
    if (confirmarSalida)
        AlertDialog(
            onDismissRequest = { confirmarSalida = false },
            title = { Text("¿Salir sin guardar?") },
            text = { Text("Tienes cambios pendientes en esta rutina.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmarSalida = false
                        back()
                    }
                ) {
                    Text("Descartar cambios")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarSalida = false }) { Text("Seguir editando") }
            },
        )
}

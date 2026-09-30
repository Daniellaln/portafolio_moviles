package com.leon.tecsupfit.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.*

@Composable
fun PantallaDetalleRutina(
    r: Rutina,
    foto: String?,
    onProfile: () -> Unit,
    back: () -> Unit,
    busy: Boolean,
    iniciar: () -> Unit,
    editar: () -> Unit,
    duplicar: () -> Unit,
    borrar: () -> Unit,
) {
    var eliminar by rememberSaveable { mutableStateOf(false) }
    Page("Rutina", foto = foto, onProfile = onProfile, onBack = back) {
        PhotoHero(
            r.nombre,
            r.imageKey,
            "${r.duracionTotalMin} MIN · ${r.equipo}",
            r.descripcion,
            detail = true,
        )
        SectionTitle("Lo que harás")
        InfoCard(
            "${r.vueltas} vueltas · ${r.ejercicios.size} movimientos",
            "Intensidad ${r.intensidad.name.lowercase()} · Descansos incluidos.",
        )
        PrimaryButton("Empezar rutina", busy = busy, onClick = iniciar)
        r.ejercicios.forEachIndexed { i, e ->
            InfoCard(
                "${i+1}. ${e.nombre}",
                "${e.segundosActivos} s de actividad · ${e.segundosTransicion} s de descanso\n${e.instruccion}",
            )
        }
        if (r.propia) SecondaryButton("Editar mi rutina", editar)
        SecondaryButton("Duplicar y personalizar", duplicar)
        if (r.propia) TextButton(onClick = { eliminar = true }) { Text("Eliminar rutina") }
    }
    if (eliminar)
        AlertDialog(
            onDismissRequest = { eliminar = false },
            title = { Text("¿Eliminar esta rutina?") },
            text = { Text("El historial de sesiones completadas se conservará.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        eliminar = false
                        borrar()
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = { TextButton(onClick = { eliminar = false }) { Text("Conservar") } },
        )
}

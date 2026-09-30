package com.leon.tecsupfit.ui.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.*

@Composable
fun PantallaRutinas(
    rutinas: List<Rutina>,
    activa: SesionRutina?,
    foto: String?,
    onProfile: () -> Unit,
    abrir: (String) -> Unit,
    crear: () -> Unit,
    catalogo: () -> Unit,
    retomar: () -> Unit,
) {
    Page("Rutinas", "Un momento para moverte hoy.", foto, onProfile) {
        if (activa != null) {
            InfoCard("Continúa a tu ritmo", "${activa.nombre} · Tu sesión está guardada.", true)
            PrimaryButton("Retomar mi sesión", onClick = retomar)
        }
        val hero = rutinas.firstOrNull { it.id == "base_1" } ?: rutinas.firstOrNull()
        if (hero != null)
            PhotoHero(
                hero.nombre,
                hero.imageKey,
                "${hero.duracionTotalMin} MIN",
                "${hero.equipo} · ${hero.ejercicios.size} movimientos",
                onClick = { abrir(hero.id) },
            )
        SectionTitle("Para ti", "Ver todas", catalogo)
        rutinas
            .filter { !it.propia && it.id != hero?.id }
            .take(2)
            .forEach { r ->
                PhotoRow(
                    r.nombre,
                    "${r.duracionTotalMin} min · ${r.equipo}",
                    imageKey = r.imageKey,
                ) {
                    abrir(r.id)
                }
            }
        SectionTitle("Mis rutinas")
        rutinas
            .filter { it.propia }
            .forEach { r ->
                PhotoRow(
                    r.nombre,
                    "${r.duracionTotalMin} min · ${r.ejercicios.size} ejercicios",
                    "Creada por ti",
                    r.imageKey,
                ) {
                    abrir(r.id)
                }
            }
        if (rutinas.none { it.propia })
            InfoCard(
                "Diseña tu propio ritmo",
                "Elige ejercicios, tiempos, descansos y vueltas. La foto se asigna según el nombre.",
            )
        SecondaryButton("+ Crear una rutina", crear)
    }
}

@Composable
fun CatalogoRutinas(
    rutinas: List<Rutina>,
    foto: String?,
    onProfile: () -> Unit,
    back: () -> Unit,
    abrir: (String) -> Unit,
    crear: () -> Unit,
) {
    var filtro by rememberSaveable { mutableIntStateOf(0) }
    Page("Todas las rutinas", foto = foto, onProfile = onProfile, onBack = back) {
        Text("Encuentra tu ritmo", style = MaterialTheme.typography.headlineMedium)
        Segments(listOf("Todas", "≤ 15 min", "Suaves"), filtro) { filtro = it }
        val visibles = rutinas.filter {
            when (filtro) {
                1 -> it.duracionTotalMin <= 15
                2 -> it.intensidad == Intensidad.BAJA
                else -> true
            }
        }
        visibles.forEach { r ->
            PhotoRow(
                r.nombre,
                "${r.duracionTotalMin} min · ${r.equipo}",
                if (r.propia) "Creada por ti"
                else r.intensidad.name.lowercase().replaceFirstChar { it.titlecase() },
                r.imageKey,
            ) {
                abrir(r.id)
            }
        }
        if (visibles.isEmpty())
            InfoCard("Sin rutinas en este filtro", "Prueba otra intensidad o crea tu propio plan.")
        SecondaryButton("+ Crear una rutina", crear)
    }
}

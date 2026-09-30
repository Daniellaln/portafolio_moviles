package com.leon.tecsupfit.data

import androidx.room.*

data class Ejercicio(
    val nombre: String,
    val instruccion: String,
    val segundosActivos: Int = 50,
    val segundosTransicion: Int = 10,
    val imageKey: String? = null,
)

enum class Intensidad {
    BAJA,
    MEDIA,
    ALTA,
}

@Entity(tableName = "rutinas")
data class Rutina(
    @PrimaryKey val id: String,
    val nombre: String,
    val descripcion: String,
    val equipo: String,
    val intensidad: Intensidad,
    val vueltas: Int = 2,
    val ejercicios: List<Ejercicio>,
    val imageKey: String? = null,
    val propia: Boolean = false,
) {
    val duracionSegundos: Int
        get() = vueltas * ejercicios.sumOf { it.segundosActivos + it.segundosTransicion }

    val duracionTotalMin: Int
        get() = (duracionSegundos + 59) / 60
}

@Entity(tableName = "sesiones")
data class SesionRutina(
    @PrimaryKey val id: String,
    val rutinaId: String,
    val nombre: String,
    val ejercicios: List<Ejercicio>,
    val vueltas: Int,
    val progresoMs: Long = 0,
    val realizadoMs: Long = 0,
    val anclaMs: Long = 0,
    val corriendo: Boolean = false,
    val estado: String = "ACTIVA",
    val creadaEn: Long,
    val completadaEn: Long? = null,
) {
    val totalMs: Long
        get() = vueltas * ejercicios.sumOf { (it.segundosActivos + it.segundosTransicion) * 1000L }
}

data class FaseRutina(
    val ejercicio: Ejercicio,
    val indice: Int,
    val vuelta: Int,
    val descanso: Boolean,
    val inicioMs: Long,
    val duracionMs: Long,
)

/** El reloj se calcula desde una marca temporal; delay solo solicita redibujar. */
object MotorRutina {
    fun fases(s: SesionRutina): List<FaseRutina> = buildList {
        var inicio = 0L
        repeat(s.vueltas) { v ->
            s.ejercicios.forEachIndexed { i, e ->
                add(FaseRutina(e, i, v, false, inicio, e.segundosActivos * 1000L))
                inicio += e.segundosActivos * 1000L
                if (e.segundosTransicion > 0) {
                    add(FaseRutina(e, i, v, true, inicio, e.segundosTransicion * 1000L))
                    inicio += e.segundosTransicion * 1000L
                }
            }
        }
    }

    fun actualizar(s: SesionRutina, ahora: Long): SesionRutina {
        if (s.estado != "ACTIVA") return s
        val avance =
            if (s.corriendo)
                (ahora - s.anclaMs)
                    .coerceAtLeast(0)
                    .coerceAtMost((s.totalMs - s.progresoMs).coerceAtLeast(0))
            else 0
        val p = (s.progresoMs + avance).coerceAtMost(s.totalMs)
        val fin = p >= s.totalMs
        return s.copy(
            progresoMs = p,
            realizadoMs = s.realizadoMs + avance,
            anclaMs = ahora,
            corriendo = s.corriendo && !fin,
            estado = if (fin) "COMPLETADA" else "ACTIVA",
            completadaEn = if (fin) ahora else null,
        )
    }

    fun fase(s: SesionRutina) = fases(s).firstOrNull { s.progresoMs < it.inicioMs + it.duracionMs }

    fun saltar(s: SesionRutina, ahora: Long): SesionRutina {
        val a = actualizar(s, ahora)
        val f = fase(a) ?: return a
        return actualizar(a.copy(progresoMs = f.inicioMs + f.duracionMs), ahora)
    }
}

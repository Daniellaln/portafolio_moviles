package com.leon.tecsupfit.data

data class Ejercicio(
    val nombre: String,
    val instruccion: String,
    val segundosActivos: Int = 50,
    val segundosTransicion: Int = 10,
    val imageKey: String? = null
)

enum class Intensidad { BAJA, MEDIA, ALTA }

data class Rutina(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val duracionTotalMin: Int,
    val equipo: String,
    val intensidad: Intensidad,
    val vueltas: Int = 2,
    val ejercicios: List<Ejercicio>,
    val imageKey: String? = null
)

data class SesionCompletada(
    val rutinaId: Int,
    val nombreRutina: String,
    val fecha: Long = System.currentTimeMillis()
)

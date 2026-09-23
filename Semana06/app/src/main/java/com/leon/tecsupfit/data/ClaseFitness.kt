package com.leon.tecsupfit.data

/**
 * Representa una clase del gimnasio que se puede reservar.
 */
data class ClaseFitness(
    val id: Int,
    val nombre: String,
    val hora: String,
    val sala: String,
    val duracionMin: Int,
    val descripcion: String,
    val cuposDisponibles: Int,
    val cuposTotales: Int
)

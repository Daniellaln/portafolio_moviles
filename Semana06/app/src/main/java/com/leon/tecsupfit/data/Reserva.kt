package com.leon.tecsupfit.data

enum class EstadoReserva {
    CONFIRMADA,
    COMPLETADA
}

/**
 * Representa una clase que el usuario ya reservó.
 */
data class Reserva(
    val id: Int,
    val clase: ClaseFitness,
    val fecha: String,
    val estado: EstadoReserva
)

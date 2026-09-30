package com.leon.tecsupfit.data

enum class EstadoReserva {
    CONFIRMADA,
    COMPLETADA,
    CANCELADA,
    FINALIZADA
}

/**
 * Representa una clase que el usuario ya reservó.
 */
data class Reserva(
    val id: Int,
    val clase: ClaseFitness,
    val fecha: String, // Fecha descriptiva "Hoy, 18:00"
    val estado: EstadoReserva,
    val timestamp: Long = System.currentTimeMillis()
)

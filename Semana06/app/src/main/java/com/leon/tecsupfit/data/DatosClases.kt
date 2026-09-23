package com.leon.tecsupfit.data

import androidx.compose.runtime.mutableStateListOf

/**
 * Datos de ejemplo de la app. Se usa un objeto simple en memoria
 * en lugar de un ViewModel para mantener el código fácil de leer.
 */
object DatosClases {

    val clases = listOf(
        ClaseFitness(
            id = 1,
            nombre = "Yoga funcional",
            hora = "7:00 am",
            sala = "Sala 2",
            duracionMin = 40,
            descripcion = "Sesión de yoga enfocada en movilidad y respiración para empezar el día.",
            cuposDisponibles = 10,
            cuposTotales = 15
        ),
        ClaseFitness(
            id = 2,
            nombre = "Cross Training",
            hora = "6:00 pm",
            sala = "Sala 1",
            duracionMin = 45,
            descripcion = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
            cuposDisponibles = 8,
            cuposTotales = 12
        ),
        ClaseFitness(
            id = 3,
            nombre = "Spinning",
            hora = "7:30 pm",
            sala = "Sala 3",
            duracionMin = 30,
            descripcion = "Rutina de ciclismo indoor al ritmo de la música.",
            cuposDisponibles = 6,
            cuposTotales = 20
        )
    )

    fun obtenerClasePorId(id: Int): ClaseFitness? =
        clases.find { it.id == id }

    // Lista mutable de reservas, empieza con datos de ejemplo iguales a la maqueta.
    val reservas = mutableStateListOf(
        Reserva(
            id = 1,
            clase = clases[1],
            fecha = "Hoy, 6:00 pm",
            estado = EstadoReserva.CONFIRMADA
        ),
        Reserva(
            id = 2,
            clase = clases[0],
            fecha = "Ayer, 7:00 am",
            estado = EstadoReserva.COMPLETADA
        )
    )

    fun reservarClase(clase: ClaseFitness) {
        reservas.add(
            0,
            Reserva(
                id = reservas.size + 1,
                clase = clase,
                fecha = "Hoy, ${clase.hora}",
                estado = EstadoReserva.CONFIRMADA
            )
        )
    }
}

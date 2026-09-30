package com.leon.tecsupfit.data

import androidx.compose.runtime.mutableStateListOf
import java.util.Calendar

object DatosClases {

    val clases = mutableStateListOf(
        // Lunes (1)
        ClaseFitness(1, "Yoga funcional", "07:00", "Sala 2", 40, "Sesión de yoga enfocada en movilidad y respiración.", 10, 15, null, 1),
        ClaseFitness(2, "Cross Training", "18:00", "Sala 1", 45, "Entrenamiento funcional de alta intensidad.", 8, 12, null, 1),
        ClaseFitness(3, "Spinning", "19:30", "Sala 3", 30, "Rutina de ciclismo indoor al ritmo de la música.", 6, 20, null, 1),
        
        // Martes (2)
        ClaseFitness(4, "Pilates Reformer", "08:00", "Sala 4", 50, "Fortalecimiento del core y flexibilidad con máquinas.", 4, 8, null, 2),
        ClaseFitness(5, "Baile Fit", "17:00", "Sala 2", 60, "Quema calorías bailando los ritmos más actuales.", 25, 30, null, 2),
        ClaseFitness(9, "Yoga Vinyasa", "19:00", "Sala 2", 60, "Fluidez y movimiento sincronizado.", 5, 15, null, 2),
        
        // Miércoles (3)
        ClaseFitness(6, "Boxeo Inicial", "19:00", "Sala 1", 45, "Técnica básica y cardio intenso.", 0, 15, null, 3),
        ClaseFitness(10, "Cross Training", "07:00", "Sala 1", 45, "Empieza el día con energía máxima.", 12, 12, null, 3),
        
        // Jueves (4)
        ClaseFitness(7, "Yoga Vinyasa", "09:00", "Sala 2", 60, "Fluidez y movimiento sincronizado.", 12, 20, null, 4),
        ClaseFitness(8, "Cardio Funcional", "18:30", "Sala 3", 45, "Mejora tu resistencia con ejercicios dinámicos.", 15, 20, null, 4),
        
        // Viernes (5)
        ClaseFitness(11, "Pilates Reformer", "17:00", "Sala 4", 50, "Cierra la semana fortaleciendo tu cuerpo.", 2, 8, null, 5),
        ClaseFitness(12, "Baile Fit", "19:00", "Sala 2", 60, "Fiesta de viernes en el gimnasio.", 20, 30, null, 5),
        
        // Sábado (6)
        ClaseFitness(13, "Cross Training", "10:00", "Sala 1", 60, "Sesión extendida de entrenamiento funcional.", 5, 20, null, 6),
        
        // Domingo (7)
        ClaseFitness(14, "Yoga Meditativo", "11:00", "Sala 2", 90, "Relajación profunda para renovarte.", 15, 15, null, 7)
    )

    fun obtenerClasePorId(id: Int): ClaseFitness? =
        clases.find { it.id == id }

    val reservas = mutableStateListOf<Reserva>()

    fun reservarClase(claseId: Int): Boolean {
        val index = clases.indexOfFirst { it.id == claseId }
        if (index == -1) return false
        
        val clase = clases[index]
        if (clase.cuposDisponibles <= 0 || estaReservada(claseId)) return false

        // Simulación de guardado atómico
        clases[index] = clase.copy(cuposDisponibles = clase.cuposDisponibles - 1)
        
        reservas.add(
            0,
            Reserva(
                id = reservas.size + 1,
                clase = clases[index],
                fecha = "Hoy, ${clase.hora}",
                estado = EstadoReserva.CONFIRMADA
            )
        )
        return true
    }

    fun cancelarReserva(reservaId: Int) {
        val indexReserva = reservas.indexOfFirst { it.id == reservaId }
        if (indexReserva != -1) {
            val reserva = reservas[indexReserva]
            val claseId = reserva.clase.id
            
            // Actualizar reserva a cancelada
            reservas[indexReserva] = reserva.copy(estado = EstadoReserva.CANCELADA)
            
            // Devolver cupo
            val indexClase = clases.indexOfFirst { it.id == claseId }
            if (indexClase != -1) {
                clases[indexClase] = clases[indexClase].copy(
                    cuposDisponibles = clases[indexClase].cuposDisponibles + 1
                )
            }
        }
    }

    fun estaReservada(claseId: Int): Boolean {
        return reservas.any { it.clase.id == claseId && it.estado == EstadoReserva.CONFIRMADA }
    }

    fun obtenerReservaPorClase(claseId: Int): Reserva? {
        return reservas.find { it.clase.id == claseId && it.estado == EstadoReserva.CONFIRMADA }
    }
}

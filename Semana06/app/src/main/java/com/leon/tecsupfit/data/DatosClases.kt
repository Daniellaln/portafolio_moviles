package com.leon.tecsupfit.data

<<<<<<< HEAD
import java.time.*

object DatosClases {
    val zona: ZoneId = ZoneId.of("America/Lima")

    private data class Plantilla(
        val id: Int,
        val dia: Int,
        val nombre: String,
        val hora: String,
        val sala: String,
        val minutos: Int,
        val descripcion: String,
        val total: Int,
        val ocupados: Int,
    )

    private val agenda =
        listOf(
            Plantilla(
                1,
                1,
                "Yoga funcional",
                "07:00",
                "Sala 2",
                40,
                "Sesión de yoga enfocada en movilidad y respiración.",
                15,
                5,
            ),
            Plantilla(
                2,
                1,
                "Cross Training",
                "18:00",
                "Sala 1",
                45,
                "Entrenamiento funcional de alta intensidad.",
                12,
                4,
            ),
            Plantilla(
                3,
                1,
                "Spinning",
                "19:30",
                "Sala 3",
                30,
                "Rutina de ciclismo indoor al ritmo de la música.",
                20,
                14,
            ),
            Plantilla(
                4,
                2,
                "Pilates Reformer",
                "08:00",
                "Sala 4",
                50,
                "Fortalecimiento del core y flexibilidad con máquinas.",
                8,
                4,
            ),
            Plantilla(
                5,
                2,
                "Baile Fit",
                "17:00",
                "Sala 2",
                60,
                "Quema calorías bailando los ritmos más actuales.",
                30,
                5,
            ),
            Plantilla(
                9,
                2,
                "Yoga Vinyasa",
                "19:00",
                "Sala 2",
                60,
                "Fluidez y movimiento sincronizado.",
                15,
                10,
            ),
            Plantilla(
                6,
                3,
                "Boxeo Inicial",
                "19:00",
                "Sala 1",
                45,
                "Técnica básica y cardio intenso.",
                15,
                15,
            ),
            Plantilla(
                10,
                3,
                "Cross Training",
                "07:00",
                "Sala 1",
                45,
                "Empieza el día con energía máxima.",
                12,
                0,
            ),
            Plantilla(
                7,
                4,
                "Yoga Vinyasa",
                "09:00",
                "Sala 2",
                60,
                "Fluidez y movimiento sincronizado.",
                20,
                8,
            ),
            Plantilla(
                8,
                4,
                "Cardio Funcional",
                "18:30",
                "Sala 3",
                45,
                "Mejora tu resistencia con ejercicios dinámicos.",
                20,
                5,
            ),
            Plantilla(
                11,
                5,
                "Pilates Reformer",
                "17:00",
                "Sala 4",
                50,
                "Cierra la semana fortaleciendo tu cuerpo.",
                8,
                6,
            ),
            Plantilla(
                12,
                5,
                "Baile Fit",
                "19:00",
                "Sala 2",
                60,
                "Fiesta de viernes en el gimnasio.",
                30,
                10,
            ),
            Plantilla(
                13,
                6,
                "Cross Training",
                "10:00",
                "Sala 1",
                60,
                "Sesión extendida de entrenamiento funcional.",
                20,
                15,
            ),
            Plantilla(
                14,
                7,
                "Yoga Meditativo",
                "11:00",
                "Sala 2",
                90,
                "Relajación profunda para renovarte.",
                15,
                0,
            ),
        )

    fun generar(hoy: LocalDate): List<ClaseFitness> =
        (0L..55L).flatMap { n ->
            val d = hoy.plusDays(n)
            agenda
                .filter { it.dia == d.dayOfWeek.value }
                .map { p ->
                    ClaseFitness(
                        "${d}_${p.id}",
                        p.nombre,
                        d.atTime(LocalTime.parse(p.hora)).atZone(zona).toInstant().toEpochMilli(),
                        p.minutos,
                        p.sala,
                        p.descripcion,
                        p.total,
                        p.ocupados,
                    )
                }
        }
=======
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
>>>>>>> sinia
}

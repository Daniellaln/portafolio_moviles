package com.leon.tecsupfit.data

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
}

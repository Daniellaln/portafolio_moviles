package com.leon.tecsupfit.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object DatosRutinas {
    val rutinas = listOf(
        Rutina(1, "Movilidad esencial", "Mejora tu rango de movimiento.", 12, "Ninguno / Mat", Intensidad.BAJA, 2, listOf(
            Ejercicio("Círculos de hombros", "Rota los hombros suavemente.", 50, 10),
            Ejercicio("Rotación torácica", "Abre el pecho en cuadrupedia.", 50, 10),
            Ejercicio("Bisagra de cadera", "Flexiona caderas.", 50, 10),
            Ejercicio("Gato-vaca", "Alterna arqueo y redondeo.", 50, 10),
            Ejercicio("Estiramiento lateral", "Flexiona el torso.", 50, 10),
            Ejercicio("Marcha suave", "Eleva rodillas.", 50, 10)
        ), "movilidad"),
        Rutina(2, "Fuerza base", "Entrenamiento de fuerza funcional.", 18, "Mancuernas", Intensidad.MEDIA, 3, listOf(
            Ejercicio("Goblet Squat", "Mancuerna al pecho y baja.", 50, 10),
            Ejercicio("Push-ups", "Flexiones de brazos.", 50, 10),
            Ejercicio("Remo con mancuerna", "Lleva el peso a la cadera.", 50, 10),
            Ejercicio("Overhead Press", "Empuja sobre la cabeza.", 50, 10),
            Ejercicio("Zancadas", "Da un paso adelante.", 50, 10),
            Ejercicio("Plancha frontal", "Cuerpo recto en antebrazos.", 50, 10)
        ), "fuerza"),
        Rutina(3, "Cardio suave", "Mejora tu resistencia sin impacto.", 15, "Ninguno", Intensidad.MEDIA, 3, listOf(
            Ejercicio("Jumping Jacks", "Abre y cierra saltando.", 50, 10),
            Ejercicio("Mountain Climbers", "Rodillas al pecho en plancha.", 50, 10),
            Ejercicio("Butt Kicks", "Talones a los glúteos.", 50, 10),
            Ejercicio("High Knees", "Eleva rodillas rápido.", 50, 10),
            Ejercicio("Shadow Boxing", "Lanza golpes al aire.", 50, 10)
        ), "cardio"),
        Rutina(4, "Core estable", "Fortalece tu zona media.", 12, "Mat", Intensidad.MEDIA, 2, listOf(
            Ejercicio("Dead Bug", "Mueve brazo y pierna opuesta.", 50, 10),
            Ejercicio("Bird Dog", "Extiende brazo y pierna opuesta.", 50, 10),
            Ejercicio("Russian Twist", "Rota el torso sentado.", 50, 10),
            Ejercicio("Leg Raises", "Eleva y baja las piernas.", 50, 10),
            Ejercicio("Hollow Hold", "Extremidades elevadas.", 50, 10),
            Ejercicio("Bicycle Crunches", "Codo a rodilla opuesta.", 50, 10)
        ), "core"),
        Rutina(5, "Estiramientos", "Relaja la musculatura.", 10, "Mat", Intensidad.BAJA, 1, listOf(
            Ejercicio("Cobra Pose", "Estira el abdomen.", 50, 10),
            Ejercicio("Child's Pose", "Siéntate sobre talones.", 50, 10),
            Ejercicio("Pigeon Pose", "Estiramiento de glúteos.", 50, 10),
            Ejercicio("Hamstring Stretch", "Piernas posteriores.", 50, 10),
            Ejercicio("Quad Stretch", "Talón al glúteo.", 50, 10),
            Ejercicio("Chest Opener", "Abre brazos y junta escápulas.", 50, 10),
            Ejercicio("Neck Stretch", "Inclina la cabeza.", 50, 10),
            Ejercicio("Butterfly Stretch", "Junta plantas de pies.", 50, 10),
            Ejercicio("Shoulder Stretch", "Brazo por delante.", 50, 10),
            Ejercicio("Calf Stretch", "Estira gemelos.", 50, 10)
        ), "estiramientos"),
        Rutina(6, "Recuperación", "Enfocada en la respiración.", 8, "Ninguno", Intensidad.BAJA, 1, listOf(
            Ejercicio("Respiración diafragmática", "Llena el abdomen.", 50, 10),
            Ejercicio("Knee to Chest", "Rodillas al pecho tumbado.", 50, 10),
            Ejercicio("Happy Baby", "Sujeta pies y balancéate.", 50, 10),
            Ejercicio("Spinal Twist", "Rota la cadera.", 50, 10),
            Ejercicio("Movilidad de tobillos", "Dibuja círculos.", 50, 10),
            Ejercicio("Shake", "Sacude extremidades.", 50, 10),
            Ejercicio("Mindful Sitting", "Observa tu cuerpo.", 50, 10),
            Ejercicio("Deep Breathing", "Respira lento.", 50, 10)
        ), "recuperacion")
    )

    val sesionesCompletadas = mutableStateListOf<SesionCompletada>()
    
    // Rastreo de sesión a medias
    var rutinaEnCursoId by mutableStateOf<Int?>(null)
    var ultimaPreferenciaIntensidad by mutableStateOf(Intensidad.MEDIA)

    fun registrarSesion(rutina: Rutina) {
        sesionesCompletadas.add(SesionCompletada(rutina.id, rutina.nombre))
        rutinaEnCursoId = null
        ultimaPreferenciaIntensidad = rutina.intensidad
    }

    fun obtenerRutinaPorId(id: Int): Rutina? = rutinas.find { it.id == id }
}

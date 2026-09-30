package com.leon.tecsupfit.navigation

object Pantallas {
    const val INICIO = "inicio"
    const val DETALLE = "detalle/{claseId}"
    const val CONFIRMACION = "confirmacion/{claseId}"
    const val RESERVAS = "reservas"
    const val RUTINAS = "rutinas"
    const val DETALLE_RUTINA = "detalle_rutina/{rutinaId}"
    const val SESION_RUTINA = "sesion_rutina/{rutinaId}"
    const val RUTINA_COMPLETADA = "rutina_completada/{rutinaId}"
    const val PERFIL = "perfil"

    fun detalleRuta(claseId: Int) = "detalle/$claseId"
    fun confirmacionRuta(claseId: Int) = "confirmacion/$claseId"
    fun detalleRutinaRuta(rutinaId: Int) = "detalle_rutina/$rutinaId"
    fun sesionRutinaRuta(rutinaId: Int) = "sesion_rutina/$rutinaId"
    fun rutinaCompletadaRuta(rutinaId: Int) = "rutina_completada/$rutinaId"
}

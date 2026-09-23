package com.leon.tecsupfit.navigation

/**
 * Rutas de navegación de la app. Se usa un objeto simple con
 * constantes de texto para que sea fácil de leer y de ampliar.
 */
object Pantallas {
    const val INICIO = "inicio"
    const val DETALLE = "detalle/{claseId}"
    const val CONFIRMACION = "confirmacion/{claseId}"
    const val RESERVAS = "reservas"
    const val RUTINAS = "rutinas"
    const val PERFIL = "perfil"

    fun detalleRuta(claseId: Int) = "detalle/$claseId"
    fun confirmacionRuta(claseId: Int) = "confirmacion/$claseId"
}

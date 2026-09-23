package com.leon.lab04carritotecsup_leon.data

// Modelo del Lab 02: nombre, precio y cantidad
data class Producto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int
) {
    // Importe de la tarjeta: precio x cantidad
    val importe: Double
        get() = precio * cantidad
}

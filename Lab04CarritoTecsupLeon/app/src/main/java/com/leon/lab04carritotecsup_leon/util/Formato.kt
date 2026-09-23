package com.leon.lab04carritotecsup_leon.util

import java.util.Locale

// Todos los montos con 2 decimales y punto: "S/ 2500.00"
fun formatoSoles(valor: Double): String =
    "S/ " + String.format(Locale.US, "%.2f", valor)

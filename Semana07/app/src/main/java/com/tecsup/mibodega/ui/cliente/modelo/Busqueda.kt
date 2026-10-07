package com.tecsup.mibodega.ui.cliente.modelo

import java.text.Normalizer
import java.util.Locale

private fun String.normalizada() = Normalizer.normalize(trim(), Normalizer.Form.NFD)
    .replace(Regex("\\p{M}+"), "").lowercase(Locale.ROOT)

fun filtrarProductos(productos: List<Producto>, categoria: String, busqueda: String): List<Producto> {
    val palabras = busqueda.normalizada().split(Regex("\\s+")).filter { it.isNotEmpty() }
    return productos.filter { producto ->
        (categoria == "Todos" || producto.categoria == categoria) &&
            palabras.all { producto.nombre.normalizada().contains(it) }
    }
}
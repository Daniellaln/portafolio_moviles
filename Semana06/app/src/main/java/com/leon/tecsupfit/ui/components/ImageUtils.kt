package com.leon.tecsupfit.ui.components

import com.leon.tecsupfit.R
import java.text.Normalizer

object ImageUtils {
    
    // Mapeo basado en catalogo_imagenes.json y ampliación solicitada
    private val mappingClases = mapOf(
        "yoga funcional" to R.drawable.foto_yoga_funcional,
        "yoga vinyasa" to R.drawable.foto_yoga_funcional,
        "yoga" to R.drawable.foto_yoga_funcional,
        "cross training" to R.drawable.foto_cross_training,
        "funcional" to R.drawable.foto_cross_training,
        "spinning" to R.drawable.foto_spinning,
        "pilates reformer" to R.drawable.foto_clase_pilates,
        "pilates" to R.drawable.foto_clase_pilates,
        "baile fit" to R.drawable.foto_clase_baile,
        "baile" to R.drawable.foto_clase_baile,
        "boxeo inicial" to R.drawable.foto_clase_boxeo,
        "boxeo" to R.drawable.foto_clase_boxeo
    )

    private val mappingRutinas = mapOf(
        "movilidad" to R.drawable.foto_rutina_movilidad,
        "movilidad esencial" to R.drawable.foto_rutina_movilidad,
        "fuerza base" to R.drawable.foto_rutina_fuerza,
        "fuerza" to R.drawable.foto_rutina_fuerza,
        "mancuernas" to R.drawable.foto_rutina_fuerza,
        "pesas" to R.drawable.foto_rutina_fuerza,
        "cardio suave" to R.drawable.foto_rutina_cardio,
        "cardio" to R.drawable.foto_rutina_cardio,
        "core estable" to R.drawable.foto_rutina_core,
        "core" to R.drawable.foto_rutina_core,
        "abdomen" to R.drawable.foto_rutina_core,
        "abdominales" to R.drawable.foto_rutina_core,
        "estiramientos" to R.drawable.foto_rutina_estiramientos,
        "flexibilidad" to R.drawable.foto_rutina_estiramientos,
        "recuperacion" to R.drawable.foto_rutina_recuperacion,
        "respiracion" to R.drawable.foto_rutina_recuperacion,
        "descanso" to R.drawable.foto_rutina_recuperacion
    )

    fun getDrawableForNombre(nombre: String, isClase: Boolean = true, imageKey: String? = null): Int {
        if (imageKey != null) {
            val keyNormalized = normalizeString(imageKey)
            val found = if (isClase) mappingClases[keyNormalized] else mappingRutinas[keyNormalized]
            if (found != null) return found
        }

        val normalized = normalizeString(nombre)
        val targetMap = if (isClase) mappingClases else mappingRutinas
        
        // Buscamos la coincidencia más específica (llave más larga contenida)
        val key = targetMap.keys
            .filter { normalized.contains(it) }
            .maxByOrNull { it.length }

        return key?.let { targetMap[it] } ?: R.drawable.foto_actividad_general
    }

    private fun normalizeString(str: String): String {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
            .lowercase()
            .trim()
    }
}

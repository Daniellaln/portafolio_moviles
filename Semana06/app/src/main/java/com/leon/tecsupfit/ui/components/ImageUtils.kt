package com.leon.tecsupfit.ui.components

import com.leon.tecsupfit.R
import java.text.Normalizer
import java.util.Locale

object ImageUtils {
    fun normalizar(s: String) =
        Normalizer.normalize(s, Normalizer.Form.NFD)
            .replace(Regex("\\p{M}+"), "")
            .lowercase(Locale.ROOT)
            .replace(Regex("\\s+"), " ")
            .trim()

    private val recursos =
        mapOf(
            "foto_yoga_funcional" to R.drawable.foto_yoga_funcional,
            "foto_cross_training" to R.drawable.foto_cross_training,
            "foto_spinning" to R.drawable.foto_spinning,
            "foto_clase_pilates" to R.drawable.foto_clase_pilates,
            "foto_clase_baile" to R.drawable.foto_clase_baile,
            "foto_clase_boxeo" to R.drawable.foto_clase_boxeo,
            "foto_rutina_movilidad" to R.drawable.foto_rutina_movilidad,
            "foto_rutina_fuerza" to R.drawable.foto_rutina_fuerza,
            "foto_rutina_cardio" to R.drawable.foto_rutina_cardio,
            "foto_rutina_core" to R.drawable.foto_rutina_core,
            "foto_rutina_estiramientos" to R.drawable.foto_rutina_estiramientos,
            "foto_rutina_recuperacion" to R.drawable.foto_rutina_recuperacion,
            "foto_actividad_general" to R.drawable.foto_actividad_general,
        )
    private val aliases =
        listOf(
                "hatha" to R.drawable.foto_yoga_funcional,
                "vinyasa" to R.drawable.foto_yoga_funcional,
                "yoga" to R.drawable.foto_yoga_funcional,
                "yoga funcional" to R.drawable.foto_yoga_funcional,
                "circuito" to R.drawable.foto_cross_training,
                "cross training" to R.drawable.foto_cross_training,
                "cross_training" to R.drawable.foto_cross_training,
                "crossfit" to R.drawable.foto_cross_training,
                "entrenamiento funcional" to R.drawable.foto_cross_training,
                "funcional" to R.drawable.foto_cross_training,
                "bici" to R.drawable.foto_spinning,
                "bicicleta" to R.drawable.foto_spinning,
                "ciclismo" to R.drawable.foto_spinning,
                "cycling" to R.drawable.foto_spinning,
                "indoor bike" to R.drawable.foto_spinning,
                "spinning" to R.drawable.foto_spinning,
                "pilates" to R.drawable.foto_clase_pilates,
                "reformer" to R.drawable.foto_clase_pilates,
                "baile" to R.drawable.foto_clase_baile,
                "baile fit" to R.drawable.foto_clase_baile,
                "dance" to R.drawable.foto_clase_baile,
                "ritmos" to R.drawable.foto_clase_baile,
                "zumba" to R.drawable.foto_clase_baile,
                "box" to R.drawable.foto_clase_boxeo,
                "boxeo" to R.drawable.foto_clase_boxeo,
                "boxing" to R.drawable.foto_clase_boxeo,
                "kickboxing" to R.drawable.foto_clase_boxeo,
                "articulaciones" to R.drawable.foto_rutina_movilidad,
                "movilidad" to R.drawable.foto_rutina_movilidad,
                "movilidad esencial" to R.drawable.foto_rutina_movilidad,
                "fuerza" to R.drawable.foto_rutina_fuerza,
                "mancuernas" to R.drawable.foto_rutina_fuerza,
                "musculacion" to R.drawable.foto_rutina_fuerza,
                "pesas" to R.drawable.foto_rutina_fuerza,
                "strength" to R.drawable.foto_rutina_fuerza,
                "aerobico" to R.drawable.foto_rutina_cardio,
                "caminata" to R.drawable.foto_rutina_cardio,
                "cardio" to R.drawable.foto_rutina_cardio,
                "cardio funcional" to R.drawable.foto_rutina_cardio,
                "marcha" to R.drawable.foto_rutina_cardio,
                "resistencia" to R.drawable.foto_rutina_cardio,
                "abdomen" to R.drawable.foto_rutina_core,
                "abdominal" to R.drawable.foto_rutina_core,
                "core" to R.drawable.foto_rutina_core,
                "estabilidad central" to R.drawable.foto_rutina_core,
                "estiramiento" to R.drawable.foto_rutina_estiramientos,
                "estiramientos" to R.drawable.foto_rutina_estiramientos,
                "flexibilidad" to R.drawable.foto_rutina_estiramientos,
                "stretch" to R.drawable.foto_rutina_estiramientos,
                "cool down" to R.drawable.foto_rutina_recuperacion,
                "descanso" to R.drawable.foto_rutina_recuperacion,
                "recuperacion" to R.drawable.foto_rutina_recuperacion,
                "relajacion" to R.drawable.foto_rutina_recuperacion,
                "respiracion" to R.drawable.foto_rutina_recuperacion,
            )
            .sortedByDescending { it.first.length }

    fun imagen(nombre: String, imageKey: String? = null): Int {
        imageKey?.let { key ->
            recursos[key]?.let {
                return it
            }
            aliases
                .firstOrNull { it.first == normalizar(key) }
                ?.let {
                    return it.second
                }
        }
        val n = normalizar(nombre)
        return aliases
            .firstOrNull {
                Regex("(?:^|[^a-z0-9])" + Regex.escape(it.first) + "(?:$|[^a-z0-9])")
                    .containsMatchIn(n)
            }
            ?.second ?: R.drawable.foto_actividad_general
    }
}

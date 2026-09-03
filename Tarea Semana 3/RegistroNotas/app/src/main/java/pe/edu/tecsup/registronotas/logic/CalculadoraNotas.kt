package pe.edu.tecsup.registronotas.logic

import pe.edu.tecsup.registronotas.model.Curso
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Observación que la app entrega según el PROMEDIO FINAL.
 * Los rangos salen de la tabla de la sección III del enunciado.
 */
enum class Observacion(val etiqueta: String) {
    EXCELENTE("EXCELENTE"),
    APROBADO("APROBADO"),
    EN_RECUPERACION("EN RECUPERACIÓN"),
    DESAPROBADO("DESAPROBADO")
}

/**
 * Resultado completo de un cálculo, listo para pintarse en la tarjeta.
 */
data class ResultadoNotas(
    val promedioPonderado: Double,
    val promedioFinal: Double,
    val fueRedondeado: Boolean,
    val observacion: Observacion
) {
    /** Promedio ponderado siempre con 2 decimales: "14.55". */
    val ponderadoTexto: String
        get() = String.format(Locale.US, "%.2f", promedioPonderado)

    /**
     * Promedio final: entero si el Switch estaba activo ("15"),
     * o con 2 decimales si no lo estaba ("10.45").
     */
    val finalTexto: String
        get() = if (fueRedondeado) {
            promedioFinal.roundToInt().toString()
        } else {
            String.format(Locale.US, "%.2f", promedioFinal)
        }
}

/**
 * Toda la matemática del ejercicio vive aquí, separada de la UI.
 * Al ser funciones puras se pueden probar con JUnit sin levantar la app
 * (ver CalculadoraNotasTest).
 */
object CalculadoraNotas {

    /**
     * Promedio ponderado = nota1×0.20 + nota2×0.25 + nota3×0.30 + nota4×0.25
     *
     * Se calcula como (nota1×20 + nota2×25 + nota3×30 + nota4×25) / 100.0
     * para mantener la suma en enteros y no arrastrar error de punto flotante.
     */
    fun promedioPonderado(notas: List<Int>, cursos: List<Curso>): Double {
        require(notas.size == cursos.size) {
            "Se esperaban ${cursos.size} notas y llegaron ${notas.size}"
        }
        val sumaEnPuntos = notas.zip(cursos) { nota, curso ->
            nota * curso.pesoPorcentaje
        }.sum()
        return sumaEnPuntos / 100.0
    }

    /**
     * Promedio final: si el Switch está ON se redondea al entero más cercano
     * con roundToInt(); si está OFF se devuelve el ponderado tal cual.
     */
    fun promedioFinal(promedioPonderado: Double, redondear: Boolean): Double =
        if (redondear) promedioPonderado.roundToInt().toDouble() else promedioPonderado

    /**
     * Observación según el promedio FINAL (no el ponderado), usando when.
     *
     *  17 a 20        -> EXCELENTE
     *  13 a 16.99     -> APROBADO
     *  10 a 12.99     -> EN RECUPERACIÓN
     *  menor a 10     -> DESAPROBADO
     */
    fun observacion(promedioFinal: Double): Observacion = when {
        promedioFinal >= 17.0 -> Observacion.EXCELENTE
        promedioFinal >= 13.0 -> Observacion.APROBADO
        promedioFinal >= 10.0 -> Observacion.EN_RECUPERACION
        else -> Observacion.DESAPROBADO
    }

    /** Une los tres pasos anteriores en el resultado que consume la UI. */
    fun calcular(notas: List<Int>, cursos: List<Curso>, redondear: Boolean): ResultadoNotas {
        val ponderado = promedioPonderado(notas, cursos)
        val final = promedioFinal(ponderado, redondear)
        return ResultadoNotas(
            promedioPonderado = ponderado,
            promedioFinal = final,
            fueRedondeado = redondear,
            observacion = observacion(final)
        )
    }
}

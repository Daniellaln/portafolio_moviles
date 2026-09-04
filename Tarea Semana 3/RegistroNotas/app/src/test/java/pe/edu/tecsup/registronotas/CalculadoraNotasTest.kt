package pe.edu.tecsup.registronotas

import org.junit.Assert.assertEquals
import org.junit.Test
import pe.edu.tecsup.registronotas.logic.CalculadoraNotas
import pe.edu.tecsup.registronotas.logic.Observacion
import pe.edu.tecsup.registronotas.model.CURSOS

/**
 * Los cuatro casos de prueba de la seccion III del enunciado.
 * Se comprueba el texto exacto que la app muestra en pantalla, no solo el
 * numero, para que no se escape ningun problema de formato o de redondeo.
 *
 * Ejecutar con:  ./gradlew test
 */
class CalculadoraNotasTest {

    private fun caso(
        notas: List<Int>,
        redondear: Boolean,
        ponderadoEsperado: String,
        finalEsperado: String,
        observacionEsperada: Observacion
    ) {
        val r = CalculadoraNotas.calcular(notas, CURSOS, redondear)
        assertEquals(ponderadoEsperado, r.ponderadoTexto)
        assertEquals(finalEsperado, r.finalTexto)
        assertEquals(observacionEsperada, r.observacion)
    }

    @Test
    fun caso1_quinceTreceDieciseisCatorce_redondeoOn() {
        caso(listOf(15, 13, 16, 14), true, "14.55", "15", Observacion.APROBADO)
    }

    @Test
    fun caso2_doceDiezOnceNueve_redondeoOff() {
        caso(listOf(12, 10, 11, 9), false, "10.45", "10.45", Observacion.EN_RECUPERACION)
    }

    @Test
    fun caso3_dieciochoDiecisieteDiecinueveDieciocho_redondeoOn() {
        caso(listOf(18, 17, 19, 18), true, "18.05", "18", Observacion.EXCELENTE)
    }

    @Test
    fun caso4_ochoNueveSieteDiez_redondeoOff() {
        caso(listOf(8, 9, 7, 10), false, "8.45", "8.45", Observacion.DESAPROBADO)
    }

    @Test
    fun losPesosSuman100() {
        assertEquals(100, CURSOS.sumOf { it.pesoPorcentaje })
    }

    @Test
    fun limitesDeLosRangosDeObservacion() {
        assertEquals(Observacion.EXCELENTE, CalculadoraNotas.observacion(17.0))
        assertEquals(Observacion.APROBADO, CalculadoraNotas.observacion(16.99))
        assertEquals(Observacion.APROBADO, CalculadoraNotas.observacion(13.0))
        assertEquals(Observacion.EN_RECUPERACION, CalculadoraNotas.observacion(12.99))
        assertEquals(Observacion.EN_RECUPERACION, CalculadoraNotas.observacion(10.0))
        assertEquals(Observacion.DESAPROBADO, CalculadoraNotas.observacion(9.99))
    }
}

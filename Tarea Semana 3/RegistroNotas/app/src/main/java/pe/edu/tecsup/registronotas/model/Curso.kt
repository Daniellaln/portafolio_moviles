package pe.edu.tecsup.registronotas.model

/**
 * Representa un curso del ciclo con su peso dentro del promedio ponderado.
 *
 * El peso se guarda como PORCENTAJE ENTERO (20, 25, 30, 25) y no como decimal
 * (0.20, 0.25...). Trabajar con enteros evita los errores de redondeo del punto
 * flotante: la suma se hace en enteros y recién al final se divide entre 100.
 * Gracias a eso los casos de prueba dan valores exactos (por ejemplo 8.45 y no
 * 8.4499999...).
 */
data class Curso(
    val nombre: String,
    val pesoPorcentaje: Int
) {
    /** Texto del peso listo para mostrar en la UI, por ejemplo "20%". */
    val pesoTexto: String get() = "$pesoPorcentaje%"
}

/** Los cuatro cursos del ciclo. Los pesos son fijos y suman 100%. */
val CURSOS = listOf(
    Curso("Fundamentos de Programación", 20),
    Curso("Programación Orientada a Objetos", 25),
    Curso("Programación en Móviles", 30),
    Curso("Base de Datos", 25)
)

/** Nota mínima y máxima que admite cada slider. */
const val NOTA_MINIMA = 0
const val NOTA_MAXIMA = 20

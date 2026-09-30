package com.leon.tecsupfit.data

import androidx.room.*

@Entity(
    tableName = "reservas",
    foreignKeys =
        [
            ForeignKey(
                entity = ClaseFitness::class,
                parentColumns = ["id"],
                childColumns = ["claseId"],
                onDelete = ForeignKey.RESTRICT,
            )
        ],
    indices = [Index("claseId"), Index(value = ["ocurrenciaActiva"], unique = true)],
)
data class Reserva(
    @PrimaryKey val id: String,
    val claseId: String,
    val creadaEn: Long,
    val estado: String = "CONFIRMADA",
    val ocurrenciaActiva: String? = claseId,
    val canceladaEn: Long? = null,
)

data class ReservaConClase(val reserva: Reserva, val clase: ClaseFitness) {
    fun proxima(ahora: Long) = reserva.estado == "CONFIRMADA" && clase.inicio > ahora

    fun enCurso(ahora: Long) =
        reserva.estado == "CONFIRMADA" && ahora >= clase.inicio && ahora < clase.fin

    fun etiqueta(ahora: Long) =
        when {
            reserva.estado == "CANCELADA" -> "Cancelada"
            ahora >= clase.fin -> "Finalizada"
            ahora >= clase.inicio -> "En curso"
            else -> "Confirmada"
        }
}

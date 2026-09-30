package com.leon.tecsupfit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clases")
data class ClaseFitness(
    @PrimaryKey val id: String,
    val nombre: String,
    val inicio: Long,
    val duracionMin: Int,
    val sala: String,
    val descripcion: String,
    val cuposTotales: Int,
    val ocupadosIniciales: Int = 0,
    val imageKey: String? = null,
) {
    val fin: Long
        get() = inicio + duracionMin * 60_000L
}

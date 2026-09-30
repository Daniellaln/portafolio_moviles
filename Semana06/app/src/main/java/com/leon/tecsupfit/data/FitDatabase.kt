package com.leon.tecsupfit.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.json.*

class Conversores {
    @TypeConverter
    fun guardarEjercicios(items: List<Ejercicio>): String =
        JSONArray()
            .apply {
                items.forEach { e ->
                    put(
                        JSONObject()
                            .put("nombre", e.nombre)
                            .put("instruccion", e.instruccion)
                            .put("activo", e.segundosActivos)
                            .put("descanso", e.segundosTransicion)
                            .put("imagen", e.imageKey)
                    )
                }
            }
            .toString()

    @TypeConverter
    fun leerEjercicios(json: String): List<Ejercicio> =
        JSONArray(json).let { a ->
            (0 until a.length()).map { i ->
                a.getJSONObject(i).let { e ->
                    Ejercicio(
                        e.getString("nombre"),
                        e.getString("instruccion"),
                        e.getInt("activo"),
                        e.getInt("descanso"),
                        if (e.isNull("imagen")) null else e.getString("imagen"),
                    )
                }
            }
        }

    @TypeConverter fun guardarIntensidad(v: Intensidad) = v.name

    @TypeConverter fun leerIntensidad(v: String) = Intensidad.valueOf(v)
}

@Dao
interface FitDao {
    @Query("SELECT * FROM clases ORDER BY inicio") fun observarClases(): Flow<List<ClaseFitness>>

    @Query("SELECT * FROM reservas ORDER BY creadaEn DESC")
    fun observarReservas(): Flow<List<Reserva>>

    @Query("SELECT * FROM rutinas ORDER BY propia DESC, id ASC")
    fun observarRutinas(): Flow<List<Rutina>>

    @Query("SELECT * FROM sesiones ORDER BY creadaEn DESC")
    fun observarSesiones(): Flow<List<SesionRutina>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun sembrarClases(items: List<ClaseFitness>)

    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun sembrarRutinas(items: List<Rutina>)

    @Upsert suspend fun guardarRutina(r: Rutina)

    @Query("DELETE FROM rutinas WHERE id=:id AND propia=1") suspend fun borrarRutina(id: String)

    @Query("SELECT * FROM clases WHERE id=:id") suspend fun clase(id: String): ClaseFitness?

    @Query("SELECT * FROM reservas WHERE id=:id") suspend fun reserva(id: String): Reserva?

    @Query("SELECT COUNT(*) FROM reservas WHERE claseId=:id AND estado='CONFIRMADA'")
    suspend fun ocupacion(id: String): Int

    @Insert suspend fun insertarReserva(r: Reserva)

    @Query(
        "UPDATE reservas SET estado='CANCELADA',ocurrenciaActiva=NULL,canceladaEn=:ahora WHERE id=:id AND estado='CONFIRMADA'"
    )
    suspend fun cancelar(id: String, ahora: Long): Int

    @Query("SELECT * FROM rutinas WHERE id=:id") suspend fun rutina(id: String): Rutina?

    @Query("SELECT * FROM sesiones WHERE estado='ACTIVA' LIMIT 1")
    suspend fun activa(): SesionRutina?

    @Query("SELECT * FROM sesiones WHERE id=:id") suspend fun sesion(id: String): SesionRutina?

    @Upsert suspend fun guardarSesion(s: SesionRutina)
}

@Database(
    entities = [ClaseFitness::class, Reserva::class, Rutina::class, SesionRutina::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Conversores::class)
abstract class FitDatabase : RoomDatabase() {
    abstract fun dao(): FitDao

    companion object {
        @Volatile private var instancia: FitDatabase? = null

        fun abrir(c: Context): FitDatabase =
            instancia
                ?: synchronized(this) {
                    instancia
                        ?: Room.databaseBuilder(
                                c.applicationContext,
                                FitDatabase::class.java,
                                "tecsup_fit.db",
                            )
                            .build()
                            .also { instancia = it }
                }
    }
}

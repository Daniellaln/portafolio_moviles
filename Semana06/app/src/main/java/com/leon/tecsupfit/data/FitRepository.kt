package com.leon.tecsupfit.data

import androidx.room.withTransaction
import java.time.*
import java.util.UUID

class FitRepository(
    private val db: FitDatabase,
    private val reloj: () -> Long = System::currentTimeMillis,
) {
    val dao = db.dao()

    suspend fun iniciar() = db.withTransaction {
        dao.sembrarClases(
            DatosClases.generar(
                Instant.ofEpochMilli(reloj()).atZone(DatosClases.zona).toLocalDate()
            )
        )
        dao.sembrarRutinas(DatosRutinas.rutinas)
        dao.activa()?.let { dao.guardarSesion(it.copy(corriendo = false, anclaMs = reloj())) }
    }

    suspend fun reservar(id: String): String = db.withTransaction {
        val c = requireNotNull(dao.clase(id)) { "Esta clase no está disponible." }
        require(c.inicio > reloj()) { "Esta clase ya empezó. Elige otro horario." }
        require(dao.ocupacion(id) == 0) { "Ya tienes una reserva para esta clase." }
        require(c.cuposTotales - c.ocupadosIniciales - dao.ocupacion(id) > 0) { "No quedan cupos." }
        UUID.randomUUID().toString().also { dao.insertarReserva(Reserva(it, id, reloj())) }
    }

    suspend fun cancelar(id: String) = db.withTransaction {
        val r = requireNotNull(dao.reserva(id))
        val c = requireNotNull(dao.clase(r.claseId))
        require(reloj() < c.inicio) { "La clase ya comenzó; no se puede cancelar." }
        dao.cancelar(id, reloj())
    }

    suspend fun guardar(r: Rutina) = db.withTransaction {
        require(r.nombre.isNotBlank() && r.nombre.length <= 70) {
            "Escribe un nombre de hasta 70 caracteres."
        }
        require(r.vueltas in 1..10 && r.ejercicios.size in 1..30) {
            "Añade entre 1 y 30 ejercicios y entre 1 y 10 vueltas."
        }
        require(
            r.ejercicios.all {
                it.nombre.isNotBlank() &&
                    it.segundosActivos in 5..600 &&
                    it.segundosTransicion in 0..300
            }
        ) {
            "Revisa los tiempos: actividad 5–600 s; descanso 0–300 s."
        }
        require(dao.rutina(r.id)?.propia != false) {
            "Duplica el plan original para personalizarlo."
        }
        dao.guardarRutina(r.copy(propia = true))
    }

    suspend fun eliminar(id: String) = db.withTransaction {
        require(dao.activa()?.rutinaId != id) {
            "Termina o abandona la sesión antes de borrar esta rutina."
        }
        dao.borrarRutina(id)
    }

    suspend fun empezar(id: String): String = db.withTransaction {
        dao.activa()?.let {
            require(it.rutinaId == id) {
                "Tienes otra sesión en pausa. Retómala o termínala primero."
            }
            return@withTransaction it.id
        }
        val r = requireNotNull(dao.rutina(id))
        val s =
            SesionRutina(
                UUID.randomUUID().toString(),
                r.id,
                r.nombre,
                r.ejercicios,
                r.vueltas,
                anclaMs = reloj(),
                creadaEn = reloj(),
                corriendo = true,
            )
        dao.guardarSesion(s)
        s.id
    }

    suspend fun sesion(id: String, accion: String) = db.withTransaction {
        val original = dao.sesion(id) ?: return@withTransaction
        if (original.estado != "ACTIVA") return@withTransaction
        val ahora = reloj()
        val s = MotorRutina.actualizar(original, ahora)
        dao.guardarSesion(
            when {
                s.estado != "ACTIVA" -> s
                accion == "pausa" -> s.copy(corriendo = false)
                accion == "continuar" -> s.copy(corriendo = true, anclaMs = ahora)
                accion == "saltar" -> MotorRutina.saltar(s, ahora)
                accion == "abandonar" -> s.copy(corriendo = false, estado = "ABANDONADA")
                else -> s
            }
        )
    }

    suspend fun pausarActiva() {
        dao.activa()?.let { sesion(it.id, "pausa") }
    }
}

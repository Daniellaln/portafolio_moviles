package com.leon.tecsupfit

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.ImageUtils
import com.leon.tecsupfit.ui.screens.unirReservas
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class FitRepositoryTest {
    private lateinit var db: FitDatabase
    private lateinit var repo: FitRepository
    private lateinit var context: Context
    private var ahora = 1_900_000_000_000L

    @Before
    fun iniciar() {
        context = ApplicationProvider.getApplicationContext()
        context.deleteDatabase("prueba-fit.db")
        abrir()
    }

    private fun abrir() {
        db =
            Room.databaseBuilder(context, FitDatabase::class.java, "prueba-fit.db")
                .allowMainThreadQueries()
                .build()
        repo = FitRepository(db) { ahora }
    }

    @After
    fun cerrar() {
        db.close()
        context.deleteDatabase("prueba-fit.db")
    }

    private fun clase(id: String, minutos: Int, capacidad: Int = 10, ocupados: Int = 0) =
        ClaseFitness(
            id,
            id,
            ahora + minutos * 60_000L,
            45,
            "Sala 1",
            "Descripción",
            capacidad,
            ocupados,
        )

    private fun rutina() =
        Rutina(
            "propia",
            "Yoga matinal",
            "Mi plan",
            "Mat",
            Intensidad.BAJA,
            2,
            listOf(Ejercicio("Respirar", "Lentamente", 5, 2)),
            propia = true,
        )

    @Test
    fun reservaProximaUsaHorarioNoCreacion() = runBlocking {
        val a = clase("temprano", 23)
        val b = clase("viernes", 3000)
        db.dao().sembrarClases(listOf(a, b))
        repo.reservar(a.id)
        ahora += 1000
        repo.reservar(b.id)
        val orden =
            unirReservas(db.dao().observarReservas().first(), db.dao().observarClases().first())
                .filter { it.proxima(ahora) }
                .sortedBy { it.clase.inicio }
        assertEquals("temprano", orden.first().clase.id)
    }

    @Test
    fun persisteAlCerrarYReabrirBase() = runBlocking {
        db.dao().sembrarClases(listOf(clase("a", 30)))
        val r = repo.reservar("a")
        repo.guardar(rutina())
        val s = repo.empezar("propia")
        ahora += 2000
        repo.sesion(s, "pausa")
        db.close()
        abrir()
        assertEquals("CONFIRMADA", db.dao().reserva(r)?.estado)
        assertEquals("Yoga matinal", db.dao().rutina("propia")?.nombre)
        assertEquals(2000L, db.dao().sesion(s)?.progresoMs)
        assertFalse(db.dao().sesion(s)!!.corriendo)
    }

    @Test
    fun cancelarDosVecesNoCreaCupos() = runBlocking {
        db.dao().sembrarClases(listOf(clase("a", 30, 1)))
        val id = repo.reservar("a")
        assertEquals(1, db.dao().ocupacion("a"))
        repo.cancelar(id)
        repo.cancelar(id)
        assertEquals(0, db.dao().ocupacion("a"))
        repo.reservar("a")
        assertEquals(1, db.dao().ocupacion("a"))
    }

    @Test
    fun impideDuplicadosPasadosYSinCupos() = runBlocking {
        db.dao()
            .sembrarClases(listOf(clase("a", 30), clase("pasada", -1), clase("llena", 40, 1, 1)))
        repo.reservar("a")
        assertTrue(runCatching { repo.reservar("a") }.isFailure)
        assertTrue(runCatching { repo.reservar("pasada") }.isFailure)
        assertTrue(runCatching { repo.reservar("llena") }.isFailure)
        assertEquals(1, db.dao().observarReservas().first().size)
    }

    @Test
    fun snapshotEstableYCompletadoUnico() = runBlocking {
        repo.guardar(rutina())
        val id = repo.empezar("propia")
        repo.guardar(rutina().copy(nombre = "Otra versión", vueltas = 9))
        assertEquals(2, db.dao().sesion(id)!!.vueltas)
        ahora += 14_000
        repo.sesion(id, "actualizar")
        repo.sesion(id, "actualizar")
        assertEquals(1, db.dao().observarSesiones().first().count { it.estado == "COMPLETADA" })
        assertEquals(14_000L, db.dao().sesion(id)!!.realizadoMs)
    }

    @Test
    fun impideBorrarRutinaActivaYOtraSesion() = runBlocking {
        repo.guardar(rutina())
        repo.guardar(rutina().copy(id = "otra"))
        repo.empezar("propia")
        assertTrue(runCatching { repo.eliminar("propia") }.isFailure)
        assertTrue(runCatching { repo.empezar("otra") }.isFailure)
    }

    @Test
    fun semillasIdempotentes() = runBlocking {
        repo.iniciar()
        repo.iniciar()
        assertEquals(6, db.dao().observarRutinas().first().size)
        val clases = db.dao().observarClases().first()
        assertEquals(clases.size, clases.map { it.id }.distinct().size)
    }

    @Test
    fun recuperaPausaSinCompletarDuranteCierre() = runBlocking {
        repo.guardar(rutina())
        val id = repo.empezar("propia")
        ahora += 2000
        repo.sesion(id, "actualizar")
        db.close()
        ahora += 60_000
        abrir()
        repo.iniciar()
        assertEquals(2000L, db.dao().sesion(id)!!.progresoMs)
        assertEquals("ACTIVA", db.dao().sesion(id)!!.estado)
        assertFalse(db.dao().sesion(id)!!.corriendo)
    }

    @Test
    fun catalogoNormalizaAliasYFallback() {
        assertEquals(R.drawable.foto_clase_baile, ImageUtils.imagen("Zumba Extrema"))
        assertEquals(R.drawable.foto_yoga_funcional, ImageUtils.imagen("  HÁTHA   Yoga matinal "))
        assertEquals(R.drawable.foto_actividad_general, ImageUtils.imagen("Actividad desconocida"))
        assertEquals(R.drawable.foto_rutina_core, ImageUtils.imagen("Otro", "foto_rutina_core"))
        assertNotEquals(R.drawable.foto_clase_boxeo, ImageUtils.imagen("Mailbox"))
    }

    @Test
    fun pausaRetomarYSaltoNoInventanTiempoRealizado() {
        val s =
            SesionRutina(
                "s",
                "r",
                "Rutina",
                listOf(Ejercicio("A", "", 10, 3)),
                1,
                creadaEn = 1000,
                anclaMs = 1000,
                corriendo = true,
            )
        val p = MotorRutina.actualizar(s, 4000).copy(corriendo = false)
        assertEquals(3000L, MotorRutina.actualizar(p, 100000).progresoMs)
        val r = p.copy(corriendo = true, anclaMs = 100000)
        assertEquals(5000L, MotorRutina.actualizar(r, 102000).progresoMs)
        val salto = MotorRutina.saltar(r, 102000)
        assertEquals(10000L, salto.progresoMs)
        assertEquals(5000L, salto.realizadoMs)
    }

    @Test
    fun validaEditorYPermiteEditarEliminarPropias() = runBlocking {
        assertTrue(runCatching { repo.guardar(rutina().copy(nombre = "")) }.isFailure)
        assertTrue(runCatching { repo.guardar(rutina().copy(ejercicios = emptyList())) }.isFailure)
        repo.guardar(rutina())
        repo.guardar(rutina().copy(nombre = "Plan editado"))
        assertEquals("Plan editado", db.dao().rutina("propia")?.nombre)
        repo.eliminar("propia")
        assertNull(db.dao().rutina("propia"))
    }

    @Test
    fun fotoSeCopiaYNoDependeDelArchivoOriginal() = runBlocking {
        val original = java.io.File(context.cacheDir, "foto-prueba.png")
        val bitmap =
            android.graphics.Bitmap.createBitmap(24, 24, android.graphics.Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(android.graphics.Color.BLUE)
        original.outputStream().use {
            bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it)
        }
        bitmap.recycle()
        val usuario = UsuarioManager(context)
        val guardada = usuario.guardarFoto(android.net.Uri.fromFile(original))
        original.delete()
        val reabierto = UsuarioManager(context)
        assertEquals(guardada, reabierto.foto())
        assertTrue(java.io.File(android.net.Uri.parse(guardada).path!!).exists())
    }
}

package com.leon.tecsupfit

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.*
import com.leon.tecsupfit.ui.screens.*
import com.leon.tecsupfit.ui.theme.*
import java.io.File
import java.time.*
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], qualifiers = "w390dp-h844dp-mdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@LooperMode(LooperMode.Mode.PAUSED)
class VisualTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    private val now =
        LocalDate.of(2026, 9, 23).atTime(6, 30).atZone(DatosClases.zona).toInstant().toEpochMilli()
    private val yoga =
        ClaseFitness(
            "yoga",
            "Yoga funcional",
            now + 30 * 60_000,
            40,
            "Sala 2",
            "Movilidad y respiración para empezar con calma.",
            15,
            5,
        )
    private val cross =
        ClaseFitness(
            "cross",
            "Cross Training",
            now + 690 * 60_000,
            45,
            "Sala 1",
            "Fuerza y resistencia.",
            12,
            6,
        )
    private val booking = Reserva("reserva", "yoga", now - 1000)

    private fun host(tab: String? = null, content: @Composable () -> Unit) {
        compose.setContent {
            TecsupFitTheme {
                val layer = rememberGraphicsLayer()
                CompositionLocalProvider(
                    LocalFondoCristal provides layer,
                    LocalMovimientoReducido provides true,
                ) {
                    Box(Modifier.fillMaxSize().background(BasePorcelana)) {
                        Box(
                            Modifier.fillMaxSize().drawWithContent {
                                layer.record { this@drawWithContent.drawContent() }
                                drawLayer(layer)
                            }
                        ) {
                            Image(
                                painterResource(R.drawable.fondo_prisma),
                                null,
                                Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillBounds,
                            )
                            Box(Modifier.fillMaxSize().background(BasePorcelana.copy(alpha = .39f)))
                        }
                        Column(Modifier.fillMaxSize().padding(top = 20.dp, bottom = 15.dp)) {
                            Box(Modifier.weight(1f)) { content() }
                            if (tab != null) BottomBar(tab) {}
                        }
                    }
                }
            }
        }
    }

    private fun captura(name: String) {
        compose.waitForIdle()
        lateinit var bitmap: Bitmap
        compose.runOnIdle {
            val view = compose.activity.window.decorView
            bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            view.draw(android.graphics.Canvas(bitmap))
        }
        val dir = File("build/visual-verification").apply { mkdirs() }
        File(dir, "$name.png").outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    @Test
    fun inicioYFiltros() {
        var chosen = ""
        host("inicio") {
            PantallaInicio(listOf(yoga, cross), emptyList(), now, null, {}) { chosen = it }
        }
        compose.onNodeWithText("Hoy, a tu ritmo.").assertExists()
        captura("01_inicio")
        compose.onNodeWithText("Yoga funcional").performClick()
        Assert.assertEquals("yoga", chosen)
        compose.onNodeWithText("Semana").performClick()
        captura("02_semana")
        compose.onNodeWithText("Explorar").performClick()
        captura("16_explorar")
    }

    @Test
    fun reservasDestacadaEHistorial() {
        host("reservas") {
            PantallaReservas(
                listOf(yoga, cross),
                listOf(Reserva("otra", "cross", now), booking),
                now,
                null,
                {},
                {},
                {},
            )
        }
        compose.onAllNodesWithText("Yoga funcional").onFirst().assertExists()
        captura("05_reservas")
        compose.onNodeWithText("Historial").performClick()
        captura("11_historial_vacio")
    }

    @Test
    fun detalleYCancelacion() {
        var cancelada = false
        host {
            PantallaDetalleClase(
                yoga,
                booking,
                listOf(booking),
                now,
                null,
                {},
                {},
                false,
                {},
                { cancelada = true },
                {},
                true,
            )
        }
        captura("09_detalle_reserva")
        compose.onNodeWithText("Cancelar reserva").performScrollTo().performClick()
        captura("10_cancelar")
        compose.onNodeWithText("Sí, cancelar").performClick()
        Assert.assertTrue(cancelada)
    }

    @Test
    fun claseYConfirmacion() {
        var reserved = false
        host {
            PantallaDetalleClase(
                yoga,
                null,
                emptyList(),
                now,
                null,
                {},
                {},
                false,
                { reserved = true },
                {},
                {},
            )
        }
        captura("03_clase")
        compose.onNodeWithText("Reservar mi cupo").performScrollTo().performClick()
        Assert.assertTrue(reserved)
    }

    @Test
    fun confirmacion() {
        host { PantallaConfirmacion(yoga, null, {}, {}, {}) }
        captura("04_confirmacion")
    }

    @Test
    fun rutinas() {
        host("rutinas") { PantallaRutinas(DatosRutinas.rutinas, null, null, {}, {}, {}, {}, {}) }
        captura("06_rutinas")
        compose.onNodeWithText("+ Crear una rutina").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun catalogo() {
        host("rutinas") { CatalogoRutinas(DatosRutinas.rutinas, null, {}, {}, {}, {}) }
        captura("13_catalogo")
    }

    @Test
    fun detalleRutina() {
        host { PantallaDetalleRutina(DatosRutinas.rutinas[1], null, {}, {}, false, {}, {}, {}, {}) }
        captura("15_detalle_rutina")
    }

    @Test
    fun sesion() {
        val r = DatosRutinas.rutinas[0]
        val s =
            SesionRutina(
                "s",
                r.id,
                r.nombre,
                r.ejercicios,
                r.vueltas,
                progresoMs = 13000,
                creadaEn = now,
            )
        var action = ""
        host { PantallaSesionRutina(s, now, null, {}, false, { action = it }, {}, {}, {}) }
        captura("07_sesion")
        compose.onNodeWithContentDescription("Continuar").performScrollTo().performClick()
        Assert.assertEquals("continuar", action)
    }

    @Test
    fun completada() {
        val r = DatosRutinas.rutinas[0]
        host {
            PantallaRutinaCompletada(
                SesionRutina(
                    "s",
                    r.id,
                    r.nombre,
                    r.ejercicios,
                    r.vueltas,
                    realizadoMs = 720000,
                    estado = "COMPLETADA",
                    creadaEn = now,
                ),
                null,
                {},
                {},
                {},
            )
        }
        captura("12_completada")
    }

    @Test
    fun perfil() {
        host("perfil") {
            PantallaPerfil(null, emptyList(), emptyList(), now, false, { _, _ -> }, {})
        }
        captura("08_perfil")
        compose.onNodeWithText("Cambiar foto").performScrollTo().performClick()
        captura("14_cambiar_foto")
        compose.onNodeWithText("Elegir de la galería").assertExists()
        compose.onNodeWithText("Cancelar").performClick()
    }

    @Test
    fun editorValidaYGuarda() {
        var saved: Rutina? = null
        host { EditorRutina(null, false, null, {}, {}, false) { saved = it } }
        captura("17_crear_rutina")
        compose.onNodeWithText("Nombre de la rutina").performTextInput("Zumba propia")
        compose.onNodeWithText("+ Añadir ejercicio").performScrollTo().performClick()
        compose.onNodeWithText("Nombre").performTextInput("Paso lateral")
        compose.onNodeWithText("Guardar ejercicio").performClick()
        compose.onNodeWithText("Guardar rutina").performScrollTo().performClick()
        Assert.assertEquals("Zumba propia", saved?.nombre)
        Assert.assertEquals(1, saved?.ejercicios?.size)
    }
}

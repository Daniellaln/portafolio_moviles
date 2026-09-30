package com.leon.tecsupfit.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.lifecycle.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.leon.tecsupfit.R
import com.leon.tecsupfit.data.*
import com.leon.tecsupfit.ui.components.*
import com.leon.tecsupfit.ui.screens.*
import com.leon.tecsupfit.ui.theme.*

@Composable
fun TecsupFitNavGraph(vm: FitViewModel = viewModel()) {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route ?: "inicio"
    val clases by vm.clases.collectAsStateWithLifecycle()
    val reservas by vm.reservas.collectAsStateWithLifecycle()
    val rutinas by vm.rutinas.collectAsStateWithLifecycle()
    val sesiones by vm.sesiones.collectAsStateWithLifecycle()
    val foto by vm.foto.collectAsStateWithLifecycle()
    val ahora by vm.ahora.collectAsStateWithLifecycle()
    val busy by vm.ocupado.collectAsStateWithLifecycle()
    val listo by vm.listo.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val reduced = recordarMovimientoReducido()
    val wall = rememberGraphicsLayer()
    val snack = remember { SnackbarHostState() }
    val lifecycle = LocalLifecycleOwner.current
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) vm.pausar()
        }
        lifecycle.lifecycle.addObserver(observer)
        onDispose { lifecycle.lifecycle.removeObserver(observer) }
    }
    LaunchedEffect(error) {
        error?.let {
            snack.showSnackbar(it)
            vm.limpiarError()
        }
    }
    fun go(path: String) {
        nav.navigate(path) { launchSingleTop = true }
    }
    fun back() {
        if (!nav.popBackStack()) go("inicio")
    }
    fun tab(path: String) {
        nav.navigate(path) {
            popUpTo(nav.graph.findStartDestination().id) { saveState = false }
            launchSingleTop = true
            restoreState = false
        }
    }
    val profile: () -> Unit = {
        vm.pausar()
        go("perfil")
    }
    val roots = listOf("inicio", "reservas", "rutinas", "perfil", "catalogo")
    val active =
        when (route) {
            "perfil" -> "perfil"
            "reservas" -> "reservas"
            "rutinas",
            "catalogo" -> "rutinas"
            else -> "inicio"
        }
    CompositionLocalProvider(
        LocalMovimientoReducido provides reduced,
        LocalFondoCristal provides wall,
    ) {
        Box(Modifier.fillMaxSize().background(BasePorcelana)) {
            Box(
                Modifier.fillMaxSize().drawWithContent {
                    wall.record { this@drawWithContent.drawContent() }
                    drawLayer(wall)
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
            Column(
                Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).imePadding()
            ) {
                Box(Modifier.weight(1f).fillMaxWidth()) {
                    if (!listo)
                        Column(
                            Modifier.align(Alignment.Center).padding(25.dp),
                            verticalArrangement = Arrangement.spacedBy(15.dp),
                        ) {
                            if (busy) CircularProgressIndicator()
                            else PrimaryButton("Volver a intentar", onClick = vm::preparar)
                            Text(
                                if (busy) "Preparando tu espacio…"
                                else "No se pudo abrir el almacenamiento."
                            )
                        }
                    else
                        NavHost(
                            navController = nav,
                            startDestination = "inicio",
                            enterTransition = {
                                fadeIn(tween(if (reduced) 0 else 240)) +
                                    scaleIn(
                                        initialScale = if (reduced) 1f else .985f,
                                        animationSpec = tween(if (reduced) 0 else 260),
                                    )
                            },
                            exitTransition = { fadeOut(tween(if (reduced) 0 else 130)) },
                            popEnterTransition = { fadeIn(tween(if (reduced) 0 else 220)) },
                            popExitTransition = { fadeOut(tween(if (reduced) 0 else 170)) },
                        ) {
                            composable("inicio") {
                                PantallaInicio(clases, reservas, ahora, foto, profile) {
                                    go("clase/$it")
                                }
                            }
                            composable("reservas") {
                                PantallaReservas(
                                    clases,
                                    reservas,
                                    ahora,
                                    foto,
                                    profile,
                                    { go("reserva/$it") },
                                    { tab("inicio") },
                                )
                            }
                            composable("clase/{id}") { e ->
                                val c = clases.find { it.id == e.arguments?.getString("id") }
                                if (c != null) {
                                    val r = reservas.find {
                                        it.claseId == c.id && it.estado == "CONFIRMADA"
                                    }
                                    PantallaDetalleClase(
                                        c,
                                        r,
                                        reservas,
                                        ahora,
                                        foto,
                                        profile,
                                        ::back,
                                        busy,
                                        { vm.reservar(c.id) { go("confirmacion/$it") } },
                                        {},
                                        { r?.let { go("reserva/${it.id}") } },
                                    )
                                } else Missing(::back)
                            }
                            composable("confirmacion/{id}") { e ->
                                val r = reservas.find { it.id == e.arguments?.getString("id") }
                                val c = clases.find { it.id == r?.claseId }
                                if (c != null && r != null)
                                    PantallaConfirmacion(
                                        c,
                                        foto,
                                        profile,
                                        {
                                            nav.navigate("reserva/${r.id}") {
                                                popUpTo("inicio")
                                                launchSingleTop = true
                                            }
                                        },
                                        { tab("inicio") },
                                    )
                                else Loading()
                            }
                            composable("reserva/{id}") { e ->
                                val r = reservas.find { it.id == e.arguments?.getString("id") }
                                val c = clases.find { it.id == r?.claseId }
                                if (c != null && r != null)
                                    PantallaDetalleClase(
                                        c,
                                        r,
                                        reservas,
                                        ahora,
                                        foto,
                                        profile,
                                        ::back,
                                        busy,
                                        {},
                                        { vm.cancelar(r.id) { tab("reservas") } },
                                        {},
                                        modoReserva = true,
                                    )
                                else Missing(::back)
                            }
                            composable("rutinas") {
                                PantallaRutinas(
                                    rutinas,
                                    sesiones.firstOrNull { it.estado == "ACTIVA" },
                                    foto,
                                    profile,
                                    { go("rutina/$it") },
                                    { go("crear") },
                                    { go("catalogo") },
                                    {
                                        sesiones
                                            .firstOrNull { it.estado == "ACTIVA" }
                                            ?.let { go("sesion/${it.id}") }
                                    },
                                )
                            }
                            composable("catalogo") {
                                CatalogoRutinas(
                                    rutinas,
                                    foto,
                                    profile,
                                    ::back,
                                    { go("rutina/$it") },
                                    { go("crear") },
                                )
                            }
                            composable("rutina/{id}") { e ->
                                val r = rutinas.find { it.id == e.arguments?.getString("id") }
                                if (r != null)
                                    PantallaDetalleRutina(
                                        r,
                                        foto,
                                        profile,
                                        ::back,
                                        busy,
                                        { vm.empezar(r.id) { go("sesion/$it") } },
                                        { go("editar/${r.id}") },
                                        { go("duplicar/${r.id}") },
                                        { vm.eliminar(r.id) { back() } },
                                    )
                                else Missing(::back)
                            }
                            composable("crear") {
                                EditorRutina(null, false, foto, profile, ::back, busy) { r ->
                                    vm.guardar(r) { back() }
                                }
                            }
                            composable("editar/{id}") { e ->
                                val r = rutinas.find { it.id == e.arguments?.getString("id") }
                                if (r != null)
                                    EditorRutina(r, false, foto, profile, ::back, busy) {
                                        vm.guardar(it) { back() }
                                    }
                                else Missing(::back)
                            }
                            composable("duplicar/{id}") { e ->
                                val r = rutinas.find { it.id == e.arguments?.getString("id") }
                                if (r != null)
                                    EditorRutina(r, true, foto, profile, ::back, busy) {
                                        vm.guardar(it) { tab("rutinas") }
                                    }
                                else Missing(::back)
                            }
                            composable("sesion/{id}") { e ->
                                val s = sesiones.find { it.id == e.arguments?.getString("id") }
                                if (s != null)
                                    PantallaSesionRutina(
                                        s,
                                        ahora,
                                        foto,
                                        profile,
                                        busy,
                                        { vm.accionSesion(s.id, it) },
                                        { vm.accionSesion(s.id, "pausa") { tab("rutinas") } },
                                        { vm.accionSesion(s.id, "abandonar") { tab("rutinas") } },
                                        {
                                            vm.accionSesion(s.id, "actualizar") {
                                                nav.navigate("completada/${s.id}") {
                                                    popUpTo("sesion/{id}") { inclusive = true }
                                                    launchSingleTop = true
                                                }
                                            }
                                        },
                                    )
                                else Loading()
                            }
                            composable("completada/{id}") { e ->
                                val s = sesiones.find { it.id == e.arguments?.getString("id") }
                                if (s != null)
                                    PantallaRutinaCompletada(
                                        s,
                                        foto,
                                        profile,
                                        { tab("rutinas") },
                                        { tab("inicio") },
                                    )
                                else Loading()
                            }
                            composable("perfil") {
                                PantallaPerfil(
                                    foto,
                                    unirReservas(reservas, clases),
                                    sesiones,
                                    ahora,
                                    busy,
                                    vm::guardarFoto,
                                    vm::informar,
                                    if (nav.previousBackStackEntry != null) ::back else null,
                                )
                            }
                        }
                }
                if (route in roots && listo) BottomBar(active, ::tab)
            }
            SnackbarHost(
                snack,
                Modifier.align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 100.dp, start = 18.dp, end = 18.dp),
            )
        }
    }
}

@Composable
private fun Loading() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
}

@Composable
private fun Missing(back: () -> Unit) {
    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Text("No se encontró este elemento.")
        SecondaryButton("Volver", back)
    }
}

package com.leon.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.compose.runtime.getValue
import com.leon.tecsupfit.data.DatosClases
import com.leon.tecsupfit.ui.screens.PantallaConfirmacion
import com.leon.tecsupfit.ui.screens.PantallaDetalleClase
import com.leon.tecsupfit.ui.screens.PantallaInicio
import com.leon.tecsupfit.ui.screens.PantallaPerfil
import com.leon.tecsupfit.ui.screens.PantallaReservas
import com.leon.tecsupfit.ui.screens.PantallaRutinas

/**
 * Grafo de navegación de la app. Cada pantalla recibe la ruta actual
 * para que el bottomBar sepa qué ícono resaltar.
 */
@Composable
fun TecsupFitNavGraph(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route ?: Pantallas.INICIO

    NavHost(navController = navController, startDestination = Pantallas.INICIO) {

        composable(Pantallas.INICIO) {
            PantallaInicio(
                nombreUsuario = "Daniella",
                rutaActual = rutaActual,
                onNavegar = { ruta -> navController.navigate(ruta) },
                onClaseSeleccionada = { clase ->
                    navController.navigate(Pantallas.detalleRuta(clase.id))
                }
            )
        }

        composable(
            route = Pantallas.DETALLE,
            arguments = listOf(navArgument("claseId") { type = NavType.IntType })
        ) { entry ->
            val claseId = entry.arguments?.getInt("claseId") ?: 0
            val clase = DatosClases.obtenerClasePorId(claseId)
            if (clase != null) {
                PantallaDetalleClase(
                    clase = clase,
                    onVolver = { navController.popBackStack() },
                    onReservar = {
                        DatosClases.reservarClase(clase)
                        navController.navigate(Pantallas.confirmacionRuta(clase.id))
                    }
                )
            }
        }

        composable(
            route = Pantallas.CONFIRMACION,
            arguments = listOf(navArgument("claseId") { type = NavType.IntType })
        ) { entry ->
            val claseId = entry.arguments?.getInt("claseId") ?: 0
            val clase = DatosClases.obtenerClasePorId(claseId)
            if (clase != null) {
                PantallaConfirmacion(
                    clase = clase,
                    onVerReservas = {
                        navController.navigate(Pantallas.RESERVAS) {
                            popUpTo(Pantallas.INICIO)
                        }
                    }
                )
            }
        }

        composable(Pantallas.RESERVAS) {
            PantallaReservas(
                rutaActual = rutaActual,
                onNavegar = { ruta -> navController.navigate(ruta) }
            )
        }

        composable(Pantallas.RUTINAS) {
            PantallaRutinas(
                rutaActual = rutaActual,
                onNavegar = { ruta -> navController.navigate(ruta) }
            )
        }

        composable(Pantallas.PERFIL) {
            PantallaPerfil(
                nombreUsuario = "Daniella Leon",
                rutaActual = rutaActual,
                onNavegar = { ruta -> navController.navigate(ruta) }
            )
        }
    }
}

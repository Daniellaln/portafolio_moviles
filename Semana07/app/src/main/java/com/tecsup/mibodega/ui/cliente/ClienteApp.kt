package com.tecsup.mibodega.ui.cliente

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.tecsup.mibodega.ui.cliente.modelo.*
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen

/**
 * "Director de orquesta" de la app cliente:
 * - Tiene el NavHost con las rutas de cada pantalla.
 * - Tiene el estado del carrito (List<ItemCarrito>), que se reparte
 *   hacia abajo a Inicio, Detalle, Carrito y Entrega.
 * Ninguna Screen navega sola ni modifica el carrito directamente:
 * todas reciben funciones (lambdas) desde aquí (state hoisting).
 */

@Composable
fun ClienteApp(oscuro: Boolean = false, onCambiarTema: (Boolean) -> Unit = {}) {
    val navController = rememberNavController()
    var mostrarTerminos by remember { mutableStateOf(false) }
    if (mostrarTerminos) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { mostrarTerminos = false },
            title = { androidx.compose.material3.Text("Términos y condiciones") },
            text = { androidx.compose.material3.Text("Aplicación académica de demostración. No procesa pagos reales. Los datos se guardan únicamente en memoria durante esta sesión.") },
            confirmButton = { androidx.compose.material3.TextButton(onClick = { mostrarTerminos = false }) { androidx.compose.material3.Text("Aceptar") } }
        )
    }

    var favoritos by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    var recojo by rememberSaveable { mutableStateOf(false) }
    fun cambiarFavorito(p: Producto) { favoritos = if(p.id in favoritos) favoritos - p.id else favoritos + p.id }
    var cliente by rememberSaveable(stateSaver = clienteSaver) { mutableStateOf(com.tecsup.mibodega.ui.cliente.modelo.DatosCliente()) }
    var pedidos by rememberSaveable(stateSaver = pedidosSaver) { mutableStateOf<List<com.tecsup.mibodega.ui.cliente.modelo.Pedido>>(emptyList()) }
    // El carrito vive aquí arriba, no en ninguna Screen.
    var carrito by rememberSaveable(stateSaver = carritoSaver) { mutableStateOf<List<ItemCarrito>>(emptyList()) }

    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA,
        // NavHost usa AnimatedContent: desplazamiento corto y fundido, sin animar dos veces.
        enterTransition = { androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(220)) + androidx.compose.animation.slideInHorizontally(androidx.compose.animation.core.tween(240)) { it / 12 } },
        exitTransition = { androidx.compose.animation.fadeOut(androidx.compose.animation.core.tween(160)) },
        popEnterTransition = { androidx.compose.animation.fadeIn(androidx.compose.animation.core.tween(200)) },
        popExitTransition = { androidx.compose.animation.fadeOut(androidx.compose.animation.core.tween(180)) + androidx.compose.animation.slideOutHorizontally(androidx.compose.animation.core.tween(220)) { it / 12 } }
    ) {
        composable(Rutas.BIENVENIDA) {
            BienvenidaScreen(
                onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) { launchSingleTop = true } },
                onTerminos = { mostrarTerminos = true }
            )
        }

        composable(Rutas.LOGIN) {
            com.tecsup.mibodega.ui.cliente.screens.login.LoginScreen(onVolver = { navController.popBackStack() }, onEntrar = {
                cliente = DatosCliente("Daniella Leon")
                navController.navigate(Rutas.INICIO) { popUpTo(Rutas.BIENVENIDA) { inclusive = true }; launchSingleTop = true }
            })
        }
        composable(Rutas.FAVORITOS) {
            com.tecsup.mibodega.ui.cliente.screens.favoritos.FavoritosScreen(favoritos, { navController.popBackStack() },
                { navController.navigate(Rutas.detalle(it.id)) { launchSingleTop = true } },
                { carrito = agregarProducto(carrito, it, 1) }, ::cambiarFavorito)
        }
        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onVolver = { navController.popBackStack() },
                onCrearCuenta = { nombre, telefono, direccion, referencia ->
                    cliente = com.tecsup.mibodega.ui.cliente.modelo.DatosCliente(nombre, telefono, direccion, referencia)
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.INICIO) {
            InicioScreen(
                cantidadCarrito = carrito.sumOf { it.cantidad },
                cliente = cliente,
                pedidos = pedidos,
                favoritos = favoritos, onFavorito = ::cambiarFavorito,
                onVerFavoritos = { navController.navigate(Rutas.FAVORITOS) { launchSingleTop = true } },
                oscuro = oscuro, onCambiarTema = onCambiarTema,
                onVerCarrito = { navController.navigate(Rutas.CARRITO) { launchSingleTop = true } },
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onAgregarProducto = { producto ->
                    carrito = com.tecsup.mibodega.ui.cliente.modelo.agregarProducto(carrito, producto, 1)
                }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            val producto = listaProductosFake.firstOrNull { it.id == productoId }

            if (producto == null) {
                androidx.compose.material3.TextButton(onClick = { navController.popBackStack() }) {
                    androidx.compose.material3.Text("Producto no encontrado. Volver")
                }
                return@composable
            }
            DetalleProductoScreen(
                producto = producto,
                favorito = producto.id in favoritos, onFavorito = { cambiarFavorito(producto) },
                onVolver = { navController.popBackStack() },
                onAgregarAlCarrito = { productoSeleccionado, cantidad ->
                    carrito = com.tecsup.mibodega.ui.cliente.modelo.agregarProducto(carrito, productoSeleccionado, cantidad)
                    navController.navigate(Rutas.CARRITO) { popUpTo(Rutas.INICIO); launchSingleTop = true }
                }
            )
        }

        composable(Rutas.ENTREGA) {
            com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen(
                cliente = cliente,
                total = com.tecsup.mibodega.ui.cliente.modelo.totalCarrito(carrito, recojo),
                recojo = recojo, onRecojo = { recojo = it },
                onVolver = { navController.popBackStack() },
                onConfirmar = { datos, pago ->
                    if (carrito.isNotEmpty()) {
                        cliente = datos
                        val pedido = com.tecsup.mibodega.ui.cliente.modelo.Pedido(1024 + pedidos.size, datos, pago, carrito.toList(), com.tecsup.mibodega.ui.cliente.modelo.totalCarrito(carrito, recojo), recojo)
                        pedidos = pedidos + pedido
                        carrito = emptyList()
                        navController.navigate(Rutas.CONFIRMACION) {
                            popUpTo(Rutas.INICIO) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
        composable(Rutas.CONFIRMACION) {
            com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen(pedidos.lastOrNull()) {
                navController.popBackStack(Rutas.INICIO, false)
            }
        }
        composable(Rutas.CARRITO) {
            CarritoScreen(
                carrito = carrito,
                recojo = recojo, onRecojo = { recojo = it },
                onVolver = { navController.popBackStack() },
                onIncrementar = { producto ->
                    carrito = carrito.map {
                        if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                    }
                },
                onDecrementar = { producto ->
                    carrito = carrito.mapNotNull {
                        when {
                            it.producto.id != producto.id -> it
                            it.cantidad > 1 -> it.copy(cantidad = it.cantidad - 1)
                            else -> null // si llega a 0, se elimina de la lista
                        }
                    }
                },
                onEliminar = { producto ->
                    carrito = carrito.filterNot { it.producto.id == producto.id }
                },
                onContinuarPedido = { if (carrito.isNotEmpty()) navController.navigate(Rutas.ENTREGA) { launchSingleTop = true } }
            )
        }
    }
}


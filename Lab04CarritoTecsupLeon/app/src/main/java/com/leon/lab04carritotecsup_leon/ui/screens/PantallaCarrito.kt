package com.leon.lab04carritotecsup_leon.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leon.lab04carritotecsup_leon.data.Producto
import com.leon.lab04carritotecsup_leon.ui.components.BarraSuperior
import com.leon.lab04carritotecsup_leon.ui.components.CarritoVacio
import com.leon.lab04carritotecsup_leon.ui.components.DialogoEliminar
import com.leon.lab04carritotecsup_leon.ui.components.FormularioProducto
import com.leon.lab04carritotecsup_leon.ui.components.PanelTotales
import com.leon.lab04carritotecsup_leon.ui.components.TarjetaProducto
import com.leon.lab04carritotecsup_leon.ui.theme.Lab04CarritoTecsupLeonTheme

// Retos opcionales: pon false para que se vea EXACTO a la Figura 2
private const val USAR_DESCUENTO = true
private const val CONFIRMAR_BORRADO = true

@Composable
fun PantallaCarrito() {
    // Estados del formulario (Lab 03)
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    // Estado NUEVO: lista observable
    val productos = remember { mutableStateListOf<Producto>() }

    // Producto pendiente de confirmar (reto AlertDialog)
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    // Cálculos del Lab 02 (se recalculan cuando la lista cambia)
    val subtotal = productos.sumOf { it.precio * it.cantidad }
    val igv = subtotal * 0.18
    val total = subtotal + igv
    val porcentajeDescuento = if (!USAR_DESCUENTO) 0.0 else when {
        total > 5000 -> 0.10
        total > 3000 -> 0.05
        else -> 0.0
    }
    val descuento = total * porcentajeDescuento
    val totalFinal = total - descuento

    Scaffold(
        topBar = { BarraSuperior(titulo = "Mi Carrito TECSUP") },
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            // Zona superior: formulario + lista (ocupa todo el alto disponible)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                FormularioProducto(
                    nombre = nombre,
                    onNombreChange = { nombre = it },
                    precio = precio,
                    onPrecioChange = { precio = it },
                    cantidad = cantidad,
                    onCantidadChange = { cantidad = it },
                    onAgregar = {
                        val precioNum = precio.toDoubleOrNull() ?: 0.0
                        val cantidadNum = cantidad.toIntOrNull() ?: 0
                        if (nombre.isNotBlank() && precioNum > 0 && cantidadNum > 0) {
                            productos.add(Producto(nombre.trim(), precioNum, cantidadNum))
                            // Limpia los 3 campos
                            nombre = ""
                            precio = ""
                            cantidad = ""
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(12.dp))

                if (productos.isEmpty()) {
                    CarritoVacio(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        items(productos) { producto ->
                            TarjetaProducto(
                                producto = producto,
                                onEliminar = {
                                    if (CONFIRMAR_BORRADO) productoAEliminar = producto
                                    else productos.remove(producto)
                                }
                            )
                        }
                    }
                }
            }

            // Panel SIEMPRE fijo abajo (fuera de la LazyColumn)
            PanelTotales(
                cantidadProductos = productos.size,
                subtotal = subtotal,
                igv = igv,
                porcentajeDescuento = porcentajeDescuento,
                descuento = descuento,
                total = totalFinal
            )
        }
    }

    productoAEliminar?.let { producto ->
        DialogoEliminar(
            producto = producto,
            onConfirmar = {
                productos.remove(producto)
                productoAEliminar = null
            },
            onCancelar = { productoAEliminar = null }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaCarritoPreview() {
    Lab04CarritoTecsupLeonTheme {
        PantallaCarrito()
    }
}

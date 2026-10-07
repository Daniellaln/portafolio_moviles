package com.tecsup.mibodega.ui.componentes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.tecsup.mibodega.R
import com.tecsup.mibodega.ui.cliente.modelo.Producto
fun unidad(producto: Producto) = when (producto.id) { 1 -> "1 kg"; 4 -> "126 g"; 5 -> "1.5 L"; else -> "1 L" }
@Composable fun ImagenProducto(producto: Producto, modifier: Modifier = Modifier) {
 val recurso = when(producto.id) { 1 -> R.drawable.clay_arroz; 2 -> R.drawable.clay_aceite; 3 -> R.drawable.clay_leche; 4 -> R.drawable.clay_galleta; else -> R.drawable.clay_gaseosa }
 Image(painterResource(recurso), null, modifier, contentScale = ContentScale.Fit)
}
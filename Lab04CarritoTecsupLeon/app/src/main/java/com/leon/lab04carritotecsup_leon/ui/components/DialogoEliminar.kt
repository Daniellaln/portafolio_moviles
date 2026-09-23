package com.leon.lab04carritotecsup_leon.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.leon.lab04carritotecsup_leon.data.Producto

// Reto opcional (+1): confirmación de borrado
@Composable
fun DialogoEliminar(
    producto: Producto,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("¿Eliminar este producto?") },
        text = { Text("Se quitará \"${producto.nombre}\" del carrito.") },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Eliminar", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}

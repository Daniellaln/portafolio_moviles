package com.tecsup.mibodega.ui.cliente.modelo

data class DatosCliente(val nombre: String = "", val telefono: String = "", val direccion: String = "", val referencia: String = "")
data class Pedido(val numero: Int, val cliente: DatosCliente, val metodoPago: String, val items: List<ItemCarrito>, val total: Double)
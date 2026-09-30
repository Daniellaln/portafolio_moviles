package com.leon.tecsupfit.data

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object UsuarioManager {
    var nombreReal by mutableStateOf("Daniella Leon")
    var fotoPerfilUri by mutableStateOf<Uri?>(null)

    fun getClasesReservadas(): Int {
        return DatosClases.reservas.count { it.estado == EstadoReserva.CONFIRMADA }
    }

    fun getSesionesCompletadas(): Int {
        return DatosRutinas.sesionesCompletadas.size
    }

    fun guardarFoto(context: Context, uri: Uri?) {
        val prefs = context.getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE)
        if (uri == null) {
            prefs.edit().remove("foto_uri").apply()
        } else {
            prefs.edit().putString("foto_uri", uri.toString()).apply()
        }
        fotoPerfilUri = uri
    }

    fun cargarDatos(context: Context) {
        val prefs = context.getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE)
        val uriString = prefs.getString("foto_uri", null)
        if (uriString != null) {
            fotoPerfilUri = Uri.parse(uriString)
        }
    }
}

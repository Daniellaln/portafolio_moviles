package com.leon.tecsupfit.data

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class FitViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = FitRepository(FitDatabase.abrir(app))
    private val usuario = UsuarioManager(app)
    val clases =
        repo.dao.observarClases().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val reservas =
        repo.dao.observarReservas().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val rutinas =
        repo.dao.observarRutinas().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val sesiones =
        repo.dao.observarSesiones().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val _foto = MutableStateFlow(usuario.foto())
    val foto = _foto.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    private val _ocupado = MutableStateFlow(false)
    val ocupado = _ocupado.asStateFlow()
    private val _listo = MutableStateFlow(false)
    val listo = _listo.asStateFlow()
    val ahora = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(200)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), System.currentTimeMillis())

    init {
        preparar()
        viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_listo.value)
                    repo.dao
                        .activa()
                        ?.takeIf { it.corriendo }
                        ?.let {
                            runCatching { repo.sesion(it.id, "actualizar") }
                                .onFailure {
                                    _error.value = "No se pudo guardar el avance de la sesión."
                                }
                        }
            }
        }
    }

    private fun tarea(bloque: suspend () -> Unit) {
        if (_ocupado.value) return
        _ocupado.value = true
        viewModelScope.launch {
            try {
                bloque()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = e.message ?: "No se pudo guardar. Inténtalo otra vez."
            } finally {
                _ocupado.value = false
            }
        }
    }

    fun preparar() = tarea {
        repo.iniciar()
        _listo.value = true
    }

    fun limpiarError() {
        _error.value = null
    }

    fun informar(texto: String) {
        _error.value = texto
    }

    fun reservar(id: String, fin: (String) -> Unit) = tarea { fin(repo.reservar(id)) }

    fun cancelar(id: String, fin: () -> Unit) = tarea {
        repo.cancelar(id)
        fin()
    }

    fun guardar(r: Rutina, fin: () -> Unit) = tarea {
        repo.guardar(r)
        fin()
    }

    fun eliminar(id: String, fin: () -> Unit) = tarea {
        repo.eliminar(id)
        fin()
    }

    fun empezar(id: String, fin: (String) -> Unit) = tarea { fin(repo.empezar(id)) }

    fun accionSesion(id: String, accion: String, fin: () -> Unit = {}) = tarea {
        repo.sesion(id, accion)
        fin()
    }

    fun pausar() {
        viewModelScope.launch {
            runCatching { repo.pausarActiva() }
                .onFailure { _error.value = "No se pudo guardar la pausa." }
        }
    }

    fun guardarFoto(uri: Uri, fin: () -> Unit) = tarea {
        _foto.value = usuario.guardarFoto(uri)
        fin()
    }
}

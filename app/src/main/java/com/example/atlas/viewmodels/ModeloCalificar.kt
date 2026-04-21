package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.EstadoCalificar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ModeloCalificar : ViewModel() {
    private val _estado = MutableStateFlow(EstadoCalificar())
    val estado: StateFlow<EstadoCalificar> = _estado.asStateFlow()

    fun actualizarCalificacion(valor: Int) {
        _estado.update { it.copy(calificacion = valor) }
    }
    fun actualizarComentario(texto: String) {
        _estado.update { it.copy(comentario = texto) }
    }
    fun actualizarEntrenador(nombre: String) {
        _estado.update { it.copy(entrenador = nombre) }
    }
}
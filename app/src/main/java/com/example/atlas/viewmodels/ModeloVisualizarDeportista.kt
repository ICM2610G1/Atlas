package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.EstadoVisualizarDeportista
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ModeloVisualizarDeportista : ViewModel() {
    private val _estado = MutableStateFlow(EstadoVisualizarDeportista())
    val estado: StateFlow<EstadoVisualizarDeportista> = _estado.asStateFlow()

    fun actualizarNombre(valor: String) {
        _estado.update { it.copy(nombre = valor) }
    }
}
package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.EstadoTroteActivo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ModeloTroteActivo : ViewModel() {
    private val _estado = MutableStateFlow(EstadoTroteActivo())
    val estado: StateFlow<EstadoTroteActivo> = _estado.asStateFlow()

    fun actualizarDistancia(distancia: Double) {
        _estado.update { it.copy(distancia = distancia) }
    }
    fun actualizarPasos(pasos: Int) {
        _estado.update { it.copy(pasos = pasos) }
    }
    fun actualizarTiempo(segundos: Int) {
        _estado.update { it.copy(tiempoSegundos = segundos) }
    }
    fun alternarTrote() {
        _estado.update { it.copy(trotando = !it.trotando) }
    }
}
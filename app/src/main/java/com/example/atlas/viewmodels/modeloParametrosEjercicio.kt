package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FormularioEjercicioState(
    val series: String = "",
    val repeticiones: String = "",
    val peso: String = ""
)

class FormularioEjercicioViewModel : ViewModel() {
    private val _formularioState = MutableStateFlow(FormularioEjercicioState())
    val formularioState = _formularioState.asStateFlow()

    fun updateSeries(value: String) {
            _formularioState.update { it.copy(series = value) }
    }

    fun updateRepeticiones(value: String) {
            _formularioState.update { it.copy(repeticiones = value) }
    }

    fun updatePeso(value: String) {
            _formularioState.update { it.copy(peso = value) }
    }
}
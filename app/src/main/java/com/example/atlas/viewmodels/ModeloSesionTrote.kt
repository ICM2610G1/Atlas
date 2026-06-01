package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DetallesTroteUiState(
    val selectedActivity: String? = null,
    val lugarFinal: String = "",
)

class DetallesTroteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DetallesTroteUiState())
    val uiState = _uiState.asStateFlow()
    fun actActivity(nombreActividad: String) {
        _uiState.update { it.copy(selectedActivity = nombreActividad) }
    }
    fun actLugarFinal(nuevoLugar: String) {
        _uiState.update { it.copy(lugarFinal = nuevoLugar) }
    }
}
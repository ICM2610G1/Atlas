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

    private val _formularios = MutableStateFlow<Map<String, FormularioEjercicioState>>(emptyMap())
    val formularios = _formularios.asStateFlow()

    fun updateSeries (id : String , value: String ){

        _formularios.update{
            val mutableMap = it.toMutableMap()
            val formActual = mutableMap[id] ?: FormularioEjercicioState()
            val formNuevo = formActual.copy(series = value)
            mutableMap[id] = formNuevo
            mutableMap
        }
    }
    fun updateRepeticiones  (id : String , value: String ){

        _formularios.update{
            val mutableMap = it.toMutableMap()
            val formActual = mutableMap[id] ?: FormularioEjercicioState()
            val formNuevo = formActual.copy(repeticiones = value)
            mutableMap[id] = formNuevo
            mutableMap
        }
    }
    fun updatePeso (id : String, value: String ){

        _formularios.update{
            val mutableMap = it.toMutableMap()
            val formActual = mutableMap[id] ?: FormularioEjercicioState()
            val formNuevo = formActual.copy(peso = value)
            mutableMap[id] = formNuevo
            mutableMap
        }
    }
}

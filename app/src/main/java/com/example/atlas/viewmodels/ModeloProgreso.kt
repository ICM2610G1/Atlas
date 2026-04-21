package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.Progreso
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgresoViewModel: ViewModel(){
    private val _progreso = MutableStateFlow(Progreso())
    val progreso = _progreso.asStateFlow()
    fun updateObjetivo(newObjetivo : String){
        _progreso.value = _progreso.value.copy(objetivo = newObjetivo)
    }
    fun updateMetaCaloria(newMetacalorica : String ){
        _progreso.value = _progreso.value.copy(metaCalorica = newMetacalorica)
    }
    fun updateFecha(newFecha : String ){
        _progreso.value = _progreso.value.copy(fecha = newFecha)
    }
    fun updateEnableObjetivo(estado : Boolean ){
        _progreso.value = _progreso.value.copy(enableObjetivo =  estado)
    }
    fun updateEnableMetacaloria(estado : Boolean ){
        _progreso.value = _progreso.value.copy(enablemetacalorica =  estado)
    }
    fun updateEnableFecha(estado : Boolean ){
        _progreso.value = _progreso.value.copy(enablefecha =  estado)
    }
}

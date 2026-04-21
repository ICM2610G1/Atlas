package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.Ejercicio
import com.example.atlas.modelos.SobreEjercicios
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EjercicioViewModel : ViewModel() {

    private val _estado = MutableStateFlow(SobreEjercicios())
    val estado: StateFlow<SobreEjercicios> = _estado.asStateFlow()

    fun agregarEjercicio(nombre: String) {
        val nuevo = Ejercicio(
            id = _estado.value.lista.size + 1,
            nombre = nombre,
            series = 3,
            rep = 10,
            kg = 22,
            completado = false
        )
        _estado.update {
            it.copy(lista = it.lista + nuevo)
        }
    }
    fun toggleCompletado(id: Int) {
        _estado.update {
            it.copy(
                lista = it.lista.map { ejercicio ->
                    if (ejercicio.id == id) {
                        ejercicio.copy(completado = !ejercicio.completado)
                    } else ejercicio
                }
            )
        }
    }
}

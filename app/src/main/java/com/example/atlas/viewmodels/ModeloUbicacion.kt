package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.EstadoUbicacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ModeloUbicacion : ViewModel() {

    private val _estado = MutableStateFlow(EstadoUbicacion())
    val estado: StateFlow<EstadoUbicacion> = _estado.asStateFlow()

    fun actualizarPosicion(lat: Double, lng: Double) {
        _estado.update { it.copy(latitud = lat, longitud = lng) }
    }

    fun actualizarDireccion(direccion: String) {
        _estado.update { it.copy(direccion = direccion) }
    }

    fun actualizarDeportista(nombre: String) {
        _estado.update { it.copy(nombreDeportista = nombre) }
    }

    // Cálculo de distancia
    fun calcularDistancia(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val latDistance = Math.toRadians(lat1 - lat2)
        val lngDistance = Math.toRadians(lng1 - lng2)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val resultado = 6371 * c
        return Math.round(resultado * 100.0) / 100.0
    }
}
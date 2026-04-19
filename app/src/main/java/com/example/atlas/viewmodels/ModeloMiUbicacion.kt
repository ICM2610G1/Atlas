package com.example.atlas.viewmodels

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.EstadoMiUbicacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class ModeloMiUbicacion : ViewModel() {

    private val _estado = MutableStateFlow(EstadoMiUbicacion())
    val estado: StateFlow<EstadoMiUbicacion> = _estado.asStateFlow()

    // Guarda la posición de inicio al comenzar la actividad
    fun registrarInicio(lat: Double, lng: Double) {
        _estado.update { it.copy(latInicio = lat, lngInicio = lng) }
        Log.i("ModeloMiUbicacion", "Inicio registrado: $lat, $lng")
    }

    // Actualiza posición actual y recalcula distancia recorrida
    fun actualizarPosicion(lat: Double, lng: Double) {
        val distancia = calcularDistancia(
            _estado.value.latInicio, _estado.value.lngInicio, lat, lng
        )
        _estado.update {
            it.copy(
                latitud = lat,
                longitud = lng,
                distanciaRecorrida = distancia
            )
        }
    }

    // Resuelve dirección textual con Geocoder
    fun resolverDireccion(context: Context, lat: Double, lng: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lng, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<android.location.Address>) {
                    if (addresses.isNotEmpty()) {
                        _estado.update {
                            it.copy(direccionActual = addresses[0].getAddressLine(0))
                        }
                    }
                }
            })
        } else {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                _estado.update {
                    it.copy(direccionActual = addresses[0].getAddressLine(0))
                }
            }
        }
    }

    // Función de distancia
    private fun calcularDistancia(
        lat1: Double, lng1: Double,
        lat2: Double, lng2: Double
    ): Double {
        val latDistance = Math.toRadians(lat1 - lat2)
        val lngDistance = Math.toRadians(lng1 - lng2)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) *
                Math.cos(Math.toRadians(lat2)) *
                Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return Math.round(6371 * c * 100.0) / 100.0
    }
}
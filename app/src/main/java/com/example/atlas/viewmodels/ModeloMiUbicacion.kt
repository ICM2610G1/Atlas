package com.example.atlas.viewmodels

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atlas.geocoder
import com.example.atlas.modelos.ElevationPoint
import com.example.atlas.modelos.EstadoMiUbicacion
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class ModeloMiUbicacion : ViewModel() {

    private val _estado = MutableStateFlow(EstadoMiUbicacion())
    val estado: StateFlow<EstadoMiUbicacion> = _estado.asStateFlow()

    fun registrarInicio(lat: Double, lng: Double) {
        _estado.update { it.copy(latInicio = lat, lngInicio = lng) }
        Log.i("ModeloMiUbicacion", "Inicio registrado: $lat, $lng")
    }

    fun actualizarTemperatura(temp: Float, promedio: Float) {
        _estado.update { it.copy(
            temperaturaActual = temp,
            temperaturaPromedio = promedio
        )}
    }

    fun agregarPuntoElevacion(distancia: Double, altitud: Float) {
        val puntos = _estado.value.puntosElevacion.toMutableList()
        val altitudRelativa = if (puntos.isEmpty()) 0f
        else altitud - puntos.first().altitud
        puntos.add(ElevationPoint(distancia, altitudRelativa))
        val minVal = puntos.minOf { it.altitud }
        val puntosAjustados = if (minVal < 0) {
            puntos.map { it.copy(altitud = it.altitud + Math.abs(minVal)) }
        } else puntos

        _estado.update { it.copy(puntosElevacion = puntosAjustados) }
    }

    fun actualizarPosicion(lat: Double, lng: Double) {
        val distancia = calcularDistancia(
            _estado.value.latInicio, _estado.value.lngInicio, lat, lng
        )
        _estado.update {
            it.copy(latitud = lat, longitud = lng, distanciaRecorrida = distancia)
        }
    }
    fun resolverDireccion(context: Context, lat: Double, lng: Double) {
        val geocoderLocal = Geocoder(context, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoderLocal.getFromLocation(lat, lng, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<android.location.Address>) {
                    if (addresses.isNotEmpty()) {
                        _estado.update { it.copy(direccionActual = addresses[0].getAddressLine(0)) }
                    }
                }
            })
        } else {
            val addresses = geocoderLocal.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                _estado.update { it.copy(direccionActual = addresses[0].getAddressLine(0)) }
            }
        }
    }
    fun establecerOrigen(lat: Double, lng: Double) {
        _estado.update { it.copy(posicionOrigen = LatLng(lat, lng)) }
    }

    fun resolverDestino(nombre: String) {
        val resultado = findLocation(nombre)
        resultado?.let {
            _estado.update { state -> state.copy(posicionDestino = it) }
        }
    }

    private fun findLocation(address: String): LatLng? {
        val addresses = geocoder.getFromLocationName(address, 2)
        if (!addresses.isNullOrEmpty()) {
            val addr = addresses[0]
            return LatLng(addr.latitude, addr.longitude)
        }
        return null
    }

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
    fun actualizarTiempo(segundos: Int) {
        _estado.update { it.copy(tiempoSegundos = segundos) }
    }

        fun calcularRuta(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val origen = _estado.value.posicionOrigen
            val destino = _estado.value.posicionDestino

            if (origen != null && destino != null) {
                val roadManager = org.osmdroid.bonuspack.routing.OSRMRoadManager(context, "ANDROID")
                val puntos = arrayListOf(org.osmdroid.util.GeoPoint(origen.latitude, origen.longitude),
                    org.osmdroid.util.GeoPoint(destino.latitude, destino.longitude)
                )
                val road = roadManager.getRoad(puntos)
                val puntosRuta = road.mRouteHigh.map {
                    LatLng(it.latitude, it.longitude)
                }
                _estado.update { it.copy(puntosRuta = puntosRuta) }
            }
        }
    }
}

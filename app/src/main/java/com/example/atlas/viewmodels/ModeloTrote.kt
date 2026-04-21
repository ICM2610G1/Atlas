package com.example.atlas.viewmodels

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.EstadoDeportista
import com.example.atlas.modelos.EstadoTrote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class ModeloTrote : ViewModel() {

    private val _estado = MutableStateFlow(EstadoTrote())
    val estado: StateFlow<EstadoTrote> = _estado.asStateFlow()

    fun cargarDeportistas() {
        _estado.update {
            it.copy(
                deportistas = listOf(
                    EstadoDeportista(nombre = "Andres Carvajal"),
                    EstadoDeportista(nombre = "Adriana Salazar"),
                    EstadoDeportista(nombre = "Pedro Gonzalez"),
                    EstadoDeportista(nombre = "Fernando Torres")
                )
            )
        }
    }

    fun actualizarUbicacion(indice: Int, lat: Double, lng: Double) {
        val lista = _estado.value.deportistas.toMutableList()
        if (indice < lista.size) {
            lista[indice] = lista[indice].copy(
                latitud = lat,
                longitud = lng,
                enLinea = true
            )
            _estado.update { it.copy(deportistas = lista) }
            Log.i("ModeloTrote", "Deportista $indice: lat=$lat lng=$lng")
        }
    }

    fun actualizarDireccion(indice: Int, direccion: String) {
        val lista = _estado.value.deportistas.toMutableList()
        if (indice < lista.size) {
            lista[indice] = lista[indice].copy(direccion = direccion)
            _estado.update { it.copy(deportistas = lista) }
        }
    }

    fun resolverDireccion(context: Context, indice: Int, lat: Double, lng: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(lat, lng, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<android.location.Address>) {
                    if (addresses.isNotEmpty()) {
                        actualizarDireccion(indice, addresses[0].getAddressLine(0))
                    }
                }
            })
        } else {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                actualizarDireccion(indice, addresses[0].getAddressLine(0))
            }
        }
    }
}
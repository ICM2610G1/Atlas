package com.example.atlas.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.atlas.database
import com.example.atlas.objetosDB.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CrearSesionUiState(
    val idSesionActiva: String = "",
    var troteSeleccionado: Boolean = false,
    val gymSeleccionado: Boolean = false
)

class CrearSesionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CrearSesionUiState())
    val uiState = _uiState.asStateFlow()

    fun updateTrote(value: Boolean) {
        _uiState.update { it.copy(troteSeleccionado = value) }
    }

    fun updateGym(value: Boolean) {
        _uiState.update { it.copy(gymSeleccionado = value) }
    }

    fun crearSesion() {
        if (_uiState.value.idSesionActiva.isNotEmpty()) return //esto es para que cada que entre a la pantalla no se resete el pushkey

        val refSesiones = database.getReference("Sesiones")
        val nuevoId = refSesiones.push().key
        if (nuevoId!=null) {
            refSesiones.child(nuevoId).setValue(Sesion())
                .addOnSuccessListener {
                    _uiState.update { it.copy(idSesionActiva = nuevoId) }
                    Log.i("DEBUG_SESION", "idSesionActiva listo: $nuevoId")
                }
        }
    }
    fun terminarSesion() {
        val idSesion = _uiState.value.idSesionActiva
        if (idSesion.isEmpty()) return
        database.getReference("Sesiones/$idSesion/completado").setValue(true)
            .addOnSuccessListener {
                Log.i("DEBUG_SESION", "Sesión $idSesion terminada")
            }
    }
}
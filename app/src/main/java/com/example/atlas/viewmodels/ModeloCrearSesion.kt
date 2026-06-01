package com.example.atlas.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.atlas.auth
import com.example.atlas.database
import com.example.atlas.objetosDB.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        if (_uiState.value.idSesionActiva.isNotEmpty()) return

        val uid = auth.currentUser?.uid ?: ""
        val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        
        val refSesiones = database.getReference("Sesiones")
        val nuevoId = refSesiones.push().key
        if (nuevoId!=null) {
            val nuevaSesion = Sesion(
                userId = uid,
                fecha = fecha,
                completado = false
            )
            refSesiones.child(nuevoId).setValue(nuevaSesion)
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

package com.example.atlas.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.atlas.database
import com.example.atlas.objetosDB.Ejercicio
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CatalogoEjercicioViewModel : ViewModel() {
    val dbReference = database.getReference("EjerciciosDisponibles/")

    private val _listaDisponibles = MutableStateFlow<List<CatalogoEjercicio>>(emptyList())
    val listaDisponibles: StateFlow<List<CatalogoEjercicio>> = _listaDisponibles.asStateFlow()

    var vel : ValueEventListener = dbReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val updatedList = mutableListOf<CatalogoEjercicio>()
            for (child in snapshot.children) {
                updatedList.add(
                    CatalogoEjercicio(
                        idActividad   = child.key ?: "",
                        nombre        = child.child("nombre").getValue(String::class.java) ?: "",
                        grupoMuscular = child.child("grupoMuscular").getValue(String::class.java) ?: ""
                    )
                )
            }
            _listaDisponibles.value = updatedList
        }
        override fun onCancelled(error: DatabaseError) {
            Log.w("EjercicioViewModel", "Failed to read value.", error.toException())
        }
    })
}

data class CatalogoEjercicio(
    val idActividad: String = "",
    val grupoMuscular: String = "",
    val nombre: String = ""
)


class DatosEjercicioViewModel : ViewModel() {
    private val _registerState = MutableStateFlow(Ejercicio())
    val registerState = _registerState.asStateFlow()
    fun updateSeries(v: Int) {
        _registerState.update { it.copy(series = v) }
    }

    fun updatePeso(value: Double) {
        _registerState.value = _registerState.value.copy(peso= value)
    }
    fun updateRepeticiones(value: Int) {
        _registerState.value = _registerState.value.copy(repeticiones = value)
    }
}


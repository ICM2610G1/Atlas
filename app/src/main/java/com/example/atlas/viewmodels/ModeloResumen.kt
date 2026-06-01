package com.example.atlas.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.atlas.database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ResumenSesionState(
    val tipoActividad: String = "",
    val distanciaKm: Double = 0.0,
    val velocidadPromedio: Double = 0.0,
    val temperaturaPromedio: Float = 0.0f,
    val tiempoSegundos: Int = 0,
    val numEjercicios: Int = 0,
    val caloriasAerobico: Double = 0.0,
    val caloriasFuerza: Double = 0.0,
    val caloriasTotal: Double = 0.0
)

class ResumenSesionViewModel : ViewModel() {

    private val _state = MutableStateFlow(ResumenSesionState())
    val state = _state.asStateFlow()

    private var listenerAerobico: ValueEventListener? = null
    private var listenerFuerza: ValueEventListener? = null

    fun cargarResumen(idSesion: String) {
        if (idSesion.isBlank()) {
            return
        }

        _state.value = ResumenSesionState()

        database.getReference("Sesiones/$idSesion")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val idAerobico = snapshot.child("idAerobico").getValue(String::class.java) ?: ""
                    val idFuerza = snapshot.child("idFuerza").getValue(String::class.java) ?: ""

                    Log.d("ResumenSesionViewModel", "idAerobico: $idAerobico, idFuerza: $idFuerza")

                    if (idAerobico.isNotBlank()) {
                        escucharAerobico(idAerobico)
                    }

                    if (idFuerza.isNotBlank()) {
                        escucharFuerza(idFuerza)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("ResumenSesionViewModel", "Error leyendo sesión", error.toException())
                }
            })
    }

    private fun escucharAerobico(idAerobico: String) {
        val ref = database.getReference("SesionesTrote/$idAerobico")

        listenerAerobico = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Log.d("ResumenSesionViewModel", "No existe SesionesTrote/$idAerobico")
                    return
                }

                val distancia = leerDouble(snapshot, "distancia")
                val velocidad = leerDouble(snapshot, "valeocidadPromedio")
                val temperatura = leerFloat(snapshot, "temperaturaPromedio")
                val duracion = leerInt(snapshot, "duracion")
                val tipoActividad = snapshot.child("tipoActividad").getValue(String::class.java) ?: ""

                val caloriasAerobico = 0.0

                _state.update {
                    it.copy(
                        distanciaKm = distancia,
                        velocidadPromedio = velocidad,
                        temperaturaPromedio = temperatura,
                        tiempoSegundos = duracion,
                        tipoActividad = tipoActividad,
                        caloriasAerobico = caloriasAerobico,
                        caloriasTotal = caloriasAerobico + it.caloriasFuerza
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ResumenSesionViewModel", "Error leyendo trote", error.toException())
            }
        })
    }

    private fun escucharFuerza(idFuerza: String) {
        val ref = database.getReference("SesionesFUERZA/$idFuerza/ejercicios")

        listenerFuerza = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val numEjercicios = snapshot.childrenCount.toInt()

                val caloriasFuerza = 0.0

                _state.update {
                    it.copy(
                        numEjercicios = numEjercicios,
                        caloriasFuerza = caloriasFuerza,
                        caloriasTotal = it.caloriasAerobico + caloriasFuerza
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ResumenSesionViewModel", "Error leyendo fuerza", error.toException())
            }
        })
    }

    private fun leerDouble(snapshot: DataSnapshot, campo: String): Double {
        val valor = snapshot.child(campo).value

        return when (valor) {
            is Long -> valor.toDouble()
            is Int -> valor.toDouble()
            is Double -> valor
            is Float -> valor.toDouble()
            is String -> valor.toDoubleOrNull() ?: 0.0
            else -> 0.0
        }
    }

    private fun leerFloat(snapshot: DataSnapshot, campo: String): Float {
        val valor = snapshot.child(campo).value

        return when (valor) {
            is Long -> valor.toFloat()
            is Int -> valor.toFloat()
            is Double -> valor.toFloat()
            is Float -> valor
            is String -> valor.toFloatOrNull() ?: 0.0f
            else -> 0.0f
        }
    }

    private fun leerInt(snapshot: DataSnapshot, campo: String): Int {
        val valor = snapshot.child(campo).value

        return when (valor) {
            is Long -> valor.toInt()
            is Int -> valor
            is Double -> valor.toInt()
            is Float -> valor.toInt()
            is String -> valor.toIntOrNull() ?: 0
            else -> 0
        }
    }

    override fun onCleared() {
        super.onCleared()

        listenerAerobico = null
        listenerFuerza = null
    }
}
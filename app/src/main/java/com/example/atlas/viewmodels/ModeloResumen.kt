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
        if (idSesion.isEmpty()) {
            return
        }
        database.getReference("Sesiones/$idSesion")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val idAerobico = snapshot.child("idAerobico").getValue(String::class.java) ?: ""
                    val idFuerza = snapshot.child("idFuerza").getValue(String::class.java) ?: ""

                    Log.d("ResumenSesionViewModel", "idAerobico: $idAerobico, idFuerza: $idFuerza")

                    if (idAerobico.isNotEmpty()) escucharAerobico(idAerobico)
                    if (idFuerza.isNotEmpty()) escucharFuerza(idFuerza)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun escucharAerobico(idAerobico: String) {
        val ref = database.getReference("SesionesTrote/$idAerobico")
        listenerAerobico = ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val distancia = snapshot.child("distancia").getValue(Double::class.java) ?: 0.0
                val velocidad = snapshot.child("valeocidadPromedio").getValue(Double::class.java) ?: 0.0
                val temperatura = snapshot.child("temperaturaPromedio").getValue(Float::class.java) ?: 0.0f
                val duracion = snapshot.child("duracion").getValue(Int::class.java) ?: 0
                val tipoActividad = snapshot.child("tipoActividad").getValue(String::class.java) ?: ""

                // fun calcularCaloriasAerobico(distancia: Double, tiempo: Int, temperatura: Float): Double
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

                // fun calcularCaloriasFuerza(ejercicios: List<Ejercicio>): Double
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

            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        listenerAerobico?.let {

        }
        listenerFuerza?.let {

        }
    }
}
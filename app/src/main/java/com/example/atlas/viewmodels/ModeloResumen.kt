package com.example.atlas.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.atlas.auth
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

    private var pesoUser: Float = 0.0f
    private var duracionSesionGeneralSegundos: Int = 0

    fun cargarResumen(idSesion: String) {
        if (idSesion.isEmpty()) {
            return
        }

        database.getReference("deportistas/${auth.currentUser?.uid}/peso")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(pesoSnapshot: DataSnapshot) {
                    pesoUser = pesoSnapshot.getValue(Float::class.java) ?: 0f
                    cargarDatosDeSesion(idSesion)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w("ResumenSesionViewModel", "Error leyendo peso", error.toException())
                    cargarDatosDeSesion(idSesion)
                }
            })
    }

    private fun cargarDatosDeSesion(idSesion: String) {
        database.getReference("Sesiones/$idSesion")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    duracionSesionGeneralSegundos = snapshot.child("duracion").getValue(Int::class.java) ?: 0

                    val idAerobico = snapshot.child("idAerobico").getValue(String::class.java) ?: ""
                    val idFuerza = snapshot.child("idFuerza").getValue(String::class.java) ?: ""

                    Log.d("ResumenSesionViewModel", "idAerobico: $idAerobico, idFuerza: $idFuerza")

                    if (idAerobico.isNotEmpty()) escucharAerobico(idAerobico)
                    if (idFuerza.isNotEmpty()) escucharFuerza(idFuerza)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w("ResumenSesionViewModel", "Error cargando estructura de sesión", error.toException())
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

                val factorDeTemperatura = if (temperatura > 30f) 1.1f else 1.0f

                val tiempoHoras = duracion / 3600.0f
                val x = tipoActividad.uppercase()
                val met = when {
                    x == "TROTE" -> 9.8f
                    x == "CICLISMO" -> 4.0f
                    x == "SENDERISMO" -> 7.0f
                    else -> 7.0f
                }
                val caloriasAerobico = (met * pesoUser * tiempoHoras * factorDeTemperatura).toDouble()

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

                Log.i("Verificación","${state.value.distanciaKm}")
                Log.i("Verificación","${state.value.caloriasTotal}")
                Log.i("Verificación","${state.value.numEjercicios}")
                Log.i("Verificación","${state.value.tiempoSegundos}")
                Log.i("Verificación","${state.value.velocidadPromedio}")
                Log.i("Verificación","${state.value.caloriasAerobico}")
                Log.i("Verificación","${state.value.caloriasFuerza}")
                Log.i("Verificación","${state.value.temperaturaPromedio}")
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

                Log.i(
                    "DEBUG_FUERZA",
                    "Ejercicios encontrados: ${snapshot.childrenCount}"
                )

                val numEjercicios = snapshot.childrenCount.toInt()

                if (numEjercicios == 0) {
                    _state.update {
                        it.copy(
                            numEjercicios = 0,
                            caloriasFuerza = 0.0
                        )
                    }
                    return
                }

                var acumuladoCaloriasFuerza = 0.0

                val tiempoEjercicioHoras = 2f

                val factorDeTemperatura = 1.0f

                for (ejercicioSnapshot in snapshot.children) {

                    val grupoMuscular =
                        ejercicioSnapshot.child("grupoMuscular")
                            .getValue(String::class.java)
                            ?: ""

                    Log.i(
                        "DEBUG_FUERZA",
                        "Grupo muscular encontrado: $grupoMuscular"
                    )

                    val met = when (grupoMuscular.uppercase()) {
                        "HOMBRO",
                        "PIERNA",
                        "PECHO",
                        "ESPALDA",
                        "BRAZO" -> 6.0f

                        else -> 5.0f
                    }

                    val caloriasEjercicio =
                        met * pesoUser * tiempoEjercicioHoras * factorDeTemperatura

                    acumuladoCaloriasFuerza += caloriasEjercicio
                }

                _state.update {
                    it.copy(
                        numEjercicios = numEjercicios,
                        caloriasFuerza = acumuladoCaloriasFuerza,
                        caloriasTotal = it.caloriasAerobico + acumuladoCaloriasFuerza
                    )
                }

                Log.i(
                    "DEBUG_FUERZA",
                    "Calorías fuerza: $acumuladoCaloriasFuerza"
                )

                Log.i(
                    "DEBUG_FUERZA",
                    "Calorías totales: ${state.value.caloriasTotal}"
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(
                    "ResumenSesionViewModel",
                    "Error leyendo ejercicios de fuerza",
                    error.toException()
                )
            }
        })
    }
    override fun onCleared() {
        super.onCleared()
    }
}
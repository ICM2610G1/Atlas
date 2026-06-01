package com.example.atlas.objetosDB

import com.example.atlas.modelos.ElevationPoint



data class Sesion(
    val completado: Boolean = false,
    val caloriasQuemadas: Double = 0.0,
    val duracion: Int = 0,
    val idAerobico: String = "",
    val idFuerza: String = ""
)

data class sesionAerobica(
    val sesionId: String = "",
    val lugarInicio: String = "",
    val lugarFinal: String = "",
    val temperaturaPromedio: Float = 0.0f,
    val tipoActividad: String? = "",
    val valeocidadPromedio: Double = 0.0,
    val puntosElevacion: List<ElevationPoint> = emptyList(),
    val duracion: Int = 0,
    val distancia: Double = 0.0
)

data class Ejercicio(
    val idActividad: String = "",
    val nombre: String = "",
    val grupoMuscular: String = "",
    val series: Int = 0,
    val repeticiones: Int = 0,
    val peso: Double = 0.0,
    val completado: Boolean = false
)

data class sesionFuerza(
    val sesionId: String = "",
    val ejercicios: List<Ejercicio> = emptyList()
)
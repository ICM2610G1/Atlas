package com.example.atlas.objetosDB

data class UsuariosGen(
    val id: String="",
    val imagen: String="",
    val nombre: String="",
    val rol: String ="",
    val correo: String="",
)
data class Deportista(
    val id: String= "",
    val imagen:String="",
    val nombre: String="",
    val fechaNacimiento : String="",
    val telefono: String= "",
    val correo: String="",
    val peso: Double=0.0,
    val altura: Double=0.0,
    val observacionesMedicas: String="",
    val entrenadores : List<String> =emptyList<String>(),
    val disponible: Boolean = false
)

data class Entrenador (
    val id: String ="",
    val imagen: String ="",
    val nombre: String ="",
    val fechaNacimiento: String = "",
    val telefono: String = "",
    val correo: String ="",
    val anosProfesionales: Double =0.0,
    val especialidad: String ="",
    val calificacionPromedio: Double =0.0,
    val entrenadores : List<String> =emptyList<String>()
)

data class Valoracion(
    val idDeportista: String = "",
    val idEntrenador: String = "",
    val calificacion: Int = 0,
    val comentario: String = "",
    val fecha: String = ""
)

package com.example.atlas.modelos

import com.google.android.gms.maps.model.LatLng


data class EstadoVisualizarDeportista(
    val nombre: String = "Andres Carvajal",
    val edad: String = "20 años",
    val peso: String = "75 kg",
    val altura: String = "1.75 cm",
    val recomendaciones: String = "N.A",
    val objetivo: String = "Aumentar masa\nmuscular en un\n50%"
)

data class EstadoDeportista(
    val nombre: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val direccion: String = "Obteniendo ubicación...",
    val enLinea: Boolean = false
)


//PARA GUARDAR DATOS MOCKEABLES


data class ElevationPoint(
    val distancia: Double=0.0,
    val altitud: Float=0.0f
)

//PARA EL MAPA MONOUSUARIO

data class EstadoMiUbicacion(
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val latInicio: Double = 0.0,
    val lngInicio: Double = 0.0,
    val direccionActual: String = "",
    val distanciaRecorrida: Double = 0.0,
    val posicionOrigen: LatLng? = null,
    val posicionDestino: LatLng? = null,
    val temperaturaActual: Float = 0f,
    val temperaturaPromedio: Float = 0f,
    val puntosElevacion: List<ElevationPoint> = emptyList(),
    val tiempoSegundos: Int = 0,
    val puntosRuta: List<LatLng> = emptyList()
)


//DE AUTENTICACION
data class Authstate(
    val email: String = "",
    val password: String = "",
    val emailError: String = "",
    val passwordError: String = ""
)
//PARA LOS PERFILES

data class EstadoPerfil(
    val usuario: String = "",
    val telefono: String = "",
    val correo: String = "",
    val peso: String = "",
    val estatura: String = "",
    val obMedicas: String = "",
    val uriImagen: android.net.Uri? = null
)

data class EstadoPerfilEnt(
    val usuario: String = "",
    val telefono: String = "",
    val correo: String = "",
    val aniosEjerciendo: String = "",
    val especialidad: String = "",
    val sobreTi: String = "",
    val uriImagen: android.net.Uri? = null
)
//PARA LAS PANTALLAS ASOCIADAS A EJERCICIOS

data class Progreso(
    val objetivo: String = "",
    val metaCalorica : String = "" ,
    val fecha  : String = "" ,
    val enableObjetivo  : Boolean = false,
    val enablemetacalorica: Boolean   = false,
    val enablefecha: Boolean = false

)
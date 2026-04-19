package com.example.atlas.modelos

data class EstadoCalificar(
    val calificacion: Int = 0,
    val comentario: String = "",
    val entrenador: String = ""
)

data class EstadoChequeoSesion(
    val ejerciciosMarcados: List<Boolean> = listOf(false, false, false, false, false)
)

data class EstadoVisualizarDeportista(
    val nombre: String = "Andres Carvajal",
    val edad: String = "20 años",
    val peso: String = "75 kg",
    val altura: String = "1.75 cm",
    val recomendaciones: String = "N.A",
    val objetivo: String = "Aumentar masa\nmuscular en un\n50%"
)

data class DeportistaTrote(
    val nombre: String,
    val ubicacion: String
)

data class EstadoTroteActivo(
    val deportistas: List<DeportistaTrote> = listOf(
        DeportistaTrote("Andres Carvajal", "Parque Nacional"),
        DeportistaTrote("Adriana Salazar", "Av. Boyacá, Calle 80"),
        DeportistaTrote("Pedro Gonzalez", "Cra 50, Av. Esperanza"),
        DeportistaTrote("Fernando Torres", "CC Gran Estación")
    ),
    val distancia: Double = 0.0,
    val pasos: Int = 0,
    val tiempoSegundos: Int = 0,
    val trotando: Boolean = false
)

data class EstadoLogIn(
    val usuario: String = "",
    val contrasena: String = "",
    val autenticado: Boolean = false,
    val mensajeError: String = ""
)

data class EstadoDeportista(
    val nombre: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val direccion: String = "Obteniendo ubicación...",
    val enLinea: Boolean = false
)

data class EstadoTrote(
    val deportistas: List<EstadoDeportista> = emptyList()
)

data class EstadoUbicacion(
    val nombreDeportista: String = "Deportista",
    val latitud: Double = 4.627293,
    val longitud: Double = -74.063228,
    val direccion: String = "Obteniendo ubicación...",
    val distanciaKm: Double = 0.0,
    val tiempoMin: Int = 0
)

data class EstadoMiUbicacion(
    val latitud: Double = 4.627293,
    val longitud: Double = -74.063228,
    val direccionActual: String = "Obteniendo ubicación...",
    val distanciaRecorrida: Double = 0.0,
    val latInicio: Double = 0.0,
    val lngInicio: Double = 0.0
)
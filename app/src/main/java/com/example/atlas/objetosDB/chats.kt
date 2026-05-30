package com.example.atlas.objetosDB

data class ChatDB(
    var idChat: String = "",
    var idOtroUsuario: String = "",
    var nombreOtroUsuario: String = "",
    var imagenOtroUsuario: String = "",
    var ultimoMensaje: String = "",
    var ultimoTimestamp: Long = 0L
)

data class MensajeDB(
    var idMensaje: String = "",
    var idEmisor: String = "",
    var nombreEmisor: String = "",
    var texto: String = "",
    var timestamp: Long = 0L
)
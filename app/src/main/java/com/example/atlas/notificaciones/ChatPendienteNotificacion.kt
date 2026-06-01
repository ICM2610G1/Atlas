package com.example.atlas.notificaciones

object ChatPendienteNotificacion {
    var idChat: String = ""
    var nombre: String = ""

    fun guardar(idChatNuevo: String, nombreNuevo: String) {
        idChat = idChatNuevo
        nombre = nombreNuevo
    }

    fun limpiar() {
        idChat = ""
        nombre = ""
    }

    fun hayChatPendiente(): Boolean {
        return idChat.isNotBlank()
    }
}
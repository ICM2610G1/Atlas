package com.example.atlas.notificaciones

object TrotePendienteNotificacion {
    var idDeportista: String = ""
    var nombreDeportista: String = ""

    fun guardar(idDeportistaNuevo: String, nombreDeportistaNuevo: String) {
        idDeportista = idDeportistaNuevo
        nombreDeportista = nombreDeportistaNuevo
    }

    fun limpiar() {
        idDeportista = ""
        nombreDeportista = ""
    }

    fun hayTrotePendiente(): Boolean {
        return idDeportista.isNotBlank()
    }
}
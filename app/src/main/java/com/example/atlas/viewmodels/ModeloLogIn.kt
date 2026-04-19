package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.EstadoLogIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ModeloLogIn : ViewModel() {

    private val _estado = MutableStateFlow(EstadoLogIn())
    val estado: StateFlow<EstadoLogIn> = _estado.asStateFlow()

    fun actualizarUsuario(valor: String) {
        _estado.update { it.copy(usuario = valor) }
    }

    fun actualizarContrasena(valor: String) {
        _estado.update { it.copy(contrasena = valor) }
    }

    // Llamado cuando el resultado de la autenticación es exitoso
    fun marcarAutenticado() {
        _estado.update { it.copy(autenticado = true, mensajeError = "") }
    }

    // Llamado cuando la autenticación falla o es cancelada
    fun registrarError(mensaje: String) {
        _estado.update { it.copy(autenticado = false, mensajeError = mensaje) }
    }
}
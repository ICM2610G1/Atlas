package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.RegisterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel : ViewModel() {
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    fun updateUsuario(value: String) {
        _registerState.value = _registerState.value.copy(usuario = value)
    }
    fun updateTelefono(value: String) {
        _registerState.value = _registerState.value.copy(telefono = value)
    }
    fun updateCorreo(value: String)
    { _registerState.value = _registerState.value.copy(correo = value)

    }
    fun updatePass(value: String) {
        _registerState.value = _registerState.value.copy(pass = value)
    }
    fun toggleMostrarPass()
    { _registerState.value = _registerState.value.copy(mostrarPass = !_registerState.value.mostrarPass)

    }
    fun toggleAceptar() {
        _registerState.value = _registerState.value.copy(aceptar = !_registerState.value.aceptar)
    }
}

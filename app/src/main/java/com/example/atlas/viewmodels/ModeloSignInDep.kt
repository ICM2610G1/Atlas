package com.example.atlas.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DepRegisterState(
    val imagenUri: Uri?=null,
    val nombre: String = "",
    val nombreError:String= "",
    val telefono: String = "",
    val telefonoError: String="",
    val correo: String = "",
    val correoError: String="",
    val pass: String = "",
    val passError: String="",
    val peso: Double=0.0,
    val pesoError: String="",
    val altura: Double=0.0,
    val alturaError:String="",
    val observacionesMedicas: String="",
    val fechaNacimiento: String = "",
    val fechaNacimientoError: String = "",
    val mostrarPass: Boolean = false,
    val aceptar: Boolean = false
)

class DepRegisterViewModel : ViewModel() {
    private val _registerState = MutableStateFlow(DepRegisterState())
    val registerState = _registerState.asStateFlow()
    fun actImagen(v: Uri?) = _registerState.update { it.copy(imagenUri = v) }

    fun updateUsuario(value: String) {
        _registerState.value = _registerState.value.copy(nombre = value)
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
    fun updatePeso(value: Double) {
        _registerState.value = _registerState.value.copy(peso = value)
    }
    fun updatealtura(value: Double) {
        _registerState.value = _registerState.value.copy(altura = value)
    }
    fun updateObs(value: String) {
        _registerState.value = _registerState.value.copy(observacionesMedicas = value)
    }
    fun toggleMostrarPass()
    { _registerState.value = _registerState.value.copy(mostrarPass = !_registerState.value.mostrarPass)

    }
    fun updateFechaNacimiento(value: String) {
        _registerState.value = _registerState.value.copy(fechaNacimiento = value)
    }
    fun actFechaError(v: String) = _registerState.update { it.copy(fechaNacimientoError = v) }
    fun toggleAceptar() {
        _registerState.value = _registerState.value.copy(aceptar = !_registerState.value.aceptar)
    }
    fun actNombreError(v: String) = _registerState.update { it.copy(nombreError = v) }

    fun actCorreoError(v: String) = _registerState.update { it.copy(correoError = v) }
    fun actpassError(v: String) = _registerState.update { it.copy(passError = v) }
    fun actTelefonoError(v: String) = _registerState.update { it.copy(telefonoError = v) }
    fun actPesoError(v: String) = _registerState.update { it.copy(pesoError = v) }
    fun actAlturaError(v: String) = _registerState.update { it.copy(alturaError = v) }
}

fun validateDepRegisterForm(model: DepRegisterViewModel, state: DepRegisterState): Boolean {
    if (state.nombre.isEmpty()) {
        model.actNombreError("El nombre es obligatorio")
        return false
    } else model.actNombreError("")
    if (state.fechaNacimiento.isEmpty() || !validDateFormat(state.fechaNacimiento)) {
        model.actCorreoError("Formato de fecha inválido")
        return false
    } else model.actFechaError("")

    if (state.telefono.isEmpty()) {
        model.actTelefonoError("El telefono es obligatorio")
        return false
    } else model.actTelefonoError("")

    if (state.correo.isEmpty() || !validEmailAddress(state.correo)) {
        model.actCorreoError("Correo inválido")
        return false
    } else model.actCorreoError("")

    if (state.pass.length < 6) {
        model.actpassError("Mínimo 6 caracteres")
        return false
    } else model.actpassError("")

    if(state.peso <=0.0){
        model.actPesoError("El peso es necesario")
        return false
    }else model.actPesoError("")

    if(state.altura <=0.0){
        model.actAlturaError("La altura es necesaria")
        return false
    }else model.actAlturaError("")
    return true
}
fun validEmailAddress(email:String):Boolean{
    val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    return email.matches(regex.toRegex())
}
fun validDateFormat(fecha: String): Boolean {
    val regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(19|20)\\d\\d$"
    return fecha.matches(regex.toRegex())
}

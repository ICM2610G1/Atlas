package com.example.atlas.viewmodels


import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EntRegisterState(
    val imagenUri: Uri?=null,
    val nombre: String = "",
    val nombreError:String= "",
    val telefono: String = "",
    val telefonoError: String="",
    val correo: String = "",
    val correoError: String="",
    val pass: String = "",
    val passError: String="",
    val fechaNacimiento: String = "",
    val fechaNacimientoError: String = "",
    val especialidad: String="",
    val especialidadError: String="",
    val experiencia: Double=0.0,
    val expError:String="",
    val calificacionProm: Double=0.0,
    val cantidadCalificacion: Int=0,
    val aceptar: Boolean = false,
    val mostrarPass: Boolean = false
)

class EntRegisterViewModel : ViewModel() {
    private val _registerState = MutableStateFlow(EntRegisterState())
    val registerState = _registerState.asStateFlow()

    fun updateNombre(value: String) {
        _registerState.value = _registerState.value.copy(nombre = value)
    }
    fun updateTelefono(value: String) {
        _registerState.value = _registerState.value.copy(telefono = value)
    }
    fun updateCorreo(value: String)
    {
        _registerState.value = _registerState.value.copy(correo = value)
    }
    fun updateFechaNacimiento(value: String) {
        _registerState.value = _registerState.value.copy(fechaNacimiento = value)
    }
    fun actFechaError(v: String) = _registerState.update { it.copy(fechaNacimientoError = v) }
    fun updatePass(value: String) {
        _registerState.value = _registerState.value.copy(pass = value)
    }
    fun actImagen(v: Uri?) = _registerState.update { it.copy(imagenUri = v) }
    fun updateEspecialidad(value: String) {
        _registerState.value = _registerState.value.copy(especialidad = value)
    }
    fun updateExperiencia(value: Double) {
        _registerState.value = _registerState.value.copy(experiencia= value)
    }
    fun toggleMostrarPass()
    { _registerState.value = _registerState.value.copy(mostrarPass = !_registerState.value.mostrarPass)

    }
    fun toggleAceptar() {
        _registerState.value = _registerState.value.copy(aceptar = !_registerState.value.aceptar)
    }
    fun actNombreError(v: String) = _registerState.update { it.copy(nombreError = v) }

    fun actCorreoError(v: String) = _registerState.update { it.copy(correoError = v) }
    fun actpassError(v: String) = _registerState.update { it.copy(passError = v) }
    fun actTelefonoError(v: String) = _registerState.update { it.copy(telefonoError = v) }
    fun actExpError(v: String) = _registerState.update { it.copy(expError = v) }
    fun actEspError(v: String) = _registerState.update { it.copy(especialidadError = v) }
}

fun validateEntRegisterForm(model: EntRegisterViewModel, state: EntRegisterState): Boolean {
    if (state.nombre.isEmpty()) {
        model.actNombreError("El nombre es obligatorio")
        return false
    } else model.actNombreError("")

    if (state.telefono.isEmpty()) {
        model.actTelefonoError("El telefono es obligatorio")
        return false
    } else model.actTelefonoError("")

    if (state.fechaNacimiento.isEmpty() || !validDateFormat(state.fechaNacimiento)) {
        model.actCorreoError("Formato de fecha inválido")
        return false
    } else model.actFechaError("")

    if (state.correo.isEmpty() || !validEmailAddress(state.correo)) {
        model.actCorreoError("Correo inválido")
        return false
    } else model.actCorreoError("")

    if (state.pass.length < 6) {
        model.actpassError("Mínimo 6 caracteres")
        return false
    } else model.actpassError("")

    if(state.experiencia <=0.0){
        model.actExpError("Debes tener experiencia como entrenador para registrarte")
        return false
    }else model.actExpError("")

    if(state.especialidad.isEmpty()){
        model.actEspError("Tu especialidad es necesaria")
        return false
    }else model.actEspError("")
    return true
}

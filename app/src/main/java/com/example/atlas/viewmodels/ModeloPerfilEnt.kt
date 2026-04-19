package com.example.atlas.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.EstadoPerfilEnt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ModeloPerfilEnt : ViewModel() {

    private val _estado = MutableStateFlow(EstadoPerfilEnt())
    val estado: StateFlow<EstadoPerfilEnt> = _estado.asStateFlow()

    fun actualizarUsuario(valor: String) = _estado.update { it.copy(usuario = valor) }
    fun actualizarTelefono(valor: String) = _estado.update { it.copy(telefono = valor) }
    fun actualizarCorreo(valor: String) = _estado.update { it.copy(correo = valor) }
    fun actualizarAnios(valor: String) = _estado.update { it.copy(aniosEjerciendo = valor) }
    fun actualizarEspecialidad(valor: String) = _estado.update { it.copy(especialidad = valor) }
    fun actualizarSobreTi(valor: String) = _estado.update { it.copy(sobreTi = valor) }
    fun guardarFotoCamara(uri: Uri) = _estado.update { it.copy(uriImagen = uri) }
    fun guardarFotoGaleria(uri: Uri?) = _estado.update { it.copy(uriImagen = uri) }
}
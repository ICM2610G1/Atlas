package com.example.atlas.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.atlas.modelos.EstadoPerfil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ModeloPerfil : ViewModel() {

    private val _estado = MutableStateFlow(EstadoPerfil())
    val estado: StateFlow<EstadoPerfil> = _estado.asStateFlow()

    fun actualizarUsuario(valor: String) = _estado.update { it.copy(usuario = valor) }
    fun actualizarTelefono(valor: String) = _estado.update { it.copy(telefono = valor) }
    fun actualizarCorreo(valor: String) = _estado.update { it.copy(correo = valor) }
    fun actualizarPeso(valor: String) = _estado.update { it.copy(peso = valor) }
    fun actualizarEstatura(valor: String) = _estado.update { it.copy(estatura = valor) }
    fun actualizarObMedicas(valor: String) = _estado.update { it.copy(obMedicas = valor) }

    // Foto desde cámara
    fun guardarFotoCamara(uri: Uri) = _estado.update { it.copy(uriImagen = uri) }

    // Foto desde galería
    fun guardarFotoGaleria(uri: Uri?) = _estado.update { it.copy(uriImagen = uri) }
}
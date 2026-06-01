package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.database
import com.example.atlas.objetosDB.MensajeDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ModeloMensajesChat : ViewModel() {
    val _mensajes = MutableStateFlow(listOf<MensajeDB>())
    val mensajes: StateFlow<List<MensajeDB>> = _mensajes.asStateFlow()
    var idChatActual = ""
    var refMensajes = database.getReference("mensajes/")
    var velMensajes: ValueEventListener? = null
    fun cargarMensajes(idChat: String) {
        if (idChatActual == idChat) {
            return
        }
        velMensajes?.let {
            refMensajes.removeEventListener(it)
        }
        idChatActual = idChat
        refMensajes = database.getReference("mensajes/").child(idChat)

        velMensajes = refMensajes.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val listaMensajes = mutableListOf<MensajeDB>()
                for (child in snapshot.children) {
                    val mensaje = child.getValue<MensajeDB>()
                    mensaje?.let {
                        listaMensajes.add(it)
                    }
                }
                _mensajes.value = listaMensajes.sortedBy {
                    it.timestamp
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    override fun onCleared() {
        super.onCleared()
        velMensajes?.let {
            refMensajes.removeEventListener(it)
        }
    }
}
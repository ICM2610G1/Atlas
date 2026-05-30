package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.auth
import com.example.atlas.database
import com.example.atlas.objetosDB.UsuariosGen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ModeloUsuariosChat : ViewModel() {

    val refUsuarios = database.getReference("UsuarioGen/")

    val _usuarios = MutableStateFlow(listOf<UsuariosGen>())
    val usuarios: StateFlow<List<UsuariosGen>> = _usuarios.asStateFlow()

    var velUsuarios: ValueEventListener =
        refUsuarios.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val listaUsuarios = mutableListOf<UsuariosGen>()
                val idActual = auth.currentUser?.uid ?: ""

                for (child in snapshot.children) {
                    val usuario = child.getValue<UsuariosGen>()

                    usuario?.let {
                        if (it.id != idActual) {
                            listaUsuarios.add(it)
                        }
                    }
                }

                _usuarios.value = listaUsuarios
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    override fun onCleared() {
        super.onCleared()
        refUsuarios.removeEventListener(velUsuarios)
    }
}
package com.example.atlas.viewmodels

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.atlas.auth
import com.example.atlas.database
import com.example.atlas.modelos.EstadoDeportista
import com.example.atlas.objetosDB.Deportista
import com.example.atlas.objetosDB.UsuariosGen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class ModeloTrote : ViewModel(){
    val dbReference = database.getReference("entrenadores/${auth.currentUser?.uid}/deportistas")
    val _deportistas = MutableStateFlow(listOf<ubicacionUsuario>())
    val deportistas: StateFlow<List<ubicacionUsuario>> = _deportistas.asStateFlow()

    var vel: ValueEventListener =
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ids = mutableListOf<String>()
                for (child in snapshot.children) {
                    child.getValue(String::class.java)?.let { ids.add(it) }
                }

                if (ids.isEmpty()) {
                    _deportistas.value = emptyList()
                    return
                }
                val usuariosRef = database.getReference("ubicacionUsuario")
                usuariosRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapUsuarios: DataSnapshot) {
                        val updatedList = mutableListOf<ubicacionUsuario>()
                        for (id in ids) {
                            val usuario = snapUsuarios.child(id).getValue(ubicacionUsuario::class.java)
                            usuario?.let {
                                if (it.disponible) {
                                    updatedList.add(it)
                                }
                            }
                        }
                        _deportistas.value = updatedList
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _deportistas.value = emptyList()
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                _deportistas.value = emptyList()
            }
        })
}

data class ubicacionUsuario(
    val id: String = "",
    val nombre: String = "",
    val imagen: String = "",
    val disponible: Boolean = false,
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val tipoActividad: String="",
    val lugarFinal: String=""

)

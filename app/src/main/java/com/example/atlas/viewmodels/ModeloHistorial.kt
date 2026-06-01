package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.auth
import com.example.atlas.database
import com.example.atlas.objetosDB.Sesion
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SesionConId(
    val id: String,
    val sesion: Sesion
)

class ModeloHistorial : ViewModel() {
    private val _sesiones = MutableStateFlow<List<SesionConId>>(emptyList())
    val sesiones = _sesiones.asStateFlow()

    init {
        fetchSesiones()
    }

    private fun fetchSesiones() {
        val uid = auth.currentUser?.uid ?: return
        database.getReference("Sesiones").orderByChild("userId").equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lista = mutableListOf<SesionConId>()
                    for (child in snapshot.children) {
                        val s = child.getValue<Sesion>()
                        s?.let {
                            lista.add(SesionConId(child.key ?: "", it))
                        }
                    }
                    // Ordenar por fecha o ID (descendente para ver las más recientes primero)
                    _sesiones.value = lista.reversed()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}

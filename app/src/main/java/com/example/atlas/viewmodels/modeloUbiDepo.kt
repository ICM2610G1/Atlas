package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UbicacionViewModel : ViewModel(){

    val _user = MutableStateFlow<ubicacionUsuario?>(null)
    val user : StateFlow<ubicacionUsuario?> = _user.asStateFlow()
    var vel : ValueEventListener? =null
    fun ubicar(id: String) {
        val dbReference = database.getReference("ubicacionUsuario/").child(id)
        vel= dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<ubicacionUsuario>()
                user?.let {
                    _user.value = it
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    override fun onCleared() {
        super.onCleared()
        vel?.let {database.getReference("ubicacionUsuario/").child(_user.value?.id ?: "")
            .removeEventListener(it) }
    }
}
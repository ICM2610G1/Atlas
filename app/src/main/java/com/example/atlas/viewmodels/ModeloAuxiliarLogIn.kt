package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginAuxViewModel : ViewModel() {

    fun obtenerTipoCuenta(uid: String, onResultado: (String?) -> Unit, onError: (String) -> Unit) {
        val userGenRef = database.getReference("UsuarioGen/$uid")

        userGenRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val tipoCuenta = snapshot.child("rol").getValue(String::class.java)
                    onResultado(tipoCuenta)
                } else {
                    onResultado(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }
}
package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.auth
import com.example.atlas.database
import com.example.atlas.objetosDB.ChatDB
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ModeloChatsUsuario : ViewModel() {

    val idActual = auth.currentUser?.uid ?: ""

    val refChats = database
        .getReference("chatsPorUsuario/")
        .child(idActual)

    val _chats = MutableStateFlow(listOf<ChatDB>())
    val chats: StateFlow<List<ChatDB>> = _chats.asStateFlow()

    var velChats: ValueEventListener =
        refChats.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val listaChats = mutableListOf<ChatDB>()

                for (child in snapshot.children) {
                    val chat = child.getValue<ChatDB>()

                    chat?.let {
                        listaChats.add(it)
                    }
                }

                _chats.value = listaChats.sortedByDescending {
                    it.ultimoTimestamp
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    override fun onCleared() {
        super.onCleared()
        refChats.removeEventListener(velChats)
    }
}
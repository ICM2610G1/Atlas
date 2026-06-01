package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.auth
import com.example.atlas.database
import com.example.atlas.objetosDB.ChatDB
import com.example.atlas.objetosDB.UsuariosGen
import com.google.firebase.database.getValue

class ModeloCrearChat : ViewModel() {

    fun crearChat(usuario: UsuariosGen, irAlChat: (String, String) -> Unit) {
        val idActual = auth.currentUser?.uid ?: return
        val idOtro = usuario.id

        val idChat = crearIdChat(idActual, idOtro)

        database.getReference("UsuarioGen/")
            .child(idActual)
            .get()
            .addOnSuccessListener { snapshot ->

                val usuarioActual = snapshot.getValue<UsuariosGen>()
                    ?: return@addOnSuccessListener

                val ahora = System.currentTimeMillis()

                val chatParaActual = ChatDB(
                    idChat = idChat,
                    idOtroUsuario = usuario.id,
                    nombreOtroUsuario = usuario.nombre,
                    imagenOtroUsuario = usuario.imagen,
                    ultimoMensaje = "",
                    ultimoTimestamp = ahora
                )

                val chatParaOtro = ChatDB(
                    idChat = idChat,
                    idOtroUsuario = usuarioActual.id,
                    nombreOtroUsuario = usuarioActual.nombre,
                    imagenOtroUsuario = usuarioActual.imagen,
                    ultimoMensaje = "",
                    ultimoTimestamp = ahora
                )

                database.getReference("chatsPorUsuario/")
                    .child(idActual)
                    .child(idChat)
                    .setValue(chatParaActual)

                database.getReference("chatsPorUsuario/")
                    .child(idOtro)
                    .child(idChat)
                    .setValue(chatParaOtro)
                    .addOnSuccessListener {
                        irAlChat(idChat, usuario.nombre)
                    }
            }
    }

    private fun crearIdChat(id1: String, id2: String): String {
        return listOf(id1, id2).sorted().joinToString("_")
    }
}
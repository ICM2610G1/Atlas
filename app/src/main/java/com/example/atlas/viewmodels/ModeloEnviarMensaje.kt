package com.example.atlas.viewmodels

import androidx.lifecycle.ViewModel
import com.example.atlas.auth
import com.example.atlas.database
import com.example.atlas.objetosDB.ChatDB
import com.example.atlas.objetosDB.MensajeDB
import com.example.atlas.objetosDB.UsuariosGen

class ModeloEnviarMensaje : ViewModel() {

    fun enviarMensaje(
        idChat: String,
        texto: String
    ) {
        val idActual = auth.currentUser?.uid


        if (idActual != null && idChat.isNotBlank() && texto.isNotBlank()) {

            database.getReference("UsuarioGen/")
                .child(idActual)
                .get()
                .addOnSuccessListener { snapshotUsuario ->

                    val usuarioActual = snapshotUsuario.getValue(UsuariosGen::class.java)

                    if (usuarioActual != null) {

                        val refMensajes = database
                            .getReference("mensajes/")
                            .child(idChat)

                        val idMensaje = refMensajes.push().key

                        if (idMensaje != null) {

                            val ahora = System.currentTimeMillis()

                            val mensaje = MensajeDB(
                                idMensaje = idMensaje,
                                idEmisor = idActual,
                                nombreEmisor = usuarioActual.nombre,
                                texto = texto,
                                timestamp = ahora
                            )

                            refMensajes
                                .child(idMensaje)
                                .setValue(mensaje)
                                .addOnSuccessListener {
                                    actualizarUltimoMensaje(
                                        idChat = idChat,
                                        texto = texto,
                                        ahora = ahora,
                                        usuarioActual = usuarioActual
                                    )
                                }
                        }
                    }
                }
        }
    }

    private fun actualizarUltimoMensaje(
        idChat: String,
        texto: String,
        ahora: Long,
        usuarioActual: UsuariosGen
    ) {
        val idActual = auth.currentUser?.uid

        if (idActual != null) {

            database.getReference("chatsPorUsuario/")
                .child(idActual)
                .child(idChat)
                .get()
                .addOnSuccessListener { snapshotChat ->

                    val chatActual = snapshotChat.getValue(ChatDB::class.java)

                    if (chatActual != null) {

                        val idOtroUsuario = chatActual.idOtroUsuario

                        val chatParaActual = chatActual.copy(
                            ultimoMensaje = texto,
                            ultimoTimestamp = ahora
                        )

                        val chatParaOtro = ChatDB(
                            idChat = idChat,
                            idOtroUsuario = usuarioActual.id,
                            nombreOtroUsuario = usuarioActual.nombre,
                            imagenOtroUsuario = usuarioActual.imagen,
                            ultimoMensaje = texto,
                            ultimoTimestamp = ahora
                        )

                        database.getReference("chatsPorUsuario/")
                            .child(idActual)
                            .child(idChat)
                            .setValue(chatParaActual)

                        database.getReference("chatsPorUsuario/")
                            .child(idOtroUsuario)
                            .child(idChat)
                            .setValue(chatParaOtro)
                    }
                }
        }
    }
}
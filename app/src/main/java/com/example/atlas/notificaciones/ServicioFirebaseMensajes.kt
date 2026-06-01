package com.example.atlas.notificaciones

import android.util.Log
import com.example.atlas.auth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ServicioFirebaseMensajes : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM_ATLAS", "Nuevo token FCM: $token")

        val usuarioActual = auth.currentUser

        if (usuarioActual != null) {
            TokenFCM.guardarTokenEnBaseDeDatos(usuarioActual.uid, token)
        } else {
            Log.d("FCM_ATLAS", "Token generado, pero todavía no hay usuario autenticado")
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FCM_ATLAS", "Mensaje recibido desde FCM")
        Log.d("FCM_ATLAS", "Data: ${message.data}")

        val tipo = message.data["tipo"] ?: "chat"

        if (tipo == "trote") {
            val idDeportista = message.data["idDeportista"] ?: return
            val nombreDeportista = message.data["nombreDeportista"] ?: "Un deportista"
            val texto = message.data["texto"]
                ?: "$nombreDeportista está trotando, ¿quieres ver dónde está?"

            AdministradorNotificaciones.mostrarNotificacionTrote(
                context = this,
                idDeportista = idDeportista,
                nombreDeportista = nombreDeportista,
                texto = texto
            )

            return
        }

        val idChat = message.data["idChat"] ?: return
        val nombre = message.data["nombre"] ?: "Nuevo mensaje"
        val texto = message.data["texto"] ?: "Te enviaron un mensaje"

        AdministradorNotificaciones.mostrarNotificacionMensaje(
            context = this,
            idChat = idChat,
            nombre = nombre,
            texto = texto
        )
    }
}
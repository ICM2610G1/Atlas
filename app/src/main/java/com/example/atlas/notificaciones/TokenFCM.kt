package com.example.atlas.notificaciones

import android.util.Log
import com.example.atlas.auth
import com.example.atlas.database
import com.google.firebase.messaging.FirebaseMessaging

object TokenFCM {

    fun guardarTokenActual() {
        val usuarioActual = auth.currentUser

        if (usuarioActual == null) {
            Log.d("FCM_ATLAS", "No hay usuario autenticado, no se guarda token FCM")
            return
        }

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                guardarTokenEnBaseDeDatos(usuarioActual.uid, token)
            }
            .addOnFailureListener { error ->
                Log.e("FCM_ATLAS", "Error obteniendo token FCM", error)
            }
    }

    fun guardarTokenEnBaseDeDatos(idUsuario: String, token: String) {
        if (idUsuario.isBlank() || token.isBlank()) {
            Log.d("FCM_ATLAS", "ID de usuario o token vacío")
            return
        }

        val refTokens = database.getReference("tokensFCM")

        refTokens.get()
            .addOnSuccessListener { snapshot ->

                for (usuarioSnapshot in snapshot.children) {
                    val idUsuarioEnBase = usuarioSnapshot.key ?: ""

                    if (idUsuarioEnBase != idUsuario) {
                        refTokens
                            .child(idUsuarioEnBase)
                            .child(token)
                            .removeValue()
                    }
                }

                refTokens
                    .child(idUsuario)
                    .child(token)
                    .setValue(true)
                    .addOnSuccessListener {
                        Log.d("FCM_ATLAS", "Token FCM guardado correctamente para $idUsuario")
                    }
                    .addOnFailureListener { error ->
                        Log.e("FCM_ATLAS", "Error guardando token FCM", error)
                    }
            }
            .addOnFailureListener { error ->
                Log.e("FCM_ATLAS", "Error revisando tokens FCM", error)
            }
    }

    fun eliminarTokenActualAntesDeCerrarSesion(onCompletado: () -> Unit) {
        val usuarioActual = auth.currentUser

        if (usuarioActual == null) {
            onCompletado()
            return
        }

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                database
                    .getReference("tokensFCM")
                    .child(usuarioActual.uid)
                    .child(token)
                    .removeValue()
                    .addOnCompleteListener {
                        Log.d("FCM_ATLAS", "Token eliminado al cerrar sesión")
                        onCompletado()
                    }
            }
            .addOnFailureListener {
                onCompletado()
            }
    }
}
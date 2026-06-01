package com.example.atlas.notificaciones

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.atlas.MainActivity
import com.example.atlas.R

object AdministradorNotificaciones {

    const val NOTIFICATION_CHANNEL_ID = "notificacion_fcm"

    fun crearCanalNotificaciones(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "FCM Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = "Channel for FCM Notifications"

            val notManager = context.getSystemService<NotificationManager>()
            notManager?.createNotificationChannel(channel)
        }
    }

    fun mostrarNotificacionMensaje(
        context: Context,
        idChat: String,
        nombre: String,
        texto: String
    ) {
        val intent = Intent(context, MainActivity::class.java)

        intent.putExtra("idChat", idChat)
        intent.putExtra("nombre", nombre)

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(
            context,
            NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle(nombre)
            .setContentText(texto)
            .setSmallIcon(R.drawable.mensajero)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notManager = context.getSystemService<NotificationManager>()
        notManager?.notify(1, notification)
    }
}
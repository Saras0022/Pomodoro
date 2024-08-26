package com.example.pomodoro

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import kotlin.random.Random

const val CHANNEL_ID = "Pomodoro Notification"

class NotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(contentTitle: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(contentTitle)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }

}
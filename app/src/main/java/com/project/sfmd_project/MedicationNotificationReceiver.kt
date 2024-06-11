package com.project.sfmd_project

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.*

class MedicationNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MedicationNotification", "Notification receiver triggered.")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if the Android version is Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "MedicationChannel"
            val channel = NotificationChannel(
                channelId,
                "Medication Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Create notification content
        val notificationIntent = Intent(context, MedicationTracker::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder = NotificationCompat.Builder(context, "MedicationChannel")
            .setSmallIcon(R.drawable.doctor)
            .setContentTitle("Medication Reminder")
            .setContentText("It's time to take your medication!")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)



        // Generate unique notification ID
        val notificationId = Random().nextInt()

        // Show notification
        notificationManager.notify(notificationId, notificationBuilder.build())
        Log.d("MedicationNotification", "Notification sent.")

    }
}

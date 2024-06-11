package com.project.sfmd_project

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService

const val channelId = "notification_channel"
const val channelName = "com.project.sfmd_project"

class MyFirebaseMessagingService : FirebaseMessagingService() {

//    override fun onMessageReceived(remotemessage: RemoteMessage) {
//        if (remotemessage.notification != null) {
//            generateNotification(remotemessage.notification!!.title!!, remotemessage.notification!!.body!!)
//
//            generateNotification("mentor_me", "you added mentor succesfully")
//
//
//        }
//    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(context: Context, title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.project.sfmd_project", R.layout.notification)
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.logo, R.drawable.doctor)
        return remoteView
    }

    fun generateNotification(context: Context, title: String, message: String) {
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.doctor)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContent(getRemoteView(context, title, message))

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }

}
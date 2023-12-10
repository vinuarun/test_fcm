package com.example.vinusnotifyingapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val tokendata = mutableMapOf<String, String>()
        tokendata["token"] = token
        val firebasefirestore= FirebaseFirestore.getInstance()
        firebasefirestore.collection("DeviceTokens").document().set(tokendata)
    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
//MESSAGE HANDLING
        message.notification?.let {
            val title = it.title
            val body = it.body
            val CHANNEL_ID = (R.string.default_notification_channel_id).toString()
            val soundUri =
                Uri.parse("android.resource://com.example.vinusnotifyingapplication" + "/" + R.raw.notification_tone)
//BROADCASTING DATA TO ACTIVITY
            val intent = Intent("transfer-message")
            intent.putExtra("title", title)
            intent.putExtra("body", body)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//CREATING NOTIFICATION CHANNEL
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel.
                val name = getString(R.string.channel_name)
                val descriptionText = getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    setSound(soundUri,
                        AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build()
                    )
                }
                mChannel.description = descriptionText
                mChannel.lightColor = Color.BLUE
                mChannel.enableLights(true)
                // Register the channel with the system. You can't change the importance
                // or other notification behaviors after this.
                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)
            }
//BUILDING NOTIFICATION
            val notification = NotificationCompat.Builder(this, CHANNEL_ID.toString())
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(soundUri)
                .build()


            val notifyManger = NotificationManagerCompat.from(this)

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Post the notification
                notifyManger.notify(1, notification)
            } else {
                Toast.makeText(this, "Custom permission not granted", Toast.LENGTH_SHORT).show()

            }


        }
    }
}
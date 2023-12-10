package com.example.vinusnotifyingapplication

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.vinusnotifyingapplication.R.id.messageTitle

class MainActivity : AppCompatActivity() {
    var Title="Title"
    var Body="Body"
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Handle the received message here
            Title = intent?.getStringExtra("title").toString()
            Body = intent?.getStringExtra("body").toString()
            Toast.makeText(context,Title, Toast.LENGTH_SHORT).show()
            showMessage(Title,Body)

        }
    }
    private fun showMessage(title: String,body:String) {
        // Display the notification message in your activity
        val textViewtitle = findViewById<TextView>(R.id.messageTitle)
        val textViewbody = findViewById<TextView>(R.id.messagebody)
        textViewtitle.text = title
        textViewbody.text=body
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//REQUESTING PERMISSION FOR PUSH NOTIFICATION
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the custom permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                22
            )
        }
//MESSAGE TRANSFER VIA BROADCAST RECEIVER
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter("transfer-message"))

        /*val MessageTitle = findViewById<TextView>(R.id.messageTitle)
        val MessageBody = findViewById<TextView>(R.id.messagebody)

        MessageTitle.text = Title
        MessageBody.text = Body*/
    }



}
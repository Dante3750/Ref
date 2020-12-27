package com.globals.netconnect.netconnect.activity


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.Toolbar
import android.view.View

import android.widget.Toast
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.app.Config
import com.google.firebase.messaging.FirebaseMessaging




class MainActivity : AppCompatActivity() {
    private var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    private var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.statusBarColor = resources.getColor(R.color.statusbar)


        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                // checking for type intent filter
                if (intent.action == Config.REGISTRATION_COMPLETE) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)


                } else if (intent.action == Config.PUSH_NOTIFICATION) {
                    // new push notification is received

                    val message = intent.getStringExtra("message")

                    Toast.makeText(applicationContext, "Push notification: $message", Toast.LENGTH_LONG).show()
                }
            }
        }

}




override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
}

    override fun onResume() {
        super.onResume()

        // register GCM registration complete receiver
        mRegistrationBroadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                it,
                IntentFilter(Config.REGISTRATION_COMPLETE))
        }

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        mRegistrationBroadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                it,
                IntentFilter(Config.PUSH_NOTIFICATION))
        }

        // clear the notification area when the app is  opened
        //NotificationUtils.clearNotifications(applicationContext)
    }

    override fun onPause() {
        mRegistrationBroadcastReceiver?.let { LocalBroadcastManager.getInstance(this).unregisterReceiver(it) }
        super.onPause()
    }

    companion object {
        private val TAG = MainActivity::class.java!!.simpleName
    }








}

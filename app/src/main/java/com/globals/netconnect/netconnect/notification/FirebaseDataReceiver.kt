package com.globals.netconnect.netconnect.notification

import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log

@Suppress("DEPRECATION")
class FirebaseDataReceiver : WakefulBroadcastReceiver() {

    private val TAG = "FirebaseDataReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "I'm in!!!")
        val dataBundle = intent.getBundleExtra("data")
        Log.d(TAG, "onReceive: $dataBundle")
        Log.d(TAG, dataBundle.toString())
    }
}
package com.globals.netconnect.netconnect.notification

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.globals.netconnect.netconnect.app.AppController
import com.globals.netconnect.netconnect.app.Config
import com.globals.netconnect.netconnect.app.SharedPrefManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

@Suppress("DEPRECATION")
class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    private lateinit var pref: SharedPrefManager

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token

//        storeRegIdInPref(refreshedToken)

        sendRegistrationToServer(refreshedToken)

        val registrationComplete = Intent(Config.REGISTRATION_COMPLETE)
        registrationComplete.putExtra("token", refreshedToken)
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete)
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.e(TAG, "sendRegistrationToServer: " + token!!)

        AppController.getInstance().pref.saveToken(token)
    }



    companion object {
        private val TAG = MyFirebaseInstanceIDService::class.java.simpleName
    }

}
package com.globals.netconnect.netconnect.notification

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.globals.netconnect.netconnect.activity.MainActivity
import com.globals.netconnect.netconnect.app.Config
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.globals.netconnect.netconnect.R
import android.annotation.SuppressLint
import android.app.*
import android.content.ContentResolver
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.content.ContextCompat


class MyFirebaseMessagingService : FirebaseMessagingService() {

    val CHANNEL_ID = "com.globals.netconnect.netcrf"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage == null)
            return
        remoteMessage?.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
//            remoteMessage.notification!!.body?.let { it1 -> handleNotification(it1) }
        }

        if (remoteMessage.data.isNotEmpty()) {

            try {
                val json = JSONObject(remoteMessage.data)
                Log.e(TAG, "data: " + remoteMessage.data)
                handleDataMessage(json)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }

        }


    }


//    private fun handleNotification(message: String) {
//        if (!NotificationUtils.isAppIsInBackground(applicationContext)) {
//            // app is in foreground, broadcast the push message
//            val pushNotification = Intent(Config.PUSH_NOTIFICATION)
//            pushNotification.putExtra("message", message)
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
//
//            // play notification sound
//            val notificationUtils = NotificationUtils(applicationContext)
//            notificationUtils.playNotificationSound()
//
//           //Toast.makeText(this, "" +message, Toast.LENGTH_SHORT).show()
//        } else {
//
//        }
//    }

    private fun handleDataMessage(json: JSONObject) {
        try {
            val title = json.getString("title")
            val body = json.getString("body")
            val timestamp = json.getString("timestamp")


            if (!NotificationUtils.isAppIsInBackground(applicationContext)) {
                // app is in foreground, broadcast the push message
                val pushNotification = Intent(Config.PUSH_NOTIFICATION)
                pushNotification.putExtra("message", title)
                //pushNotification.putExtra("message", body)
                //pushNotification.putExtra("message", timestamp)
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

                // play notification sound
                val notificationUtils = NotificationUtils(applicationContext)
                notificationUtils.playNotificationSound()


                //showNotificationMessageAppIsBackGround(title,body,timestamp)



            } else {
                // app is in background, show the notification in notification tray
                val resultIntent = Intent(applicationContext, MainActivity::class.java)
                resultIntent.putExtra("message", body)

                showNotificationMessage(applicationContext, title, body, timestamp, resultIntent)

            }
        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }


    private fun showNotificationMessage(
        context: Context,
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent
    ) {



        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.d("tag", count.toString())

//
//            if (count==1){
//                Log.d("tag", count.toString())
//
//            }
//            else{
//
//
//            }

            @SuppressLint("WrongConstant") val notificationChannel =
                NotificationChannel(CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX)
            notificationChannel.description = "Sample Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            count++
        }

        val intent = Intent(this, com.globals.netconnect.netconnect.activity.Notification::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/raw/notification")

        var mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        mBuilder.setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_icon)
            .setTicker("NetConnect")
            .setContentTitle(title)
            .setContentText(message)
            .setSound(alarmSound)
//            .setNumber(messageCount)
            .setContentIntent(pendingIntent)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message)).color = ContextCompat.getColor(context, R.color.orange)

        NOTIFICATION_ID = NotificationUtils.getTimeMilliSec(timeStamp).toInt()
        notificationManager.notify(NOTIFICATION_ID.toString(),Config.NOTIFICATION_ID, mBuilder.build())
        playNotificationSound()


        }

    private fun playNotificationSound() {
        try {
            val notSound =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

//            val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                    + "://" + packageName + "/raw/notification")
            val r = RingtoneManager.getRingtone(this, notSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.simpleName
        private var NOTIFICATION_ID = 1
        var count:Int=1

    }





}

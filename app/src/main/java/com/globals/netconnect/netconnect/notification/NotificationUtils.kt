package com.globals.netconnect.netconnect.notification

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat

import android.text.Html
import android.text.TextUtils
import android.util.Patterns
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.app.Config


import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created by Rushi on 12/1/2016.
 */

class NotificationUtils(private val context: Context) {

    @JvmOverloads
    fun showNotificationMessage(
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent,
        imageUrl: String? = null
    ) {
        if (TextUtils.isEmpty(message))
            return

        val icon = R.mipmap.ic_launcher

        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(context)

        val alarmSound =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/raw/notification")

        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageUrl != null && imageUrl.length > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                val bitmap = getBitmapFromURL(imageUrl)

                if (bitmap != null) {
                    showBigNotification(
                        bitmap,
                        builder,
                        icon,
                        title,
                        message,
                        timeStamp,
                        resultPendingIntent,
                        alarmSound
                    )
                } else {
                    showSmallNotification(builder, icon, title, message, timeStamp, resultPendingIntent, alarmSound)
                }
            }
        } else {
            showSmallNotification(builder, icon, title, message, timeStamp, resultPendingIntent, alarmSound)
            playNotificationSound()
            playNotificationSound()
        }
    }

    private fun showSmallNotification(
        builder: NotificationCompat.Builder,
        icon: Int,
        title: String,
        message: String,
        timeStamp: String,
        resultPendingIntent: PendingIntent,
        alarmSound: Uri
    ) {
        val inboxStyle = NotificationCompat.InboxStyle()

        inboxStyle.addLine(message)
        val notification: Notification
        notification = builder.setSmallIcon(icon).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentIntent(resultPendingIntent)
            .setSound(alarmSound)
            .setStyle(inboxStyle)
            .setWhen(getTimeMilliSec(timeStamp))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, icon))
            .setContentText(message)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Config.NOTIFICATION_ID, notification)
    }

    private fun showBigNotification(
        bitmap: Bitmap,
        mBuilder: NotificationCompat.Builder,
        icon: Int,
        title: String,
        message: String,
        timeStamp: String,
        resultPendingIntent: PendingIntent,
        alarmSound: Uri
    ) {
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
        bigPictureStyle.setBigContentTitle(title)
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
        bigPictureStyle.bigPicture(bitmap)
        val notification: Notification
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentIntent(resultPendingIntent)
            .setSound(alarmSound)
            .setStyle(bigPictureStyle)
            .setWhen(getTimeMilliSec(timeStamp))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, icon))
            .setContentText(message)
            .build()


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification)
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    fun getBitmapFromURL(strURL: String): Bitmap? {
        try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    // Playing notification sound
    fun playNotificationSound() {
        try {
            val alarmSound = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + context.packageName + "/raw/notification"
            )
            val r = RingtoneManager.getRingtone(context, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {

        private val TAG = NotificationUtils::class.java.simpleName

        /**
         * Method checks if the app is in background or not
         */
        fun isAppIsInBackground(context: Context): Boolean {
            var isInBackground = true
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                val runningProcesses = am.runningAppProcesses
                for (processInfo in runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (activeProcess in processInfo.pkgList) {
                            if (activeProcess == context.packageName) {
                                isInBackground = false
                            }
                        }
                    }
                }
            } else {
                val taskInfo = am.getRunningTasks(1)
                val componentInfo = taskInfo[0].topActivity
                if (componentInfo.packageName == context.packageName) {
                    isInBackground = false
                }
            }

            return isInBackground
        }

        // Clears notification tray messages
        fun clearNotifications(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }

        fun getTimeMilliSec(timeStamp: String): Long {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            try {
                val date = format.parse(timeStamp)
                return date.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return 0
        }
    }
}
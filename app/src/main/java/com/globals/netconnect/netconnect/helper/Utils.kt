package com.globals.netconnect.netconnect.helper


import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat

object Utils {

    fun darkenStatusBar(activity: Activity, color: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            activity.window.statusBarColor = darkenColor(
                ContextCompat.getColor(activity, color)
            )
        }

    }


    // Code to darken the color supplied (mostly color of toolbar)
    private fun darkenColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= 0.8f
        return Color.HSVToColor(hsv)
    }


}
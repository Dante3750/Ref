package com.globals.netconnect.netconnect.app

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

object Cons {
    const val BASE_URL = "http://10.182.5.50:8087/Referral360"
    const val URL_GET_JOB_LIST = "$BASE_URL/getJobList"
    const val URL_GET_REFERRAL_LIST = "$BASE_URL/getReferralData"
    const val URL_GET_REWARD_LIST = "$BASE_URL/getRewardsData"
    const val URL_GET_JOB_REQUISITION_LIST = "$BASE_URL/getJobDescription"
    const val URL_SUBMIT_RESUME = "$BASE_URL/referee"
    const val URL_FAQ = "$BASE_URL/getFAQ"
    const val URL_SUPPORT = "$BASE_URL/supportPage"
    const val URL_NEWS = "$BASE_URL/getLatestNews"
    const val NETWORK_ERROR = "Please connect to Internet!"
    const val LOGGED_IN_PREF = "logged_in_status"
    const val URL_NOTIFICATION ="$BASE_URL/getNotificationHistory"

    const val TOKEN = "token"

    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }


    class RecyclerTouchListener(context: Context, rv: RecyclerView, private val clickListener: ClickListener?) : RecyclerView.OnItemTouchListener {
        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = rv.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, rv.getChildPosition(child))
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {

                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }

    }


}
package com.globals.netconnect.netconnect.activity

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.adaptor.NotificationAdaptor
import com.globals.netconnect.netconnect.app.Cons
import com.globals.netconnect.netconnect.app.SharedPrefManager
import com.globals.netconnect.netconnect.model.NotificationModel
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class Notification : AppCompatActivity() {
    private lateinit var pref: SharedPrefManager

    private var toolbar: Toolbar? = null
    private var rvNotficationStack:RecyclerView?=null

    private var adaptor:NotificationAdaptor?=null

    private val rvList = java.util.ArrayList<NotificationModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        window.statusBarColor = resources.getColor(R.color.statusbar)
        toolbar = findViewById<View>(R.id.toolbar_notification) as Toolbar
        setSupportActionBar(findViewById(R.id.toolbar_notification))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //initializationLayout

        pref = SharedPrefManager.getInstance(this)

        val regular = Typeface.createFromAsset(this.assets, "fonts/lato_regular.ttf")
        val latoSemiBold = Typeface.createFromAsset(this.assets, "fonts/lato_semibold.ttf")
        val latoMedium = Typeface.createFromAsset(this.assets, "fonts/lato_medium.ttf")

        rvNotficationStack = findViewById(R.id.rv_notification)

        adaptor = NotificationAdaptor(regular,latoSemiBold,rvList)
        rvNotficationStack!!.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        rvNotficationStack!!.layoutManager = mLayoutManager
        rvNotficationStack!!.itemAnimator = DefaultItemAnimator()
        rvNotficationStack!!.adapter = adaptor


        getDataFrmServer()


    }

    private fun getDataFrmServer() {

        val rewardsDataRequest = Volley.newRequestQueue(this)
        val rewardDataObject: StringRequest = object :

            StringRequest(Request.Method.POST, Cons.URL_NOTIFICATION, com.android.volley.Response.Listener { response ->
                try {
                    val jsonObjectt = JSONObject(response)

                    val status = jsonObjectt.getString("status")
                    if (status == "0") {
                    } else {

                        val notificationList =jsonObjectt.getJSONArray("notificationHistory")
                        Log.d("Tag", notificationList.toString())

                        for (i in 0 until notificationList.length()) {

                            val notificationModel = NotificationModel()
                            val data = notificationList.getJSONObject(i)
                            notificationModel.body= data.getString("body")
                            notificationModel.titleNot= data.getString("title")
                            notificationModel.timeStamp= data.getString("timeStamp")
                            rvList.add(notificationModel)
                        }
                        adaptor!!.notifyDataSetChanged()

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, com.android.volley.Response.ErrorListener { error -> Log.d("Tag", "Error : $error") }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["EmployeeId"] = pref.user.employeeId
                return params
            }
        }
        rewardsDataRequest.add(rewardDataObject)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

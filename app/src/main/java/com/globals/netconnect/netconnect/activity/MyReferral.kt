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
import com.globals.netconnect.netconnect.adaptor.MyReferralAdaptor
import com.globals.netconnect.netconnect.app.Cons
import com.globals.netconnect.netconnect.app.SharedPrefManager
import com.globals.netconnect.netconnect.model.ReferralData
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MyReferral : AppCompatActivity() {
    private lateinit var pref: SharedPrefManager
    private var status: Int = 0
    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var mAdapter: MyReferralAdaptor? = null
    private val rvList = java.util.ArrayList<ReferralData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_referral)
        window.statusBarColor = resources.getColor(R.color.statusbar)
        toolbar = findViewById<View>(R.id.toolbar_update) as Toolbar
        setSupportActionBar(findViewById(R.id.toolbar_update))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView= this.findViewById(R.id.rv_referral)
        getDataFrmServer()
        pref = SharedPrefManager.getInstance(this)
        val regular = Typeface.createFromAsset(this.assets, "fonts/lato_regular.ttf")
        val latoSemiBold = Typeface.createFromAsset(this.assets, "fonts/lato_semibold.ttf")
        val latoMedium = Typeface.createFromAsset(this.assets, "fonts/lato_medium.ttf")

        mAdapter = MyReferralAdaptor(regular,latoSemiBold,rvList)
        recyclerView!!.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getDataFrmServer() {
        val QueueRequest = Volley.newRequestQueue(this)
        val Object: StringRequest = object :
            StringRequest(Request.Method.POST, Cons.URL_GET_REFERRAL_LIST, com.android.volley.Response.Listener { response ->
                try {
                    val jsonObjectt = JSONObject(response)
                    val message = jsonObjectt.getString("message")
                    val status = jsonObjectt.getString("status")
                    if (status == "0") {
                    } else {
                        val referralData = jsonObjectt.getJSONArray("referralData")
//                        Log.d("referralData", "destination:$referralData")

                        for (i in 0 until referralData.length()) {
                            val referralDataModel = ReferralData()
                            val data = referralData.getJSONObject(i)
                            referralDataModel.date= data.getString("date")
                            referralDataModel.refereeStatus = data.getString("refereeStatus")
                            referralDataModel.role = data.getString("role")
                            referralDataModel.name = data.getString("name")
                            referralDataModel.emailId = data.getString("emailId")
                            referralDataModel.textcolor = data.getString("textColour")
                            rvList.add(referralDataModel)
                       }

                        mAdapter!!.notifyDataSetChanged()

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, com.android.volley.Response.ErrorListener { error -> Log.d("Tag", "Error : " + error.toString()) }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["employeeId"] = pref.user.employeeId
                Log.d("params", "destination:$params")
                return params
            }
        }
        QueueRequest.add(Object)
    }
}

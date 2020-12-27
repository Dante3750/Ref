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
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.adaptor.MyRewardsAdaptor
import com.globals.netconnect.netconnect.app.Cons
import com.globals.netconnect.netconnect.app.SharedPrefManager
import com.globals.netconnect.netconnect.model.RewardsData
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MyRewards : AppCompatActivity() {
    private lateinit var pref: SharedPrefManager
    private var status: Int = 0
    private var tvTotalRewards: TextView? = null
    private var tvPendingRewards: TextView? = null
    private var toolbar: Toolbar? = null
    private var recyclerView: RecyclerView? = null
    private var mAdapter: MyRewardsAdaptor? = null
    private val rvList = java.util.ArrayList<RewardsData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_rewards)

        window.statusBarColor = resources.getColor(R.color.statusbar)
        toolbar = findViewById<View>(R.id.toolbar_update) as Toolbar
        setSupportActionBar(findViewById(R.id.toolbar_update))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView= this.findViewById(R.id.rv_reward_list)
        tvTotalRewards=findViewById(R.id.tv_ti_rewards)
        tvPendingRewards=findViewById(R.id.tv_pi_rewards)
        getDataFrmServer()
        pref = SharedPrefManager.getInstance(this)

        val regular = Typeface.createFromAsset(this.assets, "fonts/lato_regular.ttf")
        val latoSemiBold = Typeface.createFromAsset(this.assets, "fonts/lato_semibold.ttf")
        val latoMedium = Typeface.createFromAsset(this.assets, "fonts/lato_medium.ttf")

        mAdapter = MyRewardsAdaptor(this,regular,latoSemiBold,rvList)
        recyclerView!!.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

    }

    private fun getDataFrmServer() {
        val rewardsDataRequest = Volley.newRequestQueue(this)
        val rewardDataObject: StringRequest = object :
            StringRequest(Request.Method.POST, Cons.URL_GET_REWARD_LIST, com.android.volley.Response.Listener { response ->
                try {
                    val jsonObjectt = JSONObject(response)
                   // val message = jsonObjectt.getString("message")
                    val status = jsonObjectt.getString("status")
                    if (status == "0") {
                    } else {

                        val dataObject = jsonObjectt.getJSONObject("data")
                        val pendingRewards = dataObject.getString("pendingRewards")
                        val totalRewards = dataObject.getString("totalRewards")
                        tvTotalRewards!!.text = totalRewards
                        tvPendingRewards!!.text = pendingRewards

                        val rewardsList =dataObject.getJSONArray("rewardsList")
                        if (rewardsList.length()==0){


                        }
                        else{
                            for (i in 0 until rewardsList.length()) {
                            val rewardData = RewardsData()
                            val data = rewardsList.getJSONObject(i)
                            rewardData.reward= data.getString("reward")
                            rewardData.redemptionDate = data.getString("redemptionDate")
                            rewardData.diffDays = data.getInt("diffDays")
                            rewardData.textColour = data.getString("textColour")
                            rewardData.ProgressBarTrackColor = data.getString("ProgressBarTrackColor")
                            rewardData.rating = data.getString("rating")
                            rewardData.ProgressBarTintColor = data.getString("ProgressBarTintColor")
                            rewardData.refereeName = data.getString("refereeName")
                            rewardData.backgroundColour = data.getString("backgroundColour")
                            rvList.add(rewardData)
                        }
                        }



                        mAdapter!!.notifyDataSetChanged()

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, com.android.volley.Response.ErrorListener { error -> Log.d("Tag", "Error : $error") }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["employeeId"] = pref.user.employeeId
                Log.d("params", "destination:$params")
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

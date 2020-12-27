package com.globals.netconnect.netconnect.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.app.SharedPrefManager
import android.text.Html
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.globals.netconnect.netconnect.adaptor.JobOtherRecyclerView
import com.globals.netconnect.netconnect.adaptor.JobRecyclerView
import com.globals.netconnect.netconnect.app.AppController
import com.globals.netconnect.netconnect.app.Cons
import com.globals.netconnect.netconnect.model.Job
import com.globals.netconnect.netconnect.model.OtherJob
import com.globals.netconnect.netconnect.notification.MyFirebaseMessagingService
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


/**
 * Created by Himanshu on 09,April,2019
 */


@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {


    //initialization of Data.......

    private val mList = ArrayList<Job>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: JobRecyclerView? = null
    private val mListOtherJob = ArrayList<OtherJob>()
    private var recyclerViewOther: RecyclerView? = null
    private var mAdapterOtherJob: JobOtherRecyclerView? = null
    private lateinit var ivClose: ImageView
    private lateinit var tcvName: TextView
    private lateinit var flName: TextView
    private lateinit var tvUpdate: TextView
    private lateinit var tvMyReferral: TextView
    private lateinit var tvMyRewards: TextView
    private lateinit var tvSupport: TextView
    private lateinit var tvLatestNews: TextView
    private lateinit var tvNotification: TextView
    private lateinit var tvLogout: TextView
    private lateinit var pref: SharedPrefManager
    private lateinit var tvWlcText: TextView
    private lateinit var tvImage: TextView
    private lateinit var progressDialog: ProgressDialog


    //OnCreateMethod.......
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        //initialization of Layout.......
        initializationLayout()
        //API Part
        getDataFrmServer()
        //Setting Data And listeners
        setDataAndListners()
    }

    private fun initializationLayout() {
        recyclerView = findViewById<View>(R.id.rv_related_job_list) as RecyclerView
        recyclerViewOther = findViewById<View>(R.id.rv_other_job_list) as RecyclerView
        val regular = Typeface.createFromAsset(this.assets, "fonts/lato_regular.ttf")
        val latoSemiBold = Typeface.createFromAsset(this.assets, "fonts/lato_semibold.ttf")
        val latoMedium = Typeface.createFromAsset(this.assets, "fonts/lato_medium.ttf")
        val drawerToggle = ActionBarDrawerToggle(
            this, drawer_layoutt, toolbarr, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerToggle.isDrawerIndicatorEnabled = false
        drawerToggle.setHomeAsUpIndicator(R.drawable.hamburgur_menu_icon_home)
        drawer_layoutt.addDrawerListener(drawerToggle)
        drawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            drawer_layoutt.openDrawer(GravityCompat.START) }
        drawerToggle.syncState()
        toolbarr.title = "Open Job Positions"
        ivClose = findViewById(R.id.ivClose)
        tvWlcText = findViewById(R.id.tv_wlc_text)
        tvImage = findViewById(R.id.tcv_name)
        window.statusBarColor = resources.getColor(R.color.statusbar)
        mAdapter = JobRecyclerView(regular, latoSemiBold, mList)
        recyclerView!!.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter
        mAdapterOtherJob = JobOtherRecyclerView(regular, latoSemiBold, mListOtherJob)
        recyclerViewOther!!.setHasFixedSize(true)
        val mLayoutManagerr = LinearLayoutManager(applicationContext)
        recyclerViewOther!!.layoutManager = mLayoutManagerr
        recyclerViewOther!!.itemAnimator = DefaultItemAnimator()
        recyclerViewOther!!.adapter = mAdapterOtherJob
        tcvName = findViewById(R.id.tcv_name)
        flName = findViewById(R.id.tv_fl_name)
        pref = SharedPrefManager.getInstance(this)
        tvUpdate = findViewById(R.id.tv_update)
        tvMyReferral = findViewById(R.id.tv_my_referral)
        tvMyRewards = findViewById(R.id.tv_rewards)
        tvSupport = findViewById(R.id.tv_support)
        tvLatestNews = findViewById(R.id.tv_latest_news)
        tvNotification = findViewById(R.id.tv_notification)
        tvLogout = findViewById(R.id.tv_logout)
        tcvName.typeface = latoSemiBold
        flName.typeface = latoMedium
        tvUpdate.typeface = latoMedium
        tvMyReferral.typeface = latoMedium
        tvMyRewards.typeface = latoMedium
        tvSupport.typeface = latoMedium
        tvNotification.typeface = latoMedium
        tvLatestNews.typeface = latoMedium
        tvWlcText.typeface = latoMedium



    }

    private fun setDataAndListners() {
        val first = "Hey " + pref.user.firstName + ", "
        val next = "<font color='#797979'>Your Referral Is Best Suited For</font>"
        tvWlcText.text = Html.fromHtml(first + next)
        flName.text = pref.user.firstName + " " + pref.user.lastName
        val parts = pref.user.firstName
        val lastParts = pref.user.lastName
        val firstLetter: Char = parts[0]
        val lastLetter: Char = lastParts[0]
        tvImage.text = firstLetter.toString() + lastLetter.toString()
        ivClose.setOnClickListener {
            drawer_layoutt.closeDrawer(GravityCompat.START)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        recyclerView!!.addOnItemTouchListener(
            Cons.RecyclerTouchListener(
                this,
                recyclerView!!,
                object : Cons.ClickListener {
                    override fun onClick(view: View, position: Int) {
                        val job = mList[position]
                        val intent = Intent(this@HomeActivity, JobDescriptionActivity::class.java)
                        intent.putExtra("jobRequisitionNo", job.requisition)
                        startActivity(intent)
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
                    }
                    override fun onLongClick(view: View?, position: Int) {
                    }
                })
        )
        recyclerViewOther!!.addOnItemTouchListener(
            Cons.RecyclerTouchListener(
                this,
                recyclerViewOther!!,
                object : Cons.ClickListener {
                    override fun onClick(view: View, position: Int) {
                        val job = mListOtherJob[position]
                        val intent = Intent(this@HomeActivity, JobDescriptionActivity::class.java)
                        intent.putExtra("jobRequisitionNo", job.requisition)
                        startActivity(intent)
                        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
                    }
                    override fun onLongClick(view: View?, position: Int) {
                    }
                }))
    }


    override fun onBackPressed() {
        if (drawer_layoutt.isDrawerOpen(GravityCompat.START)) {
            drawer_layoutt.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        tvUpdate.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }

        tvMyReferral.setOnClickListener {
            val intent = Intent(this, MyReferral::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }

        tvMyRewards.setOnClickListener {
            val intent = Intent(this, MyRewards::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
        tvSupport.setOnClickListener {
            val intent = Intent(this, Support::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
        tvLatestNews.setOnClickListener {
            val intent = Intent(this, LatestNews::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }

        tvLogout.setOnClickListener {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            pref.setLoggedIn(applicationContext, false)
            finish()
        }

        tvNotification.setOnClickListener {
            val intent = Intent(this, Notification::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }



        var countt = MyFirebaseMessagingService.count
        Log.d("aaaaa", countt.toString())
//        if (countt!=0){
//
//            tvNotification.error
//        }
//        else{
//
//        }
    }

    private fun getDataFrmServer() {
        progressDialog = ProgressDialog(
            this@HomeActivity,
            R.style.AppTheme_Dark_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Login...")
        progressDialog.show()

        if (Cons.isNetworkAvailable(this)) {
            val jobRequest: StringRequest = object :StringRequest(
                    Request.Method.POST,
                    Cons.URL_GET_JOB_LIST,
                    Response.Listener { response ->
                        try {
                            val jobObject = JSONObject(response)
                            val status = jobObject.getString("status")
                            if (status == "0") {
                                progressDialog.dismiss()
                            } else {
                                progressDialog.dismiss()
                                val relatedJobs = jobObject.getJSONArray("relatedJobs")
                                for (i in 0 until relatedJobs.length()) {
                                    val modelJob = Job()
                                    val relatedJobObject = relatedJobs.getJSONObject(i)
                                    modelJob.jobId = relatedJobObject.getString("jobId")
                                    modelJob.rewardsAmount = relatedJobObject.getString("rewardsAmount")
                                    modelJob.role = relatedJobObject.getString("role")
                                    modelJob.requisition = relatedJobObject.getString("requisition")
                                    modelJob.location = relatedJobObject.getString("location")
                                    mList.add(modelJob)
                                    Log.d("Tag", "destination:" + modelJob.location)
                                }
                                val resultOtherJobs = jobObject.getJSONArray("otherJobs")
                                for (i in 0 until resultOtherJobs.length()) {
                                    val modelOtherJob = OtherJob()
                                    val data = resultOtherJobs.getJSONObject(i)
                                    modelOtherJob.jobId = data.getString("jobId")
                                    modelOtherJob.rewardsAmount = data.getString("rewardsAmount")
                                    modelOtherJob.role = data.getString("role")
                                    modelOtherJob.requisition = data.getString("requisition")
                                    modelOtherJob.location = data.getString("location")
                                    mListOtherJob.add(modelOtherJob)
                                }
                                mAdapter!!.notifyDataSetChanged()
                                mAdapterOtherJob!!.notifyDataSetChanged()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    Response.ErrorListener { error -> Log.d("Tag", "Error : $error") }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["employeeId"] = pref.user.employeeId
                    return params
                }
            }
            AppController.getInstance().addToRequestQueue(jobRequest, TAG)
        }


        else{
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        private const val TAG = "HomeActivity"
    }


}






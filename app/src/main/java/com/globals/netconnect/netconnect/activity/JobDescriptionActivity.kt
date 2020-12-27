package com.globals.netconnect.netconnect.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.cunoraz.tagview.Tag
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.app.Cons
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap
import com.cunoraz.tagview.TagView
import com.globals.netconnect.netconnect.app.AppController
import com.globals.netconnect.netconnect.model.Skill
import java.util.ArrayList


/**
 * Created by Himanshu on 09,April,2019
 */

class JobDescriptionActivity : AppCompatActivity() {

    //initialization of Data.......

    private var toolbar: Toolbar? = null
    private lateinit var tvDes: TextView
    private lateinit var tvDesign: TextView
    private lateinit var tvJobId: TextView
    private lateinit var tvReadMore: TextView
    private lateinit var tvMatrix: TextView
    private lateinit var tvLess: TextView
    private lateinit var tvLocation: TextView
    private lateinit var btnRefer: Button
    private var tagGroup: TagView? = null
    private var tagList: ArrayList<Skill>? = null
    private lateinit var llLayout:LinearLayout


    //OnCreateMethod.......
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_description)






        //initialization of Layout.......
        initializationLayout()
        val ss: String = intent.getStringExtra("jobRequisitionNo")
        //API Part
        getDataFrmServer(ss)

        //Setting Data And listeners
        setDataAndListners()

    }

    private fun initializationLayout() {
        window.statusBarColor = resources.getColor(R.color.statusbar)
        toolbar = findViewById<View>(R.id.toolbar_update) as Toolbar
        setSupportActionBar(findViewById(R.id.toolbar_update))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tagList = ArrayList()
        tvDes = findViewById(R.id.tv_job_desc)
        tvDesign = findViewById(R.id.tv_designation)
        tvJobId = findViewById(R.id.tv_jobId)
        tvReadMore = findViewById(R.id.tv_readmore)
        tvLess = findViewById(R.id.tv_close)
        tagGroup = findViewById(R.id.tag_group)
        tvMatrix = findViewById(R.id.tv_matrixCode)
        tvLocation = findViewById(R.id.tv_location)
        btnRefer = findViewById(R.id.bt_refer)
        llLayout=findViewById(R.id.ll_height)
    }

    private fun setDataAndListners() {
        btnRefer.setOnClickListener {
            val intent = Intent(this, SubmitResume::class.java)
            intent.putExtra("jobID", tvJobId.text)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
        }
        tvReadMore.visibility = View.VISIBLE
        tvLess.visibility = View.GONE
        tvDes.maxLines = 5
        tvReadMore.setOnClickListener {
            tvDes.maxLines = Integer.MAX_VALUE
            tvReadMore.visibility = View.GONE
            tvLess.visibility = View.VISIBLE
        }
        tvLess.setOnClickListener {
            tvDes.maxLines = 5
            tvReadMore.visibility = View.VISIBLE
            tvLess.visibility = View.GONE
        }
    }

    private fun getDataFrmServer(ss: String) {
        if (Cons.isNetworkAvailable(this)) {

            val jobDesObj: StringRequest = object :
                StringRequest(
                    Request.Method.POST,
                    Cons.URL_GET_JOB_REQUISITION_LIST,
                    Response.Listener { response ->
                        try {
                            val jsonObjectt = JSONObject(response)
                            val status = jsonObjectt.getString("status")
                            if (status == "0") {
                            } else {
                                val skillarray = jsonObjectt.getJSONArray("skill")
                                for (i in 0 until skillarray.length()) {
                                    val skill = Skill()
                                    val data = skillarray.getJSONObject(i)
                                    skill.backgroundColor = data.getString("backgroundColor")
                                    skill.skill = data.getString("skill")
                                    skill.textColour = data.getString("textColour")
                                    tagList!!.add(skill)
                                }

                                val jobObject = jsonObjectt.getJSONObject("jobDescription")
                                tvDesign.text = jobObject.getString("designation")
                                tvJobId.text = jobObject.getString("jobId")
                                tvDes.text = jobObject.getString("description")
                                tvMatrix.text = jobObject.getString("matrix")
                                tvLocation.text = jobObject.getString("jobLocation")
                                setTags()
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    },
                    com.android.volley.Response.ErrorListener { error ->
                        Log.d(
                            "Tag",
                            "Error : $error"
                        )
                    }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["requisition_no"] = ss
                    return params
                }
            }
            AppController.getInstance().addToRequestQueue(jobDesObj, JobDescriptionActivity.TAG)

        } else {
            Toast.makeText(this, "" + Cons.NETWORK_ERROR, Toast.LENGTH_SHORT).show()
        }

    }

    //getTag of all Designation
    private fun setTags() {
        var tag: Tag
        val tags = ArrayList<Tag>()
        for (i in tagList!!.indices) {
            tag = Tag(tagList!![i].skill)
            tag.radius = 10f
            tag.tagTextSize = 12f
            tag.layoutColor = Color.parseColor(tagList!![i].backgroundColor)
            tag.tagTextColor = Color.parseColor(tagList!![i].textColour)
            tags.add(tag)
        }
        tagGroup!!.addTags(tags)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val TAG = "HomeActivity"
    }
}

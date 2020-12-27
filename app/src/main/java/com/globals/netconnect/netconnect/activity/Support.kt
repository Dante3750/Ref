package com.globals.netconnect.netconnect.activity


import android.app.Activity

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log

import android.view.View
import android.widget.*

import com.android.volley.Request

import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.adaptor.ExpandableListAdapter
import com.globals.netconnect.netconnect.app.Cons

import com.globals.netconnect.netconnect.app.SharedPrefManager


import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap







class Support : AppCompatActivity(), View.OnFocusChangeListener, View.OnClickListener {



    private lateinit var pref: SharedPrefManager
    private var status: Int = 0
    private var toolbar: Toolbar? = null
    private var activity: Activity? = null
    private lateinit var expandableListView: ExpandableListView
    private lateinit var expandableListAdapter: ExpandableListAdapter
    private lateinit var groupList: ArrayList<String>
    private lateinit var childListofGroup: HashMap<String, List<String>>
    private lateinit var movies: ArrayList<String>
    private lateinit var tvSubject:TextView
    private lateinit var tvQuery:TextView
    private lateinit var etSubject:EditText
    private lateinit var etQuery:EditText
    private lateinit var btnSubmit:Button

    private var previousGroup = -1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        window.statusBarColor = resources.getColor(R.color.statusbar)
        toolbar = findViewById<View>(R.id.toolbar_update) as Toolbar
        setSupportActionBar(findViewById(R.id.toolbar_update))

        tvSubject = findViewById(R.id.tvSubject)
        tvQuery = findViewById(R.id.tvQuery)
        etSubject = findViewById(R.id.et_subject)
        etQuery = findViewById(R.id.et_query)
        btnSubmit =findViewById(R.id.bt_faq_submit)

        val regular = Typeface.createFromAsset(this.assets, "fonts/lato_regular.ttf")
        val latoSemiBold = Typeface.createFromAsset(this.assets, "fonts/lato_semibold.ttf")
        val latoMedium = Typeface.createFromAsset(this.assets, "fonts/lato_medium.ttf")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity = this@Support
        findViews()
        init()
        setListener()

        pref = SharedPrefManager.getInstance(this)

        etSubject.onFocusChangeListener = this
        etQuery.onFocusChangeListener = this
        btnSubmit.setOnClickListener(this)


    }

    private fun findViews() {
        expandableListView = findViewById(R.id.ExpandableListView)
        expandableListView.setGroupIndicator(null)
    }

    private fun init() {
        // load list data
        loadListData()

        //  set adapter

    }

    private fun setListener() {
        expandableListView.setOnGroupClickListener { parent, v, groupPosition, id ->





            if (expandableListView.isGroupExpanded(groupPosition)) {





                expandableListView.collapseGroup(groupPosition)
                previousGroup = -1
            } else {
                expandableListView.expandGroup(groupPosition)
                if (previousGroup != -1) {
                    expandableListView.collapseGroup(previousGroup)
                }
                previousGroup = groupPosition
            }
            true
        }


        expandableListView.setOnGroupExpandListener { groupPosition ->

        }


        expandableListView.setOnGroupCollapseListener { groupPosition ->

        }

        expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->


            false
        }
    }

    private fun loadListData() {

        groupList = ArrayList()
        movies = ArrayList()
        childListofGroup = HashMap()

        val getFaq = Volley.newRequestQueue(this)
        val faqDataObject: StringRequest = object :
            StringRequest(Request.Method.GET,Cons.URL_FAQ, com.android.volley.Response.Listener { response ->
                try {
                    val jsonObjectt = JSONObject(response)
                    val status = jsonObjectt.getString("status")
                    if (status == "0") {
                    } else {

                        val faqArray = jsonObjectt.getJSONArray("FAQ")
                        for (i in 0 until faqArray.length()) {
                            val questionData = faqArray.getJSONObject(i).getString("question")
                            val answerData = faqArray.getJSONObject(i).getString("answer")
                            groupList.add(questionData)
                            movies.add(answerData)
                        }

                        for (i in 0 until faqArray.length()) {
                            childListofGroup[groupList[i]] = listOf(movies[i])
                        }
                        expandableListAdapter = ExpandableListAdapter(this, groupList, childListofGroup)
                        expandableListView.setAdapter(expandableListAdapter)

                        try {

                        } catch (e: Exception) {
                        }




                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, com.android.volley.Response.ErrorListener { error -> Log.d("Tag", "Error : " + error.toString()) }) {
        }
        getFaq.add(faqDataObject)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {

        when (v?.id) {

            R.id.et_subject -> {
                etQuery.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)

                etSubject.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)

                tvSubject.setTextColor(resources.getColor(R.color.red))

                tvQuery.setTextColor(resources.getColor(R.color.tvColor))

            }
            R.id.et_query -> {
                etSubject.background.setColorFilter(resources.getColor(R.color.dimBlack), PorterDuff.Mode.SRC_ATOP)

                etQuery.background.setColorFilter(resources.getColor(R.color.red), PorterDuff.Mode.SRC_ATOP)

                tvQuery.setTextColor(resources.getColor(R.color.red))

                tvSubject.setTextColor(resources.getColor(R.color.tvColor))

            }
        }

    }

    override fun onClick(v: View?) {
        updateToServer()
    }

    private fun updateToServer() {
        if (!validate()) {

        } else {

            operation()
        }

    }

    private fun validate(): Boolean {
        var valid = true

        val subject = etSubject!!.text.toString()
        val query = etQuery!!.text.toString()

        if (subject.isEmpty()) {
            etSubject!!.error = "Enter Subject First"
            valid = false
        } else {
            etSubject.error = null
        }


        if (query.isEmpty() ) {
            etQuery!!.error = "Enter Your Query"
            valid = false
        } else {
            etQuery.error = null
        }
        return valid
    }

    private fun operation() {
        val empId =pref.user.employeeId
        val subject = etSubject.text.toString().trim { it <= ' ' }
        val query = etSubject.text.toString().trim { it <= ' ' }


        val supportRequest = Volley.newRequestQueue(this)
        val supportObject: StringRequest = object :
            StringRequest(Request.Method.POST, Cons.URL_SUPPORT, com.android.volley.Response.Listener { response ->
                try {
                    val jsonObjectt = JSONObject(response)
                    val message = jsonObjectt.getString("message")
                    val status = jsonObjectt.getString("status")
                    if (status == "0") {
                    } else {
                        Toast.makeText(this@Support, message, Toast.LENGTH_LONG).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, com.android.volley.Response.ErrorListener { error -> Log.d("Tag", "Error : " + error.toString()) }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["employeeId"] = empId
                params["subject"] = subject
                params["query"] = query
                Log.d("params", "destination:$params")
                return params
            }
        }
        supportRequest.add(supportObject)
    }
    }


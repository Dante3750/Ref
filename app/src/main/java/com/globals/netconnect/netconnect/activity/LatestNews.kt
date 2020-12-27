package com.globals.netconnect.netconnect.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.adaptor.NewsRecyclerView
import com.globals.netconnect.netconnect.app.Cons
import com.globals.netconnect.netconnect.app.Cons.BASE_URL
import com.globals.netconnect.netconnect.app.SharedPrefManager
import com.globals.netconnect.netconnect.model.News
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class LatestNews : AppCompatActivity() {
    private lateinit var pref: SharedPrefManager
    private var status: Int = 0
    private var toolbar: Toolbar? = null
    private var activity: Activity? = null
    private lateinit var ivImageNews: ImageView
    private val List = java.util.ArrayList<News>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: NewsRecyclerView? = null



    //OnCreateMethod
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_news)
        window.statusBarColor = resources.getColor(R.color.statusbar)
        toolbar = findViewById<View>(R.id.toolbar_news) as Toolbar
        setSupportActionBar(findViewById(R.id.toolbar_news))
        pref = SharedPrefManager.getInstance(this)
        ivImageNews = findViewById(R.id.iv_imageNews)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById(R.id.rv_news)
        getDataFromServer()

        val regular = Typeface.createFromAsset(this.assets, "fonts/lato_regular.ttf")
        val latoSemiBold = Typeface.createFromAsset(this.assets, "fonts/lato_semibold.ttf")
        val latoMedium = Typeface.createFromAsset(this.assets, "fonts/lato_medium.ttf")

        mAdapter = NewsRecyclerView(this,regular, latoSemiBold, List)
        recyclerView!!.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter



        recyclerView!!.addOnItemTouchListener(Cons.RecyclerTouchListener(this, recyclerView!!, object : Cons.ClickListener {
            override fun onClick(view: View, position: Int) {
                val news = List[position]

                val intent = Intent(this@LatestNews, WebViewNews::class.java)
                intent.putExtra("news", news.news)
                intent.putExtra("title", news.titleNews)
                intent.putExtra("image", news.image)
                startActivity(intent)
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)


            }

            override fun onLongClick(view: View?, position: Int) {

            }

        }))


    }

    private fun getDataFromServer()


    {
        val getFaq = Volley.newRequestQueue(this)
        val faqDataObject: StringRequest = object :
            StringRequest(Request.Method.GET, Cons.URL_NEWS, com.android.volley.Response.Listener { response ->
                try {
                    val jsonObjectt = JSONObject(response)
                    val status = jsonObjectt.getString("status")
                    if (status == "0") {
                    } else {

                        val mainImage = jsonObjectt.getString("mainImage")


                        Picasso.get()
                            .load("$BASE_URL/$mainImage")
                            .transform(RoundedTransformation(100, 0))
                            .into(ivImageNews)

                        val latestNews = jsonObjectt.getJSONArray("latestNews")
                        List.clear()

                        for (i in 0 until latestNews.length()) {
                            val news = News()
                            val data = latestNews.getJSONObject(i)
                            news.dateNews = data.getString("date")
                            news.news = data.getString("news")
                            news.image = data.getString("image")
                            news.titleNews = data.getString("title")
                            List.add(news)


                        }

                        mAdapter!!.notifyDataSetChanged()

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, com.android.volley.Response.ErrorListener { error -> Log.d("Tag", "Error : $error") }) {
        }
        getFaq.add(faqDataObject)
    } //Todo this function is finished


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }  //Todo backSpace Function
}

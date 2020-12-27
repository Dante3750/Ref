package com.globals.netconnect.netconnect.activity

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.app.Cons.BASE_URL
import com.globals.netconnect.netconnect.app.SharedPrefManager
import com.squareup.picasso.Picasso


class WebViewNews : AppCompatActivity() {
    private lateinit var webView :WebView
    private lateinit var pref: SharedPrefManager
    private var status: Int = 0
    private var toolbar: Toolbar? = null
    private var activity: Activity? = null
    private lateinit var imageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_news)
        webView=findViewById(R.id.webView)
        window.statusBarColor = resources.getColor(R.color.statusbar)
        toolbar = findViewById<View>(R.id.toolbar_webview) as Toolbar

        imageView= findViewById(R.id.iv_imageNews)
        setSupportActionBar(findViewById(R.id.toolbar_webview))
        val regular = Typeface.createFromAsset(this.assets, "fonts/lato_regular.ttf")
        val latoSemiBold = Typeface.createFromAsset(this.assets, "fonts/lato_semibold.ttf")
        val latoMedium = Typeface.createFromAsset(this.assets, "fonts/lato_medium.ttf")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity = this@WebViewNews
        pref = SharedPrefManager.getInstance(this)

        val news:String = intent.getStringExtra("news")
        val title:String = intent.getStringExtra("title")
        val image:String = intent.getStringExtra("image")

        (activity as AppCompatActivity).supportActionBar!!.show()
        (activity as AppCompatActivity).title = title


        Picasso.get()
            .load("$BASE_URL/$image")
            .transform(RoundedTransformation(100, 0))
            .into(imageView)



        webView.loadData(news,
            "text/html", "UTF-8")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

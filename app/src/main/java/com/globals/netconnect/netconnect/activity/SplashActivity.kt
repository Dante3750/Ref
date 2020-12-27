package com.globals.netconnect.netconnect.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.app.SharedPrefManager


class SplashActivity : AppCompatActivity() {

    private val ANIMATION_DURATION:Long = 3000


    private lateinit var pref: SharedPrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        pref = SharedPrefManager.getInstance(this)



        Handler().postDelayed({

            Log.d("status", "destination:$"+pref.getLoggedStatus(applicationContext))
            if (pref.getLoggedStatus(applicationContext)) {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            } else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
            }
        }, ANIMATION_DURATION)
    }

    @Override
    override fun onBackPressed() {
        System.exit(1)
    }




}

package com.globals.netconnect.netconnect.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globals.netconnect.netconnect.R
import android.support.v7.app.AppCompatActivity
import com.globals.netconnect.netconnect.adaptor.CustomSwipeAdaptor
import android.support.design.widget.TabLayout
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*


class MainFragment : Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var swipeAdaptor: CustomSwipeAdaptor


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(
            R.layout.fragment_main,
            container, false

        )

        (activity as AppCompatActivity).supportActionBar!!.hide()

        viewPager = view.findViewById(R.id.view_pager)
        swipeAdaptor = CustomSwipeAdaptor(this.context!!)

        viewPager.adapter = swipeAdaptor

        val tabLayout = view.findViewById(R.id.tabDots) as TabLayout
        tabLayout.setupWithViewPager(viewPager, true)
        val timer = Timer()

        timer.scheduleAtFixedRate(SliderTimer(), 3000, 5000)



        return view
    }

    private inner class SliderTimer : TimerTask() {

        override fun run() {

            if (activity == null) {
                return
            }


            activity!!.runOnUiThread {
                if (viewPager.currentItem < 3 - 1) {
                    viewPager.currentItem = viewPager.currentItem + 1
                } else {
                    viewPager.currentItem = 0
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        login.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toLogin))
        signup.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toSignUp))
        tv_forgot_pass.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toForget))

    }







}


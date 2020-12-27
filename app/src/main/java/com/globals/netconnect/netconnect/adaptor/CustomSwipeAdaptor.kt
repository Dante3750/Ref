package com.globals.netconnect.netconnect.adaptor

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.globals.netconnect.netconnect.R

class CustomSwipeAdaptor(private val ctx: Context) : PagerAdapter() {

    private val imageResource = intArrayOf(R.drawable.refer, R.drawable.redeem, R.drawable.rejoice)
    private var llInflater: LayoutInflater? = null


    override fun getCount(): Int {
        return imageResource.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {


        return view === o
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        llInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val item_View = llInflater!!.inflate(R.layout.swipe_layout, container, false)
        val imageView = item_View.findViewById<ImageView>(R.id.imageView)

        imageView.setImageResource(imageResource[position])
        container.addView(item_View)

        return item_View
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}

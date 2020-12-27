package com.globals.netconnect.netconnect.adaptor


import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.model.NotificationModel


class NotificationAdaptor(regular: Typeface,latoSemiBold:Typeface,private val ListT: List<NotificationModel>):
        RecyclerView.Adapter<NotificationAdaptor.MyViewHolder>(){
    var latoSemiBold: Typeface = latoSemiBold
    var regular: Typeface = regular


    override fun onBindViewHolder(myHolder: MyViewHolder, i: Int) {
        val referralData = ListT[i]

        myHolder.tvTitle!!.text =  referralData.titleNot
        myHolder.tvBody!!.text =  referralData.body
       myHolder.tvTimeStamp!!.text =  referralData.timeStamp

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): NotificationAdaptor.MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_notification, viewGroup, false)
        return NotificationAdaptor.MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return ListT.size

    }

    class MyViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var tvTitle: TextView? = null
        var tvBody: TextView? = null
       var tvTimeStamp: TextView? = null

        init {

            this.tvTitle = row?.findViewById<TextView>(R.id.tv_title)
            this.tvBody = row?.findViewById<TextView>(R.id.tv_body)
            this.tvTimeStamp = row?.findViewById<TextView>(R.id.tv_timeStamp)
        }

    }

}
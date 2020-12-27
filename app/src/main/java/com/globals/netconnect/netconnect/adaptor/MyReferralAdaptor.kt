package com.globals.netconnect.netconnect.adaptor

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.model.ReferralData





class MyReferralAdaptor(regular: Typeface, latoSemiBold: Typeface,private val Listt: List<ReferralData>) :
    RecyclerView.Adapter<MyReferralAdaptor.MyViewHolder>() {

    var latoSemiBold: Typeface = latoSemiBold
    var regular: Typeface = regular


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_referral_list, viewGroup, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val referralData = Listt[i]
        val myString = referralData.name
        val upperString = myString!!.substring(0, 1).toUpperCase() + myString.substring(1)
        myViewHolder.tvDate!!.text = referralData.date
        myViewHolder.tvName!!.typeface = latoSemiBold
        myViewHolder.tvRefereeStatus!!.text = referralData.refereeStatus
        myViewHolder.tvRefereeStatus!!.setTextColor(Color.parseColor("#"+referralData.textcolor))

//        when {
//            referralData.refereeStatus=="Hired" -> myViewHolder.tvRefereeStatus!!.setTextColor(Color.parseColor("#00CFA6"))
//            referralData.refereeStatus=="Reject" -> myViewHolder.tvRefereeStatus!!.setTextColor(Color.parseColor("#00CFA6"))
//            referralData.refereeStatus=="Hired" -> myViewHolder.tvRefereeStatus!!.setTextColor(Color.parseColor("#00CFA6"))
//        }



        myViewHolder.tvRole!!.text = referralData.role
        myViewHolder.tvName!!.text = upperString
        myViewHolder.tvEmailId!!.text = referralData.emailId

        myViewHolder.tvName!!.typeface = latoSemiBold
        myViewHolder.tvEmailId!!.typeface = regular
        myViewHolder.tvRole!!.typeface = regular
        myViewHolder.tvRefereeStatus!!.typeface = regular
        myViewHolder.tvDate!!.typeface = regular




    }

    override fun getItemCount(): Int {

        return Listt.size

    }

    class MyViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var tvDate: TextView? = null
        var tvRefereeStatus: TextView? = null
        var tvRole: TextView? = null
        var tvName: TextView? = null
        var tvEmailId: TextView? = null

        init {

            this.tvDate = row?.findViewById<TextView>(R.id.tv_referral_date)
            this.tvRefereeStatus = row?.findViewById<TextView>(R.id.tv_status)
            this.tvRole = row?.findViewById<TextView>(R.id.tv_role)
            this.tvName = row?.findViewById<TextView>(R.id.tv_name)
            this.tvEmailId = row?.findViewById<TextView>(R.id.tv_referral_email_id)
        }


    }
}

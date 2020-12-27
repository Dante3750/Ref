package com.globals.netconnect.netconnect.adaptor


import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.model.OtherJob



class JobOtherRecyclerView(regular: Typeface,latoSemiBold: Typeface,private var List: List<OtherJob>) :
    RecyclerView.Adapter<JobOtherRecyclerView.MyViewHolder>() {

    var latoSemiBold: Typeface = latoSemiBold
    var regular: Typeface = regular


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {

        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.ietm_job_list_other, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        val jobModel = List[i]
        myViewHolder.tvDestination!!.text = "Asset Name : " + jobModel.role
        myViewHolder.tvDestination!!.typeface = latoSemiBold

        myViewHolder.tvlocation!!.text = "Location : " + jobModel.location

        myViewHolder.tvlocation!!.typeface = regular

        myViewHolder.tvRewards!!.text = "Reward : " + jobModel.rewardsAmount

        myViewHolder.tvRewards!!.typeface = regular


    }

    override fun getItemCount(): Int {
        return List.size

    }

    class MyViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var tvDestination: TextView? = null
        var tvlocation: TextView? = null
        var tvRewards: TextView? = null

        init {



            this.tvDestination = row?.findViewById<TextView>(R.id.tv_designation)
            this.tvlocation = row?.findViewById<TextView>(R.id.tv_location)
            this.tvRewards = row?.findViewById<TextView>(R.id.tv_reward_amount)
        }


    }
}

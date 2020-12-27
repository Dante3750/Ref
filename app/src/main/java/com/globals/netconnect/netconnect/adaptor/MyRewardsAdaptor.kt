package com.globals.netconnect.netconnect.adaptor

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.globals.netconnect.netconnect.R

import android.widget.LinearLayout
import android.widget.SeekBar

import android.content.Context
import com.globals.netconnect.netconnect.model.RewardsData


class MyRewardsAdaptor(context: Context,regular: Typeface, latoSemiBold: Typeface, private val RewardsList: List<RewardsData>) :
    RecyclerView.Adapter<MyRewardsAdaptor.MyViewHolder>() {

    var latoSemiBold: Typeface = latoSemiBold
    var regular: Typeface = regular
    private val context: Context? = context


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_reward_list, viewGroup, false)

        return MyViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor", "SetTextI18n", "RestrictedApi")
    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        val rewardsData = RewardsList[i]

        val myString = rewardsData.refereeName
        val upperString = myString!!.substring(0, 1).toUpperCase() + myString.substring(1)


        myViewHolder.tvDate!!.text =  rewardsData.redemptionDate
        myViewHolder.tvReward!!.text = "\u20B9 "+rewardsData.reward
        myViewHolder.tvName!!.text = upperString

        myViewHolder.tvName!!.setTextColor(Color.parseColor("#"+rewardsData.textColour))

          myViewHolder.seekBar!!.indeterminateDrawable.setColorFilter(Color.parseColor("#"+rewardsData.textColour), android.graphics.PorterDuff.Mode.SRC_IN)


        myViewHolder.seekBar!!.progressDrawable.setColorFilter(Color.parseColor("#"+rewardsData.ProgressBarTintColor), android.graphics.PorterDuff.Mode.SRC_IN)



        myViewHolder.cardView!!.setBackgroundColor(Color.parseColor("#"+rewardsData.backgroundColour))


        myViewHolder.seekBar!!.progress = rewardsData.diffDays!!

        myViewHolder.seekBar!!.isEnabled = false





        if (rewardsData.diffDays==90){
            val seekThumb = context!!.resources.getDrawable(R.drawable.giftbox_icon_myrewards)
            myViewHolder.seekBar!!.thumb = seekThumb
        }
        else{
            val seekThumb = context!!.resources.getDrawable(R.drawable.trophy_icon_myrewards)
            myViewHolder.seekBar!!.thumb = seekThumb
        }


//        myViewHolder.seekBar!!.thumb =

//        myViewHolder.seekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//
//            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                // Display the current progress of SeekBar
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {
//                // Do something
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                // Do something
//            }
//        })





//
//        if (rewardsData.diffDays==90){
//            myViewHolder.seekBar.thumb.set
//        }







    }

    override fun getItemCount(): Int {

        return RewardsList.size

    }

    class MyViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var tvName: TextView? = null
        var tvReward :TextView? =null
        var tvDate: TextView? = null
        var cardView :LinearLayout?=null
        var seekBar :SeekBar?=null


        init {

            this.tvDate = row.findViewById<TextView>(R.id.tv_date)
            this.tvReward = row.findViewById<TextView>(R.id.tv_rewards)
            this.tvName = row.findViewById<TextView>(R.id.tv_name)
            this.cardView = row.findViewById<LinearLayout>(R.id.ll_rewards)
            this.seekBar = row.findViewById<SeekBar>(R.id.seekBar)

        }


    }
}

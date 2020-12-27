package com.globals.netconnect.netconnect.adaptor

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.globals.netconnect.netconnect.R
import com.globals.netconnect.netconnect.model.News
import com.squareup.picasso.Picasso


class NewsRecyclerView(context: Context, regular: Typeface, latoSemiBold: Typeface, private val List: List<News>) :
    RecyclerView.Adapter<NewsRecyclerView.MyViewHolder>() {

    var latoSemiBold: Typeface = latoSemiBold
    var regular: Typeface = regular
    var context: Context = context

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_news_row, viewGroup, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        val news = List[i]
        Picasso.get().load(news.image).into( myViewHolder.ivImage)
        myViewHolder.tvNews!!.text = news.titleNews
        myViewHolder.tvDate!!.text = news.dateNews

    }

    override fun getItemCount(): Int {

        return List.size

    }

    class MyViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var tvNews: TextView? = null
        var tvDate: TextView? = null
        var ivImage: ImageView? = null

        init {
            this.tvNews = row?.findViewById<TextView>(R.id.tv_news)
            this.tvDate = row?.findViewById<TextView>(R.id.tv_news_date)
            this.ivImage = row?.findViewById<ImageView>(R.id.iv_news_image)
        }

    }
}
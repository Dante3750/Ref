package com.globals.netconnect.netconnect.adaptor

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.globals.netconnect.netconnect.R

import java.util.HashMap

class ExpandableListAdapter(private val mContext: Context, //Group header titles
                            private val listGroup: List<String>,
        // child data in format of header title, child title
                            private val listChild: HashMap<String, List<String>>) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return this.listGroup.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this.listChild[this.listGroup[groupPosition]]?.size!!
    }

    override fun getGroup(groupPosition: Int): Any {
        return this.listGroup[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosititon: Int): Any {
        return this.listChild[this.listGroup[groupPosition]]?.get(childPosititon)!!
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val headerTitle = getGroup(groupPosition) as String

    
        
        if (convertView == null) {
            val infalInflater = this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_item_subject, null)
            
           
            
        }

        val lblListHeader = convertView!!.findViewById<TextView>(R.id.tv_sub_name)

        val parentTextView = convertView.findViewById(R.id.tv_sub_name) as TextView
        val ImageView = convertView.findViewById(R.id.dropdown) as ImageView

        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.text = headerTitle
        if (isExpanded){
            parentTextView.setTextColor(mContext.resources.getColor(R.color.faq))
            ImageView.setImageDrawable(mContext.resources.getDrawable(R.drawable.dropdown_icon))
        }
        
        else{
            parentTextView.setTextColor(mContext.resources.getColor(R.color.black))
            ImageView.setImageDrawable(mContext.resources.getDrawable(R.drawable.rotate))
        }
         

        return convertView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val childText = getChild(groupPosition, childPosition) as String

        if (convertView == null) {
            val infalInflater = this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_item_answer, null)
        }

        val txtListChild = convertView!!.findViewById<TextView>(R.id.tv_sub_answer)
        txtListChild.text = childText
        return convertView
    }

    override fun isChildSelectable(i: Int, i1: Int): Boolean {
        return true
    }
}

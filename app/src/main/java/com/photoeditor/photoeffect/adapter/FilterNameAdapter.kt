package com.photoeditor.photoeffect.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.photoeditor.photoeffect.R

class FilterNameAdapter(context: Context,filterList:Array<String>) : Adapter<FilterNameAdapter.FilterNameHolder>() {

    var filters: Array<String>? = null
    var mContext: Context
    lateinit var filterNameClicklistener: FilterNameClickListener
    var selectedindex = 0

    fun setOnFilterNameClick(filterNameClicklistener: FilterNameClickListener) {
        this.filterNameClicklistener = filterNameClicklistener
    }

    interface FilterNameClickListener {
        fun onItemClick(view: View, position: Int)
    }

    init {
        mContext = context
        filters = filterList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterNameHolder {

        var view = LayoutInflater.from(mContext).inflate(R.layout.item_font_tab, parent, false)
        return FilterNameHolder(view)
    }

    override fun getItemCount(): Int {
        return filters!!.size
    }

    override fun onBindViewHolder(holder: FilterNameHolder, position: Int) {
        holder.txt_filter_tab.setText(filters!![position])

        if (selectedindex == position) {
            holder.ll_filteritem.setBackgroundResource(R.drawable.round_corner)
        } else {
            holder.ll_filteritem.setBackgroundColor(mContext.resources.getColor(R.color.transparent))
        }

        holder.ll_filteritem.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                selectedindex = position
                filterNameClicklistener.onItemClick(v!!, position)
                notifyDataSetChanged()
            }
        })
    }


    inner class FilterNameHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_filter_tab: TextView
        var ll_filteritem: LinearLayout

        init {
            txt_filter_tab = itemView.findViewById(R.id.txt_filter_tab) as TextView
            ll_filteritem = itemView.findViewById(R.id.ll_filteritem) as LinearLayout
        }


    }
}

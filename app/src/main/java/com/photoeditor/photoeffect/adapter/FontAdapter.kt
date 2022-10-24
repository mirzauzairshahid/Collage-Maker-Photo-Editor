package com.photoeditor.photoeffect.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.photoeditor.photoeffect.R

class FontAdapter(context: Context) : Adapter<FontAdapter.FontHolder>() {

    var fonts_sticker: Array<String>? = null
    var mContext: Context
    lateinit var fontClicklistener: FontClickListener
    var selectedindex = 0

    fun setOnFontClick(fontClicklistener: FontClickListener) {
        this.fontClicklistener = fontClicklistener
    }

    interface FontClickListener {
        fun onItemClick(view: View, fontName: String)
    }

    init {
        mContext = context
        fonts_sticker = mContext.resources.getStringArray(R.array.fonts_sticker)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontHolder {

        var view = LayoutInflater.from(mContext).inflate(R.layout.item_font_tab, parent, false)
        return FontHolder(view)
    }

    override fun getItemCount(): Int {
        return fonts_sticker!!.size
    }

    override fun onBindViewHolder(holder: FontHolder, position: Int) {
        holder.txt_filter_tab.setText("HelloWorld")

        if (selectedindex == position) {
            holder.ll_filteritem.setBackgroundColor(mContext.resources.getColor(R.color.colorAccent))
        } else {
            holder.ll_filteritem.setBackgroundColor(mContext.resources.getColor(R.color.transparent))
        }
        var typeface = Typeface.createFromAsset(mContext.assets, fonts_sticker!![position])
        holder.txt_filter_tab.typeface = typeface

        holder.txt_filter_tab.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                selectedindex = position
                fontClicklistener.onItemClick(v!!, fonts_sticker!![position])
                notifyDataSetChanged()
            }
        })
    }


    inner class FontHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_filter_tab: TextView
        var ll_filteritem: LinearLayout

        init {
            txt_filter_tab = itemView.findViewById(R.id.txt_filter_tab) as TextView
            ll_filteritem = itemView.findViewById(R.id.ll_filteritem) as LinearLayout
        }


    }
}

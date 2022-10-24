package com.photoeditor.photoeffect.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.photoeditor.photoeffect.R

class ColorAdapter(context: Context) : Adapter<ColorAdapter.ColorHolder>() {

     var color_sticker: Array<String>
     var mContext: Context
    lateinit var colorClicklistener: ColorClickListener
    var selectedindex = 0

    fun setOnColorClick(colorClicklistener: ColorClickListener) {
        this.colorClicklistener = colorClicklistener
    }

    interface ColorClickListener {
        fun onItemClick(view: View, colorName: String)
    }

    init {
        mContext = context
        color_sticker = mContext.resources.getStringArray(R.array.sticker_color)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {

        var view = LayoutInflater.from(mContext).inflate(R.layout.item_color, parent, false)
        return ColorHolder(view)
    }

    override fun getItemCount(): Int {
        return color_sticker.size
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        holder.img_color.setBackgroundColor(Integer.valueOf(Color.parseColor(color_sticker[position])))

        if (selectedindex == position) {
            holder.ll_color.setBackgroundColor(mContext.resources.getColor(R.color.colorAccent))
        } else {
            holder.ll_color.setBackgroundColor(mContext.resources.getColor(R.color.transparent))
        }

        holder.img_color.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                selectedindex =position
                colorClicklistener.onItemClick(v!!, color_sticker[position])
                notifyDataSetChanged()
            }
        })
    }


    inner class ColorHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img_color: ImageView
        var ll_color: LinearLayout

        init {
            img_color = itemView.findViewById(R.id.img_color) as ImageView
            ll_color = itemView.findViewById(R.id.ll_color) as LinearLayout
        }


    }
}

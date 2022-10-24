package com.photoeditor.photoeffect.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.photoeditor.photoeffect.R

class StickerTabAdapter(context: Context) : RecyclerView.Adapter<StickerTabAdapter.TabHolder>() {

    var mcontext: Context
    var images: Array<Int> = arrayOf(
        R.drawable.sticker_emoji,
        R.drawable.sticker_cat,
        R.drawable.sticker_dog,
        R.drawable.sticker_chicken,
        R.drawable.sticker_text,
        R.drawable.sticker_tusk
    )
    var selected: Int = 0

    init {
        mcontext = context
    }

    interface StickerTabListener {
        fun onTabSelected(view: View, position: Int)
    }

    lateinit var stickerTabListener: StickerTabListener

    fun setTabClickListener(stickertabListener: StickerTabListener) {
        this.stickerTabListener = stickertabListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabHolder {
        var view = LayoutInflater.from(mcontext).inflate(R.layout.item_color, parent, false)
        return TabHolder(view)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: TabHolder, position: Int) {
        if (selected == position) {
            holder.ll_color.setBackgroundColor(mcontext.resources.getColor(R.color.hint))
        } else {
            holder.ll_color.setBackgroundColor(mcontext.resources.getColor(R.color.black))
        }

        holder.img_color.setImageResource(images[position])
        holder.img_color.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                selected = position
                stickerTabListener.onTabSelected(v!!, position)


                notifyDataSetChanged()
            }
        })
    }

    inner class TabHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img_color: ImageView
        var ll_color: LinearLayout

        init {
            img_color = itemView.findViewById(R.id.img_color)
            ll_color = itemView.findViewById(R.id.ll_color)
        }
    }
}
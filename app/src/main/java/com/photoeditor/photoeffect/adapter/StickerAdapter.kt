package com.photoeditor.photoeffect.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.photoeditor.photoeffect.R
import java.lang.Exception

class StickerAdapter(context: Context, position: Int) :
    RecyclerView.Adapter<StickerAdapter.StickerHolder>() {
    var mContext: Context = context
    lateinit var images: Array<String>
    var position: Int = position
    var names: Array<String> = arrayOf("emoji", "cat", "dog", "chicken", "texts", "tusk")
    var selectedindex = 0

    init {
        //        images = mContext.assets.list("") as Array<String>

        when {
            this.position == 0 -> images =
                mContext.assets.list(names[this.position]) as Array<String>
            this.position == 1 -> images =
                mContext.assets.list(names[this.position]) as Array<String>
            this.position == 2 -> images =
                mContext.assets.list(names[this.position]) as Array<String>
            this.position == 3 -> images =
                mContext.assets.list(names[this.position]) as Array<String>
            this.position == 4 -> images =
                mContext.assets.list(names[this.position]) as Array<String>
            this.position == 5 -> images =
                mContext.assets.list(names[this.position]) as Array<String>
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerHolder {

        var view = LayoutInflater.from(mContext).inflate(R.layout.item_color, parent, false)
        return StickerHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: StickerHolder, position: Int) {
        var inputStream = mContext.assets.open(names[this.position] + "/" + images.get(position))
        var drawable = Drawable.createFromStream(inputStream, null)

        if (selectedindex == position) {
            holder.ll_color.setBackgroundColor(mContext.resources.getColor(R.color.colorAccent))
        } else {
            holder.ll_color.setBackgroundColor(mContext.resources.getColor(R.color.transparent))
        }

        holder.img_color.setImageDrawable(drawable)
        holder.img_color.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                selectedindex = position
                try {
                    stickerListener.onStickerClick(v!!, drawable)
                    notifyDataSetChanged()
                } catch (e: Exception) {

                }
                notifyDataSetChanged()
            }
        })
    }

    inner class StickerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img_color: ImageView
        var ll_color: LinearLayout

        init {
            img_color = itemView.findViewById(R.id.img_color)
            ll_color = itemView.findViewById(R.id.ll_color)
        }
    }

    lateinit var stickerListener: StickerListener

    fun setOnStickerClick(stickerlistener: StickerListener) {
        this.stickerListener = stickerlistener
    }

    interface StickerListener {
        fun onStickerClick(view: View, drawable: Drawable)
    }
}
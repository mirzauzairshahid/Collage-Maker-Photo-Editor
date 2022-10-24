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

class BackgroundAdapter(
    context: Context,
    bgClickListener: OnBGClickListener
) : RecyclerView.Adapter<BackgroundAdapter.BackgroundHolder>() {


    var mImages: Array<String>
    var mContext = context
    var bgListener: OnBGClickListener = bgClickListener
    var selectedindex = 0

    init {
        mImages = mContext.assets.list("background") as Array<String>
    }

    interface OnBGClickListener {
        fun onBGClick(drawable: Drawable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackgroundHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_frame, parent, false)

        return BackgroundHolder(view)
    }

    override fun getItemCount(): Int {
        return mImages.size
    }

    override fun onBindViewHolder(holder: BackgroundHolder, position: Int) {

        var inputStream = mContext.assets.open("background/" + mImages[position])
        var drawable = Drawable.createFromStream(inputStream, null)
        holder.img_frame.setImageDrawable(drawable)

        if (selectedindex == position) {
            holder.ll_itemframe.setBackgroundColor(mContext.resources.getColor(R.color.colorAccent))
        } else {
            holder.ll_itemframe.setBackgroundColor(mContext.resources.getColor(R.color.transparent))
        }

        holder.img_frame.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                selectedindex = position

                bgListener.onBGClick(drawable)
                notifyDataSetChanged()
            }

        })
    }

    class BackgroundHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img_frame: ImageView = itemView.findViewById(R.id.img_frame)
        var ll_itemframe: LinearLayout = itemView.findViewById(R.id.ll_itemframe)
    }
}
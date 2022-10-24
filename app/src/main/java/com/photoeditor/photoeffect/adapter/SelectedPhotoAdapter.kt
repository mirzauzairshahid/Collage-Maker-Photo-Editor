package com.photoeditor.photoeffect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.photoeditor.photoeffect.AndroidUtils
import com.photoeditor.photoeffect.R
import java.util.ArrayList

class SelectedPhotoAdapter(
    arrayList: ArrayList<String>,
    onDeleteButtonClickListener: OnDeleteButtonClickListener
) : RecyclerView.Adapter<SelectedPhotoAdapter.SelectedPhotoViewHolder>() {

    var mImages = arrayList
    var mListener = onDeleteButtonClickListener

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClick(str: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedPhotoViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_selected_photo, parent, false)

        return SelectedPhotoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mImages.size
    }

    override fun onBindViewHolder(holder: SelectedPhotoViewHolder, position: Int) {
        AndroidUtils.loadImageWithGlide(
            holder.selectedImage.context,
            holder.selectedImage,
            mImages[position]
        )

        holder.deleteView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (mListener != null) {
                    mListener.onDeleteButtonClick(mImages[position])
                }
            }
        })
    }

    class SelectedPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var selectedImage: ImageView = itemView.findViewById(R.id.selectedImage)
        var deleteView: ImageView = itemView.findViewById(R.id.deleteView)
    }
}
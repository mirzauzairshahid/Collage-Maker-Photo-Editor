package com.photoeditor.photoeffect.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.photoeditor.photoeffect.AndroidUtils
import com.photoeditor.photoeffect.R
import com.photoeditor.photoeffect.model.GalleryAlbum
import java.lang.StringBuilder

class GalleryAlbumRecyclerAdapter(
    context: Context,
    list: List<GalleryAlbum>,
    onGalleryAlbumClickListener: GalleryAlbumAdapter.OnGalleryAlbumClickListener
) :
    RecyclerView.Adapter<GalleryAlbumRecyclerAdapter.MyViewHolder>() {

    var mContext = context
    var mAlbums: List<GalleryAlbum> = list
    var mListener = onGalleryAlbumClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.item_gallery_album, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mAlbums.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var galleryAlbum = mAlbums[position]
        if (galleryAlbum != null && holder != null) {
            if (galleryAlbum.mImageList.size > 0) {
                AndroidUtils.loadImageWithGlide(
                    mContext,
                    holder.thumbnailView,
                    galleryAlbum.mImageList[0]
                )
            } else {
                holder.thumbnailView.setImageBitmap(null)
            }

            var albumName = galleryAlbum.mAlbumName
            if (albumName.length > 7) {
                var textView = holder.titleView
                var sb = StringBuilder().append(albumName.substring(0, 5)).append("..")
                textView.text = sb.toString()
            } else {
                holder.titleView.text = albumName
            }

            var count = StringBuilder().append("(").append(galleryAlbum.mImageList.size).append(")")
            holder.itemCountView.text = count.toString()

            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    if (mListener != null) {
                        mListener.onGalleryAlbumClick(galleryAlbum)
                    }
                }

            })

        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleView: TextView
        var itemCountView: TextView
        var thumbnailView: ImageView

        init {
            titleView = itemView.findViewById(R.id.titleView)
            itemCountView = itemView.findViewById(R.id.itemCountView)
            thumbnailView = itemView.findViewById(R.id.thumbnailView)
        }
    }
}
package com.photoeditor.photoeffect.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.photoeditor.photoeffect.AndroidUtils
import com.photoeditor.photoeffect.R
import com.photoeditor.photoeffect.model.GalleryAlbum

class GalleryAlbumAdapter(
    context: Context,
    list: List<GalleryAlbum>,
    var mListener: OnGalleryAlbumClickListener?
) : ArrayAdapter<GalleryAlbum>(context, R.layout.item_gallery_album, list) {

    var mContext = context

    interface OnGalleryAlbumClickListener {
        fun onGalleryAlbumClick(galleryAlbum: GalleryAlbum?)
    }

    inner class ViewHolder {
        internal var descriptionView: TextView? = null
        internal var itemCountView: TextView? = null
        internal var thumbnailView: ImageView? = null
        internal var titleView: TextView? = null
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view
        val viewHolder: ViewHolder?
        if (view == null) {
            view =
                LayoutInflater.from(mContext).inflate(R.layout.item_gallery_album, viewGroup, false)
            viewHolder = ViewHolder()
            viewHolder.thumbnailView = view!!.findViewById(R.id.thumbnailView) as ImageView
            viewHolder.titleView = view.findViewById(R.id.titleView) as TextView
            viewHolder.itemCountView = view.findViewById(R.id.itemCountView) as TextView
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        val galleryAlbum = getItem(i)
        if (galleryAlbum != null) {
            if (galleryAlbum.mImageList.size > 0) {
                AndroidUtils.loadImageWithGlide(
                    context,
                    viewHolder.thumbnailView!!,
                    galleryAlbum.mImageList[0] as String
                )
            } else {
                viewHolder.thumbnailView!!.setImageBitmap(null)
            }
            viewHolder.titleView!!.setText(galleryAlbum.mAlbumName)
            val textView = viewHolder.itemCountView
            val sb = StringBuilder()
            sb.append("(")
            sb.append(galleryAlbum.mImageList.size)
            sb.append(")")
            textView!!.text = sb.toString()
            view.setOnClickListener {
                if (this@GalleryAlbumAdapter.mListener != null) {
                    this@GalleryAlbumAdapter.mListener!!.onGalleryAlbumClick(galleryAlbum)
                }
            }
        }
        return view
    }
}

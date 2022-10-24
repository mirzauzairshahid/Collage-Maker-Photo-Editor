package com.photoeditor.photoeffect.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView

import com.photoeditor.photoeffect.R
import com.photoeditor.photoeffect.adapter.GalleryAlbumImageAdapter
import kotlinx.android.synthetic.main.fragment_gallery_album_image.view.*

/**
 * A simple [Fragment] subclass.
 */
class GalleryAlbumImageFragment : Fragment() {

    companion object {
        val ALBUM_IMAGE_EXTRA = "albumImage"
        val ALBUM_NAME_EXTRA = "albumName"
    }

    var mImages: ArrayList<String> = ArrayList()
    lateinit var names: String
    lateinit var mListener: OnSelectImageListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is OnSelectImageListener) {
            mListener = activity as OnSelectImageListener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_gallery_album_image, container, false)

        if (arguments != null) {
            mImages = arguments!!.getStringArrayList(ALBUM_IMAGE_EXTRA)!!
            names = arguments!!.getString(ALBUM_NAME_EXTRA)!!

            if (mImages != null) {

                view.gridView.adapter = GalleryAlbumImageAdapter(view.context, mImages)
                view.gridView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (mListener != null) {
                            mListener.onSelectImage(mImages[position])
                        }
                    }
                })
            }
        }
        return view
    }

    interface OnSelectImageListener {
        fun onSelectImage(str: String)
    }
}

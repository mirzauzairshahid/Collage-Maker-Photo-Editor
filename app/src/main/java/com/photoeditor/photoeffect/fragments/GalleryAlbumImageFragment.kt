package com.photoeditor.photoeffect.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.photoeditor.photoeffect.adapter.GalleryAlbumImageAdapter
import com.photoeditor.photoeffect.databinding.FragmentGalleryAlbumImageBinding

/**
 * A simple [Fragment] subclass.
 */
class GalleryAlbumImageFragment : Fragment() {

    companion object {
        val ALBUM_IMAGE_EXTRA = "albumImage"
        val ALBUM_NAME_EXTRA = "albumName"
    }

    var mImages: ArrayList<String>? = ArrayList()
    lateinit var names: String
    var mListener: OnSelectImageListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is OnSelectImageListener) {
            mListener = activity as OnSelectImageListener
        }
    }

    var binding: FragmentGalleryAlbumImageBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryAlbumImageBinding.inflate(layoutInflater, container, false)

        if (arguments != null) {
            mImages = arguments?.getStringArrayList(ALBUM_IMAGE_EXTRA)!!
            names = arguments?.getString(ALBUM_NAME_EXTRA)!!

            mImages?.let { images ->
                binding?.gridView?.adapter = GalleryAlbumImageAdapter(requireContext(), images)
                binding?.gridView?.setOnItemClickListener { _, _, position, _ ->
                    mListener?.onSelectImage(images[position])
                }
            }

        }
        return binding?.root
    }

    interface OnSelectImageListener {
        fun onSelectImage(str: String)
    }
}

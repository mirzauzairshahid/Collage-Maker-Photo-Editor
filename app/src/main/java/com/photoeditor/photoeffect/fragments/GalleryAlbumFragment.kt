package com.photoeditor.photoeffect.fragments


import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager

import com.photoeditor.photoeffect.R
import com.photoeditor.photoeffect.adapter.GalleryAlbumAdapter
import com.photoeditor.photoeffect.adapter.GalleryAlbumRecyclerAdapter
import com.photoeditor.photoeffect.databinding.FragmentGalleryAlbumBinding
import com.photoeditor.photoeffect.model.GalleryAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class GalleryAlbumFragment(context: Context) : Fragment() {

    var mContext: Context = context
    private lateinit var mAdapter: GalleryAlbumRecyclerAdapter
    private lateinit var fragmentView: FragmentGalleryAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = FragmentGalleryAlbumBinding.inflate(layoutInflater, container, false)

        fragmentView.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            var images: ArrayList<GalleryAlbum>? = null
            val imagesTask = async {
                images = loadPhotoAlbums()
            }

            imagesTask.await()

            withContext(Dispatchers.Main) {
                images?.let {
                    fragmentView.progressBar.visibility = View.GONE
                    mAdapter = GalleryAlbumRecyclerAdapter(
                        mContext,
                        it,
                        object : GalleryAlbumAdapter.OnGalleryAlbumClickListener {
                            override fun onGalleryAlbumClick(galleryAlbum: GalleryAlbum?) {

                                var bundle = Bundle()
                                bundle.putStringArrayList(
                                    GalleryAlbumImageFragment.ALBUM_IMAGE_EXTRA,
                                    galleryAlbum!!.mImageList as java.util.ArrayList<String>
                                )
                                bundle.putString(
                                    GalleryAlbumImageFragment.ALBUM_NAME_EXTRA,
                                    galleryAlbum.mAlbumName
                                )

                                var galleryalbumImageFragment = GalleryAlbumImageFragment()
                                galleryalbumImageFragment.arguments = bundle

                                var fragmentTransaction =
                                    activity!!.supportFragmentManager.beginTransaction()
                                fragmentTransaction.replace(
                                    R.id.frame_container,
                                    galleryalbumImageFragment
                                )
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            }
                        })
                    mAdapter.notifyDataSetChanged()
                    fragmentView.listView.layoutManager =
                        GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false)
                    fragmentView.listView.adapter = mAdapter
                }

            }
        }

        return fragmentView.root
    }

    private fun loadPhotoAlbums(): ArrayList<GalleryAlbum> {

        val r0 = LinkedHashMap<Long, GalleryAlbum>()
        val r4: Array<String> =
            arrayOf("_id", "_data", "bucket_id", "bucket_display_name", "datetaken")
        val r3: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val r2: Cursor? = mContext.contentResolver.query(r3, r4, null, null, "date_added DESC")
        val arrayList: ArrayList<GalleryAlbum> = java.util.ArrayList<GalleryAlbum>()
        if (r2 != null) {
            if (r2.moveToFirst()) {

                do {
                    val r1: String = r2.getString(r2.getColumnIndex("bucket_display_name"))
                    val r3: Long = r2.getLong(r2.getColumnIndex("datetaken"))
                    val r5: String = r2.getString(r2.getColumnIndex("_data"))
                    val r6: Long = r2.getLong(r2.getColumnIndex("bucket_id"))

                    var r8: GalleryAlbum? = r0[r6]
                    if (r8 == null) {
                        r8 = GalleryAlbum(r6, r1)
                        r8.mTakenDate =
                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(r3)
                        r8.mImageList.add(r5)
                        r0[r6] = r8
                    } else {
                        r8.mImageList.add(r5)
                    }

                } while (r2.moveToNext())
                arrayList.addAll(r0.values)
            }
        }
        r2!!.close()

        return arrayList
    }

}

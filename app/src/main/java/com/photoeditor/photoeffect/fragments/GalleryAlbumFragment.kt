package com.photoeditor.photoeffect.fragments


import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import com.photoeditor.photoeffect.R
import com.photoeditor.photoeffect.adapter.GalleryAlbumAdapter
import com.photoeditor.photoeffect.adapter.GalleryAlbumRecyclerAdapter
import com.photoeditor.photoeffect.model.GalleryAlbum
import kotlinx.android.synthetic.main.fragment_gallery_album.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class GalleryAlbumFragment(context: Context) : Fragment() {

    lateinit var mAlbums: ArrayList<GalleryAlbum>
    var mContext: Context = context
    lateinit var mAdapter: GalleryAlbumRecyclerAdapter
    lateinit var fragment_view: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragment_view =
            LayoutInflater.from(mContext).inflate(R.layout.fragment_gallery_album, container, false)


        object : AsyncTask<Void, Void, ArrayList<GalleryAlbum>>() {

            override fun onPreExecute() {
                super.onPreExecute()

                fragment_view.progressBar.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg params: Void?): ArrayList<GalleryAlbum> {
                return loadPhotoAlbums()
            }

            override fun onPostExecute(arrayList: ArrayList<GalleryAlbum>?) {
                super.onPostExecute(arrayList)

                fragment_view.progressBar.visibility = View.GONE
                mAlbums = arrayList!!
                mAdapter = GalleryAlbumRecyclerAdapter(
                    mContext,
                    mAlbums,
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
                fragment_view.listView.layoutManager =
                    GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false)
                fragment_view.listView.adapter = mAdapter

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null)

        return fragment_view
    }

    fun loadPhotoAlbums(): ArrayList<GalleryAlbum> {

        var r0 = LinkedHashMap<Long, GalleryAlbum>()
        var r4: Array<String> =
            arrayOf("_id", "_data", "bucket_id", "bucket_display_name", "datetaken")
        var r3: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var r2: Cursor? = mContext.contentResolver.query(r3, r4, null, null, "date_added DESC")
        var arrayList: ArrayList<GalleryAlbum> = java.util.ArrayList<GalleryAlbum>()
        if (r2 != null) {
            if (r2.moveToFirst()) {

                do {
                    var r1: String = r2.getString(r2.getColumnIndex("bucket_display_name"))
                    var r3: Long = r2.getLong(r2.getColumnIndex("datetaken"))
                    var r5: String = r2.getString(r2.getColumnIndex("_data"))
                    var r6: Long = r2.getLong(r2.getColumnIndex("bucket_id"))

                    var r8: GalleryAlbum? = r0.get(r6)
                    if (r8 == null) {
                        r8 = GalleryAlbum(r6, r1)
                        r8.mTakenDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(r3)
                        r8.mImageList.add(r5)
                        r0.put(r6, r8)
                    }else{
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

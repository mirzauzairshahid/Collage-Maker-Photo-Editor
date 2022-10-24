package com.photoeditor.photoeffect

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.photoeditor.photoeffect.adapter.SelectedPhotoAdapter
import com.photoeditor.photoeffect.fragments.GalleryAlbumFragment
import com.photoeditor.photoeffect.fragments.GalleryAlbumImageFragment
import kotlinx.android.synthetic.main.activity_select_image.*
import java.io.File
import java.lang.Exception
import java.util.ArrayList

class SelectImageActivity : AppCompatActivity(), GalleryAlbumImageFragment.OnSelectImageListener,
    SelectedPhotoAdapter.OnDeleteButtonClickListener {


    override fun onDeleteButtonClick(str: String) {

        mSelectedImages.remove(str)
        mSelectedPhotoAdapter.notifyDataSetChanged()
        val textView = text_imgcount
        val str2 = "Select upto 10 photo(s)"
        val sb = StringBuilder()
        sb.append("(")
        sb.append(this.mSelectedImages.size)
        sb.append(")")
        textView.setText(str2 + sb.toString())
    }

    private val mSelectedImages = ArrayList<String>()
    private var maxIamgeCount = 10
    private lateinit var mSelectedPhotoAdapter: SelectedPhotoAdapter
    private var mLastClickTime: Long = 0
    fun checkClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }


    override fun onSelectImage(str: String) {
        if (str != null) {
            if (this.mSelectedImages.size == this.maxIamgeCount) {
                Toast.makeText(
                    this,
                    String.format("You only need %d photo(s)", maxIamgeCount),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                var uri = Uri.fromFile(File(str))

                this.mSelectedImages.add(str)
                this.mSelectedPhotoAdapter.notifyDataSetChanged()
                val textView = text_imgcount
                val str2 = "Select upto 10 photo(s)"
                val sb = StringBuilder()
                sb.append("(")
                sb.append(this.mSelectedImages.size)
                sb.append(")")
                textView.setText(str2 + sb.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_image)

        mSelectedPhotoAdapter = SelectedPhotoAdapter(mSelectedImages, this)

        list_images.hasFixedSize()
        list_images.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list_images.adapter = mSelectedPhotoAdapter

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, GalleryAlbumFragment(this)).commit()

        btn_next.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                checkClick()
                createCollage()
            }
        })
    }

    fun createCollage() {
        if (mSelectedImages.size == 0) {
            Toast.makeText(this, "Please select photo(s)", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            var intent = Intent(this, CollageActivity::class.java)
            intent.putExtra("imageCount", mSelectedImages.size)
            intent.putExtra("selectedImages", mSelectedImages)
            intent.putExtra("imagesinTemplate", mSelectedImages.size)

            startActivityForResult(intent, 111)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == 111) {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

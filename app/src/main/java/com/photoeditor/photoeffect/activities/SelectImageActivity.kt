package com.photoeditor.photoeffect.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.photoeditor.photoeffect.R
import com.photoeditor.photoeffect.adapter.SelectedPhotoAdapter
import com.photoeditor.photoeffect.databinding.ActivitySelectImageBinding
import com.photoeditor.photoeffect.fragments.GalleryAlbumFragment
import com.photoeditor.photoeffect.fragments.GalleryAlbumImageFragment
import java.lang.Exception
import java.util.ArrayList

class SelectImageActivity : AppCompatActivity(), GalleryAlbumImageFragment.OnSelectImageListener,
    SelectedPhotoAdapter.OnDeleteButtonClickListener {


    override fun onDeleteButtonClick(str: String) {

        mSelectedImages.remove(str)
        mSelectedPhotoAdapter.notifyDataSetChanged()

        val str2 = "Select upto 10 photo(s)"
        val sb = StringBuilder()
        sb.append("(")
        sb.append(this.mSelectedImages.size)
        sb.append(")")
        binding.textImgcount.text = str2 + sb.toString()
    }

    private val mSelectedImages = ArrayList<String>()
    private var maxIamgeCount = 10
    private lateinit var mSelectedPhotoAdapter: SelectedPhotoAdapter



    override fun onSelectImage(str: String) {
        if (this.mSelectedImages.size == this.maxIamgeCount) {
            Toast.makeText(
                this,
                String.format("You only need %d photo(s)", maxIamgeCount),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        else {
            this.mSelectedImages.add(str)
            this.mSelectedPhotoAdapter.notifyDataSetChanged()
            val str2 = "Select upto 10 photo(s)"
            val sb = StringBuilder()
            sb.append("(")
            sb.append(this.mSelectedImages.size)
            sb.append(")")
            binding.textImgcount.text =  str2 + sb.toString()
        }
    }

    private val binding by lazy {
        ActivitySelectImageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mSelectedPhotoAdapter = SelectedPhotoAdapter(mSelectedImages, this)

        binding.listImages.hasFixedSize()
        binding.listImages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.listImages.adapter = mSelectedPhotoAdapter

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, GalleryAlbumFragment(this)).commit()

        binding.btnNext.setOnClickListener {
            createCollage()
        }
    }

    fun createCollage() {
        if (mSelectedImages.size == 0) {
            Toast.makeText(this, "Please select photo(s)", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val intent = Intent(this, CollageActivity::class.java)
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
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

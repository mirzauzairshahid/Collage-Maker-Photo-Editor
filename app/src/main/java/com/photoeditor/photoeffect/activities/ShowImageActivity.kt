package com.photoeditor.photoeffect.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.photoeditor.photoeffect.BuildConfig
import com.photoeditor.photoeffect.R
import com.photoeditor.photoeffect.databinding.ActivityShowImageBinding
import com.photoeditor.photoeffect.extensions.isPackageInstalled
import java.io.File

class ShowImageActivity : AppCompatActivity() {

    private var savedFile: File? = null

    private val binding by lazy {
        ActivityShowImageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        val imageUri = intent.getStringExtra("image_uri")

        savedFile = File(imageUri!!)

        val display = resources.displayMetrics
        val dWidth = display!!.widthPixels

        val layoutParams = RelativeLayout.LayoutParams(dWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        binding.imgShow.layoutParams = layoutParams

        binding.imgShow.setImageURI(Uri.parse(imageUri))

        setButtonClicks()
    }

    private fun setButtonClicks() {
        binding.whatsappShare.setOnClickListener {
            shareImageNow("com.whatsapp", "Whatsapp")
        }
        binding.facebookShare.setOnClickListener {
            shareImageNow("com.facebook.katana", "Facebook")
        }
        binding.instagramShare.setOnClickListener {
            shareImageNow("com.instagram.android", "Instagram")
        }
        binding.messangerShare.setOnClickListener {
            shareImageNow("com.facebook.orca", "Facebook Messenger")
        }
        binding.twitterShare.setOnClickListener {
            shareImageNow("com.twitter.android", "Twitter")
        }
        binding.shareMore.setOnClickListener {
            shareImageNow(null, "NONE")
        }
        binding.imgFolder.setOnClickListener {
            startActivity(Intent(this, MyCreationActivity::class.java))
            finish()
        }
    }

    private fun shareImageNow(pkg: String?, appName: String) {
        val share = Intent("android.intent.action.SEND")

        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        share.putExtra(
            "android.intent.extra.STREAM",
            FileProvider.getUriForFile(
                this@ShowImageActivity,
                BuildConfig.APPLICATION_ID + ".provider",
                savedFile!!
            )
        )
        share.type = "image/*"

        if (pkg == null) {
            startActivity(Intent.createChooser(share, "Share Image"))
            return
        } else if (isPackageInstalled(pkg)) {
            share.putExtra(Intent.EXTRA_TEXT, getString(R.string.txt_share))
            share.setPackage(pkg)
            startActivity(Intent.createChooser(share, "Share Image"))
        } else
            Toast.makeText(applicationContext, "Please Install $appName", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}

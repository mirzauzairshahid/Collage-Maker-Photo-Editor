package com.photoeditor.photoeffect

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class ShowImageActivity : AppCompatActivity(), View.OnClickListener {

    private var image_uri: String? = null
    private var img_show: ImageView? = null
    private var saved_file: File? = null
    private var density: Float = 0.toFloat()
    internal var D_height: Int = 0
    internal var D_width: Int = 0
    private var display: DisplayMetrics? = null

    private var mLastClickTime: Long = 0
    fun checkClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_show_image)

        image_uri = intent.getStringExtra("image_uri")

        saved_file = File(image_uri!!)
        img_show = findViewById<View>(R.id.img_show) as ImageView

        display = resources.displayMetrics
        density = resources.displayMetrics.density
        D_width = display!!.widthPixels
        D_height = (display!!.heightPixels.toFloat() - density * 150.0f).toInt()

        val layoutParams = RelativeLayout.LayoutParams(D_width, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        img_show!!.layoutParams = layoutParams

        img_show!!.setImageURI(Uri.parse(image_uri))

        findViewById<View>(R.id.whatsapp_share).setOnClickListener(this)
        findViewById<View>(R.id.facebook_share).setOnClickListener(this)
        findViewById<View>(R.id.instagram_share).setOnClickListener(this)
        findViewById<View>(R.id.messanger_share).setOnClickListener(this)
        findViewById<View>(R.id.twitter_share).setOnClickListener(this)
        findViewById<View>(R.id.share_more).setOnClickListener(this)
        findViewById<View>(R.id.img_folder).setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.whatsapp_share -> {
                checkClick()
                shareImageSocialApp("com.whatsapp", "Whatsapp")

            }
            R.id.instagram_share -> {
                checkClick()
                shareImageSocialApp("com.instagram.android", "Instagram")
            }
            R.id.twitter_share -> {
                checkClick()
                shareImageSocialApp("com.twitter.android", "Twitter")
            }
            R.id.messanger_share -> {
                checkClick()
                shareImageSocialApp("com.facebook.orca", "Facebook Messanger")
            }
            R.id.share_more -> {
                checkClick()
                val share = Intent("android.intent.action.SEND")
                share.type = "image/*"
                share.putExtra(
                    "android.intent.extra.STREAM",
                    FileProvider.getUriForFile(
                        this@ShowImageActivity,
                        BuildConfig.APPLICATION_ID + ".provider",
                        saved_file!!
                    )
                )
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                //                share.putExtra("android.intent.extra.STREAM", image_uri);
                startActivity(Intent.createChooser(share, "Share Image"))
            }
            R.id.facebook_share -> {
                checkClick()
                shareImageSocialApp("com.facebook.katana", "Facebook")
            }

            R.id.img_folder -> {
                checkClick()
                var intent = Intent(this, MyCreationActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun shareImageSocialApp(pkg: String, appName: String) {
        val share = Intent("android.intent.action.SEND")

        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        share.putExtra(
            "android.intent.extra.STREAM",
            FileProvider.getUriForFile(
                this@ShowImageActivity,
                BuildConfig.APPLICATION_ID + ".provider",
                saved_file!!
            )
        )
        share.putExtra(Intent.EXTRA_TEXT,getString(R.string.txt_share))

        //        share.putExtra("android.intent.extra.STREAM", Uri.fromFile(saved_file));

        share.type = "image/*"
        if (isPackageInstalled(pkg, this)) {
            share.setPackage(pkg)
            startActivity(Intent.createChooser(share, "Share Image"))
            return
        }
        Toast.makeText(applicationContext, "Please Install $appName", Toast.LENGTH_LONG).show()
    }

    private fun isPackageInstalled(packagename: String, context: Context): Boolean {
        try {
            context.packageManager.getPackageInfo(packagename, PackageManager.GET_META_DATA)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}

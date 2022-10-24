package com.photoeditor.photoeffect

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.photoeditor.photoeffect.SplashActivity.Companion.isFromSplash
import com.photoeditor.photoblur.AdLoader
import com.vorlonsoft.android.rate.AppRate
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var PICK_IMAGE: Int = 111
    var CAMERA_REQUEST: Int = 123

    lateinit var gallary_images: ArrayList<String>
    lateinit var adapter: ImageAdapter

    companion object {
        var isFromSaved: Boolean = true
    }

    fun ImagesPath(): ArrayList<String> {
        var uri: Uri
        var cursor: Cursor
        var column_index_data: Int

        var listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        var projection = arrayOf(MediaStore.MediaColumns.DATA)

        cursor =
            contentResolver.query(
                uri,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_TAKEN + " DESC"
            )!!

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }

    lateinit var timer: Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        AppRate.with(this)
            .setInstallDays(2.toByte()) // default is 10, 0 means install day, 10 means app is launched 10 or more days later than installation
            .setLaunchTimes(2.toByte()) // default is 10, 3 means app is launched 3 or more times
            .setRemindInterval(1.toByte()) // default is 1, 1 means app is launched 1 or more days after neutral button clicked
            .setRemindLaunchesNumber(1.toByte()) // default is 0, 1 means app is launched 1 or more times after neutral button clicked
            .monitor() // Monitors the app launch times

        AdLoader.ads.loadFullAdFacebook(this)
        AdLoader.ads.loadfullAdAdmob(this)





        if (isFromSplash) {
        //    llProgressBar.visibility = View.VISIBLE
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            Handler().postDelayed(object : Runnable {
                override fun run() {
                    isFromSplash = false
               //     llProgressBar.visibility = View.GONE
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }, 1000)
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (chechkPermission()) {
                setAdapter()
            } else {
                requestPermission()
            }
        } else {
            setAdapter()
        }
        btn_select.setOnClickListener(this)
        btn_collage.setOnClickListener(this)
        btn_camera.setOnClickListener(this)
        img_share.setOnClickListener(this)
        img_rate.setOnClickListener(this)
        img_creation.setOnClickListener(this)
        img_back.setOnClickListener(this)
        img_next.setOnClickListener(this)
    }

    fun setAdapter() {

        gallary_images = ArrayList<String>()
        gallary_images.clear()

        gallary_images = ImagesPath()
        adapter = ImageAdapter()
        pager_images.adapter = adapter

        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                pager_images.post(object : Runnable {
                    override fun run() {
                        pager_images.setCurrentItem(
                            (pager_images.currentItem + 1) % gallary_images.size,
                            true
                        )
                    }
                })
            }
        }, 4000, 4000)
    }

    private fun chechkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            100
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission", "Granted")
                setAdapter()
            } else {
                Log.e("Permission", "Denied")

                requestPermission()
            }
        }
    }

    private var mLastClickTime: Long = 0
    fun checkClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    private var mCapturedImageUri: Uri? = null

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_select -> {
                checkClick()

                var intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_PICK
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
            }

            R.id.btn_collage -> {
                checkClick()
                var intent = Intent(this, SelectImageActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_camera -> {

                checkClick()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(packageManager) != null) {
                    var photofile: File? = null

                    try {
                        val capturedPath = "image_" + System.currentTimeMillis() + ".jpg"
                        photofile = File(
                            Environment.getExternalStorageDirectory().absolutePath + "/DCIM",
                            capturedPath
                        )
                        photofile.parentFile.mkdirs()
                        mCapturedImageUri = Uri.fromFile(photofile)
                    } catch (e: java.lang.Exception) {

                    }
                    if (photofile != null) {
                        mCapturedImageUri = FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photofile
                        )
                    }
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageUri)
                startActivityForResult(intent, CAMERA_REQUEST)

            }
            R.id.img_share -> {
                checkClick()
                try {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "text/plain"
                    i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                    val shareMessage = getString(R.string.txt_share)
                    i.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(i, "Share App Via"))
                } catch (e: Exception) {
                    //e.toString();
                }
            }
            R.id.img_rate -> {
                checkClick()
                try {
                    val uri = Uri.parse("market://details?id=$packageName")
                    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                        )
                    )
                }
            }

            R.id.img_creation -> {
                checkClick()
                var intent = Intent(this, MyCreationActivity::class.java)
                startActivity(intent)
            }

            R.id.img_back -> {
                checkClick()
                timer.cancel()
                if (pager_images.currentItem <= 0) {
                    pager_images.setCurrentItem(
                        gallary_images.size,
                        true
                    )
                } else {
                    pager_images.setCurrentItem(
                        (pager_images.currentItem - 1) % gallary_images.size,
                        true
                    )
                }
            }
            R.id.img_next -> {
                checkClick()
                timer.cancel()

                pager_images.setCurrentItem(
                    (pager_images.currentItem + 1) % gallary_images.size,
                    true
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            if (data != null) {
                try {
                    var uri: Uri = data.data!!

                    var intent = Intent(this, ImageEditActivity::class.java)
                    intent.putExtra("image_uri", uri.toString())
                    startActivity(intent)


                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST) {
            if (mCapturedImageUri != null) {
                var intent = Intent(this, ImageEditActivity::class.java)
                intent.putExtra("image_uri", mCapturedImageUri.toString())
                startActivity(intent)
            }
        }
    }

    inner class ImageAdapter : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var view: View =
                LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.item_slider, container, false)
            var img_slider: ImageView = view.findViewById(R.id.img_slider) as ImageView

       /*     Picasso.with(this@MainActivity)
                .load("http://i.imgur.com/DvpvklR.png")
                .fit()
                .into(img_slider)
*/

            Glide.with(this@MainActivity)
                .asBitmap()
                .apply(RequestOptions.circleCropTransform())
                .load(gallary_images[position])
                .into(img_slider)

            (container as ViewPager).addView(view)

            img_slider.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    checkClick()
                    var uri = Uri.fromFile(File(gallary_images[position]))
                    var intent = Intent(this@MainActivity, ImageEditActivity::class.java)
                    intent.putExtra("image_uri", uri.toString())
                    startActivity(intent)
                }
            })
            return view
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return (view == `object` as View)
        }

        override fun getCount(): Int {
            return gallary_images.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            (container as ViewPager).removeView(`object` as View)
        }
    }

    override fun onBackPressed() {
        var intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

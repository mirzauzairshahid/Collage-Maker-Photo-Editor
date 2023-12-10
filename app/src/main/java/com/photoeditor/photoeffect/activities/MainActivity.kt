package com.photoeditor.photoeffect.activities

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
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
import com.photoeditor.photoeffect.BuildConfig
import com.photoeditor.photoeffect.R
import com.photoeditor.photoeffect.activities.SplashActivity.Companion.isFromSplash
import com.photoeditor.photoeffect.databinding.ActivityMainBinding
import com.vorlonsoft.android.rate.AppRate
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


        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.MediaColumns.DATA)

        val cursor = contentResolver.query(
            uri,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_TAKEN + " DESC"
        )

        val column_index_data = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        while (cursor?.moveToNext() == true) {
            absolutePathOfImage = cursor.getString(column_index_data!!)
            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }

    lateinit var timer: Timer

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        AppRate.with(this)
            .setInstallDays(2.toByte()) // default is 10, 0 means install day, 10 means app is launched 10 or more days later than installation
            .setLaunchTimes(2.toByte()) // default is 10, 3 means app is launched 3 or more times
            .setRemindInterval(1.toByte()) // default is 1, 1 means app is launched 1 or more days after neutral button clicked
            .setRemindLaunchesNumber(1.toByte()) // default is 0, 1 means app is launched 1 or more times after neutral button clicked
            .monitor() // Monitors the app launch times


        if (isFromSplash) {
            //    llProgressBar.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            Handler(Looper.getMainLooper()).postDelayed({
                isFromSplash = false
                //     llProgressBar.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }, 1000)
        }

        if (Build.VERSION.SDK_INT < 23) {
            setAdapter()
        } else if (chechkPermission()) {
            setAdapter()
        } else {
            requestPermission()
        }


        binding.btnSelect.setOnClickListener(this)
        binding.btnCollage.setOnClickListener(this)
        binding.btnCamera.setOnClickListener(this)
        binding.imgShare.setOnClickListener(this)
        binding.imgRate.setOnClickListener(this)
        binding.imgCreation.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.imgNext.setOnClickListener(this)
    }

    fun setAdapter() {

        gallary_images = ArrayList()
        gallary_images.clear()

        gallary_images = ImagesPath()
        adapter = ImageAdapter()
        binding.pagerImages.adapter = adapter

        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                binding.pagerImages.post {
                    binding.pagerImages.setCurrentItem(
                        (binding.pagerImages.currentItem + 1) % gallary_images.size,
                        true
                    )
                }
            }
        }, 4000, 4000)
    }

    private fun chechkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

        } else ContextCompat.checkSelfPermission(
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.CAMERA
                )
            else
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
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission", "Granted")
                setAdapter()
            } else {
                Log.e("Permission", "Denied")

                requestPermission()
            }
        }
    }

    private var mCapturedImageUri: Uri? = null

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_select -> {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_PICK
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
            }

            R.id.btn_collage -> {
                startActivity(Intent(this, SelectImageActivity::class.java))
            }

            R.id.btn_camera -> {
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
                    } catch (_: java.lang.Exception) {

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
                startActivity(Intent(this, MyCreationActivity::class.java))
            }

            R.id.img_back -> {
                timer.cancel()
                if (binding.pagerImages.currentItem <= 0) {
                    binding.pagerImages.setCurrentItem(
                        gallary_images.size,
                        true
                    )
                } else {
                    binding.pagerImages.setCurrentItem(
                        (binding.pagerImages.currentItem - 1) % gallary_images.size,
                        true
                    )
                }
            }

            R.id.img_next -> {
                timer.cancel()

                binding.pagerImages.setCurrentItem(
                    (binding.pagerImages.currentItem + 1) % gallary_images.size,
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
                    val uri: Uri = data.data!!

                    val intent = Intent(this, ImageEditActivity::class.java)
                    intent.putExtra("image_uri", uri.toString())
                    startActivity(intent)


                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST) {
            if (mCapturedImageUri != null) {
                val intent = Intent(this, ImageEditActivity::class.java)
                intent.putExtra("image_uri", mCapturedImageUri.toString())
                startActivity(intent)
            }
        }
    }

    inner class ImageAdapter : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view: View =
                LayoutInflater.from(this@MainActivity)
                    .inflate(R.layout.item_slider, container, false)
            val img_slider: ImageView = view.findViewById(R.id.img_slider) as ImageView

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

            img_slider.setOnClickListener {
                val uri = Uri.fromFile(File(gallary_images[position]))
                val intent = Intent(this@MainActivity, ImageEditActivity::class.java)
                intent.putExtra("image_uri", uri.toString())
                startActivity(intent)
            }
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
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}

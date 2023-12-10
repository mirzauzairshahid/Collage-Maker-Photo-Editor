package com.photoeditor.photoeffect.activities

import android.content.Intent
import android.graphics.*
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.photoeditor.photoeffect.AndroidUtils
import com.photoeditor.photoeffect.R
import com.photoeditor.photoeffect.activities.MainActivity.Companion.isFromSaved
import com.photoeditor.photoeffect.adapter.FilterNameAdapter
import com.photoeditor.photoeffect.databinding.ActivityFilterCollageBinding
import com.photoeditor.photoeffect.model.FilterData
import java.io.File
import java.io.FileOutputStream
import java.util.*


class FilterCollageActivity : AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_save -> {
                isFromSaved = true
                try {
                    saveBitmap(screenShot)
                } catch (th: Throwable) {
                    th.printStackTrace()
                }
                val intent = Intent(this, ShowImageActivity::class.java)
                intent.putExtra("image_uri", savedImageUri!!.toString())
                startActivityForResult(intent, 2)
                finish()
            }
        }
    }

    val screenShot: Bitmap
        get() {
            val findViewById = findViewById<View>(R.id.img_collage)
            findViewById.background = null
            findViewById.destroyDrawingCache()
            findViewById.isDrawingCacheEnabled = true
            val createBitmap = Bitmap.createBitmap(findViewById.drawingCache)
            findViewById.isDrawingCacheEnabled = false
            val createBitmap2 = Bitmap.createBitmap(
                createBitmap.width,
                createBitmap.height,
                Bitmap.Config.ARGB_8888
            )
            findViewById.draw(Canvas(createBitmap2))
            return createBitmap2
        }

    private var savedImageUri: Uri? = null

    fun saveBitmap(bitmap: Bitmap) {
        val mainDir = File(Environment.getExternalStorageDirectory(), "ArtisticEditor")
        if (!mainDir.exists()) {
            if (mainDir.mkdir())
                Log.e("Create Directory", "Main Directory Created : $mainDir")
        }
        val now = Date()
        val fileName = (now.time / 1000).toString() + ".png"

        val file = File(mainDir.absolutePath, fileName)
        try {
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
            fOut.flush()
            fOut.close()

            savedImageUri = Uri.parse(file.path)

            MediaScannerConnection.scanFile(this, arrayOf(file.absolutePath), null) { path, uri ->
                Log.i("ExternalStorage", "Scanned $path:")
                Log.i("ExternalStorage", "-> uri=$uri")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    companion object {
        var red: Float = 0F
        var green: Float = 0F
        var blue: Float = 0F
        var saturation: Float = 0F
    }

    lateinit var bmp: Bitmap

    private val binding by lazy {
        ActivityFilterCollageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bitmapPath = this.cacheDir.absolutePath + "/tempBMP"
        bmp = BitmapFactory.decodeFile(bitmapPath)

        binding.imgCollage.setImageBitmap(bmp)

        binding.imgSave.setOnClickListener(this)
        binding.listFilterstype.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_clr1)
        binding.listFilterstype.adapter = filter_typeAdapter

        binding.filterNames.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val filter_nameAdapter = FilterNameAdapter(this, resources.getStringArray(R.array.filters))

        filter_nameAdapter.setOnFilterNameClick(object : FilterNameAdapter.FilterNameClickListener {
            override fun onItemClick(view: View, position: Int) {

                val filters = when (position) {
                    0 -> {
                        AndroidUtils.filter_clr1
                    }

                    1 -> {
                        AndroidUtils.filter_clr2
                    }

                    2 -> {
                        AndroidUtils.filter_duo
                    }

                    3 -> {
                        AndroidUtils.filter_pink
                    }

                    4 -> {
                        AndroidUtils.filter_fresh
                    }

                    5 -> {
                        AndroidUtils.filter_euro
                    }

                    6 -> {
                        AndroidUtils.filter_dark
                    }

                    7 -> {
                        AndroidUtils.filter_ins
                    }

                    8 -> {
                        AndroidUtils.filter_elegant
                    }

                    9 -> {
                        AndroidUtils.filter_golden
                    }

                    10 -> {
                        AndroidUtils.filter_tint
                    }

                    11 -> {
                        AndroidUtils.filter_film
                    }

                    12 -> {
                        AndroidUtils.filter_lomo
                    }

                    13 -> {
                        AndroidUtils.filter_movie
                    }

                    14 -> {
                        AndroidUtils.filter_retro
                    }

                    15 -> {
                        AndroidUtils.filter_bw
                    }

                    else -> {
                        AndroidUtils.filter_clr1
                    }
                }


                filter_typeAdapter = FilterDetailAdapter(filters)
                binding.listFilterstype.adapter = filter_typeAdapter

                filter_nameAdapter.notifyDataSetChanged()
                filter_typeAdapter.notifyDataSetChanged()
            }
        })

        binding.filterNames.adapter = filter_nameAdapter

    }

    inner class FilterDetailAdapter(filters: Array<FilterData>) :
        RecyclerView.Adapter<FilterDetailAdapter.FilterDetailHolder>() {
        var filterType = filters
        var selectedindex = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterDetailHolder {
            var view = LayoutInflater.from(this@FilterCollageActivity)
                .inflate(R.layout.item_filter, parent, false)
            return FilterDetailHolder(view)
        }

        override fun getItemCount(): Int {
            return filterType.size
        }

        override fun onBindViewHolder(holder: FilterDetailHolder, position: Int) {

            if (selectedindex == position) {
                holder.rl_filteritem.setBackgroundColor(resources.getColor(R.color.colorAccent))
            } else {
                holder.rl_filteritem.setBackgroundColor(resources.getColor(R.color.transparent))
            }

            holder.thumbnail_filter.setImageResource(R.drawable.thumb_filter)

            red = filterType[position].red
            green = filterType[position].green
            blue = filterType[position].blue
            saturation = filterType[position].saturation

            var bitmap = Bitmap.createBitmap(
                bmp.getWidth(),
                bmp.getHeight(),
                Bitmap.Config.ARGB_8888
            )
            var canvas = Canvas(bitmap)

            var paint = Paint()
            var colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(saturation)

            var colorScale = ColorMatrix()
            colorScale.setScale(
                red,
                green,
                blue, 1F
            )
            colorMatrix.postConcat(colorScale)

            paint.setColorFilter(ColorMatrixColorFilter(colorMatrix))
            canvas.drawBitmap(bmp, 0F, 0F, paint)

            holder.thumbnail_filter.setImageBitmap(bitmap)

            holder.filterName.setText(filterType[position].text)

            holder.rl_filteritem.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    selectedindex = position

                    red = filterType[position].red
                    green = filterType[position].green
                    blue = filterType[position].blue
                    saturation = filterType[position].saturation

                    Async_Filter(
                        bmp,
                        binding.imgCollage
                    ).executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR,
                        red,
                        green,
                        blue
                    )
                    notifyDataSetChanged()
                }
            })
        }

        inner class FilterDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var thumbnail_filter: ImageView
            var filterName: TextView
            var rl_filteritem: RelativeLayout

            init {
                thumbnail_filter = itemView.findViewById(R.id.thumbnail_filter)
                filterName = itemView.findViewById(R.id.filterName)
                rl_filteritem = itemView.findViewById(R.id.rl_filteritem)
            }
        }
    }

    class Async_Filter() : AsyncTask<Float, Void, Bitmap>() {

        lateinit var originalBitmap: Bitmap
        lateinit var imgMain: ImageView

        constructor(originalBitmap: Bitmap, imgMain: ImageView) : this() {
            this.originalBitmap = originalBitmap
            this.imgMain = imgMain
        }

        override fun doInBackground(vararg params: Float?): Bitmap {
            var r = params[0]
            var g = params[1]
            var b = params[2]

            var bitmap = Bitmap.createBitmap(
                this.originalBitmap.getWidth(),
                this.originalBitmap.getHeight(),
                Bitmap.Config.ARGB_8888
            )
            var canvas = Canvas(bitmap)

            var paint = Paint()
            var colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(saturation)

            var colorScale = ColorMatrix()
            colorScale.setScale(r!!, g!!, b!!, 1F)
            colorMatrix.postConcat(colorScale)

            paint.setColorFilter(ColorMatrixColorFilter(colorMatrix))
            canvas.drawBitmap(this.originalBitmap, 0F, 0F, paint)

            return bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)

            this.imgMain.setImageBitmap(result)

        }
    }

}

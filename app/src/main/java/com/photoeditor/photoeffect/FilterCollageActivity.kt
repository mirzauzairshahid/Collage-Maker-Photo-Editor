package com.photoeditor.photoeffect

import android.content.Intent
import android.graphics.*
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.photoeditor.photoeffect.MainActivity.Companion.isFromSaved
import com.photoeditor.photoeffect.adapter.FilterNameAdapter
import com.photoeditor.photoeffect.model.FilterData
import kotlinx.android.synthetic.main.activity_filter_collage.*
import kotlinx.android.synthetic.main.activity_filter_collage.filter_names
import kotlinx.android.synthetic.main.activity_filter_collage.list_filterstype
import java.io.File
import java.io.FileOutputStream
import java.util.*


class FilterCollageActivity : AppCompatActivity(), View.OnClickListener {

    private var mLastClickTime: Long = 0
    fun checkClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_save -> {
                checkClick()
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_collage)

        val bitmapPath = this.cacheDir.absolutePath + "/tempBMP"
        bmp = BitmapFactory.decodeFile(bitmapPath)

        img_collage.setImageBitmap(bmp)

        img_save.setOnClickListener(this)
        list_filterstype.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_clr1)
        list_filterstype.adapter = filter_typeAdapter

        filter_names.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var filter_nameAdapter = FilterNameAdapter(this, resources.getStringArray(R.array.filters))

        filter_nameAdapter.setOnFilterNameClick(object : FilterNameAdapter.FilterNameClickListener {
            override fun onItemClick(view: View, position: Int) {

                if (position == 0) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_clr1)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 1) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_clr2)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 2) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_duo)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 3) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_pink)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 4) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_fresh)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 5) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_euro)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 6) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_dark)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 7) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_ins)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 8) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_elegant)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 9) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_golden)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 10) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_tint)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 11) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_film)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 12) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_lomo)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 13) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_movie)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 14) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_retro)
                    list_filterstype.adapter = filter_typeAdapter
                } else if (position == 15) {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_bw)
                    list_filterstype.adapter = filter_typeAdapter
                } else {
                    filter_typeAdapter = FilterDetailAdapter(AndroidUtils.filter_clr1)
                    list_filterstype.adapter = filter_typeAdapter
                }
                filter_nameAdapter.notifyDataSetChanged()
                filter_typeAdapter.notifyDataSetChanged()
            }
        })

        filter_names.adapter = filter_nameAdapter

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
                        img_collage
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

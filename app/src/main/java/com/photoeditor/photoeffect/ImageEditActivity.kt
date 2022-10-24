package com.photoeditor.photoeffect

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isseiaoki.simplecropview.CropImageView
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import kotlinx.android.synthetic.main.activity_image_edit.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.photoeditor.photoeffect.AndroidUtils.filter_bw
import com.photoeditor.photoeffect.AndroidUtils.filter_clr1
import com.photoeditor.photoeffect.AndroidUtils.filter_clr2
import com.photoeditor.photoeffect.AndroidUtils.filter_dark
import com.photoeditor.photoeffect.AndroidUtils.filter_duo
import com.photoeditor.photoeffect.AndroidUtils.filter_elegant
import com.photoeditor.photoeffect.AndroidUtils.filter_euro
import com.photoeditor.photoeffect.AndroidUtils.filter_film
import com.photoeditor.photoeffect.AndroidUtils.filter_fresh
import com.photoeditor.photoeffect.AndroidUtils.filter_golden
import com.photoeditor.photoeffect.AndroidUtils.filter_ins
import com.photoeditor.photoeffect.AndroidUtils.filter_lomo
import com.photoeditor.photoeffect.AndroidUtils.filter_movie
import com.photoeditor.photoeffect.AndroidUtils.filter_pink
import com.photoeditor.photoeffect.AndroidUtils.filter_retro
import com.photoeditor.photoeffect.AndroidUtils.filter_tint
import com.photoeditor.photoeffect.adapter.*
import com.photoeditor.photoeffect.model.FilterData
import com.photoeditor.photoeffect.model.EffectData
import com.photoeditor.photoeffect.stickerview.StickerImageView
import com.photoeditor.photoeffect.stickerview.StickerTextView
import com.photoeditor.photoeffect.stickerview.StickerView
import com.photoeditor.photoblur.AdLoader
import java.io.*
import java.lang.NullPointerException
import java.util.*


class ImageEditActivity : AppCompatActivity(), View.OnClickListener,
    ResizeAdapter.OnResizeClickListener {

    companion object {
        var selectedPosition: Int = 0
        var adjust_position: Int = 0
        var red: Float = 0F
        var green: Float = 0F
        var blue: Float = 0F
        var saturation: Float = 0F
        lateinit var blend_bitmap: Bitmap
    }

    var PICK_IMAGE: Int = 111

    var light1_array: Array<EffectData> = arrayOf(
        EffectData("Light1_1", R.drawable.light1_1),
        EffectData("Light1_2", R.drawable.light1_2),
        EffectData("Light1_3", R.drawable.light1_3),
        EffectData("Light1_4", R.drawable.light1_4),
        EffectData("Light1_5", R.drawable.light1_5),
        EffectData("Light1_6", R.drawable.light1_6),
        EffectData("Light1_7", R.drawable.light1_7),
        EffectData("Light1_8", R.drawable.light1_8),
        EffectData("Light1_8", R.drawable.light1_9)
    )

    var light2_array: Array<EffectData> = arrayOf(
        EffectData("Light2_1", R.drawable.light2_1),
        EffectData("Light2_2", R.drawable.light2_2),
        EffectData("Light2_3", R.drawable.light2_3),
        EffectData("Light2_4", R.drawable.light2_4),
        EffectData("Light2_4", R.drawable.light2_5),
        EffectData("Light2_4", R.drawable.light2_6),
        EffectData("Light2_4", R.drawable.light2_7),
        EffectData("Light2_5", R.drawable.light2_8)
    )

    var festival_array: Array<EffectData> = arrayOf(
        EffectData("festival_1", R.drawable.festival_1),
        EffectData("festival_2", R.drawable.festival_2),
        EffectData("festival_3", R.drawable.festival_3),
        EffectData("festival_4", R.drawable.festival_4),
        EffectData("festival_5", R.drawable.festival_5),
        EffectData("festival_6", R.drawable.festival_6)
    )

    var love_array: Array<EffectData> = arrayOf(
        EffectData("love_1", R.drawable.love_1),
        EffectData("love_2", R.drawable.love_2),
        EffectData("love_3", R.drawable.love_3),
        EffectData("love_4", R.drawable.love_4),
        EffectData("love_5", R.drawable.love_5)
    )

    var prism_array: Array<EffectData> = arrayOf(
        EffectData("prism_1", R.drawable.prism_1),
        EffectData("prism_2", R.drawable.prism_2),
        EffectData("prism_3", R.drawable.prism_3),
        EffectData("prism_4", R.drawable.prism_4),
        EffectData("prism_5", R.drawable.prism_5)
    )

    var neon_array: Array<EffectData> = arrayOf(
        EffectData("neon_1", R.drawable.neon_1),
        EffectData("neon_2", R.drawable.neon_2),
        EffectData("neon_3", R.drawable.neon_3),
        EffectData("neon_4", R.drawable.neon_4),
        EffectData("neon_5", R.drawable.neon_5)
    )

    var dust_array: Array<EffectData> = arrayOf(
        EffectData("Dust_1", R.drawable.dust_1),
        EffectData("Dust_2", R.drawable.dust_2),
        EffectData("Dust_3", R.drawable.dust_3),
        EffectData("Dust_4", R.drawable.dust_4),
        EffectData("Dust_5", R.drawable.dust_5)
    )
    var scratch_array: Array<EffectData> = arrayOf(
        EffectData("scratch_1", R.drawable.scratch_1),
        EffectData("scratch_2", R.drawable.scratch_2),
        EffectData("scratch_3", R.drawable.scratch_3),
        EffectData("scratch_4", R.drawable.scratch_4),
        EffectData("scratch_5", R.drawable.scratch_5)
    )

    var stain_array: Array<EffectData> = arrayOf(
        EffectData("stain_1", R.drawable.stain_1),
        EffectData("stain_2", R.drawable.stain_2),
        EffectData("stain_3", R.drawable.stain_3),
        EffectData("stain_4", R.drawable.stain_4),
        EffectData("stain_5", R.drawable.stain_5)
    )

    var vintage_array: Array<EffectData> = arrayOf(
        EffectData("vintage_1", R.drawable.vintage_1),
        EffectData("vintage_2", R.drawable.vintage_2),
        EffectData("vintage_3", R.drawable.vintage_3),
        EffectData("vintage_4", R.drawable.vintage_4),
        EffectData("vintage_5", R.drawable.vintage_5)
    )

    var cloud_array: Array<EffectData> = arrayOf(
        EffectData("cloud_1", R.drawable.cloud_1),
        EffectData("cloud_2", R.drawable.cloud_2),
        EffectData("cloud_3", R.drawable.cloud_3),
        EffectData("cloud_4", R.drawable.cloud_4),
        EffectData("cloud_5", R.drawable.cloud_5)
    )

    var fog_array: Array<EffectData> = arrayOf(
        EffectData("fog_1", R.drawable.fog_1),
        EffectData("fog_2", R.drawable.fog_2),
        EffectData("fog_3", R.drawable.fog_3),
        EffectData("fog_4", R.drawable.fog_4),
        EffectData("fog_5", R.drawable.fog_5)
    )

    var snow_array: Array<EffectData> = arrayOf(
        EffectData("snow_1", R.drawable.snow_1),
        EffectData("snow_2", R.drawable.snow_2),
        EffectData("snow_3", R.drawable.snow_3),
        EffectData("snow_4", R.drawable.snow_4),
        EffectData("snow_5", R.drawable.snow_5)
    )

    var sunlight_array: Array<EffectData> = arrayOf(
        EffectData("sunlight_1", R.drawable.sunlight_1),
        EffectData("sunlight_2", R.drawable.sunlight_2),
        EffectData("sunlight_3", R.drawable.sunlight_3),
        EffectData("sunlight_4", R.drawable.sunlight_4),
        EffectData("sunlight_5", R.drawable.sunlight_5)
    )

    private var mLastClickTime: Long = 0
    fun checkClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
    }

    override fun onResizeClick(position: Int) {
        when (position) {
            0 -> {
                cropImageView.setCropMode(CropImageView.CropMode.FIT_IMAGE)
            }
            1 -> {
                cropImageView.setCropMode(CropImageView.CropMode.FREE)
            }
            2 -> {
                cropImageView.setCustomRatio(1, 1)
            }
            3 -> {
                cropImageView.setCustomRatio(4, 5)
            }
            4 -> {
                cropImageView.setCustomRatio(2, 3)
            }
            5 -> {
                cropImageView.setCustomRatio(3, 2)
            }
            6 -> {
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4)
            }
            7 -> {
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3)
            }
            8 -> {
                cropImageView.setCustomRatio(1, 2)
            }
            9 -> {
                cropImageView.setCustomRatio(2, 1)
            }
            10 -> {
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16)
            }
            11 -> {
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9)
            }
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.crop_cancel -> {
                ll_crop.visibility = View.GONE
            }
            R.id.crop_confirm -> {
                original_bitmap = cropImageView.croppedBitmap
                img_main.setImageBitmap(original_bitmap)
                ll_crop.visibility = View.GONE
            }

            R.id.filter_cancel -> {
                ll_filter.visibility = View.GONE
                img_main.setImageBitmap(original_bitmap)
            }
            R.id.filter_confirm -> {
                val bitmap = (img_main.drawable as BitmapDrawable).bitmap
                original_bitmap = bitmap

                img_main.setImageBitmap(original_bitmap)

                ll_filter.visibility = View.GONE
            }

            R.id.effect_cancel -> {
                ll_effect.visibility = View.GONE
                img_main.setImageBitmap(original_bitmap)

                overlay_light.visibility = View.GONE
                overlay_texture.visibility = View.GONE
                overlay_weather.visibility = View.GONE
            }
            R.id.effect_confirm -> {
                val bitmap = (img_main.drawable as BitmapDrawable).bitmap
                original_bitmap = bitmap

                img_main.setImageBitmap(original_bitmap)

                ll_effect.visibility = View.GONE
            }
            R.id.effect_back -> {
                ll_effect_type.visibility = View.VISIBLE
                ll_blend_type.visibility = View.GONE
                seekbar_blend.visibility = View.GONE
            }

            R.id.adjust_cancel -> {
                ll_adjust.visibility = View.GONE
                img_main.setImageBitmap(original_bitmap)
            }
            R.id.adjust_confirm -> {
                val bitmap = (img_main.drawable as BitmapDrawable).bitmap
                original_bitmap = bitmap

                img_main.setImageBitmap(original_bitmap)

                ll_adjust.visibility = View.GONE

            }
            R.id.hsl_cancel -> {
                ll_hsl.visibility = View.GONE
                img_main.setImageBitmap(original_bitmap)
            }
            R.id.hsl_confirm -> {
                val bitmap = (img_main.drawable as BitmapDrawable).bitmap
                original_bitmap = bitmap

                img_main.setImageBitmap(original_bitmap)

                ll_hsl.visibility = View.GONE
            }

            R.id.layers_cancel -> {

                HideStickers()

                ll_layers.visibility = View.GONE
            }
            R.id.layers_confirm -> {
                HideStickers()
                ll_layers.visibility = View.GONE
            }

            R.id.txt_resize -> {
                ll_rotate.visibility = View.GONE
                ll_resize.visibility = View.VISIBLE
            }

            R.id.txt_rotate -> {
                ll_resize.visibility = View.GONE
                ll_rotate.visibility = View.VISIBLE
            }

            R.id.crop_rotate_left -> {
                cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D)
            }
            R.id.crop_rotate_right -> {
                cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D)
            }
            R.id.flip_horizontal -> {

                cropImageView.imageBitmap =
                    flip(cropImageView.imageBitmap, LinearLayoutManager.HORIZONTAL)
            }
            R.id.flip_vertical -> {

                cropImageView.imageBitmap =
                    flip(cropImageView.imageBitmap, LinearLayoutManager.VERTICAL)

            }
            R.id.ll_blend -> {
                effect_gallery.visibility = View.VISIBLE
                seekbar_blend.visibility = View.GONE
                list_blend.adapter = BlendAdapter(img_blend)
                list_blend_type.adapter = BlendTypeAdapter(img_blend)
                ll_effect_type.visibility = View.GONE
                ll_blend_type.visibility = View.VISIBLE
            }
            R.id.ll_light -> {

                effect_gallery.visibility = View.GONE
                seekbar_blend.visibility = View.VISIBLE
                seekbar_blend.setOnSeekBarChangeListener(effectLight_listener())
                var light_adapter =
                    FilterNameAdapter(this, resources.getStringArray(R.array.effect_light))
                list_blend_type.adapter = light_adapter

                list_blend.adapter = LightAdapter(light1_array, overlay_light)

                setLight(overlay_light, light1_array)

                overlay_light.visibility = View.VISIBLE

                light_adapter.setOnFilterNameClick(object :
                    FilterNameAdapter.FilterNameClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (position == 0) {
                            list_blend.adapter = LightAdapter(light1_array, overlay_light)
                            setLight(overlay_light, light1_array)
                        } else if (position == 1) {
                            list_blend.adapter = LightAdapter(light2_array, overlay_light)
                            setLight(overlay_light, light2_array)
                        } else if (position == 2) {
                            list_blend.adapter = LightAdapter(festival_array, overlay_light)
                            setLight(overlay_light, festival_array)
                        } else if (position == 3) {
                            list_blend.adapter = LightAdapter(love_array, overlay_light)
                            setLight(overlay_light, love_array)
                        } else if (position == 4) {
                            list_blend.adapter = LightAdapter(prism_array, overlay_light)
                            setLight(overlay_light, prism_array)
                        } else if (position == 5) {
                            list_blend.adapter = LightAdapter(neon_array, overlay_light)
                            setLight(overlay_light, neon_array)
                        } else {
                            list_blend.adapter = LightAdapter(light1_array, overlay_light)
                            setLight(overlay_light, light1_array)
                        }
                    }
                })

                ll_effect_type.visibility = View.GONE
                ll_blend_type.visibility = View.VISIBLE
            }
            R.id.ll_texture -> {
                effect_gallery.visibility = View.GONE
                seekbar_blend.visibility = View.VISIBLE
                seekbar_blend.setOnSeekBarChangeListener(effectTexture_listener())
                var texture_adapter =
                    FilterNameAdapter(this, resources.getStringArray(R.array.effect_texture))
                list_blend_type.adapter = texture_adapter

                list_blend.adapter = LightAdapter(dust_array, overlay_texture)
                overlay_texture.visibility = View.VISIBLE

                texture_adapter.setOnFilterNameClick(object :
                    FilterNameAdapter.FilterNameClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (position == 0) {
                            list_blend.adapter = LightAdapter(dust_array, overlay_texture)
                            setLight(overlay_texture, dust_array)
                        } else if (position == 1) {
                            list_blend.adapter = LightAdapter(stain_array, overlay_texture)
                            setLight(overlay_texture, stain_array)
                        } else if (position == 2) {
                            list_blend.adapter = LightAdapter(vintage_array, overlay_texture)
                            setLight(overlay_texture, vintage_array)
                        } else if (position == 3) {
                            list_blend.adapter = LightAdapter(scratch_array, overlay_texture)
                            setLight(overlay_texture, scratch_array)
                        } else {
                            list_blend.adapter = LightAdapter(dust_array, overlay_texture)
                            setLight(overlay_texture, dust_array)
                        }
                    }
                })

                ll_effect_type.visibility = View.GONE
                ll_blend_type.visibility = View.VISIBLE
            }
            R.id.ll_weather -> {
                effect_gallery.visibility = View.GONE
                seekbar_blend.visibility = View.VISIBLE
                seekbar_blend.setOnSeekBarChangeListener(effectWeather_listener())

                var weather_adapter =
                    FilterNameAdapter(this, resources.getStringArray(R.array.effect_weather))
                list_blend_type.adapter = weather_adapter

                list_blend.adapter = LightAdapter(snow_array, overlay_weather)
                overlay_weather.visibility = View.VISIBLE

                weather_adapter.setOnFilterNameClick(object :
                    FilterNameAdapter.FilterNameClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        if (position == 0) {
                            list_blend.adapter = LightAdapter(snow_array, overlay_weather)
                            setLight(overlay_weather, snow_array)
                        } else if (position == 1) {
                            list_blend.adapter = LightAdapter(cloud_array, overlay_weather)
                            setLight(overlay_weather, cloud_array)
                        } else if (position == 2) {
                            list_blend.adapter = LightAdapter(fog_array, overlay_weather)
                            setLight(overlay_weather, fog_array)
                        } else if (position == 3) {
                            list_blend.adapter = LightAdapter(sunlight_array, overlay_weather)
                            setLight(overlay_weather, sunlight_array)
                        } else {
                            list_blend.adapter = LightAdapter(snow_array, overlay_weather)
                            setLight(overlay_weather, snow_array)
                        }
                    }

                })

                ll_effect_type.visibility = View.GONE
                ll_blend_type.visibility = View.VISIBLE
            }
            R.id.ll_text -> {

                checkClick()
                opendialogtext()
            }
            R.id.ll_sticker -> {
                checkClick()
                opendialogSticker()
            }
            R.id.ll_border -> {
                layer_layout.visibility = View.GONE
                border_layout.visibility = View.VISIBLE
            }
            R.id.border_back -> {
                layer_layout.visibility = View.VISIBLE
                border_layout.visibility = View.GONE
            }

            R.id.effect_gallery -> {
                var intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_PICK
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
            }
            R.id.border_blur -> {
                var gpuImage = GPUImage(this@ImageEditActivity)
                gpuImage.setImage(blur_bitmap)
                gpuImage.setFilter(GPUImageGaussianBlurFilter(5F))

                frame_layout.background =
                    BitmapDrawable(resources, gpuImage.bitmapWithFilterApplied)
            }
            R.id.img_save -> {

                checkClick()

                ll_crop.visibility = View.GONE
                ll_filter.visibility = View.GONE
                ll_effect.visibility = View.GONE
                ll_adjust.visibility = View.GONE
                ll_hsl.visibility = View.GONE
                ll_layers.visibility = View.GONE

                MainActivity.isFromSaved = true

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

            R.id.img_reset -> {

                checkClick()
                var builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you want to reset image?")
                    .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {

                            var doBreak = false
                            while (!doBreak) {
                                var childcount: Int = frame_layout.childCount
                                if (childcount > 0) {
                                    var i: Int = 0
                                    for (i in 0 until childcount) {

                                        var v: View = frame_layout.getChildAt(i)

                                        if (v is StickerView) {
                                            frame_layout.removeView(v)
                                            break
                                        }
                                    }
                                    if (childcount == 1) {
                                        doBreak = true
                                    }
                                }
                            }

                            image_frame.setPadding(0, 0, 0, 0)
                            frame_layout.setBackgroundColor(resources.getColor(R.color.transparent))

                            overlay_light.visibility = View.GONE
                            overlay_weather.visibility = View.GONE
                            overlay_texture.visibility = View.GONE

                            original_bitmap = image_bitmap
                            img_main.setImageBitmap(original_bitmap)

                            dialog!!.dismiss()
                        }
                    })
                    .setNegativeButton("No", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.dismiss()
                        }
                    }).show()

            }
        }
    }

    fun setLight(img_light: ImageView, effect: Array<EffectData>) {

        img_light.visibility = View.VISIBLE
        var main_bitmap = (img_main.getDrawable() as BitmapDrawable).bitmap
        var bitmap =
            (resources.getDrawable(effect[0].icon) as BitmapDrawable).bitmap
        bitmap = Bitmap.createScaledBitmap(
            bitmap,
            main_bitmap.width,
            main_bitmap.height,
            true
        )
        img_light.setImageBitmap(bitmap)

        seekbar_blend.progress = 90
        img_light.imageAlpha = seekbar_blend.progress

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            if (data != null) {
                try {
                    var uri: Uri = data.data!!

                    var inputStream: InputStream?
                    try {
                        inputStream = contentResolver.openInputStream(uri)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        inputStream = null
                    }

                    isFromGallery = true
                    blend_bitmap = BitmapFactory.decodeStream(inputStream)

                    creaate_bmp().executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR,
                        blend_bitmap
                    )

                    /* var bmp = blend_bitmap
                     bmp = AndroidUtils.resizeImageToNewSize(
                         bmp,
                         bmp.width / 2,
                         bmp.height / 2
                     )

                     var stream = ByteArrayOutputStream()
                     bmp.compress(Bitmap.CompressFormat.JPEG, 20, stream)
                     blend_bitmap = bmp

                     var gpuImage1 = GPUImage(this@ImageEditActivity)
                     gpuImage1.setImage(original_bitmap)
                     gpuImage1.setFilter(
                         createBlendFilter(
                             filters_blend[blendfilter_position],
                             blend_bitmap
                         )
                     )
                     img_main.setImageBitmap(gpuImage1.bitmapWithFilterApplied)*/

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        }
    }

    inner class creaate_bmp() : AsyncTask<Bitmap, Void, Bitmap>() {
        override fun doInBackground(vararg params: Bitmap?): Bitmap? {

            var bmp = params[0]
            bmp = AndroidUtils.resizeImageToNewSize(
                bmp!!,
                bmp.width / 2,
                bmp.height / 2
            )

            var stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 20, stream)
            blend_bitmap = bmp

            if (original_bitmap.getWidth() > original_bitmap.getHeight()) {

                blend_bitmap = ThumbnailUtils.extractThumbnail(
                    bmp,
                    original_bitmap.getWidth(),
                    original_bitmap.getHeight(),
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT
                )
            } else if (original_bitmap.getWidth() < original_bitmap.getHeight()) {
                blend_bitmap = ThumbnailUtils.extractThumbnail(
                    bmp,
                    original_bitmap.getWidth(),
                    original_bitmap.getHeight(),
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT
                )
            } else {
                blend_bitmap = bmp
            }

            return blend_bitmap
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)

            var gpuImage1 = GPUImage(this@ImageEditActivity)
            gpuImage1.setImage(original_bitmap)
            gpuImage1.setFilter(
                createBlendFilter(
                    filters_blend[blendfilter_position],
                    blend_bitmap
                )
            )
            img_main.setImageBitmap(gpuImage1.bitmapWithFilterApplied)
        }
    }

    val screenShot: Bitmap
        get() {
            val findViewById = findViewById<View>(R.id.ll_root)
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


    fun opendialogtext() {

        var dialog = Dialog(this)
//        dialog.setContentView(R.layout.textdialog_layout)
        val inflater = LayoutInflater.from(this@ImageEditActivity)
        val subview = inflater.inflate(R.layout.textdialog_layout, null)

        val editText = subview.findViewById(R.id.dialogEditText) as EditText
        val btn_done = subview.findViewById(R.id.btn_done) as Button
        var list_font: RecyclerView = subview.findViewById(R.id.list_font) as RecyclerView
        var list_color: RecyclerView = subview.findViewById(R.id.list_color) as RecyclerView

        var alert: AlertDialog.Builder = AlertDialog.Builder(this)
        alert.setView(subview)
        alert.setCancelable(true)


        list_font.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var fontadapter: FontAdapter = FontAdapter(this)
        list_font.adapter = fontadapter

        fontadapter.setOnFontClick(object : FontAdapter.FontClickListener {
            override fun onItemClick(view: View, fontName: String) {
                editText.typeface = Typeface.createFromAsset(assets, fontName)
            }
        })

        list_color.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var colorAdapter = ColorAdapter(this)
        list_color.adapter = colorAdapter
        colorAdapter.setOnColorClick(object : ColorAdapter.ColorClickListener {
            override fun onItemClick(view: View, colorName: String) {
                editText.setTextColor(Integer.valueOf(Color.parseColor(colorName)))
            }
        })

        btn_done.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var tv_sticker = StickerTextView(this@ImageEditActivity)

                tv_sticker.tv_main!!.setText(editText.text.toString())
                tv_sticker.tv_main!!.setTypeface(editText.typeface)
                tv_sticker.tv_main!!.setTextColor(editText.textColors)
                frame_layout.addView(tv_sticker)
                dialog.dismiss()
            }
        })

        dialog = alert.create()
        dialog.show()
    }


    fun opendialogSticker() {

        var dialog = Dialog(this)
//        dialog.setContentView(R.layout.textdialog_layout)
        val inflater = LayoutInflater.from(this@ImageEditActivity)
        val subview = inflater.inflate(R.layout.stickerdialog_layout, null)

//        val btn_done = subview.findViewById(R.id.btn_done) as Button
        var list_sticker: RecyclerView = subview.findViewById(R.id.list_sticker) as RecyclerView
        var list_sticker_tab: RecyclerView =
            subview.findViewById(R.id.list_sticker_tab) as RecyclerView

        var alert: AlertDialog.Builder = AlertDialog.Builder(this)
        alert.setView(subview)
        alert.setCancelable(true)

        list_sticker_tab.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var stickerTabAdapter = StickerTabAdapter(this)
        list_sticker_tab.adapter = stickerTabAdapter

        list_sticker.layoutManager = GridLayoutManager(this, 7, GridLayoutManager.VERTICAL, false)
        var stickerAdapter = StickerAdapter(this, 0)
        list_sticker.adapter = stickerAdapter

        stickerTabAdapter.setTabClickListener(object : StickerTabAdapter.StickerTabListener {
            override fun onTabSelected(view: View, position: Int) {
                stickerAdapter = StickerAdapter(this@ImageEditActivity, position)
                list_sticker.adapter = stickerAdapter
                stickerAdapter.notifyDataSetChanged()

                stickerAdapter.setOnStickerClick(object : StickerAdapter.StickerListener {
                    override fun onStickerClick(view: View, drawable: Drawable) {
                        var iv_sticker = StickerImageView(this@ImageEditActivity)
                        iv_sticker.setImageDrawable(drawable)
                        frame_layout.addView(iv_sticker)
                        dialog.dismiss()
                    }
                })
            }
        })

        stickerAdapter.setOnStickerClick(object : StickerAdapter.StickerListener {
            override fun onStickerClick(view: View, drawable: Drawable) {
                var iv_sticker = StickerImageView(this@ImageEditActivity)
                iv_sticker.setImageDrawable(drawable)
                frame_layout.addView(iv_sticker)
                dialog.dismiss()
            }
        })

        dialog = alert.create()
        dialog.show()
    }

    fun flip(src: Bitmap, type: Int): Bitmap {
        val matrix = Matrix()

        if (type == LinearLayoutManager.VERTICAL) {
            matrix.preScale(1.0f, -1.0f)
        } else if (type == LinearLayoutManager.HORIZONTAL) {
            matrix.preScale(-1.0f, 1.0f)
        } else {
            return src
        }

        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    lateinit var display: DisplayMetrics
    var density: Float = 0.0f
    internal var D_height: Int = 0
    internal var D_width: Int = 0
    lateinit var original_bitmap: Bitmap
    lateinit var image_bitmap: Bitmap
    lateinit var blur_bitmap: Bitmap
    lateinit var hsl_bitmap: Bitmap


    var array_img: TypedArray? = null
    var array_text: Array<String>? = null
    var sticker_color: Array<String>? = null
    var fonts_sticker: Array<String>? = null

    var selectedIndex: Int = 1
    var imageUri: String? = null

    private var filterAdjuster: GPUImageFilterTools.FilterAdjuster? = null

    inner class border_listener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            image_frame.setPadding(progress, progress, progress, progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

    inner class adjust1_listener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            filterAdjuster!!.adjust(progress)

            filter_apply(adjust_position)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

    inner class hue_listener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            filterAdjuster_hue!!.adjust(progress)

            groupfilter(progress, seekbar_saturation.progress, seekbar_brightness.progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

    inner class saturation_listener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            filterAdjuster_sat!!.adjust(progress)
            groupfilter(seekbar_hue.progress, progress, seekbar_brightness.progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

    inner class brightness_listener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            filterAdjuster_bright!!.adjust(progress)

            groupfilter(seekbar_hue.progress, seekbar_saturation.progress, progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    }

    inner class effectLight_listener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            overlay_light.imageAlpha = progress

//            overlay_light.setImageBitmap((overlay_light.drawable as BitmapDrawable).bitmap)

        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    }

    inner class effectTexture_listener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            overlay_texture.imageAlpha = progress
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    }

    inner class effectWeather_listener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            overlay_weather.imageAlpha = progress
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    }

    private var filterAdjuster_hue: GPUImageFilterTools.FilterAdjuster? = null
    private var filterAdjuster_sat: GPUImageFilterTools.FilterAdjuster? = null
    private var filterAdjuster_bright: GPUImageFilterTools.FilterAdjuster? = null

    fun groupfilter(progress_hue: Int, progress_sat: Int, progress_bright: Int) {

        var gpuImage1 = GPUImage(this@ImageEditActivity)
        gpuImage1.setImage(original_bitmap)

        var group = GPUImageFilterGroup()
        group.addFilter(GPUImageHueFilter())
        group.addFilter(GPUImageSaturationFilter())
        group.addFilter(GPUImageBrightnessFilter())

        var mergedFilters = group.mergedFilters
        filterAdjuster_hue = GPUImageFilterTools.FilterAdjuster(mergedFilters.get(0))
        filterAdjuster_hue!!.adjust(progress_hue)
        filterAdjuster_sat = GPUImageFilterTools.FilterAdjuster(mergedFilters.get(1))
        filterAdjuster_sat!!.adjust(progress_sat)
        filterAdjuster_bright = GPUImageFilterTools.FilterAdjuster(mergedFilters.get(2))
        filterAdjuster_bright!!.adjust(progress_bright)

        gpuImage1.setFilter(group)
        hsl_bitmap = gpuImage1.bitmapWithFilterApplied
        img_main.setImageBitmap(hsl_bitmap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_edit)

        array_img = resources.obtainTypedArray(R.array.img_options)
        array_text = resources.getStringArray(R.array.text_options)
        sticker_color = resources.getStringArray(R.array.sticker_color)
        fonts_sticker = resources.getStringArray(R.array.fonts_sticker)

        imageUri = intent.getStringExtra("image_uri")

        display = resources.displayMetrics
        density = resources.displayMetrics.density
        D_width = display.widthPixels
        D_height = (display.heightPixels.toFloat() - density * 150.0f).toInt()


        var inputStream: InputStream?
        try {
            inputStream = contentResolver.openInputStream(Uri.parse(imageUri))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            inputStream = null
        }

        original_bitmap = BitmapFactory.decodeStream(inputStream)
        original_bitmap = AndroidUtils.resizeImageToNewSize(original_bitmap, D_width, D_height)

        image_bitmap = original_bitmap
        blur_bitmap = original_bitmap
        img_main.setImageBitmap(original_bitmap)

        list_resize.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list_resize.adapter = ResizeAdapter(this, this)

        list_options.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list_options.adapter = OptionAdapter()

        list_filterstype.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var filter_detailadapter = FilterDetailAdapter(filter_clr1)
        list_filterstype.adapter = filter_detailadapter

        filter_names.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var filternameadapter = FilterNameAdapter(this, resources.getStringArray(R.array.filters))

        filternameadapter.setOnFilterNameClick(object : FilterNameAdapter.FilterNameClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (position == 0) {
                    filter_detailadapter = FilterDetailAdapter(filter_clr1)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 1) {
                    filter_detailadapter = FilterDetailAdapter(filter_clr2)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 2) {
                    filter_detailadapter = FilterDetailAdapter(filter_duo)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 3) {
                    filter_detailadapter = FilterDetailAdapter(filter_pink)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 4) {
                    filter_detailadapter = FilterDetailAdapter(filter_fresh)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 5) {
                    filter_detailadapter = FilterDetailAdapter(filter_euro)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 6) {
                    filter_detailadapter = FilterDetailAdapter(filter_dark)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 7) {
                    filter_detailadapter = FilterDetailAdapter(filter_ins)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 8) {
                    filter_detailadapter = FilterDetailAdapter(filter_elegant)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 9) {
                    filter_detailadapter = FilterDetailAdapter(filter_golden)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 10) {
                    filter_detailadapter = FilterDetailAdapter(filter_tint)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 11) {
                    filter_detailadapter = FilterDetailAdapter(filter_film)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 12) {
                    filter_detailadapter = FilterDetailAdapter(filter_lomo)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 13) {
                    filter_detailadapter = FilterDetailAdapter(filter_movie)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 14) {
                    filter_detailadapter = FilterDetailAdapter(filter_retro)
                    list_filterstype.adapter = filter_detailadapter
                } else if (position == 15) {
                    filter_detailadapter = FilterDetailAdapter(filter_bw)
                    list_filterstype.adapter = filter_detailadapter
                } else {
                    filter_detailadapter = FilterDetailAdapter(filter_clr1)
                    list_filterstype.adapter = filter_detailadapter
                }
                filternameadapter.notifyDataSetChanged()
                filter_detailadapter.notifyDataSetChanged()
            }
        })

        filter_names.adapter = filternameadapter

        list_blend.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list_blend.adapter = BlendAdapter(img_blend)

        list_blend_type.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list_blend_type.adapter = BlendTypeAdapter(img_blend)

        list_adjust.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list_adjust.adapter = AdjustAdapter()

        list_border.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        var gpuImage = GPUImage(this@ImageEditActivity)
        gpuImage.setImage(blur_bitmap)
        gpuImage.setFilter(GPUImageGaussianBlurFilter(5F))

        border_blur.setImageBitmap(gpuImage.bitmapWithFilterApplied)

        border_blur.setOnClickListener(this)

        var cAdapter = ColorAdapter(this)
        cAdapter.setOnColorClick(object : ColorAdapter.ColorClickListener {
            override fun onItemClick(view: View, colorName: String) {
                frame_layout.setBackgroundColor(Integer.valueOf(Color.parseColor(colorName)))
            }
        })
        list_border.adapter = cAdapter

        crop_cancel.setOnClickListener(this)
        crop_confirm.setOnClickListener(this)
        filter_confirm.setOnClickListener(this)
        filter_cancel.setOnClickListener(this)
        effect_confirm.setOnClickListener(this)
        effect_cancel.setOnClickListener(this)
        effect_back.setOnClickListener(this)
        adjust_confirm.setOnClickListener(this)
        adjust_cancel.setOnClickListener(this)
        hsl_confirm.setOnClickListener(this)
        hsl_cancel.setOnClickListener(this)
        layers_confirm.setOnClickListener(this)
        layers_cancel.setOnClickListener(this)


        txt_resize.setOnClickListener(this)
        txt_rotate.setOnClickListener(this)
        crop_rotate_left.setOnClickListener(this)
        crop_rotate_right.setOnClickListener(this)
        flip_horizontal.setOnClickListener(this)
        flip_vertical.setOnClickListener(this)
        img_save.setOnClickListener(this)
        img_reset.setOnClickListener(this)

        ll_blend.setOnClickListener(this)
        ll_light.setOnClickListener(this)
        ll_texture.setOnClickListener(this)
        ll_weather.setOnClickListener(this)

        ll_text.setOnClickListener(this)
        ll_sticker.setOnClickListener(this)
        ll_border.setOnClickListener(this)
        border_back.setOnClickListener(this)
        effect_gallery.setOnClickListener(this)

        seekbar_border.setOnSeekBarChangeListener(border_listener())
        seekbar_adjust1.setOnSeekBarChangeListener(adjust1_listener())

        seekbar_hue.setOnSeekBarChangeListener(hue_listener())
        seekbar_saturation.setOnSeekBarChangeListener(saturation_listener())
        seekbar_brightness.setOnSeekBarChangeListener(brightness_listener())

        AdLoader.ads.ShowFBAds(this@ImageEditActivity)

        frame_layout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if (event!!.action == MotionEvent.ACTION_DOWN) {
                    HideStickers()
                }
                return true
            }
        })
    }

    inner class FilterDetailAdapter(filters: Array<FilterData>) :
        RecyclerView.Adapter<FilterDetailAdapter.FilterDetailHolder>() {
        var filterType = filters
        var selectedindex = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterDetailHolder {
            var view = LayoutInflater.from(this@ImageEditActivity)
                .inflate(R.layout.item_filter, parent, false)
            return FilterDetailHolder(view)
        }

        override fun getItemCount(): Int {
            return filterType.size
        }

        override fun onBindViewHolder(holder: FilterDetailHolder, position: Int) {
            if (selectedindex == position) {
                holder.rl_filteritem.setBackgroundResource(R.drawable.round_corner)
            } else {
                holder.rl_filteritem.setBackgroundColor(resources.getColor(R.color.transparent))
            }
            holder.thumbnail_filter.setImageResource(R.drawable.thumb_filter)

            red = filterType[position].red
            green = filterType[position].green
            blue = filterType[position].blue
            saturation = filterType[position].saturation

            var bitmap = Bitmap.createBitmap(
                original_bitmap.getWidth(),
                original_bitmap.getHeight(),
                Bitmap.Config.ARGB_8888
            )
            var canvas = Canvas(bitmap)

            var paint = Paint()
            var colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(saturation)

            var colorScale = ColorMatrix()
            colorScale.setScale(red, green, blue, 1F)
            colorMatrix.postConcat(colorScale)

            paint.setColorFilter(ColorMatrixColorFilter(colorMatrix))
            canvas.drawBitmap(original_bitmap, 0F, 0F, paint)

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
                        original_bitmap,
                        img_main
                    ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, red, green, blue)
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

    fun HideStickers() {
        var fm = frame_layout
        var childcount: Int = frame_layout.childCount

        if (childcount != 0) {
            for (i in 0 until childcount) {
                var v: View = fm.getChildAt(i)

                if (v is StickerView) {
                    v.setControlItemsHidden(true)
                }
            }
        }
    }

    inner class OptionAdapter : RecyclerView.Adapter<OptionAdapter.OptionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
            var view: View = LayoutInflater.from(this@ImageEditActivity)
                .inflate(R.layout.item_oprion, parent, false)

            return OptionHolder(view)
        }

        override fun getItemCount(): Int {
            return array_text!!.size
        }

        override fun onBindViewHolder(holder: OptionHolder, position: Int) {
            holder.img_option.setImageResource(array_img!!.getResourceId(position, 0))
            holder.txt_option.setText(array_text!![position])


            var lp: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(D_width / 6, LinearLayout.LayoutParams.WRAP_CONTENT)
            holder.ll_option.layoutParams = lp

            if (selectedIndex == position) {
                holder.txt_option.setTextColor(resources.getColor(R.color.colorAccent))
                holder.img_option.setColorFilter(resources.getColor(R.color.colorAccent))
            } else {
                holder.txt_option.setTextColor(resources.getColor(R.color.white))
                holder.img_option.setColorFilter(resources.getColor(R.color.white))
            }

            holder.ll_option.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    selectedIndex = position

                    when (position) {
                        0 -> {
                            ll_crop.visibility = View.VISIBLE
                            ll_filter.visibility = View.GONE
                            ll_effect.visibility = View.GONE
                            ll_adjust.visibility = View.GONE
                            ll_hsl.visibility = View.GONE
                            ll_layers.visibility = View.GONE
                            cropImageView.imageBitmap = original_bitmap
                        }
                        1 -> {
                            ll_crop.visibility = View.GONE
                            ll_filter.visibility = View.VISIBLE
                            ll_effect.visibility = View.GONE
                            ll_adjust.visibility = View.GONE
                            ll_hsl.visibility = View.GONE
                            ll_layers.visibility = View.GONE
                        }
                        2 -> {
                            ll_crop.visibility = View.GONE
                            ll_filter.visibility = View.GONE
                            ll_effect.visibility = View.VISIBLE
                            ll_adjust.visibility = View.GONE
                            ll_hsl.visibility = View.GONE
                            ll_layers.visibility = View.GONE
                        }
                        3 -> {
                            ll_crop.visibility = View.GONE
                            ll_filter.visibility = View.GONE
                            ll_effect.visibility = View.GONE
                            ll_adjust.visibility = View.VISIBLE
                            ll_hsl.visibility = View.GONE
                            ll_layers.visibility = View.GONE

                            filterAdjuster = GPUImageFilterTools.FilterAdjuster(filter_adjust[0])
                            seekbar_adjust1.progress = 90
                            filterAdjuster!!.adjust(seekbar_adjust1.progress)

                            filter_apply(0)
                        }
                        4 -> {
                            ll_crop.visibility = View.GONE
                            ll_filter.visibility = View.GONE
                            ll_effect.visibility = View.GONE
                            ll_adjust.visibility = View.GONE
                            ll_hsl.visibility = View.VISIBLE
                            ll_layers.visibility = View.GONE

                            hsl_bitmap = original_bitmap
                            groupfilter(
                                seekbar_hue.progress,
                                seekbar_saturation.progress,
                                seekbar_brightness.progress
                            )
                        }
                        5 -> {
                            ll_crop.visibility = View.GONE
                            ll_filter.visibility = View.GONE
                            ll_effect.visibility = View.GONE
                            ll_adjust.visibility = View.GONE
                            ll_hsl.visibility = View.GONE
                            ll_layers.visibility = View.VISIBLE
                        }
                        else -> {
                            ll_crop.visibility = View.GONE
                            ll_filter.visibility = View.VISIBLE
                            ll_effect.visibility = View.GONE
                            ll_adjust.visibility = View.GONE
                            ll_hsl.visibility = View.GONE
                            ll_layers.visibility = View.GONE
                        }
                    }
                    notifyDataSetChanged()
                }
            })
        }

        inner class OptionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var img_option: ImageView = itemView.findViewById(R.id.img_option) as ImageView
            var txt_option: TextView = itemView.findViewById(R.id.txt_option) as TextView
            var ll_option: LinearLayout = itemView.findViewById(R.id.ll_option) as LinearLayout
        }
    }

    fun filter_apply(position: Int) {
        var gpuImage1 = GPUImage(this@ImageEditActivity)
        gpuImage1.setImage(original_bitmap)
        gpuImage1.setFilter(filter_adjust[position])
        img_main.setImageBitmap(gpuImage1.bitmapWithFilterApplied)
    }

    inner class LightAdapter(effectList: Array<EffectData>, imageview: ImageView) :
        RecyclerView.Adapter<LightAdapter.LightHolder>() {
        var selectedindex = 0
        var effects: Array<EffectData>?
        var img_overlay = imageview

        init {
            effects = effectList
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightHolder {
            var view: View = LayoutInflater.from(this@ImageEditActivity)
                .inflate(R.layout.item_filter, parent, false)

            return LightHolder(view)
        }

        override fun getItemCount(): Int {
            return effects!!.size
        }

        override fun onBindViewHolder(holder: LightHolder, position: Int) {
            holder.filterName.setText(effects!![position].name)
            holder.thumbnailFilter.setImageResource(effects!![position].icon)

            if (selectedindex == position) {
                holder.rl_filteritem.setBackgroundColor(resources.getColor(R.color.colorAccent))
            } else {
                holder.rl_filteritem.setBackgroundColor(resources.getColor(R.color.transparent))
            }

            holder.rl_filteritem.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    selectedindex = position
                    selectedPosition = position
                    img_overlay.visibility = View.VISIBLE

                    var main_bitmap = (img_main.getDrawable() as BitmapDrawable).bitmap
                    var bitmap =
                        (resources.getDrawable(effects!![position].icon) as BitmapDrawable).bitmap
                    bitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        main_bitmap.width,
                        main_bitmap.height,
                        true
                    )

                    /* var bmp: Bitmap

                     if (original_bitmap.getWidth() > original_bitmap.getHeight()) {

                         bmp = ThumbnailUtils.extractThumbnail(
                             bitmap,
                             original_bitmap.getWidth(),
                             original_bitmap.getHeight(),
                             ThumbnailUtils.OPTIONS_RECYCLE_INPUT
                         )
                     } else if (original_bitmap.getWidth() < original_bitmap.getHeight()) {
                         bmp = ThumbnailUtils.extractThumbnail(
                             bitmap,
                             original_bitmap.getWidth(),
                             original_bitmap.getHeight(),
                             ThumbnailUtils.OPTIONS_RECYCLE_INPUT
                         )
                     } else {
                         bmp = bitmap
                     }*/

                    img_overlay.setImageBitmap(bitmap)

                    seekbar_blend.progress = 90
                    img_overlay.imageAlpha = seekbar_blend.progress
                    notifyDataSetChanged()
                }
            })
        }

        inner class LightHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var thumbnailFilter: ImageView = itemView.findViewById(R.id.thumbnail_filter)
            var filterName: TextView = itemView.findViewById(R.id.filterName)
            var rl_filteritem: RelativeLayout = itemView.findViewById(R.id.rl_filteritem)
        }
    }

    inner class BlendTypeAdapter(images: Array<Int>) :
        RecyclerView.Adapter<BlendTypeAdapter.BlendTypeHolder>() {

        var selectedindex = 0
        var text_Blend_type: Array<String> = arrayOf(
            "Alpha",
            "Normal",
            "Lighten",
            "Screen",
            "Color Dodge",
            "Linear Burn",
            "Darken",
            "Multiply",
            "Overlay",
            "Hard Light",
            "Exclusion",
            "Difference",
            "Divide"
        )

        var img_effect = images

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlendTypeHolder {
            var view: View = LayoutInflater.from(this@ImageEditActivity)
                .inflate(R.layout.item_blend_type, parent, false)

            return BlendTypeHolder(view)
        }

        override fun getItemCount(): Int {
            return text_Blend_type.size
        }

        override fun onBindViewHolder(holder: BlendTypeHolder, position: Int) {
            holder.text_blend_type.setText(text_Blend_type[position])

            if (selectedindex == position) {
                holder.item_adjust.setBackgroundColor(resources.getColor(R.color.colorAccent))
            } else {
                holder.item_adjust.setBackgroundColor(resources.getColor(R.color.transparent))
            }

            holder.item_adjust.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    blendfilter_position = position
                    selectedindex = position

                    var gpuImage1 = GPUImage(this@ImageEditActivity)
                    gpuImage1.setImage(original_bitmap)

                    var blendFilter: GPUImageFilter
                    if (!isFromGallery) {

                        var image: Bitmap = BitmapFactory.decodeResource(
                            resources, img_effect[bledImage_position]
                        )
                        if (original_bitmap.getWidth() > original_bitmap.getHeight()) {

                            image = ThumbnailUtils.extractThumbnail(
                                image,
                                original_bitmap.getWidth(),
                                original_bitmap.getHeight(),
                                ThumbnailUtils.OPTIONS_RECYCLE_INPUT
                            )
                        } else if (original_bitmap.getWidth() < original_bitmap.getHeight()) {
                            image = ThumbnailUtils.extractThumbnail(
                                image,
                                original_bitmap.getWidth(),
                                original_bitmap.getHeight(),
                                ThumbnailUtils.OPTIONS_RECYCLE_INPUT
                            )
                        } else {
                            image = image
                        }

                        blendFilter = createBlendFilter(
                            filters_blend[blendfilter_position],
                            image
                        )
                        gpuImage1.setFilter(blendFilter)
                        img_main.setImageBitmap(gpuImage1.bitmapWithFilterApplied)
                    } else {

                        creaate_bmp().executeOnExecutor(
                            AsyncTask.THREAD_POOL_EXECUTOR,
                            blend_bitmap
                        )

//                        blendFilter = createBlendFilter(
//                            filters_blend[blendfilter_position],
//                            blend_bitmap
//                        )
                    }


                    notifyDataSetChanged()
                }
            })
        }

        inner class BlendTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var text_blend_type: TextView = itemView.findViewById(R.id.text_blend_type) as TextView
            var item_adjust: LinearLayout = itemView.findViewById(R.id.item_adjust) as LinearLayout
        }
    }

    var isFromGallery: Boolean = false
    var blendfilter_position: Int = 0
    var bledImage_position: Int = 0
    var filters_blend: Array<Class<out GPUImageTwoInputFilter>> = arrayOf(
        GPUImageAlphaBlendFilter::class.java,
        GPUImageNormalBlendFilter::class.java,
        GPUImageLightenBlendFilter::class.java,
        GPUImageScreenBlendFilter::class.java,
        GPUImageColorDodgeBlendFilter::class.java,
        GPUImageLinearBurnBlendFilter::class.java,
        GPUImageDarkenBlendFilter::class.java,
        GPUImageMultiplyBlendFilter::class.java,
        GPUImageOverlayBlendFilter::class.java,
        GPUImageHardLightBlendFilter::class.java,
        GPUImageExclusionBlendFilter::class.java,
        GPUImageDifferenceBlendFilter::class.java,
        GPUImageDivideBlendFilter::class.java
    )

    var img_blend: Array<Int> = arrayOf(
        R.drawable.blend_1,
        R.drawable.blend_2,
        R.drawable.blend_3,
        R.drawable.blend_4,
        R.drawable.blend_5,
        R.drawable.blend_6,
        R.drawable.blend_7,
        R.drawable.blend_8,
        R.drawable.blend_9,
        R.drawable.blend_10,
        R.drawable.blend_11,
        R.drawable.blend_12,
        R.drawable.blend_13,
        R.drawable.blend_14,
        R.drawable.blend_15,
        R.drawable.blend_16,
        R.drawable.blend_17,
        R.drawable.blend_18,
        R.drawable.blend_19,
        R.drawable.blend_20
    )

    private fun createBlendFilter(
        filterClass: Class<out GPUImageTwoInputFilter>,
        image: Bitmap
    ): GPUImageFilter {
        return try {
            /*      var bmp: Bitmap = image
                  if (original_bitmap.getWidth() > original_bitmap.getHeight()) {

                      bmp = ThumbnailUtils.extractThumbnail(
                          image,
                          original_bitmap.getWidth(),
                          original_bitmap.getHeight(),
                          ThumbnailUtils.OPTIONS_RECYCLE_INPUT
                      )
                  } else if (original_bitmap.getWidth() < original_bitmap.getHeight()) {
                      bmp = ThumbnailUtils.extractThumbnail(
                          image,
                          original_bitmap.getWidth(),
                          original_bitmap.getHeight(),
                          ThumbnailUtils.OPTIONS_RECYCLE_INPUT
                      )
                  } else {
                      bmp = image
                  }
      */
            filterClass.newInstance().apply {
                bitmap = image
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GPUImageFilter()
        }
    }

    inner class BlendAdapter(images: Array<Int>) :
        RecyclerView.Adapter<BlendAdapter.BlendHolder>() {
        var selectedindex = 0

        var img_effects = images
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlendHolder {
            var view: View = LayoutInflater.from(this@ImageEditActivity)
                .inflate(R.layout.item_blend, parent, false)

            return BlendHolder(view)
        }

        override fun getItemCount(): Int {
            return img_effects.size
        }

        override fun onBindViewHolder(holder: BlendHolder, position: Int) {
            holder.thumbnail_blend.setImageResource(img_effects[position])

            if (selectedindex == position) {
                holder.rl_blenditem.setBackgroundColor(resources.getColor(R.color.colorAccent))
            } else {
                holder.rl_blenditem.setBackgroundColor(resources.getColor(R.color.transparent))
            }

            holder.thumbnail_blend.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    isFromGallery = false
                    bledImage_position = position
                    selectedindex = position

                    var image: Bitmap = BitmapFactory.decodeResource(
                        resources, img_effects[bledImage_position]
                    )
                    if (original_bitmap.getWidth() > original_bitmap.getHeight()) {

                        image = ThumbnailUtils.extractThumbnail(
                            image,
                            original_bitmap.getWidth(),
                            original_bitmap.getHeight(),
                            ThumbnailUtils.OPTIONS_RECYCLE_INPUT
                        )
                    } else if (original_bitmap.getWidth() < original_bitmap.getHeight()) {
                        image = ThumbnailUtils.extractThumbnail(
                            image,
                            original_bitmap.getWidth(),
                            original_bitmap.getHeight(),
                            ThumbnailUtils.OPTIONS_RECYCLE_INPUT
                        )
                    } else {
                        image = image
                    }

                    var gpuImage1 = GPUImage(this@ImageEditActivity)
                    gpuImage1.setImage(original_bitmap)
                    gpuImage1.setFilter(
                        createBlendFilter(
                            filters_blend[blendfilter_position],
                            image
                        )
                    )
                    img_main.setImageBitmap(gpuImage1.bitmapWithFilterApplied)
                    notifyDataSetChanged()
                }

            })
        }


        inner class BlendHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var thumbnail_blend: ImageView =
                itemView.findViewById(R.id.thumbnail_blend) as ImageView
            var rl_blenditem: RelativeLayout =
                itemView.findViewById(R.id.rl_blenditem) as RelativeLayout
        }
    }

    var filter_adjust: Array<GPUImageFilter> = arrayOf(
        GPUImageContrastFilter(),
        GPUImageHighlightShadowFilter(0.0f, 1.0f),//fade
        GPUImageSepiaToneFilter(),
        GPUImageOpacityFilter(1.0f),//grain
        GPUImageBilateralBlurFilter(),//convex
        GPUImageExposureFilter(0.0f),
        GPUImageRGBFilter(1.0f, 1.0f, 1.0f),  //  ambiance
        GPUImageVignetteFilter(PointF(0.5f, 0.5f), floatArrayOf(0.0f, 0.0f, 0.0f), 0.3f, 0.75f),
        GPUImageSharpenFilter(),
        GPUImageWhiteBalanceFilter(),
        GPUImageVibranceFilter(),
        GPUImageSaturationFilter(1.0f),
        GPUImageColorBalanceFilter()//skintone
    )

    inner class AdjustAdapter : RecyclerView.Adapter<AdjustAdapter.AdjustHolder>() {
        var selectedindex = 0
        var imgs_adjust: Array<Int> = arrayOf(
            R.drawable.icon_adjust_contrast,
            R.drawable.icon_adjust_fade,
            R.drawable.icon_adjust_tone,
            R.drawable.icon_adjust_grain,
            R.drawable.icon_adjust_convex,
            R.drawable.icon_adjust_exposure,
            R.drawable.icon_adjust_ambiance,
            R.drawable.icon_adjust_vignette,
            R.drawable.icon_adjust_sharpen,
            R.drawable.icon_adjust_temp,
            R.drawable.icon_adjust_vibrance,
            R.drawable.icon_adjust_saturation,
            R.drawable.icon_adjust_skintone
        )

        var texts_adjust: Array<String> = arrayOf(
            "Contrast",
            "Fade",
            "Tone",
            "Grain",
            "Convex",
            "Exposure",
            "Ambiance",
            "Vignette",
            "Sharpen",
            "Temperature",
            "Vibrance",
            "Saturation",
            "Skintone"
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdjustHolder {
            var view: View = LayoutInflater.from(this@ImageEditActivity)
                .inflate(R.layout.item_adjust, parent, false)

            return AdjustHolder(view)
        }

        override fun getItemCount(): Int {
            return imgs_adjust.size
        }

        override fun onBindViewHolder(holder: AdjustHolder, position: Int) {
            holder.img_adjust.setImageResource(imgs_adjust[position])
            holder.text_adjust.text = texts_adjust[position]

            if (selectedindex == position) {
                holder.item_adjust.setBackgroundColor(resources.getColor(R.color.colorAccent))
            } else {
                holder.item_adjust.setBackgroundColor(resources.getColor(R.color.transparent))
            }

            holder.item_adjust.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {

                    adjust_position = position

                    selectedindex = position

                    filterAdjuster =
                        GPUImageFilterTools.FilterAdjuster(filter_adjust[position])
                    seekbar_adjust1.progress = 90
                    filterAdjuster!!.adjust(seekbar_adjust1.progress)

                    filter_apply(adjust_position)
                    notifyDataSetChanged()
                }

            })

        }

        inner class AdjustHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var img_adjust: ImageView = itemView.findViewById(R.id.img_adjust) as ImageView
            var text_adjust: TextView = itemView.findViewById(R.id.text_adjust) as TextView
            var item_adjust: LinearLayout = itemView.findViewById(R.id.item_adjust) as LinearLayout
        }
    }
}

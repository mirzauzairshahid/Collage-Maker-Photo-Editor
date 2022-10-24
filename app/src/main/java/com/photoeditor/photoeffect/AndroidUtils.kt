package com.photoeditor.photoeffect

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.photoeditor.photoeffect.model.FilterData
import java.io.File

object AndroidUtils {

    var filter_fresh: Array<FilterData> = arrayOf(
        FilterData("Fresh1", 1.45F, 1.29F, 1.55F, 1F),
        FilterData("Fresh2", 1.55F, 1.35F, 1.29F, 1F),
        FilterData("Fresh3", 1.55F, 1.29F, 1.85F, 1F),
        FilterData("Fresh4", 1.55F, 1.29F, 1.50F, 1F),
        FilterData("Fresh5", 1.65F, 1.20F, 1.25F, 1F)
    )

    var filter_euro: Array<FilterData> = arrayOf(
        FilterData("Euro1", 1.45F, 1.35F, 1.10F, 1F),
        FilterData("Euro2", 1.30F, 1.30F, 1.55F, 1F),
        FilterData("Euro3", 1.45F, 1.55F, 1.63F, 1F),
        FilterData("Euro4", 1.1F, 1.35F, 1.35F, 1F)
    )

    var filter_dark: Array<FilterData> = arrayOf(
        FilterData("Dark1", 1.5F, 1.5F, 1.5F, 0F),
        FilterData("Dark2", 0.5F, 0.5F, 0.5F, 0F),
        FilterData("Dark3", 1.0F, 1.0F, 1.0F, 0F)
    )

    var filter_ins: Array<FilterData> = arrayOf(
        FilterData("Ins1", 1.15F, 1.02F, 1.15F, 1F),
        FilterData("Ins2", 1.20F, 1.20F, 1.02F, 1F),
        FilterData("Ins3", 1F, 1.15F, 1.15F, 0.8F),
        FilterData("Ins4", 1.58F, 1.58F, 1.58F, 0.5F)
    )

    var filter_elegant: Array<FilterData> = arrayOf(
        FilterData("Elegant1", 1.35F, 1.38F, 1.33F, 1.1F),
        FilterData("Elegant2", 1.45F, 1.25F, 1.15F, 0.5F),
        FilterData("Elegant3", 1.35F, 1.33F, 1.33F, 0.6F),
        FilterData("Elegant4", 1.55F, 1.53F, 1.53F, 1F),
        FilterData("Elegant5", 1.55F, 1.53F, 1.53F, 0.7F)
    )

    var filter_golden: Array<FilterData> = arrayOf(
        FilterData("Golden1", 1.15F, 1.1F, 1.0F, 0.8F),
        FilterData("Golden2", 0.87F, 0.73F, 0.87F, 1F),
        FilterData("Golden3", 1.0F, 0.9F, 0.7F, 1F),
        FilterData("Golden4", 1.0F, 0.8F, 0.5F, 0.8F)
    )

    var filter_tint: Array<FilterData> = arrayOf(
        FilterData("Tint1", 1.0F, 0.85F, 0.65F, 1F),
        FilterData("Tint2", 0.67F, 1.0F, 0.93F, 1F),
        FilterData("Tint3", 0.61F, 0.77F, 1.3F, 0.9F),
        FilterData("Tint4", 1.47F, 1.23F, 1.42F, 0.4F)
    )

    var filter_film: Array<FilterData> = arrayOf(
        FilterData("Film1", 1.25F, 1.02F, 1.02F, 1F),
        FilterData("Film2", 1.0F, 0.8F, 0.74F, 1F),
        FilterData("Film3", 0.84F, 0.8F, 0.78F, 1F),
        FilterData("Film4", 1.4F, 1.34F, 1.3F, 0.8F),
        FilterData("Film5", 1.0F, 0.75F, 0.72F, 0.7F)
    )

    var filter_lomo: Array<FilterData> = arrayOf(
        FilterData("Lomo1", 0.74F, 0.67F, 0.64F, 0.65F),
        FilterData("Lomo2", 0.95F, 1.14F, 0.95F, 0.85F),
        FilterData("Lomo3", 0.75F, 0.4F, 0.3F, 0.3F),
        FilterData("Lomo4", 1.47F, 1.23F, 1.42F, 0.5F)
    )

    var filter_movie: Array<FilterData> = arrayOf(
        FilterData("Movie1", 1.02F, 1.02F, 1.02F, 0.5F),
        FilterData("Movie2", 1.35F, 1.43F, 1.33F, 0.7F),
        FilterData("Movie3", 1.10F, 1.04F, 1.14F, 1F),
        FilterData("Movie4", 1.7F, 1.3F, 1.8F, 0.6F),
        FilterData("Movie5", 1.20F, 1.1F, 0.95F, 1.2F)
    )

    var filter_retro: Array<FilterData> = arrayOf(
        FilterData("Retro1", 1.25F, 1.02F, 1.02F, 1F),
        FilterData("Retro2", 1.55F, 1.53F, 1.53F, 1F),
        FilterData("Retro3", 1.55F, 2.04F, 2.04F, 1F),
        FilterData("Retro4", 1.25F, 1.55F, 1.55F, 1F)
    )

    var filter_bw: Array<FilterData> = arrayOf(
        FilterData("BW1", 1.2F, 1.02F, 1.02F, 0F),
        FilterData("BW2", 1.55F, 1.53F, 1.53F, 0F),
        FilterData("BW3", 1.85F, 1.8F, 1.85F, 0F)
    )

    var filter_clr1: Array<FilterData> = arrayOf(
        FilterData("Color1", 0.9F, 0.0F, 0.3F, 0F),
        FilterData("Color2", 0.3F, 0.8F, 1.2F, 0F),
        FilterData("Color3", 1.2F, 0.8F, 0.3F, 0F),
        FilterData("Color4", 0.5F, 1.5F, 1.0F, 0.1F)
    )
    var filter_clr2: Array<FilterData> = arrayOf(
        FilterData("Color1", 0.7F, 1.2F, 1.6F, 0F),
        FilterData("Color2", 1.4F, 1.1F, 0.7F, 0F),
        FilterData("Color3", 1.2F, 0.8F, 0.3F, 0F),
        FilterData("Color4", 1.2F, 0.84F, 0.8F, 0.1F)
    )
    var filter_duo: Array<FilterData> = arrayOf(
        FilterData("Duo1", 0.9F, 0.5F, 1.3F, 0F),
        FilterData("Duo2", 0.33F, 0.51F, 1.00F, 0F),
        FilterData("Duo3", 1F, 0.50F, 0.99F, 0F),
        FilterData("Duo4", 0.8F, 0.44F, 0.4F, 0.1F)
    )

    var filter_pink: Array<FilterData> = arrayOf(
        FilterData("Pink1", 1.28F, 0.84F, 1.28F, 0F),
        FilterData("Pink2", 1.55F, 1.04F, 1.04F, 0F),
        FilterData("Pink3", 1.1F, 0.5F, 0.5F, 0F),
        FilterData("Pink4", 1.5F, 1.10F, 1.5F, 0F)
    )

    fun dipTopx(dip: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dip,
            context.resources.displayMetrics
        ).toInt()
    }

    /**
     * px to dp
     * @param px
     * @param context
     * @return
     */
    fun pxTodip(px: Int, context: Context): Float {
        return px / context.resources.displayMetrics.density
    }

    /**
     * Drawable을 Bitmap으로 바꾸어 준다.
     * @param drawable
     * @return
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        return bitmap
    }

    fun resizeImageToNewSize(bitmap: Bitmap, i: Int, i2: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        var f = i.toFloat()
        var f2 = i2.toFloat()
        if (!(height == i2 && width == i)) {
            val f3 = width.toFloat()
            val f4 = f / f3
            val f5 = height.toFloat()
            var f6 = f2 / f5
            if (f4 < f6) {
                f6 = f4
            }
            f = f3 * f6
            f2 = f5 * f6
        }
        return Bitmap.createScaledBitmap(bitmap, f.toInt(), f2.toInt(), true)
    }


    fun drawableToIntArray(drawable: Drawable): IntArray {
        val bitmap = AndroidUtils.drawableToBitmap(drawable)

        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        val colors = IntArray(bitmapWidth * bitmapHeight)
        bitmap.getPixels(colors, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight)

        return colors
    }

    fun bitmapToIntArray(bitmap: Bitmap): IntArray {
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        val colors = IntArray(bitmapWidth * bitmapHeight)
        bitmap.getPixels(colors, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight)

        return colors
    }

    /** Get Bitmap's Width  */
    fun getBitmapOfWidth(res: Resources, id: Int): Int {
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeResource(res, id, options)
            return options.outWidth
        } catch (e: Exception) {
            return 0
        }

    }

    /** Get Bitmap's height  */
    fun getBitmapOfHeight(res: Resources, id: Int): Int {
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeResource(res, id, options)
            return options.outHeight
        } catch (e: Exception) {
            return 0
        }
    }

    val ASSET_PREFIX = "assets://"
    val DRAWABLE_PREFIX = "drawable://"

    fun loadImageWithGlide(context: Context, imageView: ImageView, str: String?) {
        if (str != null && str.length > 1) {
            if (str.startsWith("http://") || str.startsWith("https://")) {
                Glide.with(context).load(str).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView)
            } else if (str.startsWith(DRAWABLE_PREFIX)) {
                try {
                    Glide.with(context)
                        .load(Integer.valueOf(Integer.parseInt(str.substring(DRAWABLE_PREFIX.length)))).into(imageView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else if (str.startsWith(ASSET_PREFIX)) {
                Glide.with(context)
                    .load(Uri.parse("file:///android_asset/" + str.substring(ASSET_PREFIX.length))).signature(
                        (
                            System.currentTimeMillis().toString()
                        ) as Key
                    ).into(imageView)
            } else {
                Glide.with(context).load(File(str)).into(imageView)
            }
        }
    }
}

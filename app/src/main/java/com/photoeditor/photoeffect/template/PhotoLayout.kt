package com.photoeditor.photoeffect.template

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Build
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout

import com.photoeditor.photoeffect.model.ImageTemplate
import com.photoeditor.photoeffect.utils.ImageDecoder
import com.photoeditor.photoeffect.utils.ImageUtils
import com.photoeditor.photoeffect.utils.PhotoUtils

import java.util.ArrayList
import java.util.Collections

/**
 * Created by vanhu_000 on 3/11/2016.
 */
class PhotoLayout : RelativeLayout, ItemImageView.OnImageClickListener {

    internal var mOnDragListener: View.OnDragListener = OnDragListener { v, event ->
        val dragEvent = event.action

        when (dragEvent) {
            DragEvent.ACTION_DRAG_ENTERED -> {
            }

            DragEvent.ACTION_DRAG_EXITED -> {
            }

            DragEvent.ACTION_DROP -> {
                val target = v as ItemImageView
                val dragged = event.localState as ItemImageView
                var targetPath: String? = ""
                var draggedPath: String? = ""
                if (target.photoItem != null)
                    targetPath = target.photoItem.imagePath
                if (dragged.photoItem != null)
                    draggedPath = dragged.photoItem.imagePath
                if (targetPath == null) targetPath = ""
                if (draggedPath == null) draggedPath = ""
                if (targetPath != draggedPath)
                    target.swapImage(dragged)
            }
        }

        true
    }

    private var mPhotoItems: List<PhotoItem>? = null
    private var mImageWidth: Int = 0
    private var mImageHeight: Int = 0
    private var mItemImageViews: MutableList<ItemImageView>? = null
    var backgroundImageView: TransitionImageView? = null
        private set
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0
    private var mInternalScaleRatio = 1f
    private var mOutputScaleRatio = 1f
    var templateImage: Bitmap? = null
        private set

    private var mProgressBar: ProgressBar? = null
    private var mBackgroundImage: Bitmap? = null

    var backgroundImage: Bitmap?
        get() = backgroundImageView!!.image
        set(image) {
            mBackgroundImage = image
        }

    constructor(context: Context, template: ImageTemplate) : super(context) {
        val templateImage = PhotoUtils.decodePNGImage(context, template.mtemplate!!)
        val photoItems = parseImageTemplate(template)
        init(photoItems, templateImage)
    }

    constructor(context: Context, photoItems: List<PhotoItem>, templateImage: Bitmap) : super(
        context
    ) {
        init(photoItems, templateImage)
    }

    private fun init(photoItems: List<PhotoItem>, templateImage: Bitmap?) {
        mPhotoItems = photoItems
        this.templateImage = templateImage
        mImageWidth = this.templateImage!!.width
        mImageHeight = this.templateImage!!.height
        mItemImageViews = ArrayList()
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    private fun asyncCreateBackgroundImage(path: String?) {
        val task = object : AsyncTask<Void, Void, Bitmap>() {
            override fun onPreExecute() {
                super.onPreExecute()
                mProgressBar!!.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg params: Void): Bitmap? {
                try {
                    var image = ImageDecoder.decodeFileToBitmap(path!!)
                    if (image != null) {
                        val result = PhotoUtils.blurImage(image, 10f)
                        if (image != result) {
                            image.recycle()
                            image = null
                            System.gc()
                        }
                        return result
                    }
                } catch (err: OutOfMemoryError) {
                    err.printStackTrace()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(result: Bitmap?) {
                super.onPostExecute(result)
                mProgressBar!!.visibility = View.GONE
                if (result != null)
                    backgroundImageView!!.init(result, mViewWidth, mViewHeight, mOutputScaleRatio)
            }
        }

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    fun build(viewWidth: Int, viewHeight: Int, outputScaleRatio: Float) {
        if (viewWidth < 1 || viewHeight < 1) {
            return
        }
        //add children views
        mViewWidth = viewWidth
        mViewHeight = viewHeight
        mOutputScaleRatio = outputScaleRatio
        mItemImageViews!!.clear()
        mInternalScaleRatio =
            1.0f / PhotoUtils.calculateScaleRatio(mImageWidth, mImageHeight, viewWidth, viewHeight)
        for (item in mPhotoItems!!) {
            mItemImageViews!!.add(addPhotoItemView(item, mInternalScaleRatio, mOutputScaleRatio)!!)
        }
        //add template image
        val templateImageView = ImageView(context)
        if (Build.VERSION.SDK_INT >= 16) {
            templateImageView.background = BitmapDrawable(resources, templateImage)
        } else {
            templateImageView.setBackgroundDrawable(BitmapDrawable(resources, templateImage))
        }

        var params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        addView(templateImageView, params)
        //Create progress bar
        mProgressBar = ProgressBar(context)
        params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        mProgressBar!!.visibility = View.GONE
        addView(mProgressBar, params)
        //add background image
        backgroundImageView = TransitionImageView(context)
        params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        addView(backgroundImageView, 0, params)
        //Create background
        backgroundImageView!!.setOnImageClickListener(object :
            TransitionImageView.OnImageClickListener {
            override fun onLongClickImage(view: TransitionImageView) {

            }

            override fun onDoubleClickImage(view: TransitionImageView) {

            }
        })

        if (mBackgroundImage == null || mBackgroundImage!!.isRecycled) {
            if (mPhotoItems!!.size > 0 && mPhotoItems!![0].imagePath != null && mPhotoItems!![0].imagePath!!.length > 0) {
                asyncCreateBackgroundImage(mPhotoItems!![0].imagePath)
            }
        } else {
            backgroundImageView!!.init(mBackgroundImage!!, mViewWidth, mViewHeight, mOutputScaleRatio)
        }
    }

    private fun addPhotoItemView(
        item: PhotoItem?,
        internalScale: Float,
        outputScaleRatio: Float
    ): ItemImageView? {
        if (item == null || item.maskPath == null) {
            return null
        }
        val imageView = ItemImageView(context, item)
        val viewWidth = internalScale * imageView.maskImage!!.width
        val viewHeight = internalScale * imageView.maskImage!!.height
        imageView.init(viewWidth, viewHeight, outputScaleRatio)
        imageView.setOnImageClickListener(this)
        if (mPhotoItems!!.size > 1)
            imageView.setOnDragListener(mOnDragListener)

        val params = RelativeLayout.LayoutParams(viewWidth.toInt(), viewHeight.toInt())
        params.leftMargin = (internalScale * item.x).toInt()
        params.topMargin = (internalScale * item.y).toInt()
        imageView.originalLayoutParams = params
        addView(imageView, params)
        return imageView
    }

    fun createImage(): Bitmap {
        val template = Bitmap.createBitmap(
            (mOutputScaleRatio * mViewWidth).toInt(),
            (mOutputScaleRatio * mViewHeight).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(template)
        val paint = Paint()
        paint.isAntiAlias = true
        if (backgroundImageView!!.image != null && !backgroundImageView!!.image!!.isRecycled) {
            canvas.drawBitmap(
                backgroundImageView!!.image!!,
                backgroundImageView!!.scaleMatrix,
                paint
            )
        }

        canvas.saveLayer(
            0f,
            0f,
            template.width.toFloat(),
            template.height.toFloat(),
            paint,
            Canvas.ALL_SAVE_FLAG
        )

        for (view in mItemImageViews!!)
            if (view.image != null && !view.image!!.isRecycled) {
                val left = (view.left * mOutputScaleRatio).toInt()
                val top = (view.top * mOutputScaleRatio).toInt()
                val width = (view.width * mOutputScaleRatio).toInt()
                val height = (view.height * mOutputScaleRatio).toInt()
                canvas.saveLayer(
                    left.toFloat(),
                    top.toFloat(),
                    (left + width).toFloat(),
                    (top + height).toFloat(),
                    paint,
                    Canvas.ALL_SAVE_FLAG
                )
                //draw image
                canvas.save()
                canvas.translate(left.toFloat(), top.toFloat())
                canvas.clipRect(0, 0, width, height)
                canvas.drawBitmap(view.image!!, view.scaleMatrix, paint)
                canvas.restore()
                //draw mask
                canvas.save()
                canvas.translate(left.toFloat(), top.toFloat())
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
                canvas.drawBitmap(view.maskImage!!, view.scaleMaskMatrix, paint)
                paint.xfermode = null
                canvas.restore()
                canvas.restore()
            }
        //draw frame
        if (templateImage != null) {
            canvas.drawBitmap(
                templateImage!!,
                ImageUtils.createMatrixToDrawImageInCenterView(
                    mOutputScaleRatio * mViewWidth, mOutputScaleRatio * mViewHeight,
                    templateImage!!.width.toFloat(), templateImage!!.height.toFloat()
                ), paint
            )
        }

        canvas.restore()

        return template
    }

    fun recycleImages(recycleBackground: Boolean) {
        if (recycleBackground) {
            backgroundImageView!!.recycleImages()
        }

        for (view in mItemImageViews!!) {
            view.recycleImages(recycleBackground)
        }
        if (templateImage != null && !templateImage!!.isRecycled) {
            templateImage!!.recycle()
            templateImage = null
        }
        System.gc()
    }

    override fun onLongClickImage(v: ItemImageView) {
        if (mPhotoItems!!.size > 1) {
            v.tag = "x=" + v.photoItem.x + ",y=" + v.photoItem.y + ",path=" + v.photoItem.imagePath
            val item = ClipData.Item(v.tag as CharSequence)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val dragData = ClipData(v.tag.toString(), mimeTypes, item)
            val myShadow = View.DragShadowBuilder(v)
            v.startDrag(dragData, myShadow, v, 0)
        }
    }

    override fun onDoubleClickImage(v: ItemImageView) {}

    companion object {
        private val TAG = PhotoLayout::class.java.simpleName

        //action id
        private val ID_EDIT = 1
        private val ID_CHANGE = 2
        private val ID_DELETE = 3
        private val ID_CANCEL = 4

        fun parseImageTemplate(template: ImageTemplate): ArrayList<PhotoItem> {
            val photoItems = ArrayList<PhotoItem>()
            try {
                val childTexts =
                    template.child!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                if (childTexts != null) {
                    for (child in childTexts) {
                        val properties =
                            child.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (properties != null) {
                            val item = PhotoItem()
                            item.index = Integer.parseInt(properties[0])
                            item.x = Integer.parseInt(properties[1]).toFloat()
                            item.y = Integer.parseInt(properties[2]).toFloat()
                            item.maskPath = properties[3]
                            photoItems.add(item)
                        }
                    }
                    //Sort via index
                    Collections.sort(photoItems) { lhs, rhs -> rhs.index - lhs.index }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return photoItems
        }
    }
}

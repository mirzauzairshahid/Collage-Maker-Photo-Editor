package com.photoeditor.photoeffect.template

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout

import com.photoeditor.photoeffect.multitouch.MultiTouchHandler
import com.photoeditor.photoeffect.utils.ImageDecoder
import com.photoeditor.photoeffect.utils.ImageUtils
import com.photoeditor.photoeffect.utils.PhotoUtils
import com.photoeditor.photoeffect.utils.ResultContainer

class ItemImageView(context: Context, val photoItem: PhotoItem) : ImageView(context) {

    private val mGestureDetector: GestureDetector
    private var mTouchHandler: MultiTouchHandler? = null
    var image: Bitmap? = null
    var maskImage: Bitmap? = null
        private set
    private val mPaint: Paint
    private val mImageMatrix: Matrix
    val scaleMatrix: Matrix
    val maskMatrix: Matrix
    val scaleMaskMatrix: Matrix
    var viewWidth: Float = 0.toFloat()
        private set
    var viewHeight: Float = 0.toFloat()
        private set
    private var mOutputScale = 1f
    private var mOnImageClickListener: OnImageClickListener? = null
    private var mOriginalLayoutParams: RelativeLayout.LayoutParams? = null
    private var mEnableTouch = true

    var originalLayoutParams: RelativeLayout.LayoutParams
        get() {
            if (mOriginalLayoutParams != null) {
                val params = RelativeLayout.LayoutParams(
                    mOriginalLayoutParams!!.width,
                    mOriginalLayoutParams!!.height
                )
                params.leftMargin = mOriginalLayoutParams!!.leftMargin
                params.topMargin = mOriginalLayoutParams!!.topMargin
                return params
            } else {
                return layoutParams as RelativeLayout.LayoutParams
            }
        }
        set(originalLayoutParams) {
            mOriginalLayoutParams =
                RelativeLayout.LayoutParams(originalLayoutParams.width, originalLayoutParams.height)
            mOriginalLayoutParams!!.leftMargin = originalLayoutParams.leftMargin
            mOriginalLayoutParams!!.topMargin = originalLayoutParams.topMargin
        }

    interface OnImageClickListener {
        fun onLongClickImage(view: ItemImageView)

        fun onDoubleClickImage(view: ItemImageView)
    }

    init {
        if (photoItem.imagePath != null && photoItem.imagePath!!.length > 0) {
            image = ResultContainer.getInstance().getImage(photoItem.imagePath!!)
            if (image == null || image!!.isRecycled) {
                image = ImageDecoder.decodeFileToBitmap(photoItem.imagePath!!)
                ResultContainer.getInstance().putImage(photoItem.imagePath!!, image!!)

            }
        }

        if (photoItem.maskPath != null && photoItem.maskPath!!.length > 0) {
            maskImage = ResultContainer.getInstance().getImage(photoItem.maskPath!!)
            if (maskImage == null || maskImage!!.isRecycled) {
                maskImage = PhotoUtils.decodePNGImage(context, photoItem.maskPath!!)
                ResultContainer.getInstance().putImage(photoItem.maskPath!!, maskImage!!)

            }
        }

        mPaint = Paint()
        mPaint.isFilterBitmap = true
        mPaint.isAntiAlias = true
        scaleType = ImageView.ScaleType.MATRIX
        setLayerType(View.LAYER_TYPE_HARDWARE, mPaint)
        mImageMatrix = Matrix()
        scaleMatrix = Matrix()
        maskMatrix = Matrix()
        scaleMaskMatrix = Matrix()

        mGestureDetector =
            GestureDetector(getContext(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent) {
                    if (mOnImageClickListener != null) {
                        mOnImageClickListener!!.onLongClickImage(this@ItemImageView)
                    }
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    if (mOnImageClickListener != null) {
                        mOnImageClickListener!!.onDoubleClickImage(this@ItemImageView)
                    }
                    return true
                }
            })
    }

    fun swapImage(view: ItemImageView) {
        val temp = view.image
        view.image = image
        image = temp

        val tmpPath = view.photoItem.imagePath
        view.photoItem.imagePath = photoItem.imagePath
        photoItem.imagePath = tmpPath
        resetImageMatrix()
        view.resetImageMatrix()
    }

    fun setOnImageClickListener(onImageClickListener: OnImageClickListener) {
        mOnImageClickListener = onImageClickListener
    }

    override fun getImageMatrix(): Matrix {
        return mImageMatrix
    }

    fun init(viewWidth: Float, viewHeight: Float, scale: Float) {
        this.viewWidth = viewWidth
        this.viewHeight = viewHeight
        mOutputScale = scale
        if (image != null) {
            mImageMatrix.set(
                ImageUtils.createMatrixToDrawImageInCenterView(
                    viewWidth,
                    viewHeight,
                    image!!.width.toFloat(),
                    image!!.height.toFloat()
                )
            )
            scaleMatrix.set(
                ImageUtils.createMatrixToDrawImageInCenterView(
                    scale * viewWidth,
                    scale * viewHeight,
                    image!!.width.toFloat(),
                    image!!.height.toFloat()
                )
            )
        }
        //mask
        if (maskImage != null) {
            maskMatrix.set(
                ImageUtils.createMatrixToDrawImageInCenterView(
                    viewWidth,
                    viewHeight,
                    maskImage!!.width.toFloat(),
                    maskImage!!.height.toFloat()
                )
            )
            scaleMaskMatrix.set(
                ImageUtils.createMatrixToDrawImageInCenterView(
                    scale * viewWidth,
                    scale * viewHeight,
                    maskImage!!.width.toFloat(),
                    maskImage!!.height.toFloat()
                )
            )
        }

        mTouchHandler = MultiTouchHandler()
        mTouchHandler!!.setMatrices(mImageMatrix, scaleMatrix)
        mTouchHandler!!.setScale(scale)
        mTouchHandler!!.setEnableRotation(true)
        invalidate()
    }

    fun setImagePath(imagePath: String) {
        photoItem.imagePath = imagePath
        recycleMainImage()
        image = ImageDecoder.decodeFileToBitmap(imagePath)
        mImageMatrix.set(
            ImageUtils.createMatrixToDrawImageInCenterView(
                viewWidth,
                viewHeight,
                image!!.width.toFloat(),
                image!!.height.toFloat()
            )
        )
        scaleMatrix.set(
            ImageUtils.createMatrixToDrawImageInCenterView(
                mOutputScale * viewWidth,
                mOutputScale * viewHeight,
                image!!.width.toFloat(),
                image!!.height.toFloat()
            )
        )
        mTouchHandler!!.setMatrices(mImageMatrix, scaleMatrix)
        invalidate()
        ResultContainer.getInstance().putImage(photoItem.imagePath!!, image!!)
    }

    fun resetImageMatrix() {
        mImageMatrix.set(
            ImageUtils.createMatrixToDrawImageInCenterView(
                viewWidth,
                viewHeight,
                image!!.width.toFloat(),
                image!!.height.toFloat()
            )
        )
        scaleMatrix.set(
            ImageUtils.createMatrixToDrawImageInCenterView(
                mOutputScale * viewWidth,
                mOutputScale * viewHeight,
                image!!.width.toFloat(),
                image!!.height.toFloat()
            )
        )
        mTouchHandler!!.setMatrices(mImageMatrix, scaleMatrix)
        invalidate()
    }

    fun clearMainImage() {
        photoItem.imagePath = null
        recycleMainImage()
        invalidate()
    }

    private fun recycleMainImage() {
        if (image != null && !image!!.isRecycled) {
            image!!.recycle()
            image = null
            System.gc()
        }
    }

    private fun recycleMaskImage() {
        if (maskImage != null && !maskImage!!.isRecycled) {
            maskImage!!.recycle()
            maskImage = null
            System.gc()
        }
    }

    fun recycleImages(recycleMainImage: Boolean) {

        if (recycleMainImage) {
            recycleMainImage()
        }
        recycleMaskImage()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (image != null && !image!!.isRecycled && maskImage != null && !maskImage!!.isRecycled) {
            canvas.drawBitmap(image!!, mImageMatrix, mPaint)
            mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            canvas.drawBitmap(maskImage!!, maskMatrix, mPaint)
            mPaint.xfermode = null
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mEnableTouch) {
            return super.onTouchEvent(event)
        } else {
            mGestureDetector.onTouchEvent(event)
            if (mTouchHandler != null && image != null && !image!!.isRecycled) {
                mTouchHandler!!.touch(event)
                mImageMatrix.set(mTouchHandler!!.matrix)
                scaleMatrix.set(mTouchHandler!!.scaleMatrix)
                invalidate()
                return true
            } else {
                return true//super.onTouchEvent(event);
            }
        }
    }

    fun setEnableTouch(enableTouch: Boolean) {
        mEnableTouch = enableTouch
    }

    companion object {
        private val TAG = ItemImageView::class.java.simpleName
    }
}

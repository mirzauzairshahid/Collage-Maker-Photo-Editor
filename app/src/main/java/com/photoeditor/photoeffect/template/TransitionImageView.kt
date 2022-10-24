package com.photoeditor.photoeffect.template

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView

import com.photoeditor.photoeffect.multitouch.MultiTouchHandler
import com.photoeditor.photoeffect.utils.ImageDecoder
import com.photoeditor.photoeffect.utils.ImageUtils

class TransitionImageView(context: Context) : ImageView(context) {

    private val mGestureDetector: GestureDetector
    private var mTouchHandler: MultiTouchHandler? = null
    private val mImageMatrix: Matrix
    val scaleMatrix: Matrix
    var image: Bitmap? = null
        private set
    private val mPaint: Paint
    private var mOnImageClickListener: OnImageClickListener? = null
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0
    private var mScale = 1f

    interface OnImageClickListener {
        fun onLongClickImage(view: TransitionImageView)

        fun onDoubleClickImage(view: TransitionImageView)
    }

    init {
        scaleType = ImageView.ScaleType.MATRIX
        mPaint = Paint()
        mPaint.isFilterBitmap = true
        mPaint.isAntiAlias = true
        mImageMatrix = Matrix()
        scaleMatrix = Matrix()
        mGestureDetector =
            GestureDetector(getContext(), object : GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent) {
                    if (mOnImageClickListener != null) {
                        mOnImageClickListener!!.onLongClickImage(this@TransitionImageView)
                    }
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    if (mOnImageClickListener != null) {
                        mOnImageClickListener!!.onDoubleClickImage(this@TransitionImageView)
                    }
                    return true
                }
            })
    }

    fun reset() {
        mTouchHandler = null
        scaleType = ImageView.ScaleType.MATRIX
    }

    fun setOnImageClickListener(onImageClickListener: OnImageClickListener) {
        mOnImageClickListener = onImageClickListener
    }

    fun recycleImages() {
        if (image != null && !image!!.isRecycled) {
            image!!.recycle()
            image = null
            System.gc()
            invalidate()
        }
    }

    fun setImagePath(path: String) {
        recycleImages()
        val image = ImageDecoder.decodeFileToBitmap(path)
        if (image != null)
            init(image, mViewWidth, mViewHeight, mScale)
    }

    fun init(image: Bitmap, viewWidth: Int, viewHeight: Int, scale: Float) {
        this.image = image
        mViewWidth = viewWidth
        mViewHeight = viewHeight
        mScale = scale
        if (this.image != null) {
            mImageMatrix.set(
                ImageUtils.createMatrixToDrawImageInCenterView(
                    viewWidth.toFloat(),
                    viewHeight.toFloat(),
                    this.image!!.width.toFloat(),
                    this.image!!.height.toFloat()
                )
            )
            scaleMatrix.set(
                ImageUtils.createMatrixToDrawImageInCenterView(
                    scale * viewWidth,
                    scale * viewHeight,
                    this.image!!.width.toFloat(),
                    this.image!!.height.toFloat()
                )
            )
        }

        mTouchHandler = MultiTouchHandler()
        mTouchHandler!!.setMatrices(mImageMatrix, scaleMatrix)
        mTouchHandler!!.setScale(scale)
        mTouchHandler!!.setEnableRotation(false)
        mTouchHandler!!.setEnableZoom(false)
        val ratioWidth = viewWidth.toFloat() / this.image!!.width
        val ratioHeight = viewHeight.toFloat() / this.image!!.height
        if (ratioWidth > ratioHeight) {
            mTouchHandler!!.setEnableTranslateX(false)
            val maxOffset = (ratioWidth * this.image!!.height - viewHeight) / 2.0f
            mTouchHandler!!.setMaxPositionOffset(maxOffset)
        } else {
            mTouchHandler!!.setEnableTranslateY(false)
            val maxOffset = (ratioHeight * this.image!!.width - viewWidth) / 2.0f
            mTouchHandler!!.setMaxPositionOffset(maxOffset)
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (image != null && !image!!.isRecycled) {
            canvas.drawBitmap(image!!, mImageMatrix, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mGestureDetector.onTouchEvent(event)
        if (mTouchHandler != null && image != null && !image!!.isRecycled) {
            mTouchHandler!!.touch(event)
            mImageMatrix.set(mTouchHandler!!.matrix)
            scaleMatrix.set(mTouchHandler!!.scaleMatrix)
            invalidate()
        }
        return true
    }

    override fun getImageMatrix(): Matrix {
        return mImageMatrix
    }
}

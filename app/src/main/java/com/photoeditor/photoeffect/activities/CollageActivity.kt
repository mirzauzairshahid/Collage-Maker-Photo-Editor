package com.photoeditor.photoeffect.activities

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.photoeditor.photoeffect.adapter.BackgroundAdapter
import com.photoeditor.photoeffect.adapter.FrameAdapter
import com.photoeditor.photoeffect.frame.FramePhotoLayout
import com.photoeditor.photoeffect.multitouch.PhotoView
import com.photoeditor.photoeffect.model.TemplateItem
import com.photoeditor.photoeffect.utils.FrameImageUtils
import com.photoeditor.photoeffect.utils.ImageUtils
import android.content.Intent
import com.photoeditor.photoeffect.AndroidUtils
import com.photoeditor.photoeffect.R
import com.photoeditor.photoeffect.databinding.ActivityCollageBinding
import java.io.*


class CollageActivity : AppCompatActivity(), View.OnClickListener,
    FrameAdapter.OnFrameClickListener, BackgroundAdapter.OnBGClickListener {

    var mFramePhotoLayout: FramePhotoLayout? = null
    var DEFAULT_SPACE: Float = 0.0f
    var MAX_SPACE: Float = 0.0f
    var MAX_CORNER: Float = 0.0f

    protected val RATIO_SQUARE = 0
    protected val RATIO_GOLDEN = 2

    private var mSpace = DEFAULT_SPACE
    private var mCorner = 0f
    val MAX_SPACE_PROGRESS = 300.0f
    val MAX_CORNER_PROGRESS = 200.0f
    private var mBackgroundColor = Color.WHITE
    private var mBackgroundImage: Bitmap? = null
    private var mSavedInstanceState: Bundle? = null
    protected var mLayoutRatio = RATIO_SQUARE
    protected lateinit var mPhotoView: PhotoView
    protected var mOutputScale = 1f
    protected var mSelectedTemplateItem: TemplateItem? = null
    private var mImageInTemplateCount = 0
    protected var mTemplateItemList: ArrayList<TemplateItem>? = ArrayList()
    protected var mSelectedPhotoPaths: MutableList<String> = java.util.ArrayList()

    lateinit var frameAdapter: FrameAdapter

    override fun onBGClick(drawable: Drawable) {

        val bmp = mFramePhotoLayout!!.createImage()
        val bitmap = (drawable as BitmapDrawable).bitmap
        mBackgroundImage = AndroidUtils.resizeImageToNewSize(bitmap, bmp.width, bmp.height)

//        img_background.background = BitmapDrawable(resources, mBackgroundImage)
        binding.imgBackground.setImageBitmap(mBackgroundImage)

    }

    override fun onFrameClick(templateItem: TemplateItem) {

        mSelectedTemplateItem!!.isSelected = false

        for (idx in 0 until mSelectedTemplateItem!!.photoItemList.size) {
            val photoItem = mSelectedTemplateItem!!.photoItemList[idx]
            if (photoItem.imagePath != null && photoItem.imagePath!!.length > 0) {
                if (idx < mSelectedPhotoPaths.size) {
                    mSelectedPhotoPaths.add(idx, photoItem.imagePath!!)
                } else {
                    mSelectedPhotoPaths.add(photoItem.imagePath!!)
                }
            }
        }

        val size = Math.min(mSelectedPhotoPaths.size, templateItem.photoItemList.size)
        for (idx in 0 until size) {
            val photoItem = templateItem.photoItemList[idx]
            if (photoItem.imagePath.isNullOrEmpty()) {
                photoItem.imagePath = mSelectedPhotoPaths[idx]
            }
        }

        mSelectedTemplateItem = templateItem
        mSelectedTemplateItem!!.isSelected = true
        frameAdapter.notifyDataSetChanged()
        buildLayout(templateItem)
    }

    inner class space_listener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            mSpace = MAX_SPACE * seekBar!!.progress / MAX_SPACE_PROGRESS
            if (mFramePhotoLayout != null)
                mFramePhotoLayout!!.setSpace(mSpace, mCorner)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    }

    inner class corner_listener : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            mCorner = MAX_CORNER * seekBar!!.progress / MAX_CORNER_PROGRESS
            if (mFramePhotoLayout != null)
                mFramePhotoLayout!!.setSpace(mSpace, mCorner)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.tab_layout -> {
                binding.tabLayout.setBackgroundColor(resources.getColor(R.color.colorAccent))
                binding.tabBorder.setBackgroundColor(resources.getColor(R.color.windowBackground))
                binding.tabBg.setBackgroundColor(resources.getColor(R.color.windowBackground))

                binding.llFrame.visibility = View.VISIBLE
                binding.llBorder.visibility = View.GONE
                binding.llBg.visibility = View.GONE
            }

            R.id.tab_border -> {
                binding.tabLayout.setBackgroundColor(resources.getColor(R.color.windowBackground))
                binding.tabBorder.setBackgroundColor(resources.getColor(R.color.colorAccent))
                binding.tabBg.setBackgroundColor(resources.getColor(R.color.windowBackground))

                binding.llFrame.visibility = View.GONE
                binding.llBorder.visibility = View.VISIBLE
                binding.llBg.visibility = View.GONE
            }

            R.id.tab_bg -> {
                binding.tabLayout.setBackgroundColor(resources.getColor(R.color.windowBackground))
                binding.tabBorder.setBackgroundColor(resources.getColor(R.color.windowBackground))
                binding.tabBg.setBackgroundColor(resources.getColor(R.color.colorAccent))

                binding.llFrame.visibility = View.GONE
                binding.llBorder.visibility = View.GONE
                binding.llBg.visibility = View.VISIBLE

            }

            R.id.btn_next -> {

                var outStream: FileOutputStream? = null
                try {
                    val collageBitmap = createOutputImage()
                    outStream = FileOutputStream(File(cacheDir, "tempBMP"))
                    collageBitmap.compress(Bitmap.CompressFormat.JPEG, 75, outStream)
                    outStream.close()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val intent = Intent(this, FilterCollageActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private val binding by lazy {
        ActivityCollageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        DEFAULT_SPACE = ImageUtils.pxFromDp(this, 2F)
        MAX_SPACE = ImageUtils.pxFromDp(this, 30F)
        MAX_CORNER = ImageUtils.pxFromDp(this, 60F)
        mSpace = DEFAULT_SPACE

        if (savedInstanceState != null) {
            mSpace = savedInstanceState.getFloat("mSpace")
            mCorner = savedInstanceState.getFloat("mCorner")
            mSavedInstanceState = savedInstanceState
        }

        mImageInTemplateCount = intent.getIntExtra("imagesinTemplate", 0)
        val extraImagePaths = intent.getStringArrayListExtra("selectedImages")

        binding.listBg.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.listBg.adapter = BackgroundAdapter(this, this)

        binding.tabLayout.setOnClickListener(this)
        binding.tabBorder.setOnClickListener(this)
        binding.tabBg.setOnClickListener(this)

        binding.seekbarSpace.setOnSeekBarChangeListener(space_listener())
        binding.seekbarCorner.setOnSeekBarChangeListener(corner_listener())

        mPhotoView = PhotoView(this)
        binding.rlContainer.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    mOutputScale = ImageUtils.calculateOutputScaleFactor(
                        binding.rlContainer.width,
                        binding.rlContainer.height
                    )
                    buildLayout(mSelectedTemplateItem!!)
                    // remove listener
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        binding.rlContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    } else {
                        binding.rlContainer.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                }
            })

        loadFrameImages()
        binding.listFrames.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        frameAdapter = FrameAdapter(this, mTemplateItemList!!, this)
        binding.listFrames.adapter = frameAdapter


        mSelectedTemplateItem = mTemplateItemList!![0]
        mSelectedTemplateItem!!.isSelected = true

        if (extraImagePaths != null) {
            val size =
                Math.min(extraImagePaths.size, mSelectedTemplateItem!!.photoItemList.size)
            for (i in 0 until size)
                mSelectedTemplateItem!!.photoItemList[i].imagePath = extraImagePaths[i]
        }

        binding.btnNext.setOnClickListener(this)
    }

    private fun loadFrameImages() {
        val mAllTemplateItemList = java.util.ArrayList<TemplateItem>()

        mAllTemplateItemList.addAll(FrameImageUtils.loadFrameImages(this))

        mTemplateItemList = java.util.ArrayList<TemplateItem>()
        if (mImageInTemplateCount > 0) {
            for (item in mAllTemplateItemList)
                if (item.photoItemList.size === mImageInTemplateCount) {
                    mTemplateItemList!!.add(item)
                }
        } else {
            mTemplateItemList!!.addAll(mAllTemplateItemList)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putFloat("mSpace", mSpace)
        outState.putFloat("mCornerBar", mCorner)
        if (mFramePhotoLayout != null) {
            mFramePhotoLayout!!.saveInstanceState(outState)
        }

    }

    fun buildLayout(item: TemplateItem) {
        mFramePhotoLayout = FramePhotoLayout(this, item.photoItemList)

//        if (mBackgroundImage != null && !mBackgroundImage!!.isRecycled()) {
//            if (Build.VERSION.SDK_INT >= 16)
//                rl_container.setBackground(BitmapDrawable(resources, mBackgroundImage))
//            else
//                rl_container.setBackgroundDrawable(BitmapDrawable(resources, mBackgroundImage))
//        } else {
//            rl_container.setBackgroundColor(mBackgroundColor)
//        }

        var viewWidth = binding.rlContainer.width
        var viewHeight = binding.rlContainer.height
        if (mLayoutRatio === RATIO_SQUARE) {
            if (viewWidth > viewHeight) {
                viewWidth = viewHeight
            } else {
                viewHeight = viewWidth
            }
        } else if (mLayoutRatio === RATIO_GOLDEN) {
            val goldenRatio = 1.61803398875
            if (viewWidth <= viewHeight) {
                if (viewWidth * goldenRatio >= viewHeight) {
                    viewWidth = (viewHeight / goldenRatio).toInt()
                } else {
                    viewHeight = (viewWidth * goldenRatio).toInt()
                }
            } else if (viewHeight <= viewWidth) {
                if (viewHeight * goldenRatio >= viewWidth) {
                    viewHeight = (viewWidth / goldenRatio).toInt()
                } else {
                    viewWidth = (viewHeight * goldenRatio).toInt()
                }
            }
        }

        mOutputScale = ImageUtils.calculateOutputScaleFactor(viewWidth, viewHeight)
        mFramePhotoLayout!!.build(viewWidth, viewHeight, mOutputScale, mSpace, mCorner)
        if (mSavedInstanceState != null) {
            mFramePhotoLayout!!.restoreInstanceState(mSavedInstanceState!!)
            mSavedInstanceState = null
        }
        val params = RelativeLayout.LayoutParams(viewWidth, viewHeight)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        binding.rlContainer.removeAllViews()

        binding.rlContainer.removeView(binding.imgBackground)
        binding.rlContainer.addView(binding.imgBackground, params)

        binding.rlContainer.addView(mFramePhotoLayout, params)
        //add sticker view
        binding.rlContainer.removeView(mPhotoView)
        binding.rlContainer.addView(mPhotoView, params)
        //reset space and corner seek bars

        binding.seekbarSpace.progress = (MAX_SPACE_PROGRESS * mSpace / MAX_SPACE).toInt()
        binding.seekbarCorner.progress = (MAX_CORNER_PROGRESS * mCorner / MAX_CORNER).toInt()
    }

    @Throws(OutOfMemoryError::class)
    fun createOutputImage(): Bitmap {
        try {
            val template = mFramePhotoLayout!!.createImage()
            val result =
                Bitmap.createBitmap(template.width, template.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(result)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            if (mBackgroundImage != null && !mBackgroundImage!!.isRecycled) {
                canvas.drawBitmap(
                    mBackgroundImage!!,
                    Rect(0, 0, mBackgroundImage!!.width, mBackgroundImage!!.height),
                    Rect(0, 0, result.width, result.height),
                    paint
                )
            } else {
                canvas.drawColor(mBackgroundColor)
            }

            canvas.drawBitmap(template, 0f, 0f, paint)
            template.recycle()
            var stickers = mPhotoView.getImage(mOutputScale)
            canvas.drawBitmap(stickers!!, 0f, 0f, paint)
            stickers.recycle()
            stickers = null
            System.gc()
            return result
        } catch (error: OutOfMemoryError) {
            throw error
        }
    }
}

package com.photoeditor.photoeffect.stickerview


import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


/**
 * Created by cheungchingai on 6/15/15.
 */
class StickerTextView : StickerView {
    var tv_main: AutoResizeTextView? = null

    override//tv_main.setTextSize(22);
    val mainView: View
        get() {
            if (tv_main != null)
                return tv_main as AutoResizeTextView

            tv_main = AutoResizeTextView(context)
            tv_main!!.setTextColor(Color.WHITE)
            tv_main!!.gravity = Gravity.CENTER
            tv_main!!.textSize = 300f
            tv_main!!.setShadowLayer(4f, 0f, 0f, Color.BLACK)
            tv_main!!.maxLines = 1
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            params.gravity = Gravity.CENTER
            tv_main!!.layoutParams = params
            if (imageViewFlip != null)
                imageViewFlip!!.visibility = View.GONE
            return tv_main as AutoResizeTextView
        }

    var text: String?
        get() = if (tv_main != null) tv_main!!.text.toString() else null
        set(text) {
            if (tv_main != null)
                tv_main!!.text = text
        }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun onScaling(scaleUp: Boolean) {
        super.onScaling(scaleUp)
    }

    companion object {

        fun pixelsToSp(context: Context, px: Float): Float {
            val scaledDensity = context.resources.displayMetrics.scaledDensity
            return px / scaledDensity
        }
    }
}

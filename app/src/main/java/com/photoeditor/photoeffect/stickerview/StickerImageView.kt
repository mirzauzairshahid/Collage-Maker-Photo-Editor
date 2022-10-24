package com.photoeditor.photoeffect.stickerview


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView


class StickerImageView : StickerView {

    var ownerId: String? = null
    private var iv_main: ImageView? = null

    override val mainView: View
        get() {
            if (this.iv_main == null) {
                this.iv_main = ImageView(context)
                this.iv_main!!.scaleType = ImageView.ScaleType.FIT_XY
            }
            return iv_main as ImageView
        }

    var imageBitmap: Bitmap
        get() = (this.iv_main!!.drawable as BitmapDrawable).bitmap
        set(bmp) = this.iv_main!!.setImageBitmap(bmp)

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    fun setImageResource(res_id: Int) {
        this.iv_main!!.setImageResource(res_id)
    }

    fun setImageDrawable(drawable: Drawable) {
        this.iv_main!!.setImageDrawable(drawable)
    }

}

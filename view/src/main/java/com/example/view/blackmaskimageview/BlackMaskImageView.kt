package com.example.view.blackmaskimageview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ImageView

class BlackMaskImageView @JvmOverloads constructor(context : Context, attributeSet : AttributeSet? = null, defaultStyle : Int = 0)
    : ImageView(context, attributeSet, defaultStyle){

    private var mIsBlackMask = false

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mIsBlackMask) {
            canvas?.drawColor(Color.parseColor("#4C000000"))
        }
    }

    public fun setBlackMask(isBlackMask : Boolean) {
        this.mIsBlackMask = isBlackMask
        invalidate()
    }
}
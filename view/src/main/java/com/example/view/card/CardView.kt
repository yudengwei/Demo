package com.example.view.card

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.view.R

class CardView @JvmOverloads constructor(context : Context, attributeSet : AttributeSet? = null, defaultStyle : Int = 0)
    : View(context, attributeSet, defaultStyle){

    private val mSrcBm = BitmapFactory.decodeResource(resources, R.drawable.img_sample)

    private val mPaint = Paint().also {
        it.style = Paint.Style.FILL
        it.color = Color.RED
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        canvas?.let {
            mPaint.setShadowLayer(10f, 0f, 10f, Color.GRAY)
            mPaint.color = Color.WHITE
            it.drawRoundRect(RectF(width / 4F, height / 4F, width * 3 / 4f, height / 2f), 20f, 20f, mPaint)
        }
    }
}
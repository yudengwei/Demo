package com.example.view.path

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.base.util.LogUtil

class PathView @JvmOverloads constructor(context: Context, attributeSet : AttributeSet? = null, defStyleAttr : Int = 0) : View(context, attributeSet, defStyleAttr) {

    private var mWidth = 0
    private var mHeight = 0

    private val mPath = Path()

    private val mPaint = Paint().also {
        it.style = Paint.Style.STROKE
        it.color = Color.BLACK
        it.strokeWidth = 5f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val centerX = (mWidth / 2).toFloat()
            val centerY = (mHeight / 2).toFloat()
            it.translate(centerX, centerY)
            val path1 = Path()
            path1.addOval(RectF(-100f, -100f, 300f, 100f), Path.Direction.CW)
            //mPaint.color = Color.GREEN
            //it.drawPath(path1, mPaint)
            val dst = Path()
            val pm = PathMeasure(path1, false)
            //pm.getSegment(50f,300f, dst, true)
            //pm.getSegment(300f, 500f, dst, true)
            pm.getSegment(400f, 700f, dst, false)
            val path2 = Path()
            path2.addOval(RectF(-100f, -100f, 400f, 200f), Path.Direction.CW)
            val dst1 = Path()
            val pm1 = PathMeasure(path2, false)
            //pm.getSegment(50f,300f, dst, true)
            //pm.getSegment(300f, 500f, dst, true)
            pm1.getSegment(100f, 300f, dst1, false)
            dst.addPath(dst1)
            mPaint.color = Color.RED
            it.drawPath(dst, mPaint)
        }
    }
}
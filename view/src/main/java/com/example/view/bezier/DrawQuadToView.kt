package com.example.view.bezier

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

//二阶贝塞尔曲线
class DrawQuadToView @JvmOverloads constructor(context : Context, attributeSet : AttributeSet? = null, defStyleAttr : Int = 0)
    : View(context, attributeSet, defStyleAttr) {

    var centerX = 0f
    var centery = 0f
    var startX = 0f
    var startY = 0f
    var endX = 0f
    var endY = 0f
    var eventX = 0f
    var eventY = 0f
    var legth = 150f

    val mPaint = Paint().let {
        it.isAntiAlias = true
        it
    }
    val mPath = Path()
    val mPathBottom = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = width.toFloat() / 2
        centery = height.toFloat() / 2
        startX = centerX - 150
        startY = centery
        endX = centerX + 250
        endY = centery
        eventX = centerX
        eventY = centery - 250
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {

        }
    }

}
package com.example.view.bezier

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

class DrawCubicView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet?= null, defStyleAttr : Int = 0)
    : View(context, attributeSet, defStyleAttr) {

    private var leftX = 0
    private var leftY = 0
    private var rightX = 0
    private var rightY = 0
    private var centerX = 0
    private var centerY = 0
    private var startX = 0
    private var startY = 0
    private var endX = 0
    private var endY = 0

    private val mPaint = Paint().let{
        it.isAntiAlias = true
        it
    }

    private val mPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
        startX = centerX - 250
        startY = centerY
        endX = centerX + 250
        endY = centerY
        leftX = startX
        leftY = centerY - 250
        rightX = endX
        rightY = endY - 250
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            mPaint.color = Color.GRAY
            it.drawCircle(startX.toFloat(), startY.toFloat(), 8f, mPaint)
            it.drawCircle(endX.toFloat(), endY.toFloat(), 8f, mPaint)
            it.drawCircle(leftX.toFloat(), leftY.toFloat(), 8f, mPaint)
            it.drawCircle(rightX.toFloat(), rightY.toFloat(), 8f, mPaint)

            mPaint.color = Color.RED
            mPaint.strokeWidth = 3f
            it.drawLine(startX.toFloat(), startY.toFloat(), leftX.toFloat(), leftY.toFloat(), mPaint)
            it.drawLine(leftX.toFloat(), leftY.toFloat(), rightX.toFloat(), rightY.toFloat(), mPaint)
            it.drawLine(rightX.toFloat(),rightY.toFloat(), endX.toFloat(), endY.toFloat(),mPaint)

            mPaint.color = Color.GREEN
            mPath.reset()
            mPath.moveTo(startX.toFloat(), startY.toFloat())
            mPath.cubicTo(leftX.toFloat(), leftY.toFloat(), rightX.toFloat(), rightY.toFloat(), endX.toFloat(), endY.toFloat())
            it.drawPath(mPath, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE ->{
                leftX = event.x.toInt()
                leftY = event.y.toInt()
                invalidate()
            }
        }
        return true
    }
}
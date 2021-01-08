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

    var centerX = 0
    var centery = 0
    var startX = 0
    var startY = 0
    var endX = 0
    var endY = 0
    var eventX = 0
    var eventY = 0
    var eventBottomX = 0
    var eventBottomY = 0
    val mPaint = Paint().let {
        it.isAntiAlias = true
        it
    }
    val mPath = Path()
    val mPathBottom = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centery = h / 2
        startX = centerX - 250
        startY = centery
        endX = centerX + 250
        endY = centery
        eventX = centerX
        eventY = centery - 250
        eventBottomX = centerX
        eventBottomY = centery + 250
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
           /* mPaint.color = Color.GRAY
            it.drawCircle(startX.toFloat(), startY.toFloat(), 8f, mPaint)
            it.drawCircle(endX.toFloat(), endY.toFloat(), 8f, mPaint)
            it.drawCircle(eventX.toFloat(), eventY.toFloat(), 8f, mPaint)

            mPaint.strokeWidth = 3f
            mPaint.color = Color.RED
            it.drawLine(startX.toFloat(), centery.toFloat(), eventX.toFloat(), eventY.toFloat(), mPaint)
            it.drawLine(endX.toFloat(), endY.toFloat(), eventX.toFloat(), eventY.toFloat(), mPaint)*/

            mPaint.color = Color.BLACK
            mPaint.style = Paint.Style.STROKE
            mPath.reset()
            mPath.moveTo(startX.toFloat(), startY.toFloat())
            /**
             * (eventX.toFloat(), eventY.toFloat()) 为控制点
             * (endX.toFloat(), endY.toFloat()) 结束点
             */
            mPath.quadTo(eventX.toFloat(), eventY.toFloat(), endX.toFloat(), endY.toFloat())
            it.drawPath(mPath, mPaint)
            mPathBottom.reset()
            mPathBottom.moveTo(startX.toFloat(), startY.toFloat())
            /**
             * (eventX.toFloat(), eventY.toFloat()) 为控制点
             * (endX.toFloat(), endY.toFloat()) 结束点
             */
            mPathBottom.quadTo(eventBottomX.toFloat(), eventBottomY.toFloat(), endX.toFloat(), endY.toFloat())
            it.drawPath(mPathBottom, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if (event.y.toInt() > centery) {
                    eventBottomX = event.x.toInt()
                    eventBottomY = event.y.toInt()
                } else {
                    eventX = event.x.toInt()
                    eventY = event.y.toInt()
                }
                invalidate()
            }
        }
        return true
    }
}
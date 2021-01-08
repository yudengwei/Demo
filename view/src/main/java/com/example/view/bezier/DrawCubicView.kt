package com.example.view.bezier

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

//三阶贝塞尔曲线
class DrawCubicView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet?= null, defStyleAttr : Int = 0)
    : View(context, attributeSet, defStyleAttr) {

    private var leftX = 0
    private var leftY = 0
    private var rightX = 0
    private var rightY = 0
    private var leftBottomX = 0
    private var leftBottomY = 0
    private var rightBottomX = 0
    private var rightBottomY = 0
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
    private val mPathBottom = Path()

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
        leftBottomX = startX
        leftBottomY = centerY + 250
        rightBottomX = endX
        rightBottomY = endY + 250
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            /*mPaint.color = Color.GRAY
            it.drawCircle(startX.toFloat(), startY.toFloat(), 8f, mPaint)
            it.drawCircle(endX.toFloat(), endY.toFloat(), 8f, mPaint)
            it.drawCircle(leftX.toFloat(), leftY.toFloat(), 8f, mPaint)
            it.drawCircle(rightX.toFloat(), rightY.toFloat(), 8f, mPaint)

            mPaint.color = Color.RED
            mPaint.strokeWidth = 3f
            it.drawLine(startX.toFloat(), startY.toFloat(), leftX.toFloat(), leftY.toFloat(), mPaint)
            it.drawLine(leftX.toFloat(), leftY.toFloat(), rightX.toFloat(), rightY.toFloat(), mPaint)
            it.drawLine(rightX.toFloat(),rightY.toFloat(), endX.toFloat(), endY.toFloat(),mPaint)*/

            mPaint.color = Color.BLACK
            mPaint.style = Paint.Style.STROKE
            mPath.reset()
            mPath.moveTo(startX.toFloat(), startY.toFloat())
            mPath.cubicTo(leftX.toFloat(), leftY.toFloat(), rightX.toFloat(), rightY.toFloat(), endX.toFloat(), endY.toFloat())
            it.drawPath(mPath, mPaint)
            mPathBottom.reset()
            mPathBottom.moveTo(startX.toFloat(), startY.toFloat())
            mPathBottom.cubicTo(leftBottomX.toFloat(), leftBottomY.toFloat(), rightBottomX.toFloat(), rightBottomY.toFloat(), endX.toFloat(), endY.toFloat())
            it.drawPath(mPathBottom, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val realX = event.x.toInt()
                val realY = event.y.toInt()
                if (realX > centerX && realY > centerY) {
                    rightBottomX = realX
                    rightBottomY = realY
                } else if (realX > centerX && realY < centerY) {
                    rightX = realX
                    rightY = realY
                } else if(realX < centerX && realY > centerY) {
                    leftBottomX = realX
                    leftBottomY = realY
                } else if(realX < centerX && realY < centerY) {
                    leftX = realX
                    leftY = realY
                }
                invalidate()
            }
        }
        return true
    }
}
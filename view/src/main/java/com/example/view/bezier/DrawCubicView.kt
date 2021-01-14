package com.example.view.bezier

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

//三阶贝塞尔曲线
class DrawCubicView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet?= null, defStyleAttr : Int = 0)
    : View(context, attributeSet, defStyleAttr) {

    private var mLeftControlPoint = PointF()
    private var mRightControlPoint = PointF()
    private var mStartPoint = PointF()
    private var mEndPoint = PointF()

    private var mLength = 150f

    private val mPaint = Paint().also {
        it.isAntiAlias = true
        it.color = Color.RED
        it.style = Paint.Style.STROKE
    }

    private val mPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val centerX = width.toFloat() / 2
        val centerY = height.toFloat() / 2
        mStartPoint.x = centerX - mLength
        mStartPoint.y = centerY
        mEndPoint.x = centerX + mLength
        mEndPoint.y = centerY
        mRightControlPoint.x = centerX + 70
        mRightControlPoint.y = centerY - 70
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            mPath.reset()
            mPath.moveTo(mStartPoint.x, mStartPoint.y)
            mPath.cubicTo(mLeftControlPoint.x, mLeftControlPoint.y, mRightControlPoint.x, mRightControlPoint.y, mEndPoint.x, mEndPoint.y)
            it.drawPath(mPath, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                mLeftControlPoint.x = event.x
                mLeftControlPoint.y = event.y
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

}
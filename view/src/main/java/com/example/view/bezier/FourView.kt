package com.example.view.bezier

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class FourView @JvmOverloads constructor(context : Context, attributeSet: AttributeSet? = null, defStyleAttr : Int = 0) : View(context, attributeSet, defStyleAttr) {

    private val pathHeight = 200f
    private val pathDiff = 80f

    private val mPaint = Paint().apply {
        this.color = Color.BLACK
        this.style = Paint.Style.STROKE
        this.strokeWidth = 5f
        this.isAntiAlias = true
    }

    private val mCenterPoint = Point()

    private val mStartPoint1 = Point()
    private val mStartPoint2 = Point()

    private val mStartPoint3 = Point()
    private val mStartPoint4 = Point()

    private val mControlPoint1 = Point()
    private val mControlPoint2 = Point()

    private val mControlPoint3 = Point()
    private val mControlPoint4 = Point()

    private val mPath = Path()
    private val mPath1 = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterPoint.x = w / 2
        mCenterPoint.y = mCenterPoint.x

        mStartPoint1.x = mCenterPoint.x / 2
        mStartPoint1.y = mCenterPoint.y

        mStartPoint3.x = mCenterPoint.x / 2
        mStartPoint3.y = mCenterPoint.y

        mStartPoint2.x =mCenterPoint.x + mCenterPoint.x / 2
        mStartPoint2.y = mCenterPoint.y

        mStartPoint4.x =mCenterPoint.x + mCenterPoint.x / 2
        mStartPoint4.y = mCenterPoint.y

        mControlPoint1.x = mCenterPoint.x / 4 + mCenterPoint.x / 2
        mControlPoint1.y = (mCenterPoint.y - pathHeight).toInt()

        mControlPoint2.x = mCenterPoint.x + mCenterPoint.x / 2
        mControlPoint2.y = (mCenterPoint.y + pathHeight).toInt()

        mControlPoint3.x = mCenterPoint.x + mCenterPoint.x / 4
        mControlPoint3.y = (mCenterPoint.y - pathHeight).toInt()

        mControlPoint4.x = mCenterPoint.x / 2
        mControlPoint4.y = (mCenterPoint.y + pathHeight).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        mPath.reset()
        mPath1.reset()
        mPath.moveTo(mStartPoint1.x.toFloat(), mStartPoint1.y.toFloat())
        mPath1.moveTo(mStartPoint4.x.toFloat(), mStartPoint4.x.toFloat())
        mPath.cubicTo(mControlPoint1.x.toFloat(),mControlPoint1.y.toFloat() , mControlPoint2.x.toFloat(),mControlPoint2.y.toFloat(), mStartPoint2.x.toFloat(),  mStartPoint2.y.toFloat())
        mPath.cubicTo(mControlPoint3.x.toFloat(),mControlPoint3.y.toFloat() , mControlPoint4.x.toFloat(),mControlPoint4.y.toFloat(), mStartPoint1.x.toFloat(),  mStartPoint1.y.toFloat())
        mPath.cubicTo(mControlPoint1.x.toFloat(),mControlPoint1.y.toFloat() + pathDiff , mControlPoint2.x.toFloat(),mControlPoint2.y.toFloat() + pathDiff, mStartPoint2.x.toFloat(),  mStartPoint2.y.toFloat())
        mPath.cubicTo(mControlPoint3.x.toFloat(),mControlPoint3.y.toFloat() + pathDiff , mControlPoint4.x.toFloat(),mControlPoint4.y.toFloat() + pathDiff, mStartPoint1.x.toFloat(),  mStartPoint1.y.toFloat())
        canvas.drawPath(mPath, mPaint)
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.RED
        canvas.drawPoint(mControlPoint1.x.toFloat(), mControlPoint1.y.toFloat(), mPaint)
        canvas.drawPoint(mControlPoint2.x.toFloat(), mControlPoint2.y.toFloat(), mPaint)
    }
}
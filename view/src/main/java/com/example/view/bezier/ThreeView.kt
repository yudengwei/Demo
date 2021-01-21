package com.example.view.bezier

import android.animation.Animator
import android.animation.PropertyValuesHolder
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.base.util.LogUtil
import kotlin.random.Random

class ThreeView @JvmOverloads constructor(context : Context, attributeSet : AttributeSet? = null, defStyleAttr : Int = 0) : View(context, attributeSet, defStyleAttr) {

    private val mPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.FILL
    }

    private val mPath = Path()

    private var mCenterPoint = Point()

    private val mStartEndPoints = Array(4) {
        Point()
    }

    private val mControlPoints = Array(8) {
        Point()
    }

    private val mStartPointValue = PointValue()
    private val mEndPointValue = PointValue()
    private val mCurrentPointValue = PointValue()
    private val mCurrentMaxPointValue = PointValue()

    private val mRadius = 200
    private val mRange = 80

    private var mIsRunning = false

    private val mValueAnimator = ValueAnimator().also {
        it.duration = 1000
        it.addUpdateListener { value ->
            LogUtil.d("1234")
            val current = value.animatedValue as PointValue
            mStartEndPoints[0].x = current.point1
            mStartEndPoints[1].y = current.point2
            mStartEndPoints[2].x = current.point3
            mStartEndPoints[3].y = current.point4
            invalidate()
        }
        it.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                mIsRunning = true
            }

            override fun onAnimationEnd(animation: Animator?) {
                LogUtil.d("mIsRunning: $mIsRunning")
                if (mIsRunning) {
                    start()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
                mIsRunning = false
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
    }

    private val type = PointValueType()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterPoint.x = w / 2
        mCenterPoint.y = h / 2

        setInitialPoints()
        val gradient = LinearGradient(mControlPoints[5].x.toFloat(), mControlPoints[5].y.toFloat(),
            mControlPoints[1].x.toFloat(),mControlPoints[1].y.toFloat(),
            intArrayOf(Color.parseColor("#5486c9"),Color.parseColor("#7cc9da")),null,Shader.TileMode.MIRROR)
        mPaint.shader = gradient
    }

    override fun onDraw(canvas: Canvas) {

        mPath.reset()
        mPath.moveTo(mStartEndPoints[0].x.toFloat(), mStartEndPoints[0].y.toFloat())
        for (i in mStartEndPoints.indices) {
            val endPoint = if (i != mStartEndPoints.size - 1) mStartEndPoints[i + 1] else mStartEndPoints[0]
            val controlPoint1 = mControlPoints[i * 2]
            val controlPoint2 = mControlPoints[i * 2 + 1]
            cubicTo(controlPoint1, controlPoint2, endPoint)
        }
        canvas.drawPath(mPath, mPaint)
    }

    private fun cubicTo(controlPoint1 : Point, controlPoint2 : Point, endPoint : Point) {
        mPath.cubicTo(controlPoint1.x.toFloat(), controlPoint1.y.toFloat(), controlPoint2.x.toFloat(), controlPoint2.y.toFloat(), endPoint.x.toFloat(), endPoint.y.toFloat())
    }

    private fun setInitialPoints() {

        mStartEndPoints[0].x = mCenterPoint.x
        mStartEndPoints[0].y = mCenterPoint.y - mRadius
        mStartPointValue.point1 = mStartEndPoints[0].x - mRange
        mEndPointValue.point1 = mStartEndPoints[0].x + mRange

        mStartEndPoints[1].x = mCenterPoint.x + mRadius
        mStartEndPoints[1].y = mCenterPoint.y
        mStartPointValue.point2 = mStartEndPoints[1].y - mRange
        mEndPointValue.point2 = mStartEndPoints[1].y + mRange

        mStartEndPoints[2].x = mCenterPoint.x
        mStartEndPoints[2].y = mCenterPoint.y + mRadius
        mStartPointValue.point3 = mStartEndPoints[2].x - mRange
        mEndPointValue.point3 = mStartEndPoints[2].x + mRange

        mStartEndPoints[3].x = mCenterPoint.x - mRadius
        mStartEndPoints[3].y = mCenterPoint.y
        mStartPointValue.point4 = mStartEndPoints[3].y - mRange
        mEndPointValue.point4 = mStartEndPoints[3].y + mRange

        mControlPoints[0].x = mCenterPoint.x + mRadius / 2
        mControlPoints[0].y = mCenterPoint.y - mRadius

        mControlPoints[1].x = mCenterPoint.x + mRadius
        mControlPoints[1].y = mCenterPoint.y - mRadius / 2

        mControlPoints[2].x = mCenterPoint.x + mRadius
        mControlPoints[2].y = mCenterPoint.y + mRadius / 2

        mControlPoints[3].x = mCenterPoint.x + mRadius / 2
        mControlPoints[3].y = mCenterPoint.y + mRadius

        mControlPoints[4].x = mCenterPoint.x - mRadius / 2
        mControlPoints[4].y = mCenterPoint.y + mRadius

        mControlPoints[5].x = mCenterPoint.x - mRadius
        mControlPoints[5].y = mCenterPoint.y + mRadius / 2

        mControlPoints[6].x = mCenterPoint.x - mRadius
        mControlPoints[6].y = mCenterPoint.y - mRadius / 2

        mControlPoints[7].x = mCenterPoint.x - mRadius / 2
        mControlPoints[7].y = mCenterPoint.y - mRadius
    }

    fun start() {
        mCurrentMaxPointValue.point1 = Random.nextInt(mStartPointValue.point1, mEndPointValue.point1)
        mCurrentMaxPointValue.point2 = Random.nextInt(mStartPointValue.point2, mEndPointValue.point2)
        mCurrentMaxPointValue.point3 = Random.nextInt(mStartPointValue.point3, mEndPointValue.point3)
        mCurrentMaxPointValue.point4 = Random.nextInt(mStartPointValue.point4, mEndPointValue.point4)

        mCurrentPointValue.point1 = mStartEndPoints[0].x
        mCurrentPointValue.point2 = mStartEndPoints[1].y
        mCurrentPointValue.point3 = mStartEndPoints[2].x
        mCurrentPointValue.point4 = mStartEndPoints[3].y

        mValueAnimator.setObjectValues(mCurrentPointValue, mCurrentMaxPointValue)
        mValueAnimator.setEvaluator(type)
        mValueAnimator.start()
    }

    fun stop() {
        mValueAnimator.cancel()
        mIsRunning = false
    }

    fun isRunning() = mIsRunning

    class PointValue {
        var point1 : Int = 0
        var point2 : Int = 0
        var point3 : Int = 0
        var point4 : Int = 0

        constructor(point1: Int = 0, point2: Int = 0, point3: Int = 0, point4: Int = 0) {
            this.point1 = point1
            this.point2 = point2
            this.point3 = point3
            this.point4 = point4
        }


    }

    class PointValueType : TypeEvaluator<PointValue> {

        override fun evaluate(fraction: Float, startValue: PointValue, endValue: PointValue): PointValue {
            return PointValue().also { current ->
                current.point1 = (startValue.point1 + fraction * (endValue.point1 - startValue.point1)).toInt()
                current.point2 = (startValue.point2 + fraction * (endValue.point2 - startValue.point2)).toInt()
                current.point3 = (startValue.point3 + fraction * (endValue.point3 - startValue.point3)).toInt()
                current.point4 = (startValue.point4 + fraction * (endValue.point4 - startValue.point4)).toInt()
            }
        }

    }
}
package com.example.view.bezier

import android.animation.Animator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ThreeView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attributeSet, defStyleAttr) {

    private var mBigListener : BigListener? = null

    private val mPaint = Paint().also {
        it.isAntiAlias = true
        it.style = Paint.Style.STROKE
        it.color = Color.WHITE
    }

    private val StringTextBIG = "呼气"
    private var StringTextSHRINK = "吸气"

    private var mCurrentText = StringTextBIG

    private val mTextPaint = Paint().apply {
        this.style = Paint.Style.FILL
        this.strokeWidth = 4f
        this.textSize = 50f
        this.textAlign = Paint.Align.CENTER
        this.color = Color.WHITE
        this.isAntiAlias = true
    }

    private val mPath = Path()

    private var mCenterPoint = Point()

    private val mPoints = Array(12) {
        Point()
    }

    private val mInitPoints = Array(12) {
        Point()
    }

    private val mRadius = 200
    private val mBigRadius = 100

    private var mIsShrink = false
    private var mIsRunning = false

    private val mBigAnimator = ValueAnimator().also {
        it.duration = 4000
        it.addUpdateListener { value ->
            val realPoints = value.animatedValue as Array<Point>
            for (i in mPoints.indices) {
                when(i) {
                    0 -> {
                        mPoints[0].y = realPoints[0].y
                    }
                    3 -> {
                        mPoints[3].x = realPoints[3].x
                    }
                    6 -> {
                        mPoints[6].y = realPoints[6].y
                    }
                    9 -> {
                        mPoints[9].x = realPoints[9].x
                    }
                    else -> {
                        mPoints[i].x = realPoints[i].x
                        mPoints[i].y = realPoints[i].y
                    }
                }
            }
            invalidate()
        }
        it.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
                mIsRunning = true
            }

            override fun onAnimationEnd(p0: Animator?) {
                mBigListener?.let {
                    it.bigAnimatorEnd()
                }
                for (i in mInitPoints.indices) {
                    mInitPoints[i].x = mPoints[i].x
                    mInitPoints[i].y = mPoints[i].y
                }
                mIsRunning = false
                mCurrentText = StringTextSHRINK
                mIsShrink = true
                mShrinkAnimator.setObjectValues(mInitPoints, mInitPoints)
                mShrinkAnimator.setEvaluator(bigEvaluator)
                mShrinkAnimator.start()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }

        })
    }

    private val mShrinkAnimator = ValueAnimator().also {
        it.duration = 4000
        it.addUpdateListener { value ->
            val realPoints = value.animatedValue as Array<Point>
            for (i in mPoints.indices) {
                when(i) {
                    0 -> {
                        mPoints[0].y = realPoints[0].y
                    }
                    3 -> {
                        mPoints[3].x = realPoints[3].x
                    }
                    6 -> {
                        mPoints[6].y = realPoints[6].y
                    }
                    9 -> {
                        mPoints[9].x = realPoints[9].x
                    }
                    else -> {
                        mPoints[i].x = realPoints[i].x
                        mPoints[i].y = realPoints[i].y
                    }
                }
            }
            invalidate()
        }
        it.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
                mIsRunning = true
            }

            override fun onAnimationEnd(p0: Animator?) {
                for (i in mInitPoints.indices) {
                    mInitPoints[i].x = mPoints[i].x
                    mInitPoints[i].y = mPoints[i].y
                }
                mIsRunning = false
                mCurrentText = StringTextBIG
                mIsShrink = false
                invalidate()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }

        })
    }

    private val bigEvaluator = BigEvaluator()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterPoint.x = w / 2
        mCenterPoint.y = 500

        setInitialPoints()
    }

    override fun onDraw(canvas: Canvas) {

        mPath.reset()
        mPath.moveTo(mPoints[0].x.toFloat(), mPoints[0].y.toFloat())
        var i = 1
        while (i < mPoints.size) {
            val endPoint = if (i != 10) mPoints[i + 2] else mPoints[0]
            val controlPoint1 = mPoints[i]
            val controlPoint2 = mPoints[i + 1]
            cubicTo(controlPoint1, controlPoint2, endPoint)
            i += 3
        }
        canvas.drawPath(mPath, mPaint)
        val fontMetrics: Paint.FontMetrics = mTextPaint.fontMetrics
        val distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        val baseline: Float = mCenterPoint.y + distance
        canvas.drawText(mCurrentText, mCenterPoint.x.toFloat(), baseline, mTextPaint)
    }

    private fun cubicTo(controlPoint1: Point, controlPoint2: Point, endPoint: Point) {
        mPath.cubicTo(controlPoint1.x.toFloat(), controlPoint1.y.toFloat(), controlPoint2.x.toFloat(), controlPoint2.y.toFloat(), endPoint.x.toFloat(), endPoint.y.toFloat())
    }

    private fun setInitialPoints() {

        mPoints[0].x = mCenterPoint.x
        mPoints[0].y = mCenterPoint.y - mRadius
        mInitPoints[0].x = mPoints[0].x
        mInitPoints[0].y = mPoints[0].y

        mPoints[1].x = mCenterPoint.x + mRadius / 2
        mPoints[1].y = mCenterPoint.y - mRadius
        mInitPoints[1].x = mPoints[1].x
        mInitPoints[1].y = mPoints[1].y

        mPoints[2].x = mCenterPoint.x + mRadius
        mPoints[2].y = mCenterPoint.y - mRadius / 2
        mInitPoints[2].x = mPoints[2].x
        mInitPoints[2].y = mPoints[2].y

        mPoints[3].x = mCenterPoint.x + mRadius
        mPoints[3].y = mCenterPoint.y
        mInitPoints[3].x = mPoints[3].x
        mInitPoints[3].y = mPoints[3].y

        mPoints[4].x = mCenterPoint.x + mRadius
        mPoints[4].y = mCenterPoint.y + mRadius / 2
        mInitPoints[4].x = mPoints[4].x
        mInitPoints[4].y = mPoints[4].y

        mPoints[5].x = mCenterPoint.x + mRadius / 2
        mPoints[5].y = mCenterPoint.y + mRadius
        mInitPoints[5].x = mPoints[5].x
        mInitPoints[5].y = mPoints[5].y

        mPoints[6].x = mCenterPoint.x
        mPoints[6].y = mCenterPoint.y + mRadius
        mInitPoints[6].x = mPoints[6].x
        mInitPoints[6].y = mPoints[6].y

        mPoints[7].x = mCenterPoint.x - mRadius / 2
        mPoints[7].y = mCenterPoint.y + mRadius
        mInitPoints[7].x = mPoints[7].x
        mInitPoints[7].y = mPoints[7].y

        mPoints[8].x = mCenterPoint.x - mRadius
        mPoints[8].y = mCenterPoint.y + mRadius / 2
        mInitPoints[8].x = mPoints[8].x
        mInitPoints[8].y = mPoints[8].y

        mPoints[9].x = mCenterPoint.x - mRadius
        mPoints[9].y = mCenterPoint.y
        mInitPoints[9].x = mPoints[9].x
        mInitPoints[9].y = mPoints[9].y

        mPoints[10].x = mCenterPoint.x - mRadius
        mPoints[10].y = mCenterPoint.y - mRadius / 2
        mInitPoints[10].x = mPoints[10].x
        mInitPoints[10].y = mPoints[10].y

        mPoints[11].x = mCenterPoint.x - mRadius / 2
        mPoints[11].y = mCenterPoint.y - mRadius
        mInitPoints[11].x = mPoints[11].x
        mInitPoints[11].y = mPoints[11].y
    }

    fun big() {
        if (mIsRunning) {
            return
        }
        mBigAnimator.setObjectValues(mInitPoints, mInitPoints)
        mBigAnimator.setEvaluator(bigEvaluator)
        mBigAnimator.start()
    }

    fun isBig() = mIsShrink

    private inner class BigEvaluator : TypeEvaluator<Array<Point>> {

        override fun evaluate(p0: Float, p1: Array<Point>, p2: Array<Point>): Array<Point> {
            return Array(12) {
                var endValueX = 0
                var endValueY = 0
                when (it) {
                    0 -> {
                        endValueY = if (!mIsShrink) p1[it].y - mBigRadius else p1[it].y + mBigRadius
                    }
                    1 -> {
                        endValueX = if (!mIsShrink) p1[it].x + mBigRadius / 2 else p1[it].x - mBigRadius / 2
                        endValueY = if (!mIsShrink) p1[it].y - mBigRadius else p1[it].y + mBigRadius
                    }
                    2 -> {
                        endValueX = if (!mIsShrink) p1[it].x + mBigRadius else p1[it].x - mBigRadius
                        endValueY = if (!mIsShrink) p1[it].y - mBigRadius / 2 else p1[it].y + mBigRadius / 2
                    }
                    3 -> {
                        endValueX = if (!mIsShrink) p1[it].x + mBigRadius else p1[it].x - mBigRadius
                    }
                    4 -> {
                        endValueX = if (!mIsShrink) p1[it].x + mBigRadius else p1[it].x - mBigRadius
                        endValueY = if (!mIsShrink) p1[it].y + mBigRadius / 2 else p1[it].y - mBigRadius / 2
                    }
                    5 -> {
                        endValueX = if (!mIsShrink) p1[it].x + mBigRadius / 2 else p1[it].x - mBigRadius / 2
                        endValueY = if (!mIsShrink) p1[it].y + mBigRadius else p1[it].y - mBigRadius
                    }
                    6 -> {
                        endValueY = if (!mIsShrink) p1[it].y + mBigRadius else p1[it].y - mBigRadius
                    }
                    7 -> {
                        endValueX = if (!mIsShrink) p1[it].x - mBigRadius / 2 else p1[it].x + mBigRadius / 2
                        endValueY = if (!mIsShrink) p1[it].y + mBigRadius else p1[it].y - mBigRadius
                    }
                    8 -> {
                        endValueX = if (!mIsShrink) p1[it].x - mBigRadius else p1[it].x + mBigRadius
                        endValueY = if (!mIsShrink) p1[it].y + mBigRadius / 2 else p1[it].y - mBigRadius / 2
                    }
                    9 -> {
                        endValueX = if (!mIsShrink) p1[it].x - mBigRadius else p1[it].x + mBigRadius
                    }
                    10 -> {
                        endValueX = if (!mIsShrink) p1[it].x - mBigRadius else p1[it].x + mBigRadius
                        endValueY = if (!mIsShrink) p1[it].y - mBigRadius / 2 else p1[it].y + mBigRadius / 2
                    }
                    11 -> {
                        endValueX = if (!mIsShrink) p1[it].x - mBigRadius / 2 else p1[it].x + mBigRadius / 2
                        endValueY = if (!mIsShrink) p1[it].y - mBigRadius else p1[it].y + mBigRadius
                    }
                }
                val x = (p1[it].x + p0 * (endValueX - p1[it].x)).toInt()
                val y = (p1[it].y + p0 * (endValueY - p1[it].y)).toInt()
                Point(x, y)
            }
        }
    }

    fun setListener(bigListener : BigListener) {
        this.mBigListener = bigListener
    }

    interface BigListener {
        fun bigAnimatorEnd()
    }
}
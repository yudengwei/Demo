package com.example.view.bezier.grid

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.animation.addListener
import com.example.base.util.LogUtil
import java.sql.Time
import java.util.*
import kotlin.random.Random
import kotlin.reflect.typeOf

class SimpleCubicView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    //pointX = x + radius * cos(angle * PI / 2)
    //pointY = y + radius * sin(angle * PI / 2)
    //pointX = x + radius * Math.cos(Math.toRadians(angle))
    //pointY = y + radius * Math.sin(Math.toRadians(angle))

    private val centerPoint = Point()
    private val windowPoint = Point()

    private val mPath = Path()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.style = Paint.Style.FILL
        it.strokeWidth = 5f
    }


    private var clickType = -1

    private var mHelpPint = getHelpPoint(-0x7cae9)

    private var p0 = Point()
    private var p1 = Point()
    private var p2 = Point()
    private var p3 = Point()

    private var cp0 = Point()
    private var cp1 = Point()
    private var cp2 = Point()
    private var cp3 = Point()
    private var cp4 = Point()
    private var cp5 = Point()
    private var cp6 = Point()
    private var cp7 = Point()

    private val startPoints = Array(4){
        Point()
    }

    private val startControlPoints = Array(8){
        Point()
    }

    private val mRadius = 200
    private val mMaxRadius = 400
    private val c = 60

    private var isRuning = false
    private var isBigger = true
    private var isShowHelp = true
    private var isKeep = true


    private val mAnimator = ValueAnimator.ofFloat(0f, 1f).also {
        it.duration = 4000
        it.addUpdateListener {value ->
            val realValue = value.animatedValue as Float
            val radius = if (isBigger) (realValue * 200).toInt() else 0 - (realValue * 200).toInt()
            setPoint2(radius)
            invalidate()
        }
        it.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                isRuning = true
            }

            override fun onAnimationEnd(animation: Animator?) {
                isBigger = !isBigger
                isRuning = false
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerPoint.x = width / 2
        centerPoint.y = height / 2
        windowPoint.x = width
        windowPoint.y = height
        p0.x = centerPoint.x
        p0.y = centerPoint.y - mRadius
        p1.x = centerPoint.x + mRadius
        p1.y = centerPoint.y
        p2.x = centerPoint.x
        p2.y = centerPoint.y + mRadius
        p3.x = centerPoint.x - mRadius
        p3.y = centerPoint.y

        cp0.x = centerPoint.x + mRadius / 2
        cp0.y = centerPoint.y - mRadius

        cp1.x = centerPoint.x + mRadius
        cp1.y = centerPoint.y - mRadius / 2

        cp2.x = centerPoint.x + mRadius
        cp2.y = centerPoint.y + mRadius / 2

        cp3.x = centerPoint.x + mRadius / 2
        cp3.y = centerPoint.y + mRadius

        cp4.x = centerPoint.x - mRadius / 2
        cp4.y = centerPoint.y + mRadius

        cp5.x = centerPoint.x - mRadius
        cp5.y = centerPoint.y + mRadius / 2

        cp6.x = centerPoint.x - mRadius
        cp6.y = centerPoint.y - mRadius / 2

        cp7.x = centerPoint.x - mRadius / 2
        cp7.y = centerPoint.y - mRadius

        for (i in startPoints.indices) {
            val point = startPoints[i]
            when(i) {
                0 -> {
                    point.x = p0.x
                    point.y = p0.y
                }
                1 ->{
                    point.x = p1.x
                    point.y = p1.y
                }
                2 -> {
                    point.x = p2.x
                    point.y = p2.y
                }
                3 -> {
                    point.x = p3.x
                    point.y = p3.y
                }
            }
        }
        for (i in startControlPoints.indices) {
            val point = startControlPoints[i]
            when(i) {
                0 -> {
                    point.x = cp0.x
                    point.y = cp0.y
                }
                1 -> {
                    point.x = cp1.x
                    point.y = cp1.y
                }
                2 -> {
                    point.x = cp2.x
                    point.y = cp2.y
                }
                3 -> {
                    point.x = cp3.x
                    point.y = cp3.y
                }
                4 -> {
                    point.x = cp4.x
                    point.y = cp4.y
                }
                5 -> {
                    point.x = cp5.x
                    point.y = cp5.y
                }
                6 -> {
                    point.x = cp6.x
                    point.y = cp6.y
                }
                else ->{
                    point.x = cp7.x
                    point.y = cp7.y
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isShowHelp) {
            drawGrid(canvas, getGrid(windowPoint), getCoo(centerPoint, windowPoint))
            helpView(canvas)
        }
        drawText(canvas)
        val gradient = LinearGradient(cp5.x.toFloat(), cp5.y.toFloat(),cp1.x.toFloat(),cp1.y.toFloat(),
            intArrayOf(Color.parseColor("#d59b14"),Color.parseColor("#8dda8b")),null,Shader.TileMode.MIRROR)
        mPaint.shader = gradient
        mPath.reset()
        mPath.moveTo(p0.x.toFloat(), p0.y.toFloat())
        mPath.cubicTo(cp0.x.toFloat(), cp0.y.toFloat(), cp1.x.toFloat(), cp1.y.toFloat(), p1.x.toFloat(), p1.y.toFloat())
        mPath.cubicTo(cp2.x.toFloat(), cp2.y.toFloat(), cp3.x.toFloat(), cp3.y.toFloat(), p2.x.toFloat(), p2.y.toFloat())
        mPath.cubicTo(cp4.x.toFloat(), cp4.y.toFloat(), cp5.x.toFloat(), cp5.y.toFloat(), p3.x.toFloat(), p3.y.toFloat())
        mPath.cubicTo(cp6.x.toFloat(), cp6.y.toFloat(), cp7.x.toFloat(), cp7.y.toFloat(), p0.x.toFloat(), p0.y.toFloat())
        canvas.drawPath(mPath, mPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                val clickPoint = Point()
                clickPoint.x = event.x.toInt()
                clickPoint.y = event.y.toInt()
                clickType = when {
                    isClickPoint(clickPoint, p0, 50f) -> {
                        0
                    }
                    isClickPoint(clickPoint, p1, 50f) -> {
                        1
                    }
                    isClickPoint(clickPoint, p2, 50f) -> {
                        2
                    }
                    isClickPoint(clickPoint, p3, 50f) -> {
                        3
                    }
                    isClickPoint(clickPoint, cp0, 50f) -> {
                        4
                    }
                    isClickPoint(clickPoint, cp1, 50f) -> {
                        5
                    }
                    isClickPoint(clickPoint, cp2, 50f) -> {
                        6
                    }
                    isClickPoint(clickPoint, cp3, 50f) -> {
                        7
                    }
                    isClickPoint(clickPoint, cp4, 50f) -> {
                        8
                    }
                    isClickPoint(clickPoint, cp5, 50f) -> {
                        9
                    }
                    isClickPoint(clickPoint, cp6, 50f) -> {
                        10
                    }
                    isClickPoint(clickPoint, cp7, 50f) -> {
                        11
                    }
                    else -> {
                        -1
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x.toInt()
                val y = event.y.toInt()
                when (clickType) {
                    0 -> {
                        p0.x = x
                        p0.y = y
                    }
                    1 -> {
                        p1.x = x
                        p1.y = y
                    }
                    2 -> {
                        p2.x = x
                        p2.y = y
                    }
                    3 -> {
                        p3.x = x
                        p3.y = y
                    }
                    4 -> {
                        cp0.x = x
                        cp0.y = y
                    }
                    5 -> {
                        cp1.x = x
                        cp1.y = y
                    }
                    6 -> {
                        cp2.x = x
                        cp2.y = y
                    }
                    7 -> {
                        cp3.x = x
                        cp3.y = y
                    }
                    8 -> {
                        cp4.x = x
                        cp4.y = y
                    }
                    9 -> {
                        cp5.x = x
                        cp5.y = y
                    }
                    10 -> {
                        cp6.x = x
                        cp6.y = y
                    }
                    11 -> {
                        cp7.x = x
                        cp7.y = y
                    }
                }
                invalidate()
            }
        }
        return true
    }

    private fun helpView(canvas: Canvas) {
        mHelpPint.strokeWidth = 20f
        drawPos(mHelpPint, canvas, p0, p1, p2, p3, cp0, cp1, cp2, cp3, cp4,cp5,cp6,cp7)
    }

    private fun drawText(canvas: Canvas) {
        mHelpPint.strokeWidth = 2f
        mHelpPint.pathEffect = null
        mHelpPint.style = Paint.Style.FILL
        canvas.drawText("起始点p0:$p0", centerPoint.x.toFloat() + 100, 100f, mHelpPint)
        canvas.drawText("控制点p1:$cp0", centerPoint.x.toFloat() + 100, 200f, mHelpPint)
        canvas.drawText("控制点p2:$cp1", centerPoint.x.toFloat() + 100, 300f, mHelpPint)
        canvas.drawText("终止点p3:$p1", centerPoint.x.toFloat() + 100, 400f, mHelpPint)

        canvas.drawText("起始点p0:$p1", centerPoint.x.toFloat() + 100, height - 200f, mHelpPint)
        canvas.drawText("控制点p1:$cp2", centerPoint.x.toFloat() + 100, height - 250f, mHelpPint)
        canvas.drawText("控制点p2:$cp3", centerPoint.x.toFloat() + 100, height - 300f, mHelpPint)
        canvas.drawText("终止点p3:$p2", centerPoint.x.toFloat() + 100, height - 350f, mHelpPint)

        canvas.drawText("起始点p0:$p2", 100f, height - 200f, mHelpPint)
        canvas.drawText("控制点p1:$cp4", 100f, height - 250f, mHelpPint)
        canvas.drawText("控制点p2:$cp5", 100f, height - 300f, mHelpPint)
        canvas.drawText("终止点p3:$p3", 100f, height - 350f, mHelpPint)

        canvas.drawText("起始点p0:$p3", 100f, 100f, mHelpPint)
        canvas.drawText("控制点p1:$cp6", 100f, 200f, mHelpPint)
        canvas.drawText("控制点p2:$cp7", 100f, 300f, mHelpPint)
        canvas.drawText("终止点p3:$p0", 100f, 400f, mHelpPint)
    }

    private fun setPoint2(radius : Int) {
        p0.y = startPoints[0].y - radius
        p1.x = startPoints[1].x + radius
        p2.y = startPoints[2].y + radius
        p3.x = startPoints[3].x - radius

        cp0.x = startControlPoints[0].x + radius / 2
        cp0.y = startControlPoints[0].y - radius

        cp1.x = startControlPoints[1].x + radius
        cp1.y = startControlPoints[1].y - radius / 2

        cp2.x = startControlPoints[2].x + radius
        cp2.y = startControlPoints[2].y + radius / 2

        cp3.y = startControlPoints[3].y + radius
        cp3.x = startControlPoints[3].x + radius / 2

        cp4.x = startControlPoints[4].x - radius / 2
        cp4.y = startControlPoints[4].y + radius

        cp5.x = startControlPoints[5].x - radius
        cp5.y = startControlPoints[5].y + radius / 2

        cp6.x = startControlPoints[6].x - radius
        cp6.y = startControlPoints[6].y - radius / 2

        cp7.x = startControlPoints[7].x - radius / 2
        cp7.y = startControlPoints[7].y - radius
    }

    private fun setPoint3() {

        var f = Random.nextInt(startPoints[0].x - c, startPoints[0].x + c)
        val a1 = ValueAnimator.ofInt(p0.x, f).also {
            it.addUpdateListener { value ->
                val realValue = value.animatedValue as Int
                p0.x = realValue
                invalidate()
            }
        }

        f = Random.nextInt(startPoints[1].y - c, startPoints[1].y + c)
        val a2 = ValueAnimator.ofInt(p1.y, f).also {
            it.addUpdateListener { value ->
                val realValue = value.animatedValue as Int
                p1.y = realValue
                invalidate()
            }
        }

        f = Random.nextInt(startPoints[2].x - c, startPoints[2].x + c)
        val a3 = ValueAnimator.ofInt(p2.x, f).also {
            it.addUpdateListener { value->
                val realValue = value.animatedValue as Int
                p2.x = realValue
                invalidate()
            }
        }

        f = Random.nextInt(startPoints[3].y - c, startPoints[3].y + c)
        val a4 = ValueAnimator.ofInt(p3.y, f).also {
            it.addUpdateListener { value->
                val realValue = value.animatedValue as Int
                p3.y = realValue
                invalidate()
            }
        }

        /*f = Random.nextInt(startControlPoints[0].x - c, startControlPoints[0].x + c)
        val a5 = ValueAnimator.ofInt(cp0.x, f).also {
            it.addUpdateListener { value->
                val realValue = value.animatedValue as Int
                cp0.x = realValue
                invalidate()
            }
        }

        f = Random.nextInt(startControlPoints[1].y - c, startControlPoints[1].y + c)
        val a6 = ValueAnimator.ofInt(cp1.y, f).also {
            it.addUpdateListener { value->
                val realValue = value.animatedValue as Int
                cp1.y = realValue
                invalidate()
            }
        }

        f = Random.nextInt(startControlPoints[2].x - c, startControlPoints[2].x + c)
        val a7 = ValueAnimator.ofInt(cp2.x, f).also {
            it.addUpdateListener { value->
                val realValue = value.animatedValue as Int
                cp2.x = realValue
                invalidate()
            }
        }

        f = Random.nextInt(startControlPoints[4].x - c, startControlPoints[4].x + c)
        val a8 = ValueAnimator.ofInt(cp4.x, f).also {
            it.addUpdateListener { value->
                val realValue = value.animatedValue as Int
                cp4.x = realValue
                invalidate()
            }
        }

        f = Random.nextInt(startControlPoints[6].x - c, startControlPoints[6].x + c)
        val a9 = ValueAnimator.ofInt(cp6.x, f).also {
            it.addUpdateListener { value->
                val realValue = value.animatedValue as Int
                cp6.x = realValue
                invalidate()
            }
        }

        f = Random.nextInt(startControlPoints[3].y - c, startControlPoints[3].y + c)
        val a10 = ValueAnimator.ofInt(cp3.y, f).also {
            it.addUpdateListener { value->
                val realValue = value.animatedValue as Int
                cp3.y = realValue
                invalidate()
            }
        }

        f = Random.nextInt(startControlPoints[5].y - c, startControlPoints[5].y + c)
        val a11 = ValueAnimator.ofInt(cp5.y, f).also {
            it.addUpdateListener { value->
                val realValue = value.animatedValue as Int
                cp5.y = realValue
                invalidate()
            }
        }

        f = Random.nextInt(startControlPoints[7].y - c, startControlPoints[7].y + c)
        val a12 = ValueAnimator.ofInt(cp7.y, f).also {
            it.addUpdateListener { value->
                val realValue = value.animatedValue as Int
                cp7.y = realValue
                invalidate()
            }
        }*/

        val animator = AnimatorSet().also {
            //it.playTogether(a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12)
            it.playTogether(a1,a2,a3,a4)
            it.duration = 1000
            it.addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    setPoint3()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

            })
        }
        animator.start()
    }

    fun start() {
        setPoint3()

    }

    fun setShowHelp() {
        isShowHelp = !isShowHelp
        invalidate()
    }
}
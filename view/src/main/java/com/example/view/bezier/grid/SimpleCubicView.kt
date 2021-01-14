package com.example.view.bezier.grid

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SimpleCubicView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private val centerPoint = Point()
    private val windowPoint = Point()

    private val mPath = Path()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).also {
        it.color = Color.BLUE
        it.style = Paint.Style.STROKE
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerPoint.x = width / 2
        centerPoint.y = height / 2
        windowPoint.x = width
        windowPoint.y = height
        p0.x = centerPoint.x
        p0.y = centerPoint.y - 300
        p1.x = centerPoint.x + 300
        p1.y = centerPoint.y
        p2.x = centerPoint.x
        p2.y = centerPoint.y + 300
        p3.x = centerPoint.x - 300
        p3.y = centerPoint.y

        cp0.x = centerPoint.x + 100
        cp0.y = centerPoint.y - 200

        cp1.x = centerPoint.x + 200
        cp1.y = centerPoint.y - 100

        cp2.x = centerPoint.x + 100
        cp2.y = centerPoint.y + 200

        cp3.x = centerPoint.x + 200
        cp3.y = centerPoint.y + 100

        cp4.x = centerPoint.x - 100
        cp4.y = centerPoint.y + 200

        cp5.x = centerPoint.x - 200
        cp5.y = centerPoint.y + 100

        cp6.x = centerPoint.x - 100
        cp6.y = centerPoint.y - 200

        cp7.x = centerPoint.x - 200
        cp7.y = centerPoint.y - 100
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGrid(canvas, getGrid(windowPoint), getCoo(centerPoint, windowPoint))
        helpView(canvas)
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
        mHelpPint.strokeWidth = 2f
        mHelpPint.pathEffect = null
        mHelpPint.style = Paint.Style.FILL
        canvas.drawText("起始点p0:$p0", centerPoint.x.toFloat() + 100, 100f, mHelpPint)
        canvas.drawText("控制点p1:$cp0", centerPoint.x.toFloat() + 100, 200f, mHelpPint)
        canvas.drawText("控制点p2:$cp1", centerPoint.x.toFloat() + 100, 300f, mHelpPint)
        canvas.drawText("终止点p3:$p1", centerPoint.x.toFloat() + 100, 400f, mHelpPint)

        canvas.drawText("起始点p0:$p1", centerPoint.x.toFloat() + 200, height - 100f, mHelpPint)
        canvas.drawText("控制点p1:$cp2", centerPoint.x.toFloat() + 200, height - 200f, mHelpPint)
        canvas.drawText("控制点p2:$cp3", centerPoint.x.toFloat() + 200, height - 300f, mHelpPint)
        canvas.drawText("终止点p3:$p2", centerPoint.x.toFloat() + 200, height - 400f, mHelpPint)

        canvas.drawText("起始点p0:$p2", 100f, height - 100f, mHelpPint)
        canvas.drawText("控制点p1:$cp4", 100f, height - 200f, mHelpPint)
        canvas.drawText("控制点p2:$cp5", 100f, height - 300f, mHelpPint)
        canvas.drawText("终止点p3:$p3", 100f, height - 400f, mHelpPint)

        canvas.drawText("起始点p0:$p3", 100f, 100f, mHelpPint)
        canvas.drawText("控制点p1:$cp6", 100f, 200f, mHelpPint)
        canvas.drawText("控制点p2:$cp7", 100f, 300f, mHelpPint)
        canvas.drawText("终止点p3:$p0", 100f, 400f, mHelpPint)
    }
}
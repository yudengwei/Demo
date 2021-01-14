package com.example.view.bezier

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*

/**
 * Created by Administrator on 2017/3/31 0031.
 * E-Mail：543441727@qq.com
 * 使用贝塞尔曲线绘制圆心
 */
class MyViewCircle @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val mPaint: Paint
    private val mPaintCircle: Paint
    private val mPaintPoint: Paint
    private var mCenterX = 0f
    private var mCenterY = 0f
    private val mCircleRadius: Int
    private val mDuration = 1000   //动画总时间
    private var mCurrTime = 0    //当前已进行时间
    private val mCount = 100     //将总时间划分多少块
    private val mPiece = (mDuration / mCount).toFloat() //每一块的时间 ；
    private var mPointDatas = ArrayList<PointF>()
    private var mPointControlls = ArrayList<PointF>()

    private var isRuning = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //初始化坐标系
        mCenterX = width.toFloat() / 2
        mCenterY = height.toFloat() / 2

        //初始化数据点数据和辅助点位置

        mPointDatas.add(PointF(mCenterX, mCenterY - mCircleRadius))
        mPointDatas.add(PointF(mCenterX + mCircleRadius, mCenterY))
        mPointDatas.add(PointF(mCenterX, mCenterY + mCircleRadius))
        mPointDatas.add(PointF(mCenterX - mCircleRadius, mCenterY))

        mPointControlls.add(PointF(mCenterX + 70, mCenterY - mCircleRadius))
        mPointControlls.add(PointF(mCenterX + mCircleRadius, mCenterY - mCircleRadius / 2))

        mPointControlls.add(PointF(mCenterX + mCircleRadius + 50, mCenterY + mCircleRadius))
        mPointControlls.add(PointF(mCenterX + mCircleRadius, mCenterY + 70))

        mPointControlls.add(PointF(mCenterX - 100, mCenterY + mCircleRadius))
        mPointControlls.add(PointF(mCenterX - mCircleRadius, mCenterY +50))

        mPointControlls.add(PointF(mCenterX - 100, mCenterY - mCircleRadius))
        mPointControlls.add(PointF(mCenterX - mCircleRadius, mCenterY - 50))
    }

    override fun onDraw(canvas: Canvas) {

        //绘制坐标系
        drawCoordinates(canvas)

        //绘制圆
//        drawCenterCircle(canvas);


        //绘制数据点和辅助点
        drawBezierLine(canvas)
        /*if (isRuning) {
            //动态改变数据点和辅助点
            mCurrTime += mPiece.toInt()
            if (mCurrTime < mDuration) {
                mPointDatas[0].y -= (120 / mCount)
                mPointDatas[1].x += (120 / mCount)
                *//*mPointDatas[2].y += (120 / mCount)
                mPointDatas[3].x -= (120 / mCount)*//*
                mPointControlls[0].x += (120 / mCount)
                mPointControlls[0].y -= (120 / mCount)
                mPointControlls[1].x += (120 / mCount)
                mPointControlls[1].y -= (120 / mCount)
               *//*mPointControlls[2].x += (120 / mCount)
                mPointControlls[2].y += (120 / mCount)
                mPointControlls[3].x += (120 / mCount)
                mPointControlls[3].y += (120 / mCount)
                mPointControlls[4].x -= (120 / mCount)
                mPointControlls[4].y += (120 / mCount)
                mPointControlls[5].x -= (120 / mCount)
                mPointControlls[5].y += (120 / mCount)
                mPointControlls[6].x -= (120 / mCount)
                mPointControlls[6].y -= (120 / mCount)
                mPointControlls[7].x -= (120 / mCount)
                mPointControlls[7].y -= (120 / mCount)*//*
                postInvalidateDelayed(mPiece.toLong())
            }
        }*/
    }

    /**
     * @param canvas
     */
    fun drawCoordinates(canvas: Canvas) {
        canvas.save()
        //绘制x，y轴坐标系
        canvas.drawLine(mCenterX, 0f, mCenterX, height.toFloat(), mPaint)
        canvas.drawLine(0f, mCenterY, width.toFloat(), mCenterY, mPaint)
        canvas.restore()
    }

    fun drawCenterCircle(canvas: Canvas) {
        canvas.save()
        //绘制中心圆
        canvas.drawCircle(
            mCenterX,
            mCenterY,
            mCircleRadius.toFloat(),
            mPaintCircle
        )
        canvas.restore()
    }

    /**
     * @param canvas
     */
    fun drawBezierLine(canvas: Canvas) {
        //绘制数据点
        canvas.save()
        for (i in mPointDatas.indices) {
            canvas.drawPoint(mPointDatas[i].x, mPointDatas[i].y, mPaintPoint)
        }
        //绘制控制点
        for (i in mPointControlls.indices) {
            canvas.drawPoint(mPointControlls[i].x, mPointControlls[i].y, mPaintPoint)
        }

        //利用三阶贝塞尔曲线实现画圆
        val path = Path()
        path.moveTo(mPointDatas[0].x, mPointDatas[0].y)
        for (i in mPointDatas.indices) {
            if (i == mPointDatas.size - 1) {
                path.cubicTo(
                    mPointControlls[2 * i].x,
                    mPointControlls[2 * i].y,
                    mPointControlls[2 * i + 1].x,
                    mPointControlls[2 * i + 1].y,
                    mPointDatas[0].x,
                    mPointDatas[0].y
                )
            } else {
                path.cubicTo(
                    mPointControlls[2 * i].x,
                    mPointControlls[2 * i].y,
                    mPointControlls[2 * i + 1].x,
                    mPointControlls[2 * i + 1].y,
                    mPointDatas[i + 1].x,
                    mPointDatas[i + 1].y
                )
            }
        }
        canvas.drawPath(path, mPaintCircle)
        canvas.restore()
    }

    fun isRunning(): Boolean {
        return isRuning
    }

    fun setRunning(runing: Boolean) {
        isRuning = runing
        invalidate()
    }

    init {
        mPaint = Paint()
        mPaint.color = Color.BLACK
        mPaint.strokeWidth = 3f
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        mPaintCircle = Paint()
        mPaintCircle.color = Color.RED
        mPaintCircle.strokeWidth = 3f
        mPaintCircle.style = Paint.Style.STROKE
        mPaintCircle.isAntiAlias = true
        mPaintPoint = Paint()
        mPaintPoint.color = Color.GREEN
        mPaintPoint.strokeWidth = 5f
        mPaintPoint.style = Paint.Style.FILL
        mPaintPoint.isAntiAlias = true
        mCircleRadius = 300
    }
}
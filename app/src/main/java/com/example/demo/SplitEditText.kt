package com.example.demo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.InflateException
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText

class SplitEditText @JvmOverloads constructor(context: Context,attributeSet: AttributeSet?=null,defaultStyle : Int = 0)
    : AppCompatEditText(context,attributeSet,defaultStyle){

    private val INPUT_BOX_STYLE_SINGLE = 1
    private val INPUT_BOX_STYLE_UNDERLINE = 2
    private val INPUT_BOX_STYLE_CONNECT = 3

    private val CONTENT_SHOW_MODE_PASSWORD = 1
    private val CONTENT_SHOW_MODE_TEXT = 2

    private val mDivisionLineSize = dp2px(10f)
    private val mDivisionColor = Color.TRANSPARENT
    private val mBorderSize = dp2px(0f)
    private val mBorderColor = Color.TRANSPARENT
    private val mTextSize = sp2px(16f)
    private val mCursorColor = Color.BLACK
    private val mCursorWidth = dp2px(2f)
    private val mUnderlineNormalColor = Color.BLACK
    private val mContentNumber = 6
    private val mSpaceSize = dp2px(10f)
    private val mContentShowMode = CONTENT_SHOW_MODE_TEXT
    private val mCircleRadius = dp2px(5f)
    private val mTextColor = Color.BLACK
    private var mCursorHeight = 0
    private val mCornerSize = dp2px(0f)
    private val mCursorDuration = 500

    private var cursorRunnable =  CursorRunnable()

    private val mInputBoxSquare = true

    private val mInputBoxStyle = INPUT_BOX_STYLE_CONNECT

    private val mRectFConnect = RectF()
    private val mRectFSingleBox = RectF()

    private val mPaintUnderline = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = mBorderSize
        color = mUnderlineNormalColor
    }

    private val mPaintCursor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = mCursorWidth
        color = mCursorColor
    }

    private val mPaintContent = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = mTextSize
    }

    private val mPaintBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = mBorderSize
        color = mBorderColor
    }

    private val mPaintDivisionLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = mDivisionLineSize
        color = mDivisionColor
    }

    init {
        setSingleLine()
        isFocusableInTouchMode = true
        /*if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            setTextSelectHandle(android.R.color.transparent)
        }else{
            val f = TextView::class.java.getDeclaredField("mTextSelectHandleRes")
            f.isAccessible = true
            f.set(this,android.R.color.transparent)
        }*/
        filters = arrayOf(InputFilter.LengthFilter(mContentNumber))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        cursorRunnable = CursorRunnable()
        postDelayed(cursorRunnable, mCursorDuration.toLong())
    }

    override fun onDetachedFromWindow() {
        removeCallbacks(cursorRunnable)
        super.onDetachedFromWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if(mInputBoxSquare){
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val itemWidth = getContentItemWidthOnMeasure(width)
            when(mInputBoxStyle){
                INPUT_BOX_STYLE_UNDERLINE->{
                    setMeasuredDimension(width, (itemWidth + mBorderSize).toInt())
                }
                else->{
                    setMeasuredDimension(width, (itemWidth + mBorderSize * 2).toInt())
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        when(mInputBoxStyle){
            INPUT_BOX_STYLE_UNDERLINE->{
                drawUnderlineStyle(canvas)
            }
            INPUT_BOX_STYLE_SINGLE->{
                drawSingleStyle(canvas)
            }
            else->{
                drawConnectStyle(canvas)
            }
        }
        drawContent(canvas)
        drawCursor(canvas)
    }

    fun drawConnectStyle(canvas: Canvas){
        mRectFConnect.setEmpty()
        mRectFConnect.set(mBorderSize / 2,mBorderSize /2,
        width - mBorderSize /2,height - mBorderSize/2)
        canvas.drawRoundRect(mRectFConnect,mCornerSize,mCornerSize,mPaintBorder)
        drawDivisionLine(canvas)
    }

    fun drawDivisionLine(canvas: Canvas){
        val stopY = height - mBorderSize
        for (i in 0 until mContentNumber - 1) {
            //对于分割线条,startX = stopX
            val startX =
                (i + 1) * getContentItemWidth() + i * mDivisionLineSize + mBorderSize + mDivisionLineSize / 2
            canvas.drawLine(startX, mBorderSize, startX, stopY, mPaintDivisionLine)
        }
    }

    fun drawSingleStyle(canvas: Canvas){
        for (i in 0 until mContentNumber) {
            mRectFSingleBox.setEmpty()
            val left =
                i * getContentItemWidth() + i * mSpaceSize + i * mBorderSize * 2 + mBorderSize / 2
            val right =
                i * mSpaceSize + (i + 1) * getContentItemWidth() + (i + 1) * 2 * mBorderSize - mBorderSize / 2
            //为避免在onDraw里面创建RectF对象,这里使用rectF.set()方法
            mRectFSingleBox[left, mBorderSize / 2, right] = height - mBorderSize / 2
            canvas.drawRoundRect(mRectFSingleBox, mCornerSize, mCornerSize, mPaintBorder)
        }
    }

    fun drawUnderlineStyle(canvas: Canvas){}

    fun drawCursor(canvas: Canvas){
        if(mCursorHeight>height){
            throw InflateException("cursor height must smaller than view height")
        }
        val content = text.toString().trim()
        val startX = getDrawContentStartX(content.length)
        if(mCursorHeight==0){
            mCursorHeight = height /2
        }
        val sy = (height-mCursorHeight) /2
        val startY = sy + mBorderSize
        val stopY = height - sy - mBorderSize
        canvas.drawLine(startX,startY,startX,stopY,mPaintCursor)
    }

    private fun drawContent(canvas : Canvas){
        val cy = (height / 2).toFloat()
        val password = text.toString().trim()
        if(mContentShowMode==CONTENT_SHOW_MODE_PASSWORD){
            mPaintContent.color = Color.BLACK
            for(i in password.indices){
                val startX = getDrawContentStartX(i)
                canvas.drawCircle(startX,cy,mCircleRadius,mPaintContent)
            }
        }else{
            mPaintContent.color = mTextColor
            val baselineText = getTextBaseline(mPaintContent,cy)
            for(i in password.indices){
                val startX = getDrawContentStartX(i)
                //计算文字宽度
                val text = password[i].toString()
                val textWidth = mPaintContent.measureText(text)
                //绘制文字x应该还需要减去文字宽度的一半
                canvas.drawText(text, startX - textWidth / 2, baselineText, mPaintContent)
            }
        }
    }

    fun getTextBaseline(paint : Paint,cy : Float) : Float{
        val fontMetrics = paint.fontMetrics
        val dy = (fontMetrics.bottom - fontMetrics.top) /2 - fontMetrics.bottom
        return cy + dy
    }

    private fun getDrawContentStartX(index : Int) : Float{
        return when(mInputBoxStyle){
            INPUT_BOX_STYLE_SINGLE->{
                getContentItemWidth() / 2 + index * getContentItemWidth() + index * mSpaceSize + (2 * index + 1) * mBorderSize
            }
            INPUT_BOX_STYLE_UNDERLINE->{
                getContentItemWidth() / 2 + index * mSpaceSize + index * getContentItemWidth()
            }
            else ->{
                getContentItemWidth() / 2 + index * getContentItemWidth() + index * mDivisionLineSize + mBorderSize
            }
        }
    }

    fun getContentItemWidth() : Float{
        val tempwidth = when(mInputBoxStyle){
            INPUT_BOX_STYLE_SINGLE->{
                width - (mContentNumber - 1) * mSpaceSize - 2 * mContentNumber * mBorderSize
            }
            INPUT_BOX_STYLE_UNDERLINE->{
                getWidth() - (mContentNumber - 1) * mSpaceSize
            }
            else->{
                width - mDivisionLineSize * (mContentNumber - 1) - 2 * mBorderSize
            }
        }
        return tempwidth / mContentNumber
    }

    private fun getContentItemWidthOnMeasure(measureWidth : Int) : Float{
        val tempWidth = when(mInputBoxStyle){
            INPUT_BOX_STYLE_CONNECT->{
                measureWidth-(mDivisionLineSize * (mContentNumber-1))-2*mBorderSize
            }
            INPUT_BOX_STYLE_SINGLE->{
                measureWidth - (mContentNumber - 1) * mSpaceSize - 2 * mContentNumber * mBorderSize
            }
            else ->{
                //下划线样式：宽度-间距宽度(字符数-1)*每个间距宽度
                measureWidth - (mContentNumber - 1) * mSpaceSize
            }
        }
        val fl = tempWidth / mContentNumber
        Log.d("ABiao","tempWidth : $tempWidth, fl: $fl")
        return fl
    }

    private fun dp2px(dpValue : Float) : Float{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,dpValue,resources.displayMetrics
        )
    }

    private fun sp2px(spValue : Float) : Float{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,spValue,resources.displayMetrics
        )
    }

    private inner class CursorRunnable : Runnable {
        override fun run() {
            //获取光标画笔的alpha值
            val alpha: Int = mPaintCursor.alpha
            //设置光标画笔的alpha值
            mPaintCursor.alpha = if (alpha == 0) 255 else 0
            invalidate()
            postDelayed(this, mCursorDuration.toLong())
        }
    }
}
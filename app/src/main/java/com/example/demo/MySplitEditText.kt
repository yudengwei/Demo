package com.example.demo

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MySplitEditText @JvmOverloads constructor(context: Context,attributeSet: AttributeSet?=null,defaultStyle : Int = 0) :
    AppCompatEditText(context,attributeSet,defaultStyle){

    private var mTextFinishListener : ((content : String) -> Unit)? = null

    private var mItemBorderColor : Int = 0
    private var mItemBorderWidth : Float = 0f
    private var mItemWidth = 0f
    private var mTextLength = 0
    private var mSpacing = 0f
    private var mContentColor = 0
    private var mContentSize = 0f
    private var mCursorWidth = 0f
    private var mCursorColor = 0
    private var mCurrentItemColor = 0

    private var mCurrentIndex = 0
    private var mIsClickItem = false

    private val mChangeListener = ChangeListener()

    private var mBeforeText = ""

    private val mObservable = Observable.interval(0L,500L,TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    private lateinit var mDisposable : Disposable

    private val mItemRectF = RectF()

    private val mContentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
    }

    private val mItemBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val mCursorPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        initResource(attributeSet)
        setSingleLine()
        isFocusableInTouchMode = true
        /*if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
            setTextSelectHandle(android.R.color.transparent)
        }else{
            val f = TextView::class.java.getDeclaredField("mTextSelectHandleRes")
            f.isAccessible = true
            f.set(this,android.R.color.transparent)
        }*/
        filters = arrayOf(InputFilter.LengthFilter(mTextLength))
        setBackgroundColor(Color.parseColor("#ffffff"))
        addTextChangedListener(mChangeListener)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(MotionEvent.ACTION_DOWN == event.action) {
            val x = event.x
            val y = event.y
            for(i in 0 until mTextLength){
                val startX = getItemStartX(i)
                val endX = startX + mItemWidth
                if(x in startX..endX && y in 0 .. height){
                    var contentLength = text.toString().length
                    mIsClickItem = true
                    mCurrentIndex = i
                    while (mCurrentIndex!=0 && contentLength<mCurrentIndex){
                        removeTextChangedListener(mChangeListener)
                        append(" ")
                        contentLength++
                    }
                    addTextChangedListener(mChangeListener)
                    setSelection(mCurrentIndex)
                    Log.d("MainActivity","mCurrentIndex: $mCurrentIndex")
                    break
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun initResource(attributeSet: AttributeSet?){
        val typearry = context.obtainStyledAttributes(attributeSet,R.styleable.MySplitEditText)
        mTextLength = typearry.getInteger(R.styleable.MySplitEditText_textLength,6)
        mSpacing = typearry.getDimension(R.styleable.MySplitEditText_spacing,UiUtil.dp2px(resources,20f))
        initItemPaint(typearry)
        initContentPaint(typearry)
        initCursorPaint(typearry)
        typearry.recycle()
    }

    private fun initItemPaint(typearry : TypedArray){
        mItemBorderColor = typearry.getColor(R.styleable.MySplitEditText_itemBorderColor, Color.BLACK)
        mItemBorderWidth = typearry.getDimension(R.styleable.MySplitEditText_itemBorderWidth,UiUtil.dp2px(resources,1f))
        mCurrentItemColor = typearry.getColor(R.styleable.MySplitEditText_currentItemColor,Color.BLUE)
        mItemBorderPaint.color = mItemBorderColor
        mItemBorderPaint.strokeWidth = mItemBorderWidth
    }

    private fun initContentPaint(typearry: TypedArray){
        mContentColor = typearry.getColor(R.styleable.MySplitEditText_contentColor,Color.BLACK)
        mContentSize = typearry.getDimension(R.styleable.MySplitEditText_contentSize,UiUtil.sp2px(resources,25f))
        mContentPaint.color = mContentColor
        mContentPaint.textSize = mContentSize
    }

    private fun initCursorPaint(typearry: TypedArray){
        mCursorColor = typearry.getColor(R.styleable.MySplitEditText_cursorColor,Color.BLACK)
        mCursorWidth = typearry.getDimension(R.styleable.MySplitEditText_cursorWidth,UiUtil.dp2px(resources,1f))
        mCursorPaint.color = mContentColor
        mCursorPaint.strokeWidth = mCursorWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val allSpacing = mSpacing * (mTextLength-1)
        val realWidth = measureWidth - allSpacing - mTextLength * mItemBorderWidth * 2 - paddingStart - paddingEnd
        mItemWidth = realWidth / mTextLength
        setMeasuredDimension(measureWidth,( mItemWidth + mItemBorderWidth * 2).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        drawItemRectF(canvas)
        drawContent(canvas)
        drawCursor(canvas)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mDisposable = mObservable.subscribe {
            mCursorPaint.alpha = if(mCursorPaint.alpha==255) 0 else 255
            invalidate()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mDisposable.dispose()
    }

    private fun drawItemRectF(canvas: Canvas){
        for(i in 0 until mTextLength){
            val startX = getItemStartX(i)
            if(mCurrentIndex==i){
                mItemBorderPaint.color = mCurrentItemColor
            }else{
                mItemBorderPaint.color = mItemBorderColor
            }
            mItemRectF.set(startX,mItemBorderWidth,startX+mItemWidth,mItemWidth-mItemBorderWidth)
            canvas.drawRoundRect(mItemRectF,0f,0f,mItemBorderPaint)
        }
    }

    private fun drawContent(canvas: Canvas){
        val content = text.toString()
        val cy = (height / 2).toFloat()
        //Log.d("MainActivity","content: ${content.length}")
        for(i in content.indices){
            if(" " == content){
                continue
            }
            //Log.d("MainActivity","i: $i")
            val currentText = content[i]
            val startX = getItemStartX(i)
            val baseLine = getTextBaseline(mContentPaint,cy)
            val textWidth = mContentPaint.measureText(currentText.toString())
            val fl = startX + mItemWidth / 2 - textWidth / 2
            canvas.drawText(currentText.toString(),fl,baseLine,mContentPaint)
        }
    }

    private fun drawCursor(canvas: Canvas){
        if(mCurrentIndex < mTextLength){
            val startX = getItemStartX(mCurrentIndex)
            canvas.drawLine(startX+mItemWidth/4,mItemWidth / 4,startX+mItemWidth/4,mItemWidth * 3 / 4,mCursorPaint)
        }
    }

    private fun getItemStartX(index : Int) : Float{
        return index * mItemWidth + index * mSpacing + paddingStart + mItemBorderWidth
    }

    private fun getTextBaseline(paint : Paint,cy : Float) : Float {
        val fontMetrics = paint.fontMetrics
        val dy = (fontMetrics.bottom - fontMetrics.top) /2 - fontMetrics.bottom
        return cy + dy
    }

    inner class ChangeListener : TextWatcher{
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val content = p0?.toString()
            Log.d("MainActivity","content: ${content?.length}")
            content?.let {
                mCurrentIndex += 1
                val realContent = it.trim()
                if(realContent.length >= mTextLength){
                    mTextFinishListener?.invoke(content)
                }
            }
        }

    }

    fun setTextFinishListener(block : (content : String)->Unit){
        mTextFinishListener = block
    }
}
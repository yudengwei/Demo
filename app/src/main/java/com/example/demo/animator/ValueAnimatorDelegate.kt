package com.example.demo.animator

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PathMeasure
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.Animation
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ValueAnimatorDelegate(val view : View) : ReadOnlyProperty<Any?,ValueAnimator>{

    private var mValueAnimator : ValueAnimator? = null
    private var mMetrics : DisplayMetrics = view.resources.displayMetrics

    override fun getValue(thisRef: Any?, property: KProperty<*>): ValueAnimator {
        if(mValueAnimator!=null){
            return mValueAnimator!!
        }
        val path = PathUtil.createBubble(mMetrics.widthPixels,mMetrics.heightPixels,10,25,0.3f)
        val pathMeasure = PathMeasure(path,false)
        return ValueAnimator.ofFloat(0f, toPx(pathMeasure.length)).apply {
            duration = 5000
            addUpdateListener {
                val floatArray = FloatArray(2)
                val fl = it.animatedValue as Float
                pathMeasure.getPosTan(fl, floatArray, null)
                view.x = floatArray[0]
                view.y = floatArray[1]
            }
        }
    }

    fun toPx(dp : Float) : Float{
        return dp * mMetrics.density
    }

}
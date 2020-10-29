package com.example.demo

import android.content.res.Resources
import android.util.TypedValue

object UiUtil {

    fun dp2px(resources : Resources, dpValue : Float) : Float{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,dpValue,resources.displayMetrics
        )
    }

    fun sp2px(resources : Resources,spValue : Float) : Float{
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,spValue,resources.displayMetrics
        )
    }
}
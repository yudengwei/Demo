package com.example.view.bezier

import android.animation.TimeInterpolator
import com.example.base.util.LogUtil

class MyInterpolator : TimeInterpolator {

    override fun getInterpolation(input: Float): Float {
        LogUtil.d("input: $input")
        return input
    }
}
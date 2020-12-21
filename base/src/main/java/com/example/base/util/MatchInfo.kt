package com.example.base.util

import android.content.Context

object MatchInfo {

    const val MATCH_BASE_WIDTH = 0
    const val MATCH_BASE_HEIGHT = 1

    var screenWidth = 0f

    var screenHeight = 0f

    var appDensity = 0f

    var appDensityDpi = 0

    var appScaledDensity = 0f

    var appXdpi = 0f

    fun init(applicationContext : Context){
        val displayMetrics = applicationContext.resources.displayMetrics
        screenWidth = displayMetrics.widthPixels.toFloat()
        screenHeight = displayMetrics.heightPixels.toFloat()
        appDensity = displayMetrics.density
        appDensityDpi = displayMetrics.densityDpi
        appScaledDensity = displayMetrics.scaledDensity
        appXdpi = displayMetrics.xdpi
    }

    fun matchByDP(context : Context, design : Float, base : Int){
        val targetDensity : Float = if( base == MATCH_BASE_HEIGHT) screenHeight / design else screenWidth / design
        val targetDensityDpi = (targetDensity * 160).toInt()
    }
}
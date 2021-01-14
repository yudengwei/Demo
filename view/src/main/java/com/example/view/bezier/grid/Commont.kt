package com.example.view.bezier.grid

import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlin.math.sqrt

fun isClickPoint(src : Point, dst: Point, radius: Float) = disPos2d(src.x.toFloat(), src.y.toFloat(), dst.x.toFloat(), dst.y.toFloat()) <= radius

fun loadWinSize(context: Context) : Point{
    val point = Point()
    val outMetrics = DisplayMetrics()
    (context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.defaultDisplay?.getMetrics(outMetrics)
    point.x = outMetrics.widthPixels
    point.y = outMetrics.heightPixels
    return point
}

fun disPos2d(x1: Float, y1: Float, x2: Float, y2: Float) = sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
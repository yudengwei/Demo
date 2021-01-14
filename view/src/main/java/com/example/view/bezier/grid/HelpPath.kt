package com.example.view.bezier.grid

import android.graphics.Path
import android.graphics.Point

fun gridPath(step : Int, windowPoint : Point) : Path{
    val path = Path()
    val maxY = windowPoint.y / step
    val maxX = windowPoint.x / step

    for (i in 0..maxY) {
        path.moveTo(0f, (step * i).toFloat())
        path.lineTo(windowPoint.x.toFloat(), (step * i).toFloat())
    }

    for (i in 0..maxX) {
        path.moveTo((step * i).toFloat(), 0f)
        path.lineTo((step * i).toFloat(), windowPoint.y.toFloat())
    }
    return path
}
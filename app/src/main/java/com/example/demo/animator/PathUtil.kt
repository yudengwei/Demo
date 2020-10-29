package com.example.demo.animator

import android.graphics.Path
import androidx.annotation.FloatRange
import java.util.*
import kotlin.collections.ArrayList

object PathUtil {

    fun createBubble(width : Int,height : Int,quadCount : Int,wobbleRange : Int,
                     @FloatRange(from = 0.toDouble(),to = 1.toDouble()) intensity : Float) : Path{
        val max = width * 3 / 4
        val min = width / 4
        val bezierPoints = ArrayList<BezierPoint>()
        val random = Random()
        val startPoint = BezierPoint((random.nextInt(max) % (max - min + 1)).toFloat() + min,height.toFloat())

        for(i in quadCount downTo 0){
            if(i==quadCount){
                bezierPoints.add(startPoint)
            }else{
                val otherPoint = BezierPoint(0f,0f)
                if((0 .. 100).random() % 2 ==0){
                    otherPoint.x = startPoint.x + (0 .. wobbleRange).random()
                }else{
                    otherPoint.x = startPoint.x - (0 .. wobbleRange).random()
                }
                otherPoint.y = height / quadCount.toFloat() * i
                bezierPoints.add(otherPoint)
            }
        }
        return createCubicSpline(bezierPoints,intensity)
    }

    fun createCubicSpline(bezierPoints : List<BezierPoint>,@FloatRange(from = 0.toDouble(),to = 1.toDouble()) intensity : Float) : Path{
        val path = Path()
        for(i in bezierPoints.indices){
            val point = bezierPoints[i]
            when(i){
                0 -> {
                    val next = bezierPoints[i+1]
                    point.dx = (next.x-point.x)*intensity
                    point.dy = (next.y-point.y)*intensity
                }
                bezierPoints.size-1 -> {
                    val prev = bezierPoints[i-1]
                    point.dx = (point.x-prev.x)*intensity
                    point.dy = (point.y-point.y)*intensity
                }
                else -> {
                    val next = bezierPoints[i+1]
                    val pre = bezierPoints[i-1]
                    point.dx = (next.x - pre.x)*intensity
                    point.dy = (next.y - pre.y)*intensity
                }
            }
            if(i==0){
                path.moveTo(point.x,point.y)
            }else{
                val pre = bezierPoints[i-1]
                path.cubicTo(pre.x+pre.dx,pre.y+pre.y,point.x-point.dx,point.y-point.dy,point.x,point.y)
            }
        }
        return path
    }


}
package com.example

import android.graphics.Path
import android.renderscript.RenderScript
import androidx.annotation.FloatRange
import androidx.annotation.NonNull
import com.example.PathUtils.BezierPoint
import java.util.*

/**
 * Create [Path] Utility
 *
 *
 * Author:李玉江[QQ:1032694760]
 * DateTime:2016/1/24 0:07
 * Builder:Android Studio
 *
 * @see BezierPoint
 *
 * @see SvgPathParser
 */
object PathUtils {
    /**
     * create bubble, Just the opposite with follower
     *
     * @param width       the width
     * @param height      the height
     * @param quadCount   the quad count
     * @param wobbleRange the wobble range
     * @param intensity   the intensity
     * @return the path
     */
    /**
     * Create bubble path.
     *
     * @param width  the width
     * @param height the height
     * @return the path
     */
    @JvmOverloads
    fun createBubble(
        width: Int, height: Int, quadCount: Int = 10, wobbleRange: Int = 25,
        @FloatRange(from = 0.toDouble(), to = 1.toDouble()) intensity: Float = 0.3f
    ): Path {
        val max = (width * 3 / 4f).toInt()
        val min = (width / 4f).toInt()
        val random = Random()
        val points = ArrayList<BezierPoint>()
        val startPoint =
            BezierPoint((random.nextInt(max) % (max - min + 1) + min).toFloat(), height.toFloat())
        for (i in quadCount downTo 1) {
            if (i == quadCount) {
                points.add(startPoint)
            } else {
                val otherPoint = BezierPoint(0f, 0f)
                if (random.nextInt(100) % 2 == 0) {
                    otherPoint.x = startPoint.x + random.nextInt(wobbleRange)
                } else {
                    otherPoint.x = startPoint.x - random.nextInt(wobbleRange)
                }
                otherPoint.y = (height / quadCount.toFloat() * i)
                points.add(otherPoint)
            }
        }
        return createCubicSpline(points, intensity)
    }
    /**
     * create follower
     *
     * @param width       the width
     * @param height      the height
     * @param quadCount   the quad count
     * @param wobbleRange the wobble range
     * @param intensity   the intensity
     * @return the path
     */
    /**
     * create follower
     *
     * @param width  the width
     * @param height the height
     * @return the path
     */
    @JvmOverloads
    fun createFollower(
        width: Int, height: Int, quadCount: Int = 10, wobbleRange: Int = 70,
        @FloatRange(from = 0.toDouble(), to = 1.toDouble()) intensity: Float = 0.3f
    ): Path {
        val max = (width * 3 / 4f).toInt()
        val min = (width / 4f).toInt()
        val random = Random()
        val points = ArrayList<BezierPoint>()
        val startPoint = BezierPoint((random.nextInt(max) % (max - min + 1) + min).toFloat(), 0f)
        for (i in 0 until quadCount) {
            if (i == 0) {
                points.add(startPoint)
            } else {
                val otherPoint = BezierPoint(0f, 0f)
                if (random.nextInt(100) % 2 == 0) {
                    otherPoint.x = startPoint.x + random.nextInt(wobbleRange)
                } else {
                    otherPoint.x = startPoint.x - random.nextInt(wobbleRange)
                }
                otherPoint.y = (height / quadCount.toFloat() * i)
                points.add(otherPoint)
            }
        }
        return createCubicSpline(points, intensity)
    }

    /**
     * Create cubic spline path.
     *
     * @param points    the points
     * @param intensity the intensity
     * @return the path
     */
    @NonNull
    fun createCubicSpline(
        points: ArrayList<BezierPoint>,
        @FloatRange(from = 0.toDouble(), to = 1.toDouble()) intensity: Float
    ): Path {
        val path = Path()
        for (i in points.indices) {
            val point = points[i]
            when (i) {
                0 -> {
                    val next = points[i + 1]
                    point.dx = (next.x - point.x) * intensity
                    point.dy = (next.y - point.y) * intensity
                }
                points.size - 1 -> {
                    val prev = points[i - 1]
                    point.dx = (point.x - prev.x) * intensity
                    point.dy = (point.y - prev.y) * intensity
                }
                else -> {
                    val next = points[i + 1]
                    val prev = points[i - 1]
                    point.dx = (next.x - prev.x) * intensity
                    point.dy = (next.y - prev.y) * intensity
                }
            }
            // create the cubic-spline path
            if (i == 0) {
                path.moveTo(point.x, point.y)
            } else {
                val prev = points[i - 1]
                path.cubicTo(
                    prev.x + prev.dx, prev.y + prev.dy,
                    point.x - point.dx, point.y - point.dy,
                    point.x, point.y
                )
            }
        }
        return path
    }

    /**
     * The type Bezier point.
     */
    class BezierPoint(x: Float, y: Float) {
        /**
         * The X.
         */
        var x = 0f

        /**
         * The Y.
         */
        var y = 0f

        /**
         * The x-axis distance
         */
        var dx = 0f

        /**
         * The y-axis distance
         */
        var dy = 0f

        /**
         * Instantiates a new bezier point.
         *
         * @param x the x
         * @param y the y
         */
        init {
            this.x = x
            this.y = y
        }
    }
}
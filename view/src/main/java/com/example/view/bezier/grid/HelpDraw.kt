package com.example.view.bezier.grid

import android.content.Context
import android.graphics.*

fun getGrid(context: Context) = getGrid(loadWinSize(context))
fun getCoo(context : Context, originPoint: Point = Point(0, 0)) = getCoo(originPoint, loadWinSize(context))

fun getGrid(windowPoint: Point) : Picture {
    val picture = Picture()
    val draw = picture.beginRecording(windowPoint.x, windowPoint.y)
    val paint = getHelpPoint(Color.GRAY)
    draw.drawPath(gridPath(100, windowPoint), paint)
    return picture
}

fun getHelpPoint(color: Int) = Paint().also { paint ->
    paint.strokeWidth = 2f
    paint.color = color
    paint.textSize = 50f
    paint.strokeCap = Paint.Cap.ROUND
    paint.style = Paint.Style.STROKE
    //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
    paint.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
}

fun drawGrid(canvas: Canvas, vararg pictures: Picture) {
    for (picture in pictures) {
        picture.draw(canvas)
    }
}

fun drawPos(paint : Paint, canvas: Canvas, vararg points : Point) {
    for (point in points) {
        canvas.drawPoint(point.x.toFloat(), point.y.toFloat(), paint)
    }
}

fun drawLines(paint : Paint, canvas : Canvas, vararg points : Point) {
    for (i in points.indices step 2) {
        if (i > points.size - 2) {
            break
        }
        canvas.drawLine(points[i].x.toFloat(), points[i].y.toFloat(), points[i + 1].x.toFloat(), points[i + 1].y.toFloat(), paint)
    }
}

fun getCoo(originPoint: Point, windowPoint: Point) : Picture {
    val picture = Picture()
    val recording = picture.beginRecording(windowPoint.x, windowPoint.y)

    //初始化网格画笔
    val paint = Paint()
    paint.strokeWidth = 4f
    paint.color = Color.BLACK
    paint.style = Paint.Style.STROKE
    //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
    paint.pathEffect = null

    recording.drawPath(cooPath(originPoint, windowPoint), paint)
    //绘制左箭头
    recording.drawLine(windowPoint.x.toFloat(), originPoint.y.toFloat(), windowPoint.x.toFloat() - 40, originPoint.y.toFloat() - 20, paint)
    recording.drawLine(windowPoint.x.toFloat(), originPoint.y.toFloat(), windowPoint.x.toFloat() - 40, originPoint.y.toFloat() + 20, paint)
    //绘制下箭头
    recording.drawLine(originPoint.x.toFloat(), windowPoint.y.toFloat(), originPoint.x.toFloat() + 20, windowPoint.y.toFloat() - 40, paint)
    recording.drawLine(originPoint.x.toFloat(), windowPoint.y.toFloat(), originPoint.x.toFloat() - 20, windowPoint.y.toFloat() - 40, paint)
    drawCooText(recording, windowPoint, paint, originPoint)
    return picture
}

/**
 * 坐标系路径
 *
 * @param coo     坐标点
 * @param winSize 屏幕尺寸
 * @return 坐标系路径
 */
fun cooPath(coo: Point, winSize: Point): Path {
    val path = Path()
    //x正半轴线
    path.moveTo(coo.x.toFloat(), coo.y.toFloat())
    path.lineTo(winSize.x.toFloat(), coo.y.toFloat())
    //x负半轴线
    path.moveTo(coo.x.toFloat(), coo.y.toFloat())
    path.lineTo((coo.x - winSize.x).toFloat(), coo.y.toFloat())
    //y负半轴线
    path.moveTo(coo.x.toFloat(), coo.y.toFloat())
    path.lineTo(coo.x.toFloat(), (coo.y - winSize.y).toFloat())
    //y负半轴线
    path.moveTo(coo.x.toFloat(), coo.y.toFloat())
    path.lineTo(coo.x.toFloat(), winSize.y.toFloat())
    return path
}

fun drawCooText(canvas : Canvas, winSize : Point,paint : Paint, coo : Point) {
    paint.textSize = 50f
    canvas.drawText("x", winSize.x.toFloat() - 50, coo.y.toFloat() + 50, paint)
    canvas.drawText("y", coo.x.toFloat() + 50, winSize.y.toFloat() - 30, paint)
}
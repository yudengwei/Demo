package com.example.view.opengl.flatgraphics

import android.opengl.GLES20
import kotlin.math.cos
import kotlin.math.sin

open class MyCircle : FlatGraphics(){

    // 圆形分割的数量，分成 360 份，可由 360 个线段组成空心圆，也可以由 360 个三角形组成实心圆
    private var VERTEX_DATA_NUM = 360

    override fun onDraw() {
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 1, VERTEX_DATA_NUM)
    }

    override fun getFloatData() = initVertexData()

    // 初始化圆形的顶点数据
    private fun initVertexData() : FloatArray {
        VERTEX_DATA_NUM = 360
        val radian = (2 * Math.PI).toFloat() / VERTEX_DATA_NUM
        val radius = 0.8f
        // 360 个顶点的位置，因为有 x 和 y 坐标，所以 double 一下，再加上中心点 和 闭合的点
        return FloatArray(VERTEX_DATA_NUM * 2 + 4).also {
            // 中心点
            it[0] = 0f
            it[1] = 0f
            // 圆的 360 份的顶点数据
            for (i in 0 until VERTEX_DATA_NUM) {
                it[2 * i + 2] = (radius * cos(radian * i))
                it[2 * i + 1 + 2] = (radius * sin(radian * i))
            }
            // 闭合点
            it[VERTEX_DATA_NUM * 2 + 2] = (radius * cos(radian))
            it[VERTEX_DATA_NUM * 2 + 3] = (radius * sin(radian))
        }
    }
}
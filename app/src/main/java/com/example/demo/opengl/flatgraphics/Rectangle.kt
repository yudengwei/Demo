package com.example.demo.opengl.flatgraphics

import android.opengl.GLES20
import com.example.demo.R
import com.example.demo.opengl.VertexArray

class Rectangle : FlatGraphics() {

    override fun onDraw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
    }

    override fun getFloatData() = floatArrayOf(
        0f,    0f,
        -0.5f, -0.8f,
        0.5f, -0.8f,
        0.5f,  0.8f,
        -0.5f,  0.8f,
        -0.5f, -0.8f)
}
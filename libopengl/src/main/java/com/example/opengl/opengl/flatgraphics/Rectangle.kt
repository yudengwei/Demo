package com.example.opengl.opengl.flatgraphics

import android.opengl.GLES20

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
package com.example.opengl.opengl.flatgraphics

import android.opengl.GLES20

class MyLine : FlatGraphics() {

    override fun onDraw() {
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0,2)
    }

    override fun getFloatData() = floatArrayOf(
            -0.5f, 0.5f,
            0.5f, -0.5f
        )
}
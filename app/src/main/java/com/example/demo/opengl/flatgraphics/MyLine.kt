package com.example.demo.opengl.flatgraphics

import android.opengl.GLES20
import com.example.demo.R
import com.example.demo.opengl.VertexArray
import com.example.demo.opengl.buildProgram

class MyLine : FlatGraphics() {

    override fun onDraw() {
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0,2)
    }

    override fun getFloatData() = floatArrayOf(
            -0.5f, 0.5f,
            0.5f, -0.5f
        )
}
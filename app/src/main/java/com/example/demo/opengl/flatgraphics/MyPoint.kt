package com.example.demo.opengl.flatgraphics

import android.opengl.GLES20
import com.example.demo.R
import com.example.demo.opengl.VertexArray
import com.example.demo.opengl.buildProgram

class MyPoint : FlatGraphics(){

    override fun onDraw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
    }

    override fun getFloatData() = floatArrayOf(0f, 0f)

    override fun getVertexId() = R.raw.vertex_point
}
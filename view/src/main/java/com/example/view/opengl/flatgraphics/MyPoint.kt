package com.example.view.opengl.flatgraphics

import android.opengl.GLES20
import com.example.view.R

class MyPoint : FlatGraphics(){

    override fun onDraw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1)
    }

    override fun getFloatData() = floatArrayOf(0f, 0f)

    override fun getVertexId() = R.raw.vertex_point
}
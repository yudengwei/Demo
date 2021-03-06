package com.example.opengl.opengl.flatgraphics

import android.opengl.GLES20

class Triangle : FlatGraphics(){

    override fun onDraw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)
    }

    override fun getFloatData() = floatArrayOf(
            0.0f, 0.5f, // top
            -0.5f, -0.5f, // bottom left
            0.5f, -0.5f  // bottom right
        )
}
package com.example.view.opengl.render

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.example.view.opengl.flatgraphics.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyRender : GLSurfaceView.Renderer {

    private lateinit var mGraphics : FlatGraphics

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        mGraphics = MyCircleMatrix()
        GLES20.glClearColor(1f, 1f, 1f, 1f)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        mGraphics.onSurfaceChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        mGraphics.draw()
    }
}
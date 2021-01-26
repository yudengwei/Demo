package com.example.opengl

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyRender(private val nativeRender : NativeRender = NativeRender()) : GLSurfaceView.Renderer {

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        nativeRender.native_onSurfaceCreated()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        nativeRender.native_onSurfaceChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        nativeRender.native_onDrawFrame()
    }

    fun destroy() {
        nativeRender.native_unInit()
    }
}
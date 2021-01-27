package com.example.opengl.opengl

import android.content.Context
import android.opengl.GLSurfaceView
import com.example.opengl.opengl.render.MyRender

class MySurfaceView1(context: Context) : GLSurfaceView(context) {

    private val myRender = MyRender()

    init {
        setEGLContextClientVersion(2)
        setRenderer(myRender)
        renderMode = RENDERMODE_CONTINUOUSLY
    }
}
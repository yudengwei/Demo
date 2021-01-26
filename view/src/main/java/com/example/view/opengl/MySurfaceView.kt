package com.example.view.opengl

import android.content.Context
import android.opengl.GLSurfaceView
import com.example.view.opengl.render.MyRender

class MySurfaceView(context: Context) : GLSurfaceView(context) {

    private val myRender = MyRender()

    init {
        setEGLContextClientVersion(2)
        setRenderer(myRender)
        renderMode = RENDERMODE_CONTINUOUSLY
    }
}
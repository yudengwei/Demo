package com.example.demo.opengl

import android.content.Context
import android.opengl.GLSurfaceView
import com.example.demo.opengl.render.MyRender

class MySurfaceView(context: Context) : GLSurfaceView(context) {

    private val myRender = MyRender()

    init {
        setEGLContextClientVersion(2)
        setRenderer(myRender)
    }
}
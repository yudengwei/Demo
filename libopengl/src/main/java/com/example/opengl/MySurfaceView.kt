package com.example.opengl

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class MySurfaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    private val mRender: MyRender
) : GLSurfaceView(context, attributeSet) {

    init {
        setEGLContextClientVersion(2)
        setRenderer(mRender)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

}
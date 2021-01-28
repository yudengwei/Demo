package com.example.opengl.opengl.render

import android.opengl.GLSurfaceView
import com.example.opengl.opengl.flatgraphics.TextureMap
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class NdkRender : GLSurfaceView.Renderer {

    private val mTextureMap = TextureMap()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        mTextureMap.onCreate()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        mTextureMap.onSizeChange()
    }

    override fun onDrawFrame(gl: GL10?) {
        mTextureMap.onDraw()
    }
}
package com.example.opengl

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import com.example.opengl.opengl.readTextFileFromResource
import java.nio.ByteBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyRender(private val nativeRender: NativeRender = NativeRender(), private val context: Context) : GLSurfaceView.Renderer {

    val IMAGE_FORMAT_RGBA = 0x01
    val IMAGE_FORMAT_NV21 = 0x02
    val IMAGE_FORMAT_NV12 = 0x03
    val IMAGE_FORMAT_I420 = 0x04

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //nativeRender.native_init(readTextFileFromResource(R.raw.vertex_tri_ndk), readTextFileFromResource(R.raw.frag_tri_ndk))
        nativeRender.native_init(readTextFileFromResource(R.raw.vertex_texture_ndk), readTextFileFromResource(R.raw.frag_texture_ndk))
        context.assets.open("YUV_Image_840x1074.NV21").use {
            val buffer = ByteArray(it.available())
            it.read(buffer)
            nativeRender.native_setImageData(IMAGE_FORMAT_NV21, 840, 1074, buffer)
        }
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
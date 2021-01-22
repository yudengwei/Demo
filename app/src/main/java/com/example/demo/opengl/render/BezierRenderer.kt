package com.example.demo.opengl.render

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.example.demo.opengl.flatgraphics.BezierCurve
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class BezierRenderer : GLSurfaceView.Renderer{

    private val mModelMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mMVPMatrix = FloatArray(16)
    private val mTemporaryMatrix = FloatArray(16)

    private lateinit var mBezierCurve: BezierCurve

    private var num = 0f
    private val delta = 200


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        mBezierCurve = BezierCurve()
        Matrix.setIdentityM(mModelMatrix, 0)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        val aspectRatio =
            if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 3f, 7f)
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, 3f, 7f)
        }
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {

        num++

        mBezierCurve.setAmp((3.0f * (num % delta) / delta))

        val resultMatrix = FloatArray(16)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        Matrix.multiplyMM(resultMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0)

        mBezierCurve.draw(resultMatrix)
    }
}
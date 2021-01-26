package com.example.view.opengl.flatgraphics

import android.opengl.GLES20
import android.opengl.Matrix
import com.example.view.R

class MyCircleMatrix : MyCircle() {

    private val U_ProMatrix = "u_ProjectMatrix"
    private var uProMatrixLocation = 0

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    override fun onDraw() {
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(modelMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        GLES20.glUniformMatrix4fv(uProMatrixLocation, 1, false, modelMatrix, 0)
        super.onDraw()
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        super.onSurfaceChanged(width, height)
        uProMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_ProMatrix)
        val aspectRatio = if (width > height) width.toFloat() / height else height.toFloat() / width
        Matrix.frustumM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 3f, 7f)
    }

    override fun getVertexId() = R.raw.vertex_line_matrix
}
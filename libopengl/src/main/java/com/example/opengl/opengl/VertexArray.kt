package com.example.opengl.opengl

import android.opengl.GLES20

class VertexArray(floatArray: FloatArray) {

    private val mData = floatArray.toFloatBuff()

    fun setVertexPosition(dataOffset : Int, attributeLocation : Int, size : Int, stride : Int) {
        mData.position(dataOffset)
        GLES20.glVertexAttribPointer(attributeLocation, size, GLES20.GL_FLOAT, false, stride, mData)
        GLES20.glEnableVertexAttribArray(attributeLocation)
        mData.position(0)
    }
}
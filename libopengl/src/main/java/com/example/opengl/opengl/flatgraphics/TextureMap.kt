package com.example.opengl.opengl.flatgraphics

import android.opengl.GLES30
import android.opengl.Matrix
import com.example.opengl.R
import com.example.opengl.opengl.buildProgram30
import com.example.opengl.opengl.loadTexture
import com.example.opengl.opengl.toFloatBuff
import com.example.opengl.opengl.toShortBuff

class TextureMap {

    private var uMatrixLocation = 0

    private val mMatrix = FloatArray(16)

    private var mProgram = 0

    private var mTextureId = 0

    private val vertexBuffer = floatArrayOf(
        0f, 0f, 0f,  //顶点坐标V0
        1f, 1f, 0f,  //顶点坐标V1
        -1f, 1f, 0f,  //顶点坐标V2
        -1f, -1f, 0f,  //顶点坐标V3
        1f, -1f, 0f //顶点坐标V4
    ).toFloatBuff()

    val mTexVertexBuffer = floatArrayOf(
        0.5f, 0.5f, //纹理坐标V0
        1f, 0f,     //纹理坐标V1
        0f, 0f,     //纹理坐标V2
        0f, 1.0f,   //纹理坐标V3
        1f, 1.0f    //纹理坐标V4
    ).toFloatBuff()

    val VERTEX_INDEX = shortArrayOf(
        0, 1, 2,  //V0,V1,V2 三个顶点组成一个三角形
        0, 2, 3,  //V0,V2,V3 三个顶点组成一个三角形
        0, 3, 4,  //V0,V3,V4 三个顶点组成一个三角形
        0, 4, 1 //V0,V4,V1 三个顶点组成一个三角形
    )

    val mVertexIndexBuffer = VERTEX_INDEX.toShortBuff()

    fun onCreate() {
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        mProgram = buildProgram30(R.raw.vertex_texture_ndk, R.raw.frag_texture_ndk)
        uMatrixLocation = GLES30.glGetUniformLocation(mProgram, "u_Matrix")
        mTextureId = loadTexture(R.drawable.java)
    }

    fun onSizeChange(width : Int, height : Int) {
        val aspectRatio =
            if (width > height) width.toFloat() / height else height.toFloat() / width
        if (width > height) {
            //横屏
            Matrix.orthoM(mMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            //竖屏
            Matrix.orthoM(mMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
    }

    fun onDraw() {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0)
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(0, 2, GLES30.GL_FLOAT, false, 0, mTexVertexBuffer)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId)
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, VERTEX_INDEX.size, GLES30.GL_UNSIGNED_SHORT, mVertexIndexBuffer)
    }
}
package com.example.opengl.opengl

import android.opengl.GLES20
import android.opengl.GLES20.GL_COMPILE_STATUS
import android.opengl.GLES20.glDeleteShader
import android.opengl.GLES30
import androidx.annotation.RawRes
import com.example.base.App
import com.example.opengl.opengl.flatgraphics.BezierCurve
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.*


fun FloatArray.toFloatBuff() : FloatBuffer {
    return ByteBuffer.allocateDirect(this.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().also {
        it.put(this)
        it.position(0)
    }
}

fun ShortArray.toShortBuff() : ShortBuffer {
    return ByteBuffer.allocateDirect(this.size * 2).order(ByteOrder.nativeOrder()).asShortBuffer().also {
        it.put(this)
        it.position(0)
    }
}

fun IntArray.toIntBuff() : IntBuffer {
    return ByteBuffer.allocateDirect(this.size * 4).order(ByteOrder.nativeOrder()).asIntBuffer().also {
        it.put(this)
        it.position(0)
    }
}

/**
 * 创造顶点着色器类型(GLES20.GL_VERTEX_SHADER)
 * 或者是片段着色器类型 (GLES20.GL_FRAGMENT_SHADER)
 */
fun loadShader(type: Int, shaderCode: String?): Int {
    //根据不同type创建Id， 为0时则创建失败
    val shaderId = GLES30.glCreateShader(type)
    return if (shaderId == 0) 0 else {
        GLES20.glShaderSource(shaderId, shaderCode)
        GLES20.glCompileShader(shaderId)
        // 以下为验证编译结果是否失败
        // 以下为验证编译结果是否失败
        val compileStatsu = IntArray(1)
        GLES20.glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatsu, 0)
        return if (compileStatsu[0] == 0) {
            // 失败则删除
            glDeleteShader(shaderId)
            0
        } else shaderId
    }
}

/**
 * 读取着色器代码
 * @param context 上下文
 * @param resourceId 资源ID
 * @return 代码字符串
 */
fun readTextFileFromResource(resourceId: Int): String {
    val sb = StringBuilder()
    try {
        val `is`: InputStream = App.getApplicationContext().resources.openRawResource(resourceId)
        val inputStreamReader = InputStreamReader(`is`)
        val br = BufferedReader(inputStreamReader)
        var nextLine: String?
        while (br.readLine().also { nextLine = it } != null) {
            sb.append(nextLine)
            sb.append('\n')
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return sb.toString()
}

fun buildProgram(@RawRes vertexId: Int, @RawRes fragId : Int) : Int{
    val vertex = loadShader(GLES30.GL_VERTEX_SHADER, readTextFileFromResource(vertexId))
    val fragment = loadShader(GLES30.GL_FRAGMENT_SHADER, readTextFileFromResource(fragId))
    val programId = GLES20.glCreateProgram()
    GLES20.glAttachShader(programId, vertex)
    GLES20.glAttachShader(programId, fragment)
    GLES20.glLinkProgram(programId)
    GLES20.glUseProgram(programId)
    return programId
}

fun FloatArray.makeInterleavedBuffer(triangles : Int) : FloatBuffer{
    return ByteBuffer.allocateDirect(this.size * BezierCurve.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer().also {
        var offSet = 0
        for (i in 0 until triangles) {
            for (j in 0 until BezierCurve.POINTS_PER_TRIANGLE) {
                it.put(this, offSet, BezierCurve.T_DATA_SIZE)
                offSet += BezierCurve.T_DATA_SIZE
            }
        }
        it.position(0)
    }
}
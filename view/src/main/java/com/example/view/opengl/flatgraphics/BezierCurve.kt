package com.example.view.opengl.flatgraphics

import android.opengl.GLES20
import com.example.view.R
import com.example.view.opengl.buildProgram
import com.example.view.opengl.makeInterleavedBuffer
import java.nio.FloatBuffer

class BezierCurve{

    companion object {
        val BYTES_PER_FLOAT = 4

        val POINTS_PER_TRIANGLE = 3

        val T_DATA_SIZE = 1

        val NUM_POINTS = 40000
    }

    private val mStartEndPoints = floatArrayOf(-1f, 0f, 1f, 0f)
    private val mControlPoints = floatArrayOf(0f, 0.5f, 1f, 0f)
    private val mDataPoints = FloatArray(POINTS_PER_TRIANGLE * T_DATA_SIZE * NUM_POINTS).also {
        val size = it.size
        var i = 0
        while (i < size) {
            it[i] = i.toFloat() / size
            it[i + 1] = (i + 1).toFloat() / size
            it[i + 2] = (i + 2).toFloat() / size
            i += POINTS_PER_TRIANGLE
        }
    }

    private var mProgram = 0
    private var mStartEndHandle = 0
    private var mControlHandle = 0
    private var mDataHandle = 0
    private var mAmpsHandle = 0
    private var mMvpHandle = 0

    private var mAmps = 1.0f

    private val mBuffer : FloatBuffer

    private var mBufferId = 0

    init {
        mBuffer = mDataPoints.makeInterleavedBuffer(NUM_POINTS)

        mProgram = buildProgram(R.raw.vertex_line_bezier, R.raw.fragment_line_bezier)

        mStartEndHandle = GLES20.glGetAttribLocation(mProgram, "u_StartEndData")

        mControlHandle = GLES20.glGetAttribLocation(mProgram, "u_ControlData")

        mDataHandle = GLES20.glGetAttribLocation(mProgram, "a_Data")

        mAmpsHandle = GLES20.glGetUniformLocation(mProgram, "u_Amp")

        mMvpHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix")

        val buffers = IntArray(1)
        GLES20.glGenBuffers(1, buffers, 0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0])
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER, mBuffer.capacity() * BYTES_PER_FLOAT,
            mBuffer, GLES20.GL_STATIC_DRAW
        )
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)

        mBufferId = buffers[0]
    }

    fun draw() {
        GLES20.glClearColor(0.0f, 0f, 0f, 1f)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

        GLES20.glUniform4f(
            mStartEndHandle,
            mStartEndPoints[0],
            mStartEndPoints[1],
            mStartEndPoints[2],
            mStartEndPoints[3]
        )
        GLES20.glUniform4f(
            mControlHandle,
            mControlPoints[0],
            mControlPoints[1],
            mControlPoints[2],
            mControlPoints[3]
        )
        GLES20.glUniform1f(mAmpsHandle, mAmps)

        val stride = BYTES_PER_FLOAT * T_DATA_SIZE

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBufferId)
        GLES20.glEnableVertexAttribArray(mDataHandle)
        GLES20.glVertexAttribPointer(
            mDataHandle,
            T_DATA_SIZE,
            GLES20.GL_FLOAT,
            false,
            stride,
            0
        )

        // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, NUM_POINTS * POINTS_PER_TRIANGLE)

    }

    fun draw(mvp: FloatArray?) {
        GLES20.glClearColor(0.0f, 0f, 0f, 1f)
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUniform4f(
            mStartEndHandle,
            mStartEndPoints[0],
            mStartEndPoints[1],
            mStartEndPoints[2],
            mStartEndPoints[3]
        )
        GLES20.glUniform4f(
            mControlHandle,
            mControlPoints[0],
            mControlPoints[1],
            mControlPoints[2],
            mControlPoints[3]
        )
        GLES20.glUniform1f(mAmpsHandle, mAmps)
        val stride: Int = BYTES_PER_FLOAT * T_DATA_SIZE
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBufferId)
        GLES20.glEnableVertexAttribArray(mDataHandle)
        GLES20.glVertexAttribPointer(
            mDataHandle,
            T_DATA_SIZE,
            GLES20.GL_FLOAT,
            false,
            stride,
            0
        )

        // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        GLES20.glUniformMatrix4fv(mMvpHandle, 1, false, mvp, 0)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, NUM_POINTS * POINTS_PER_TRIANGLE)
    }

    fun setAmp(amp: Float) {
        mAmps = amp
    }
}
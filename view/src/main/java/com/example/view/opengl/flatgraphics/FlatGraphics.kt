package com.example.view.opengl.flatgraphics

import android.opengl.GLES20
import com.example.view.R
import com.example.view.opengl.VertexArray
import com.example.view.opengl.buildProgram

abstract class FlatGraphics{

    private val color = floatArrayOf(255f, 0f, 0f, 1.0f)

    protected val POSITION_COMPONENT_COUNT = 2

    open fun onSurfaceChanged(width : Int, height : Int) {
        aColorLocation = GLES20.glGetUniformLocation(mProgram, A_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION)
        vertexBuffer.setVertexPosition(0, aPositionLocation, POSITION_COMPONENT_COUNT, 0)
    }

    protected val A_POSITION = "a_Position"

    protected val A_COLOR = "a_Color"

    protected var aColorLocation = 0
    protected var aPositionLocation = 0

    fun draw() {
        GLES20.glUniform4fv(aColorLocation, 1, color, 0)
        onDraw()
    }

    abstract fun onDraw()

    abstract fun getFloatData() : FloatArray

    protected var vertexBuffer = VertexArray(getFloatData())

    open fun getVertexId()  = R.raw.vertex_line
    open fun getFragId()  = R.raw.fragment_point

    protected var mProgram = buildProgram(getVertexId(), getFragId())

}
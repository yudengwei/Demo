package com.example.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.opengl.opengl.MySurfaceView1


class MainActivity4 : AppCompatActivity() {

    private lateinit var mGLSurfaceView: MySurfaceView1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLSurfaceView = MySurfaceView1(this)
        setContentView(mGLSurfaceView)
    }

    override fun onPause() {
        super.onPause()
        mGLSurfaceView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mGLSurfaceView.onResume()
    }
}
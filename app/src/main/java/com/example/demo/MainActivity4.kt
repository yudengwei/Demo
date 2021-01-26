package com.example.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.view.opengl.MySurfaceView


class MainActivity4 : AppCompatActivity() {

    private lateinit var mGLSurfaceView: MySurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGLSurfaceView = MySurfaceView(this)
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
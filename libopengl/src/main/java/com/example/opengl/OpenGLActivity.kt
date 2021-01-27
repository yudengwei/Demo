package com.example.opengl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.opengl.opengl.MySurfaceView1

class OpenGLActivity : AppCompatActivity() {

    private lateinit var myRender: MyRender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myRender = MyRender()
        setContentView(MySurfaceView(this, mRender = myRender))
    }

    override fun onDestroy() {
        super.onDestroy()
        myRender.destroy()
    }
}
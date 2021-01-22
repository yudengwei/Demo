package com.example.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.demo.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity(){

    private lateinit var mBind : ActivityMain3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_main3)

        mBind.startOrStop.setOnClickListener {
            if (mBind.threeView.isRunning()) {
                mBind.startOrStop.text = "start"
                mBind.threeView.stop()
            } else {
                mBind.startOrStop.text = "stop"
                mBind.threeView.start()
            }
        }
        mBind.showPartricle.setOnClickListener {
            mBind.particleView.showOrHideParticle()
        }
    }
}
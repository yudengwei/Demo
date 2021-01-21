package com.example.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.demo.databinding.ActivityMain5Binding

class MainActivity5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMain5Binding>(this, R.layout.activity_main5)
        binding.toBzer.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }
        binding.start.setOnClickListener {
            binding.bzer.start()
        }
        binding.setShowHelp.setOnClickListener {
            binding.bzer.setShowHelp()
        }
    }
}
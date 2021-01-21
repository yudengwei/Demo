package com.example.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.demo.databinding.ActivityMain6Binding

class MainActivity6 : AppCompatActivity() {

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMain6Binding>(this, R.layout.activity_main6)
        binding.toBzer.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }
    }
}
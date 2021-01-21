package com.example.demo

import android.app.Application
import android.content.Context

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    companion object {
        private lateinit var app : Context

        fun getApplicationContext() : Context{
            return app
        }
    }
}
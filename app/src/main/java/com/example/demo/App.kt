package com.example.demo

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MatchInfo.init(this)
    }
}
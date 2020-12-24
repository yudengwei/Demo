package com.abiao.libjetpackdemo.app

import android.app.Application
import android.content.Context
import com.abiao.libjetpackdemo.model.Shoe
import com.abiao.libjetpackdemo.repository.ShoeRepository
import com.abiao.libjetpackdemo.room.AppDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        private lateinit var instance : Context

        fun getApplicationContext() = instance
    }
}
package com.abiao.libjetpackdemo.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.abiao.libjetpackdemo.app.App
import com.abiao.libjetpackdemo.model.Shoe
import com.abiao.libjetpackdemo.room.AppDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

class GetDataWork(context: Context, parameters: WorkerParameters) : Worker(context, parameters) {

    override fun doWork(): Result {
        val appContext = App.getApplicationContext()
        val dao = AppDatabase.getInstance().shoeDao()
        var originDatas = dao.getAllShoes()
        if (originDatas.isEmpty()) {
            appContext.assets.open("shoes.json").use { stream ->
                JsonReader(stream.reader()).use {
                    val shoeType = object : TypeToken<List<Shoe>>(){}.type
                    originDatas = Gson().fromJson<List<Shoe>>(it, shoeType)
                    dao.insertShoes(originDatas)
                }
            }
        }

        return Result.success()
    }
}
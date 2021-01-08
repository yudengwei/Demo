package com.abiao.libjetpackdemo.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.abiao.libjetpackdemo.app.App
import com.abiao.libjetpackdemo.app.Constant
import com.abiao.libjetpackdemo.model.Shoe
import com.abiao.libjetpackdemo.room.AppDatabase
import com.example.base.delegate.PreferenceDelegate
import com.example.base.util.LogUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

class GetDataWork(context: Context, parameters: WorkerParameters) : Worker(context, parameters) {

    private var isFirst by PreferenceDelegate(App.getApplicationContext(), Constant.SP_FIRST, true)

    override fun doWork(): Result {
        LogUtil.d("doWork: ${Thread.currentThread().name}")
        val dao = AppDatabase.getInstance().shoeDao()
        val appContext = App.getApplicationContext()
        appContext.assets.open("shoes.json").use { stream ->
            JsonReader(stream.reader()).use {
                val shoeType = object : TypeToken<List<Shoe>>(){}.type
                val originDatas = Gson().fromJson<List<Shoe>>(it, shoeType)
                dao.insertShoes(originDatas)
            }
        }
        isFirst = false
        return Result.success()
    }
}
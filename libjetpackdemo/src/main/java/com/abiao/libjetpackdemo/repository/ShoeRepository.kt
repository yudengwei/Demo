package com.abiao.libjetpackdemo.repository

import com.abiao.libjetpackdemo.app.App
import com.abiao.libjetpackdemo.model.Shoe
import com.abiao.libjetpackdemo.room.AppDatabase
import com.example.base.util.LogUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

class ShoeRepository : BaseRepository<Shoe> {

    private var originDatas : List<Shoe>

    init {
        val appContext = App.getApplicationContext()
        val dao = AppDatabase.getInstance().shoeDao()
        originDatas = dao.getAllShoes()
        LogUtil.d("isEmpty: ${originDatas.size}")
        if (originDatas.isEmpty()) {
            appContext.assets.open("shoes.json").use { stream ->
                JsonReader(stream.reader()).use {
                    val shoeType = object : TypeToken<List<Shoe>>(){}.type
                    originDatas = Gson().fromJson<List<Shoe>>(it, shoeType)
                }
            }
        }
    }

    override fun produce(page : Int) : List<Shoe> {
        return getData(page)
    }

    private fun getData(page : Int) : List<Shoe> {
        val start = 10 * page
        val end = start + 10
        val datas = mutableListOf<Shoe>()
        for (i in start until end) {
            val index = (0..14).random()
            val data = originDatas[index].clone()
            data.name += " 第${i}个"
            datas.add(data)
        }
        return datas
    }
}
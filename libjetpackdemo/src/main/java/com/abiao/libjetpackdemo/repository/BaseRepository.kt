package com.abiao.libjetpackdemo.repository

interface BaseRepository<value> {

    fun produce(page : Int) : List<value>
}
package com.abiao.libjetpackdemo.model

import androidx.paging.PageKeyedDataSource
import com.abiao.libjetpackdemo.repository.ShoeRepository
import com.example.base.util.LogUtil

class ShoePageDataSource(private val shoeRepository: ShoeRepository) : PageKeyedDataSource<Int, Shoe>(){

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Shoe>) {
        callback.onResult(shoeRepository.produce(0), null, 2)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Shoe>) {
        callback.onResult(shoeRepository.produce(params.key - 1), params.key - 1)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Shoe>) {
        callback.onResult(shoeRepository.produce(params.key - 1), params.key + 1)
    }
}
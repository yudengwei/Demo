package com.abiao.libjetpackdemo.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.abiao.libjetpackdemo.model.Shoe
import com.abiao.libjetpackdemo.model.ShoePageDataSource
import com.abiao.libjetpackdemo.repository.ShoeRepository
import com.abiao.libjetpackdemo.ui.adapter.home.ShoeAdapter

class ShoeViewModel(shoeRepository: ShoeRepository) : ViewModel(){

    private val mDataSource = ShoePageDataSource(shoeRepository)

    private val mFactory = object : DataSource.Factory<Int, Shoe>() {
        override fun create(): DataSource<Int, Shoe> {
            return mDataSource
        }
    }

    private val mConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(10)
        .setInitialLoadSizeHint(10)
        .build()

    private var mShoes : LiveData<PagedList<Shoe>>? = null

    fun getShoeLiveData() = mShoes

    fun refresh() : LiveData<PagedList<Shoe>>? {
        mShoes = LivePagedListBuilder<Int, Shoe>(mFactory, mConfig).build()
        return mShoes
    }

    fun onItemClick(view : View, shoe : Shoe) {
        shoe.setName("你点击了我")
    }
}
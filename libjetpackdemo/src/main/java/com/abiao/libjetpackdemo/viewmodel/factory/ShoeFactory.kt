package com.abiao.libjetpackdemo.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abiao.libjetpackdemo.repository.ShoeRepository
import com.abiao.libjetpackdemo.viewmodel.ShoeViewModel

class ShoeFactory(private val shoeRepository: ShoeRepository) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShoeViewModel(shoeRepository) as T
    }
}
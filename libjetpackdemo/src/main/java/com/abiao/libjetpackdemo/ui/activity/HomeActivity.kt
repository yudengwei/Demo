package com.abiao.libjetpackdemo.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.abiao.libjetpackdemo.R
import com.abiao.libjetpackdemo.app.App
import com.abiao.libjetpackdemo.databinding.ActivityHomeBinding
import com.abiao.libjetpackdemo.model.Shoe
import com.abiao.libjetpackdemo.repository.ShoeRepository
import com.abiao.libjetpackdemo.room.AppDatabase
import com.abiao.libjetpackdemo.ui.adapter.home.ShoeAdapter
import com.abiao.libjetpackdemo.viewmodel.ShoeViewModel
import com.abiao.libjetpackdemo.viewmodel.factory.ShoeFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class HomeActivity : AppCompatActivity(){

    private lateinit var mBinding : ActivityHomeBinding

    private lateinit var mViewModel : ShoeViewModel

    private lateinit var mAdapter : ShoeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        val shoeRepository = ShoeRepository()
        mViewModel = ShoeFactory(shoeRepository).create(ShoeViewModel::class.java)

        mAdapter = ShoeAdapter(this, mViewModel)
        mBinding.recyclerView.adapter = mAdapter

        mViewModel.refresh()
        mViewModel.getShoeLiveData()?.observe(this) {
            mAdapter.submitList(it)
        }

        mBinding.refresh.setOnRefreshListener { refresh ->
            mAdapter.submitList(null)
            mViewModel.refresh()?.observe(this) {
                mAdapter.submitList(it)
                refresh.finishRefresh()
            }
        }
    }
}
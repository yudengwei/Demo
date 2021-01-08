package com.example.demo.tworecyclerview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo.R
import com.example.demo.databinding.ActivityRecyclerviewBinding
import com.example.demo.tworecyclerview.adapter.LeftAdapter
import com.example.demo.tworecyclerview.data.LeftBean
import com.example.demo.tworecyclerview.data.SortBean
import com.google.gson.Gson
import java.io.IOException

class RecyclerViewActivity : AppCompatActivity(){

    companion object {
        const val TAG = "RecyclerViewActivity"
    }

    private lateinit var mBinding : ActivityRecyclerviewBinding
    private val mLeftBeans = mutableListOf<LeftBean>()
    private lateinit var mSortbean : SortBean
    private lateinit var mLeftAdapter : LeftAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recyclerview)
        getData()
        initLeftRecyclerView()
    }

    private fun initLeftRecyclerView() {
        mLeftAdapter = LeftAdapter(this, mLeftBeans)
        mBinding.leftRecyclerview.adapter = mLeftAdapter
        mBinding.leftRecyclerview.layoutManager = LinearLayoutManager(this)
        mBinding.leftRecyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun getData() {
        val dataString = getAssetsData("sort.json")
        val gson = Gson()
        mSortbean = gson.fromJson(dataString, SortBean::class.java)
        for (i in 0 until mSortbean.categoryOneArray!!.size) {
            mLeftBeans.add(LeftBean(mSortbean.categoryOneArray!![i].name!!, false)
                { _: View, bean: LeftBean ->
                    for (leftBean in mLeftBeans) {
                        if (leftBean.checked) {
                            leftBean.setChecked(false)
                            break
                        }
                    }
                    bean.setChecked(true)
                })
        }
    }

    //从资源文件中获取分类json
    private fun getAssetsData(path: String): String {
        var result = ""
        return try {
            //获取输入流
            val mAssets = assets.open(path)
            //获取文件的字节数
            val lenght = mAssets.available()
            //创建byte数组
            val buffer = ByteArray(lenght)
            //将文件中的数据写入到字节数组中
            mAssets.read(buffer)
            mAssets.close()
            result = String(buffer)
            result
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, e.message!!)
            result
        }
    }
}
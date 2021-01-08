package com.example.demo

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.base.delegate.PreferenceDelegate
import com.example.base.util.LogUtil
import com.example.demo.databinding.ActivityMain2Binding
import com.example.demo.permission.requestPermissionByCoroutines
import kotlinx.coroutines.*

class MainActivity2 : AppCompatActivity() {

    private lateinit var mBind : ActivityMain2Binding
    private var mIsFirst  by PreferenceDelegate(this, "isFirst",true)

    companion object {
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = DataBindingUtil.setContentView(this, R.layout.activity_main2)

        mBind.b.setOnClickListener {
            request(*permissions)

        }
    }

    private fun request(vararg p : String) {
        val realPermissions = mutableListOf<String>()
        for (permission in p) {
            if (mIsFirst) {
                realPermissions.add(permission)
            } else {
                val show = ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                if (!show) {
                    LogUtil.d("不再展示：$permission")
                } else {
                    realPermissions.add(permission)
                }
            }
        }
        if (mIsFirst) {
            mIsFirst = false
        }
        val pp = realPermissions.toTypedArray()
        CoroutineScope(Dispatchers.Main).launch {
            val result = requestPermissionByCoroutines(*pp)
            LogUtil.d("result: $result")
            if (!result.success) {
                val permissions = result.failPermission!!
                request(*permissions)
            }
        }
    }
}
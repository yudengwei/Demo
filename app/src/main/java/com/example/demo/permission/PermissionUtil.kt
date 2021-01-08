package com.example.demo.permission

import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val FRAGMENT_TAG = "fragment_tag"

suspend fun AppCompatActivity.requestPermissionByCoroutines(vararg permissions : String) : PermissionResult =
    suspendCoroutine {
        val manager = this.supportFragmentManager
        val fragment = RequestPermissionFragment.newInstance(*permissions) { result ->
            it.resume(result)
        }
        manager.beginTransaction().add(fragment, FRAGMENT_TAG).commit()
    }
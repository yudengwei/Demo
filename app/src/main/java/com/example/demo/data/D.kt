package com.example.demo.data

import android.database.Observable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class D(@Bindable var src : Int) : BaseObservable() {

    @JvmName("setSrc1")
    fun setSrc(src : Int) {
        this.src = src
        notifyPropertyChanged(com.example.demo.BR.src)
    }
}
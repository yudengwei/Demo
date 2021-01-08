package com.example.demo.tworecyclerview.data

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class LeftBean(@Bindable var title : String, @Bindable var checked : Boolean,
               val itemClick : (view : View, bean : LeftBean) -> Unit) : BaseObservable(){

    @JvmName("title")
    fun setTitle(title : String) {
        this.title = title
        notifyPropertyChanged(com.example.demo.BR.title)
    }

    @JvmName("checked")
    fun setChecked(isChecked : Boolean) {
        this.checked = isChecked
        notifyPropertyChanged(com.example.demo.BR.checked)
    }

    fun setItemClick(view : View, bean : LeftBean) {
        itemClick(view, bean)
    }
}
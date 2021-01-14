package com.example.demo.tworecyclerview.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class RightBean(@Bindable var name : String? = null, @Bindable var titleName : String? = null, @Bindable var titleed : Boolean = false,
                @Bindable var tag : String? = null, @Bindable var imgSrc : Boolean = false) : BaseObservable(){

    @JvmName("setName1")
    fun setName(name : String) {
        this.name = name
        notifyPropertyChanged(com.example.demo.BR.name)
    }

    @JvmName("setTitleName1")
    fun setTitleName(name : String) {
        this.name = name
        notifyPropertyChanged(com.example.demo.BR.titleName)
    }
}
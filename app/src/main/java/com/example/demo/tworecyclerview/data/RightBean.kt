package com.example.demo.tworecyclerview.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class RightBean(@Bindable var name : String, @Bindable var titleName : String, @Bindable var titleed : Boolean,
                @Bindable var tag : String, @Bindable var imgSrc : Boolean) : BaseObservable(){

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
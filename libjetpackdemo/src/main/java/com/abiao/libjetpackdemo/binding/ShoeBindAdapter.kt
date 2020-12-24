package com.abiao.libjetpackdemo.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.abiao.libjetpackdemo.R
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun shoeImage(imageView : ImageView, imageUrl : String) {
    Glide.with(imageView).asBitmap().load(imageUrl).placeholder(R.drawable.glide_placeholder).centerCrop().into(imageView)
}
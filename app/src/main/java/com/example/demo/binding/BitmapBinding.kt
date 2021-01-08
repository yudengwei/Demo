package com.example.demo.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("show")
fun show(view : ImageView, src : Int) {
    view.setImageResource(src)
}
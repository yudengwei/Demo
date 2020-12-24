package com.abiao.libjetpackdemo.ui.adapter.home

import androidx.recyclerview.widget.DiffUtil
import com.abiao.libjetpackdemo.model.Shoe

class ShoeDiff : DiffUtil.ItemCallback<Shoe>() {

    override fun areItemsTheSame(oldItem: Shoe, newItem: Shoe): Boolean {

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Shoe, newItem: Shoe): Boolean {
        return oldItem == newItem
    }
}
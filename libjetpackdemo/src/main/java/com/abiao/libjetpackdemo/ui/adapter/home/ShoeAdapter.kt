package com.abiao.libjetpackdemo.ui.adapter.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abiao.libjetpackdemo.databinding.ItemShoeBinding
import com.abiao.libjetpackdemo.model.Shoe
import com.abiao.libjetpackdemo.viewmodel.ShoeViewModel
import com.example.base.util.LogUtil

class ShoeAdapter(private val context : Context, private val viewModel : ShoeViewModel) :  PagedListAdapter<Shoe,ShoeAdapter.Holder>(ShoeDiff()){

    class Holder(private val binding : ItemShoeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(shoe : Shoe?, viewModel : ShoeViewModel) {
            binding.shoe = shoe
            binding.viewModel = viewModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemShoeBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val shoe = getItem(position)
        holder.bind(shoe, viewModel)
    }
}
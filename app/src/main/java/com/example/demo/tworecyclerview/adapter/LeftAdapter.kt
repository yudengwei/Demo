package com.example.demo.tworecyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.databinding.ItemSortLeftBinding
import com.example.demo.tworecyclerview.data.LeftBean

class LeftAdapter(val mContext : Context, val mDatas : List<LeftBean>) : RecyclerView.Adapter<LeftAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemSortLeftBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(leftBean : LeftBean) {
            binding.leftBean = leftBean
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext),
            R.layout.item_sort_left, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mDatas[position])
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }
}
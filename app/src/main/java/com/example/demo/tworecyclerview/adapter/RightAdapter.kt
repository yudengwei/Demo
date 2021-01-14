package com.example.demo.tworecyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.databinding.ItemRightTitleBinding
import com.example.demo.databinding.ItemSortDetailBinding
import com.example.demo.tworecyclerview.data.RightBean

class RightAdapter(private val mContext : Context, private val mDatas : List<RightBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    abstract class Holder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(rightBean: RightBean)
    }

    class DetailHolder(val binding : ItemSortDetailBinding) : Holder(binding) {
        override fun bind(rightBean: RightBean) {
            binding.rightBean = rightBean
        }
    }

    class TitleHolder(val binding : ItemRightTitleBinding) : Holder(binding) {
        override fun bind(rightBean: RightBean) {
            binding.rightBean = rightBean
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val binding = DataBindingUtil.inflate<ItemRightTitleBinding>(LayoutInflater.from(mContext), R.layout.item_right_title, parent, false)
            TitleHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemSortDetailBinding>(LayoutInflater.from(mContext), R.layout.item_sort_detail, parent, false)
            DetailHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as Holder
        viewHolder.bind(mDatas[position])
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun getItemViewType(position: Int): Int {
        val bean = mDatas.get(position)
        return if(bean.titleed) {
            1
        } else {
            2
        }
    }
}
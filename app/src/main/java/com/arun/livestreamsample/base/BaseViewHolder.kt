package com.arun.livestreamsample.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(private val binding: ViewDataBinding):RecyclerView.ViewHolder(binding.root) {
    fun unBind(){
        binding.unbind()
    }
}
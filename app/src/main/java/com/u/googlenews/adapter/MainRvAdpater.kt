package com.u.googlenews.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.u.googlenews.NewsListeners
import com.u.googlenews.R
import com.u.googlenews.NewsVO
import com.u.googlenews.databinding.ItemMainBinding

class MainRvAdpater(private val list:ArrayList<NewsVO>): RecyclerView.Adapter<MainRvAdpater.ViewHolder>()  {

    override fun getItemCount(): Int = list.size

    var itemClick: NewsListeners? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.apply {
            bind(item)
            itemView.tag = item
            itemView.setOnClickListener {  v ->  itemClick?.onClick(v, position) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_main,
                parent,
                false
            )
        )
    }

    class ViewHolder(private val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NewsVO) {
            binding.apply {
                rvItem = item
            }
        }
    }
}
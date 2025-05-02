package com.example.kotlin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin.Model.ItemsModel
import com.example.kotlin.databinding.ViewholderCategoryBinding


class ListItemsAdapter(val items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<ListItemsAdapter.Viewholder>(){
    private lateinit var context: Context

    inner class Viewholder(val binding: ViewholderCategoryBinding):
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemsAdapter.Viewholder {
        context = parent.context
        val binding=ViewholderCategoryBinding.inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }
        override fun onBindViewHolder(holder: ListItemsAdapter.Viewholder, position: Int) {
            val item=items[position]
            holder.binding.titleCat.text=item.title

            Glide.with(holder.itemView.context)
                .load(item.picUrl)
                .into(holder.binding.picCat)
        }

    override fun getItemCount(): Int = items.size
}
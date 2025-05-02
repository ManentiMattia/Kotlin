package com.example.kotlin.Adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin.R
import com.example.kotlin.Activity.ListItemsActivity
import com.example.kotlin.Model.CategoryModel
import com.example.kotlin.databinding.ViewholderCategoryBinding


class CategoryAdapter(val items:MutableList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.Viewholder>(){
    private var selectedPosition = -1
    private var LastselectedPosition = -1

    inner class Viewholder(val binding: ViewholderCategoryBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.Viewholder {
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }


    override fun onBindViewHolder(holder: CategoryAdapter.Viewholder, position: Int) {
        val item = items[position]
        holder.binding.titleCat.text = item.title

        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .into(holder.binding.picCat)

        if (selectedPosition == position) {
            holder.binding.picCat.setBackgroundResource(0)
            holder.binding.mainLayout.setBackgroundResource(R.drawable.purple_bg3)
            ImageViewCompat.setImageTintList(
                holder.binding.picCat,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.white
                    )
                )
            )
            holder.binding.titleCat.visibility = View.VISIBLE
            holder.binding.titleCat.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        } else {
            holder.binding.picCat.setBackgroundResource(R.drawable.grey_bg)
            holder.binding.mainLayout.setBackgroundResource(0)
            ImageViewCompat.setImageTintList(
                holder.binding.picCat,
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.black
                    )
                )
            )

            holder.binding.root.setOnClickListener{
            val position = position
            if (position != RecyclerView.NO_POSITION) {
                LastselectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(LastselectedPosition)
                notifyItemChanged(selectedPosition)
            }
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(holder.itemView.context, ListItemsActivity::class.java).apply {
                    putExtra("id", item.id.toString())
                    putExtra("title", item.title)
                }
                ContextCompat.startActivity(holder.itemView.context, intent, null)
            }, 1000)
        }

            holder.binding.titleCat.visibility = View.GONE
            holder.binding.titleCat.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                )
            )
        }
    }
    override fun getItemCount(): Int = items.size
}
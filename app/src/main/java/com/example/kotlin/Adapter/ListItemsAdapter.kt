package com.example.kotlin.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.kotlin.Model.ItemsModel
import com.example.kotlin.databinding.ViewholderBestSellerBinding
import com.example.kotlin.Activity.DetailActivity

class ListItemsAdapter(private val items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<ListItemsAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(val binding: ViewholderBestSellerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderBestSellerBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.priceTxt.text = "$${item.price}"
        holder.binding.ratingTxt.text = item.rating.toString()

        val requestOption = RequestOptions().transform(CenterCrop())

        Glide.with(context)
            .load(item.picUrl.firstOrNull()) // fallback se l'immagine è vuota
            .apply(requestOption)
            .into(holder.binding.picBestSeller)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("object", item)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}

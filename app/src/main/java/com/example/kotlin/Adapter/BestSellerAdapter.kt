package com.example.kotlin.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.kotlin.Activity.DetailActivity
import com.example.kotlin.Model.ItemsModel
import com.example.kotlin.databinding.ViewholderBestSellerBinding
import com.example.kotlin.R

class BestSellerAdapter(private var items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<BestSellerAdapter.Viewholder>() {

    private var context: Context? = null

    class Viewholder(val binding: ViewholderBestSellerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BestSellerAdapter.Viewholder {
        context = parent.context
        val binding =
            ViewholderBestSellerBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: BestSellerAdapter.Viewholder, position: Int) {
        val item = items[position]

        // Imposta il titolo, il prezzo e il rating
        holder.binding.titleTxt.text = item.title
        holder.binding.priceTxt.text = "$" + item.price.toString()
        holder.binding.ratingTxt.text = item.rating.toString()

        // Carica l'immagine del prodotto
        val requestOption = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(item.picUrl.firstOrNull())
            .apply(requestOption)
            .into(holder.binding.picBestSeller)

        // Imposta l'icona iniziale del cuore
        holder.binding.addToWish.setImageResource(
            if (item.wish) R.drawable.filled_icon else R.drawable.btn_3
        )

        // Click listener sull'icona del cuore
        holder.binding.addToWish.setOnClickListener {
            item.wish = !item.wish // Cambia lo stato

            // Cambia icona visiva
            holder.binding.addToWish.setImageResource(
                if (item.wish) R.drawable.filled_icon else R.drawable.btn_3
            )

            // ✅ Aggiorna Firebase se ha un ID valido
            item.wishId?.let { id ->
                val dbRef = com.google.firebase.database.FirebaseDatabase.getInstance().getReference("Items")
                dbRef.child(id.toString()).child("wish").setValue(item.wish)
            }

            // ✅ (Facoltativo) Notifica all'utente
            // Toast.makeText(holder.itemView.context, if (item.wish) "Aggiunto ai preferiti" else "Rimosso dai preferiti", Toast.LENGTH_SHORT).show()
        }

        // Navigazione al dettaglio
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("object", item)
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = items.size

    // ✅ Metodo per aggiornare dinamicamente la lista
    fun updateList(newItems: List<ItemsModel>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}

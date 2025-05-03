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

        // Modifica l'icona del cuore in base al valore di 'wish'
        if (item.wish) {
            holder.binding.addToWish.setImageResource(R.drawable.filled_icon) // Icona cuore pieno
        } else {
            holder.binding.addToWish.setImageResource(R.drawable.btn_3) // Icona cuore vuoto
        }

        // Aggiungi un click listener per l'icona del cuore
        holder.binding.addToWish.setOnClickListener {
            // Cambia lo stato del wish
            item.wish = !item.wish

            // Aggiorna l'icona
            if (item.wish) {
                holder.binding.addToWish.setImageResource(R.drawable.filled_icon)
            } else {
                holder.binding.addToWish.setImageResource(R.drawable.btn_3)
            }

            // Aggiungi la logica per aggiornare Firebase o altro storage (se necessario)
            // Es: FirebaseDatabase.getInstance().getReference("Items").child(item.id).child("wish").setValue(item.wish)

            // Potresti voler informare l'utente che l'elemento è stato aggiunto ai preferiti
            // Es: Toast.makeText(context, "Aggiunto ai preferiti", Toast.LENGTH_SHORT).show()
        }

        // Navigazione alla pagina di dettaglio
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("object", item)
            holder.itemView.context.startActivity(intent)
        }

        /*val item=items[position]

        with(holder.binding){
            titleTxt.text=item.title
            priceTxt.text="$${item.price}"
            ratingTxt.text=item.rating.toString()

            Glide.with(holder.itemView.context)
                .load(item.picUrl[0])
                .into(holder.binding.picBestSeller)

            root.setOnClickListener {

            }
        }*/
    }

    override fun getItemCount(): Int = items.size

    // ✅ Metodo per aggiornare dinamicamente la lista
    fun updateList(newItems: List<ItemsModel>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}

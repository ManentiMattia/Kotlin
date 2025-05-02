package com.example.kotlin.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlin.Adapter.BestSellerAdapter
import com.example.kotlin.Model.ItemsModel
import com.example.kotlin.R
import com.example.kotlin.databinding.ActivityWishBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWishBinding
    private lateinit var adapter: BestSellerAdapter
    private val wishlistItems = mutableListOf<ItemsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewList.layoutManager = GridLayoutManager(this, 2)
        adapter = BestSellerAdapter(wishlistItems)
        binding.viewList.adapter = adapter

        loadWishlist()
    }

    private fun loadWishlist() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("Items")
        databaseRef.orderByChild("wish").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    wishlistItems.clear()  // Pulisce la lista prima di riempirla
                    for (child in snapshot.children) {
                        val item = child.getValue(ItemsModel::class.java)
                        if (item != null) {
                            wishlistItems.add(item) // Aggiungi l'item alla lista
                        }
                    }

                    // Log per verificare se gli items sono stati caricati correttamente
                    Log.d("WishActivity", "Loaded items: ${wishlistItems.size}")

                    // Se ci sono degli items, notifichi all'adattatore che i dati sono cambiati
                    if (wishlistItems.isEmpty()) {
                        Log.d("WishActivity", "No items found with wish = true")
                    }

                    // Aggiorna l'adattatore per riflettere la lista aggiornata
                    adapter.notifyDataSetChanged()

                    // Nascondi la ProgressBar dopo aver caricato i dati
                    binding.progressBarList.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    // Log di errore
                    Log.e("WishActivity", "Failed to load wishlist items: ${error.message}")
                    Toast.makeText(this@WishActivity, "Errore: ${error.message}", Toast.LENGTH_SHORT).show()

                    // Nascondi la ProgressBar in caso di errore
                    binding.progressBarList.visibility = View.GONE
                }
            })
    }

}


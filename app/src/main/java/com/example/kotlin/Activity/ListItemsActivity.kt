package com.example.kotlin.Activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlin.Adapter.ListItemsAdapter
import com.example.kotlin.ViewModel.MainViewModel
import com.example.kotlin.databinding.ActivityListItemsBinding

class ListItemsActivity : BaseActivity() {
    private lateinit var binding: ActivityListItemsBinding
    private val viewModel = MainViewModel()
    private var id: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SetVariable()
        getBundle()  // Estrai l'ID della categoria e il titolo
        initList()   // Inizializza la lista
        viewModel.loadFiltered(id)  // Carica i prodotti per l'ID della categoria selezionata
    }

    private fun initList() {
        binding.apply {
            progressBarList.visibility = View.VISIBLE  // Mostra il progress bar mentre carichi
            viewModel.bestSeller.observe(this@ListItemsActivity, Observer { items ->
                // Quando i dati sono disponibili, imposta l'adapter del RecyclerView
                viewList.layoutManager = GridLayoutManager(this@ListItemsActivity, 2)
                viewList.adapter = ListItemsAdapter(items)
                progressBarList.visibility = View.GONE  // Nascondi il progress bar dopo il caricamento
            })
        }
    }

    private fun getBundle() {
        id = intent.getStringExtra("id") ?: ""  // Ottieni l'ID della categoria dal bundle
        title = intent.getStringExtra("title") ?: ""

        binding.categoryTxt.text = title  // Mostra il titolo della categoria
    }

    private fun SetVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }
}

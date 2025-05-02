package com.example.kotlin.Activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlin.Adapter.ListItemsAdapter
import com.example.kotlin.ViewModel.MainViewModel
import com.example.kotlin.databinding.ActivityListItemsBinding
import android.text.Editable
import android.text.TextWatcher
import com.example.kotlin.Model.ItemsModel

class ListItemsActivity : BaseActivity() {
    private lateinit var binding: ActivityListItemsBinding
    private val viewModel = MainViewModel()
    private var id: String = ""
    private var title: String = ""
    private var fullItemList: List<ItemsModel> = listOf()

    private lateinit var adapter: ListItemsAdapter  // 🔧 adapter salvato per filtro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SetVariable()
        getBundle()
        initList()
        viewModel.loadFiltered(id)
    }

    private fun initList() {
        binding.apply {
            progressBarList.visibility = View.VISIBLE

            adapter = ListItemsAdapter(mutableListOf())  // inizializzato vuoto
            viewList.layoutManager = GridLayoutManager(this@ListItemsActivity, 2)
            viewList.adapter = adapter

            viewModel.bestSeller.observe(this@ListItemsActivity, Observer { items ->
                fullItemList = items
                adapter.updateList(items)  // 🔄 aggiorna lista
                progressBarList.visibility = View.GONE
            })
        }
    }

    private fun getBundle() {
        id = intent.getStringExtra("id") ?: ""
        title = intent.getStringExtra("title") ?: ""
        binding.categoryTxt.text = title
    }

    private fun filterList(query: String) {
        val filtered = fullItemList.filter {
            it.title.contains(query, ignoreCase = true)
        }
        adapter.updateList(filtered)  // ✅ usa updateList() dell'adapter
    }

    private fun SetVariable() {
        binding.backBtn.setOnClickListener { finish() }

        binding.searchTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}

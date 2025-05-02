package com.example.kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.Activity.BaseActivity
import com.example.kotlin.Activity.CartActivity
import com.example.kotlin.Activity.WishActivity
import com.example.kotlin.Adapter.BestSellerAdapter
import com.example.kotlin.Adapter.CategoryAdapter
import com.example.kotlin.Adapter.SliderAdapter
import com.example.kotlin.Model.ItemsModel
import com.example.kotlin.Model.SliderModel
import com.example.kotlin.ViewModel.MainViewModel
import com.example.kotlin.databinding.ActivityMainBinding
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var allBestSellers: List<ItemsModel> = listOf()
    private lateinit var bestSellerAdapter: BestSellerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanners()
        initCategory()
        initBestSeller()
        initSearch()
        bottomNavigation()
        HomeNavigation()
        WishNavigation()
    }

    private fun bottomNavigation() {
        binding.cartBtn.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun HomeNavigation() {
        binding.esploraBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    private fun WishNavigation() {
        binding.WishBtn.setOnClickListener {
            startActivity(Intent(this, WishActivity::class.java))
        }
    }

    private fun initBestSeller() {

        binding.progressBarBestSeller.visibility = View.VISIBLE
        viewModel.bestSeller.observe(this, Observer { items ->
            allBestSellers = items
            bestSellerAdapter = BestSellerAdapter(items.toMutableList())
            binding.viewBestSeller.layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.viewBestSeller.adapter = bestSellerAdapter
            binding.progressBarBestSeller.visibility = View.GONE
        })
        viewModel.loadBestSeller()

    }

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.category.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                Log.d("MainActivity", "Nessuna categoria trovata")
            } else {
                Log.d("MainActivity", "Categorie caricate: ${it.size}")
            }

            binding.viewCategory.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            val categoryAdapter = CategoryAdapter(it)
            binding.viewCategory.adapter = categoryAdapter

            binding.progressBarCategory.visibility = View.GONE
        })
        viewModel.loadCategory()
    }

    private fun initBanners() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this) { images ->
            banners(images)
            binding.progressBarBanner.visibility = View.GONE
        }
        viewModel.loadBanner()
    }

    private fun banners(images: List<SliderModel>) {
        binding.viewPagerSlider.adapter = SliderAdapter(images, binding.viewPagerSlider)
        binding.viewPagerSlider.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewPagerSlider)
        }
    }

    private fun initSearch() {
        // Crea un ArrayAdapter per suggerire i titoli degli articoli
        val titles = allBestSellers.map { it.title }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, titles)

        // Imposta l'adapter alla AutoCompleteTextView
        binding.editTextText.setAdapter(adapter)

        // Aggiungi un TextWatcher per filtrare gli articoli mentre l'utente digita
        binding.editTextText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filtra i best sellers in base al testo inserito nella barra di ricerca
                filterBestSellers(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun filterBestSellers(query: String) {
        val filtered = allBestSellers.filter {
            it.title.contains(query, ignoreCase = true)
        }
        bestSellerAdapter.updateList(filtered)
    }
}

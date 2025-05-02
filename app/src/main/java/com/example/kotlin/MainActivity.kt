package com.example.kotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.Activity.BaseActivity
import com.example.kotlin.Activity.CartActivity
import com.example.kotlin.Adapter.BestSellerAdapter
import com.example.kotlin.Adapter.CategoryAdapter
import com.example.kotlin.Adapter.SliderAdapter
import com.example.kotlin.Model.SliderModel
import com.example.kotlin.ViewModel.MainViewModel
import com.example.kotlin.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()  // Usa viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) //Forza white mode

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Usa il binding per settare il contenuto

        initBanners()
        initCategory()
        initBestSeller()
        bottomNavigation()
    }

    private fun bottomNavigation() {
        binding.cartBtn.setOnClickListener { startActivity(Intent(this, CartActivity::class.java)) }
    }

    private fun initBestSeller() {
        binding.progressBarBestSeller.visibility = View.VISIBLE
        viewModel.bestSeller.observe(this, Observer {
            binding.viewBestSeller.layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.viewBestSeller.adapter = BestSellerAdapter(it)
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
        // Set adapter
        binding.viewPagerSlider.adapter = SliderAdapter(images, binding.viewPagerSlider)

        // Set view pager properties
        binding.viewPagerSlider.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            getChildAt(0)?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        // Show dots if more than 1 image
        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewPagerSlider)
        }
    }
}

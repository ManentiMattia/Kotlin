package com.example.kotlin

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.kotlin.Adapter.SliderAdapter
import com.example.kotlin.Model.SliderModel
import com.example.kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        initBanners()

    }

    private fun initBanners(){
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer{
            banners(it)
            binding.progressBarBanner.visibility=View.GONE
        })
        viewModel.loadBanners()
    }

    private fun banners (image: List<SliderModel>){
        binding.viewPagerSlider.adapter=SliderAdapter(images, binding.viewPagerSlider)
        binding.viewPagerSlider.clipToPadding=false
        binding.viewPagerSlider.clipChildren=false
        binding.viewPagerSlider.offscreenPageLimit=3
        binding.viewPagerSlider.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

        val compositoPageTransformer = CompositoPageTransformer().apply{
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewPagerSlider.setPageTransformer(compositoPageTransformer)
        if(images.size>1){
            binding.dotIndicator.visibility.View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewPagerSlider)
        }

    }

}
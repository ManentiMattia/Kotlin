package com.example.kotlin

    import android.os.Bundle
    import android.view.View
    import androidx.activity.enableEdgeToEdge
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import androidx.lifecycle.Observer
    import androidx.recyclerview.widget.GridLayoutManager
    import androidx.recyclerview.widget.LinearLayoutManager

    import com.example.kotlin.ViewModel.MainViewModel
    
    import androidx.recyclerview.widget.RecyclerView
    import androidx.viewpager2.widget.CompositePageTransformer
    import androidx.viewpager2.widget.MarginPageTransformer
    import com.example.kotlin.Adapter.BestSellerAdapter
    import com.example.kotlin.Adapter.CategoryAdapter
    import com.example.kotlin.Adapter.SliderAdapter
    import com.example.kotlin.Model.SliderModel
    import com.example.kotlin.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()  // Usa viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Usa il binding per settare il contenuto

        initBanners()
        initCategories()
        initBestSeller()
    }

    private fun initBestSeller() {
        binding.progressBarBestSeller.visibility=View. VISIBLE
        viewModel.bestSeller. observe(this, Observer {
            binding.viewBestSeller.layoutManager = GridLayoutManager(this, 2)
            binding.viewBestSeller.adapter = BestSellerAdapter(it)
            binding.progressBarBestSeller.visibility = View.GONE
        })
            viewModel.loadBestSeller()
    }

    private fun initCategories() {
        binding.progressBarCategory. visibility = View. VISIBLE
        viewModel.category.observe(this, Observer {
            binding.viewCategory.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewCategory.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility=View.GONE
        })
            viewModel. loadCategory()
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

        // Add transformer for pager effect ------------------- Rompe lo slider :(
        /*val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewPagerSlider.setPageTransformer(compositePageTransformer)*/

        // Show dots if more than 1 image
        if (images.size > 1) {
            binding.dotIndicator.visibility = View.VISIBLE
            binding.dotIndicator.attachTo(binding.viewPagerSlider)
        }
    }
}

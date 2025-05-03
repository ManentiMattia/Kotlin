    package com.example.kotlin.Activity

    import android.content.Intent
    import android.net.Uri
    import android.os.Bundle
    import android.widget.Toast
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import com.google.firebase.database.FirebaseDatabase
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.bumptech.glide.Glide
    import com.bumptech.glide.load.resource.bitmap.CenterCrop
    import com.bumptech.glide.request.RequestOptions
    import com.example.kotlin.Adapter.PicListAdapter
    import com.example.kotlin.Adapter.SizeListAdapter
    import com.example.kotlin.Model.ItemsModel
    import com.example.kotlin.databinding.ActivityDetailBinding
    import com.example.kotlin.Helper.ManagmentCart
    import com.example.kotlin.R

    class DetailActivity : BaseActivity() {
        private lateinit var binding:ActivityDetailBinding
        private lateinit var item: ItemsModel
        private var numberOrder = 1
        private lateinit var managmentCart: ManagmentCart

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding= ActivityDetailBinding.inflate(layoutInflater)
            setContentView(binding.root)

            managmentCart=ManagmentCart(this)

            getBundle()
            initList()

        }

        private fun initList() {
            val sizeList= ArrayList<String>()
            for (size in item.size){
                sizeList.add(size.toString())
            }

            binding.sizeList.adapter = SizeListAdapter(sizeList)
            binding.sizeList.layoutManager= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            val colorList= ArrayList<String>()
            for(imageUrl in item.picUrl){
                colorList.add(imageUrl)
            }

            Glide.with(this)
                .load(colorList[0])
                .into(binding.picMain)

            binding.picList.adapter=PicListAdapter(colorList,binding.picMain)
            binding.picList.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

        private fun getBundle(){
            item = intent.getParcelableExtra("object")!!

            binding.titleTxt.text = item.title
            binding.descriptionTxt.text = item.description
            binding.priceTxt.text = "$" + item.price
            binding.ratingTxt.text = "${item.rating} Rating"
            binding.SellerNameTxt.text = item.sellerName

            // Imposta l'icona del cuore in base al valore di wish
            if (item.wish) {
                binding.Favicon.setImageResource(R.drawable.fav1_icon) // Cuore pieno se wish è true
            } else {
                binding.Favicon.setImageResource(R.drawable.fav_icon) // Cuore vuoto se wish è false
            }

            binding.AddToCartBtn.setOnClickListener {
                item.numberInCart = numberOrder
                managmentCart.insertItems(item)
            }

            binding.backBtn.setOnClickListener { finish() }
            binding.CartBtn.setOnClickListener {
                startActivity(Intent(this@DetailActivity, CartActivity::class.java))
            }

            Glide.with(this)
                .load(item.sellerPic)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(binding.picSeller)

            binding.msgToSellerBtn.setOnClickListener {
                val sendIntent = Intent(Intent.ACTION_VIEW)
                sendIntent.setData(Uri.parse("sms:" + item.sellerTell))
                sendIntent.putExtra("sms_body", "type your message")
                startActivity(sendIntent)
            }

            binding.calToSellerBtn.setOnClickListener {
                val phone = item.sellerTell.toString()
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                startActivity(intent)
            }

            binding.Favicon.setOnClickListener {
                val dbRef = FirebaseDatabase.getInstance().getReference("Items")

                // Controllo: l'oggetto deve avere un id valido
                if (item.wishId != null) {
                    // Cambia il valore del wish e aggiorna l'icona
                    item.wish = !item.wish

                    dbRef.child(item.wishId.toString()).child("wish").setValue(item.wish)
                        .addOnSuccessListener {
                            // Aggiorna l'icona in base allo stato di wish
                            if (item.wish) {
                                binding.Favicon.setImageResource(R.drawable.fav1_icon) // Cuore pieno
                            } else {
                                binding.Favicon.setImageResource(R.drawable.fav_icon) // Cuore vuoto
                            }
                            Toast.makeText(this, "Aggiunto ai preferiti", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Errore: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "ID dell'elemento non valido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
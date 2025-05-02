package com.example.kotlin.ViewModel

import android.app.DownloadManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.kotlin.Model.CategoryModel
import com.example.kotlin.Model.ItemsModel
import com.example.kotlin.Model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainViewModel:ViewModel() {
    private val fireBaseDatabase = FirebaseDatabase.getInstance()


    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _category = MutableLiveData<MutableList<CategoryModel>>()
    private val _bestSeller = MutableLiveData<MutableList<ItemsModel>>()
    private val _recommended = MutableLiveData<MutableList<ItemsModel>>()


    val banners: LiveData<List<SliderModel>> = _banner
    val category: LiveData<MutableList<CategoryModel>> = _category
    val bestSeller: LiveData<MutableList<ItemsModel>> = _bestSeller
    //val recommended: LiveData<MutableList<ItemsModel>> = _recommended

    fun loadFiltered(id: String) {
        Log.d("MainViewModel", "Categoria ID passato: $id")
        val Ref = fireBaseDatabase.getReference("Items")
        val query = Ref.orderByChild("categoryId").equalTo(id.toDouble())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(ItemsModel::class.java)
                    if (item != null) {
                        lists.add(item)
                    }
                }
                Log.d("MainViewModel", "Articoli caricati: ${lists.size}")  // Log per verificare i dati
                _bestSeller.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainViewModel", "Errore nel caricare i dati", error.toException())
            }
        })
    }



    fun loadBanner() {
        val Ref = fireBaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                    _banner.value = lists
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


    fun loadCategory() {
        val Ref = fireBaseDatabase.getReference("Category")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<CategoryModel>()


                for (childSnapshot in snapshot.children) {
                    var list = childSnapshot.getValue(CategoryModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _category.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun loadBestSeller() {
        val Ref = fireBaseDatabase.getReference("Items")
        val query: Query=Ref.orderByChild("showRecommended").equalTo(true)
        query.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()

                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                Log.d("BestSeller", "Articoli ricevuti: ${lists.size}")
                _bestSeller.value = lists
            }

            override fun onCancelled(error: DatabaseError) {


            }
        })
    }
}
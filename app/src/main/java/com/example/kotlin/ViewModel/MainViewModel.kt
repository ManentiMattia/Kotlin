package com.example.kotlin.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.kotlin.Model.CategoryModel
import com.example.kotlin.Model.ItemsModel
import com.example.kotlin.Model.SliderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel:ViewModel() {
    private val fireBaseDatabase=FirebaseDatabase.getInstance()


    private val _banner = MutableLiveData<List<SliderModel>>()
    private val _category =MutableLiveData<MutableList<CategoryModel>>()
    private val _bestSeller =MutableLiveData<MutableList<ItemsModel>>()


    val banners:LiveData<List<SliderModel>> = _banner
    val category:LiveData<MutableList<CategoryModel>> = _category
    val bestSeller:LiveData<MutableList<ItemsModel>> = _bestSeller



    fun loadBanner(){
        val Ref=fireBaseDatabase.getReference("Banner")
        Ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
                val lists = mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(SliderModel::class.java)
                    if(list != null){
                        lists.add(list)
                    }
                    _banner.value = lists
                }
            }

            override fun onCancelled(error: DatabaseError){
                TODO("Non ancora implementato")
            }
        })
    }


    fun loadCategory(){
        val Ref=fireBaseDatabase.getReference("Category")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists=mutableListOf<CategoryModel>()


                for(childSnapshot in snapshot.children){
                    var list=childSnapshot.getValue(CategoryModel::class.java)
                    if(list!=null){
                        lists.add(list)
                    }
                }
                _category.value =lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun loadBestSeller(){
        val Ref=fireBaseDatabase.getReference("Items")
        Ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lists=mutableListOf<ItemsModel>()

                    for(childSnapshot in snapshot.children) {
                        val list=childSnapshot.getValue(ItemsModel::class.java)
                        if(list!=null) {
                            lists.add(list)
                        }
                    }
                    _bestSeller.value=lists
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
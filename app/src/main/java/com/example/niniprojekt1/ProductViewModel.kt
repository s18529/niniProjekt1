package com.example.niniprojekt1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase

class ProductViewModel(app: Application) : AndroidViewModel(app) {


    private val repo: ProductRepository
    var firebaseDatabase: FirebaseDatabase
    val allProducts: MutableLiveData<HashMap<String,Product>>

    init {

        firebaseDatabase = FirebaseDatabase.getInstance()

        //val productDao = ProductDB.getDatabase(app.applicationContext)!!.getProductDao()
        repo = ProductRepository(firebaseDatabase)
        allProducts = repo.allProducts
    }

    suspend fun insert(product: Product) = repo.inster(product)

    suspend fun update(product: Product) = repo.update(product)

    suspend fun delete(product: Product) = repo.delete(product)

    suspend fun deleteAll() = repo.deleteAll()

    //suspend fun maxId() : Long = repo.maxId()





}
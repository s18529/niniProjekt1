package com.example.niniprojekt1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ProductViewModel(app: Application) : AndroidViewModel(app) {


    private val repo: ProductRepository
    val allProducts: LiveData<List<Product>>

    init {
        val productDao = ProductDB.getDatabase(app.applicationContext)!!.getProductDao()
        repo = ProductRepository(productDao)
        allProducts = repo.allProduct
    }

    suspend fun insert(product: Product) = repo.inster(product)

    suspend fun update(product: Product) = repo.update(product)

    suspend fun delete(product: Product) = repo.delete(product)

    suspend fun deleteAll() = repo.deleteAll()

    suspend fun maxId() : Long = repo.maxId()





}
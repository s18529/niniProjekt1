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
    val allUserProduct: MutableLiveData<HashMap<String,Product>>

    init {

        firebaseDatabase = FirebaseDatabase.getInstance()

        //val productDao = ProductDB.getDatabase(app.applicationContext)!!.getProductDao()
        repo = ProductRepository(firebaseDatabase)
        allProducts = repo.allProducts

        allUserProduct = repo.allUserProducts
    }

 fun insert(product: Product) = repo.inster(product)


    fun insterUserProduct(users: String?,product: Product) = repo.insterUserProduct(users,product)

     fun update(product: Product) = repo.update(product)

     fun updateUserProduct(users: String?,product: Product) = repo.updateUserProduct(users,product)

     fun delete(product: Product) = repo.delete(product)

     fun deleteUserProduct(users: String?,product: Product) = repo.deleteUserProduct(users,product)

     fun deleteAll() = repo.deleteAll()

     fun deleteAllUserProducts(users: String?) = repo.deleteAllUserProducts(users)

    //suspend fun maxId() : Long = repo.maxId()





}
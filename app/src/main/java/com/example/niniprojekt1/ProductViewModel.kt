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
    val allShops: MutableLiveData<HashMap<String,Shop>>

    init {

        firebaseDatabase = FirebaseDatabase.getInstance()

        //val productDao = ProductDB.getDatabase(app.applicationContext)!!.getProductDao()
        repo = ProductRepository(firebaseDatabase)
        allShops = repo.allShops
        allProducts = repo.allProducts
        allUserProduct = repo.allUserProducts

    }



    fun insertShop(shop: Shop) = repo.insertShop(shop)
    fun updateShop(shop: Shop) = repo.updateShop(shop)
    fun deleteShop(shop: Shop) = repo.deleteShop(shop)
    fun deleteAllShops() = repo.deleteAllShops()


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
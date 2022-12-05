package com.example.niniprojekt1

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ProductRepository(private val firebaseDatabase: FirebaseDatabase) {


    val allProducts: MutableLiveData<HashMap<String, Product>> = MutableLiveData<HashMap<String,Product>>().also {
        it.value = HashMap<String,Product>()
    }

    init {
        firebaseDatabase.getReference("products")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val product = Product(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        price = snapshot.child("price").value as Double,
                        quantity = snapshot.child("quantity").value as Long,
                        state = snapshot.child("state").value as Boolean
                    )
                    allProducts.value?.put(product.id, product)
                    allProducts.postValue(allProducts.value)
                    Log.i("productAdd", "Product added: ${allProducts.value.toString()}")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val product = Product(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        price = snapshot.child("price").value as Double,
                        quantity = snapshot.child("quantity").value as Long,
                        state = snapshot.child("state").value as Boolean
                    )
                    allProducts.value?.set(product.id, product)
                    allProducts.postValue(allProducts.value)
                    Log.i("productChange", "Product updated: ${product.id}")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val product = Product(
                        id = snapshot.ref.key as String,
                        name = snapshot.child("name").value as String,
                        price = snapshot.child("price").value as Double,
                        quantity = snapshot.child("quantity").value as Long,
                        state = snapshot.child("state").value as Boolean
                    )
                    Log.i("removed", product.toString())
                    allProducts.value?.remove(product.id)
                    allProducts.postValue(allProducts.value)

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

   // val allProduct = productDao.getProduct()

        suspend fun inster(product: Product){
            firebaseDatabase.getReference("products").push().also {
                product.id = it.ref.key!!
                it.setValue(product)
            }
        }

        fun update(product: Product) {
            var ref = firebaseDatabase.getReference("products/${product.id}")
            ref.child("name").setValue(product.name)
            ref.child("price").setValue(product.price)
            ref.child("quantity").setValue(product.quantity)
            ref.child("state").setValue(product.state)
        }

    fun delete(product: Product) = firebaseDatabase.getReference("products/${product.id}").removeValue()

    fun deleteAll() = firebaseDatabase.getReference("products").removeValue()
}

        //fun maxId() :Long = productDao.maxId()



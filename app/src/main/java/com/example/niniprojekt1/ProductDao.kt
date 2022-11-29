package com.example.niniprojekt1

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDao {

    @Query("Select * from product")
    fun getProduct(): LiveData<List<Product>>

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete (product: Product)

    @Query("Delete from product")
    fun deleteAll()

    @Query("SELECT max(id) FROM product")
    fun maxId() :Long
}
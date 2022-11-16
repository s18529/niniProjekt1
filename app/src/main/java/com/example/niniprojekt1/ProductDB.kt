package com.example.niniprojekt1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.AccessControlContext

@Database(entities = [Product::class], version = 1)
abstract class ProductDB : RoomDatabase() {

    abstract fun getProductDao(): ProductDao

    companion object{
        var instance: ProductDB? = null

        fun getDatabase(context: Context): ProductDB?{

            if(instance!=null){
                return instance
            }else{
                instance = Room.databaseBuilder(
                    context,
                    ProductDB::class.java,
                    "Baza produktów"
                ).build()
                return instance
            }
        }
    }
}
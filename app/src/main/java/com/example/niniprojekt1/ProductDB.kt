package com.example.niniprojekt1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.AccessControlContext

//@Database(entities = [Product::class], version = 1)
//@Suppress("UNREACHABLE_CODE")
abstract class ProductDB : RoomDatabase() {

    abstract fun getProductDao(): ProductDao

    companion object{
        private var instance: ProductDB? = null

        fun getDatabase(context: Context): ProductDB{
            if(instance != null)
                return instance as ProductDB
            instance = Room.databaseBuilder(
                context.applicationContext,
                ProductDB::class.java,
                "A database of students."
            ).build()
            return instance as ProductDB
        }
    }
}
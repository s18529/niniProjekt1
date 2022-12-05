package com.example.niniprojekt1



data class Product(
//    @PrimaryKey(autoGenerate = true)
    var id: String,
    val name: String,
    val price: Double,
    val quantity: Long,
    var state: Boolean,
)

package com.example.niniprojekt1

class ProductRepository(private val productDao: ProductDao) {

    val allProduct = productDao.getProduct()

    suspend fun inster(product: Product) = productDao.insert(product)
    suspend fun update(product: Product) = productDao.update(product)
    suspend fun delete(product: Product) = productDao.delete(product)
    suspend fun deleteAll() = productDao.deleteAll()


}
package com.example.storeapp

object ProductRepository {
    private val products = mutableListOf<Product>()

    fun getProducts(query: String? = null): List<Product> {
        if (query.isNullOrBlank()) return products
        return products.filter { it.name.contains(query, ignoreCase = true) }
    }

    fun addProduct(product: Product) {
        products.add(product)
    }
}

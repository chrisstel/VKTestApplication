package com.example.vktestapplication.model.data

data class ProductResponse(
    val products: MutableList<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)

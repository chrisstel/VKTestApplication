package com.example.vktestapplication.model.repository

import com.example.vktestapplication.model.api.RetrofitInstance
import com.example.vktestapplication.model.data.ProductResponse
import retrofit2.Response

class ProductRepository {

    suspend fun getProducts(skip: Int): Response<ProductResponse> = RetrofitInstance.productAPI.getProducts(skip)
}
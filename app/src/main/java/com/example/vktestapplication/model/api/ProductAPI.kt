package com.example.vktestapplication.model.api

import com.example.vktestapplication.model.data.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductAPI {

    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 20,
    ): Response<ProductResponse>
}
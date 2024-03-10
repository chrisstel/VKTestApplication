package com.example.vktestapplication.model.api

import com.example.vktestapplication.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
            Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val productAPI by lazy {
        retrofit.create(ProductAPI::class.java)
    }
}
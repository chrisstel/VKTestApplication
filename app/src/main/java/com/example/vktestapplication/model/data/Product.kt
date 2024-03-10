package com.example.vktestapplication.model.data

import java.io.Serializable

typealias URL = String

data class Product(
    val id: Int?,
    val title: String?,
    val description: String?,
    val price: Int?,
    val discountPercentage: Double?,
    val rating: Double?,
    val stock: Int?,
    val brand: String?,
    val category: String?,
    val thumbnail: URL?,
    val images: List<URL?>,
) : Serializable

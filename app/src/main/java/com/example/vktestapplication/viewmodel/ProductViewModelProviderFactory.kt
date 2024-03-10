package com.example.vktestapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vktestapplication.model.repository.ProductRepository

class ProductViewModelProviderFactory(
    val app: Application,
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = ProductViewModel(app, productRepository) as T
}
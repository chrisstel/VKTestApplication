package com.example.vktestapplication.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.vktestapplication.ProductApplication
import com.example.vktestapplication.model.data.Product
import com.example.vktestapplication.model.data.ProductResponse
import com.example.vktestapplication.model.repository.ProductRepository
import com.example.vktestapplication.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ProductViewModel(
    app: Application,
    private val productRepository: ProductRepository
): AndroidViewModel(app) {

    private val productLiveData: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()
    fun getLiveData(): LiveData<Resource<ProductResponse>> = productLiveData

    var skip = 0
    private var productResponse: ProductResponse? = null

    init {
        getProducts()
    }

    fun getProducts() = viewModelScope.launch {
        safeProductCall(skip = skip)
    }

    private suspend fun safeProductCall(skip: Int) {
        productLiveData.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = productRepository.getProducts(skip = skip)
                productLiveData.postValue(handleProductResponse(response))
            } else {
                productLiveData.postValue(Resource.Error(message = "No Internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException -> productLiveData.postValue(Resource.Error(message = "Network failure"))
                else -> productLiveData.postValue(Resource.Error(message = "Conversion error"))
            }
        }


    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<ProductApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun handleProductResponse(response: Response<ProductResponse>): Resource<ProductResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                skip += 20

                if (productResponse == null) {
                    productResponse = resultResponse
                } else {
                    val oldProducts = productResponse?.products
                    val newProducts = resultResponse.products

                    oldProducts?.addAll(newProducts)
                }

                return Resource.Success(data = productResponse ?: resultResponse)
            }
        }

        return Resource.Error(message = response.message())
    }
}
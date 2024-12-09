package com.bangkit.nutrisee.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.nutrisee.data.product.ApiProductConfig
import com.bangkit.nutrisee.data.product.ApiProductService
import com.bangkit.nutrisee.data.product.ProductResponse
import com.bangkit.nutrisee.data.retrofit.ApiNewsConfig
import com.bangkit.nutrisee.data.user.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchProductViewModel : ViewModel() {
    private val _products = MutableLiveData<List<ProductResponse>>()
    val products: LiveData<List<ProductResponse>> = _products

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fetchProducts(token: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = ApiProductConfig.getApiService().getProducts(token)
                if (response.isSuccessful) {
                    _products.value = response.body() ?: emptyList()
                } else {
                    Log.e("FetchProducts", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("FetchProducts", "Exception: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
}




//class SearchProductViewModel : ViewModel() {
//
//    private val _products = MutableLiveData<List<ProductResponse>>()
//    val products: LiveData<List<ProductResponse>> = _products
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _error = MutableLiveData<String?>()
//    val error: LiveData<String?> = _error
//
//    fun fetchProducts(accessToken: String) {
//        _isLoading.value = true
//        _error.value = null
//
//        viewModelScope.launch {
//            try {
//                Log.d("Authorization Header", "Bearer $accessToken")
//                val response = ApiProductConfig.getApiService().getProducts(accessToken)
//                _isLoading.value = false
//
//                if (response.isSuccessful) {
//                    Log.d("Response Body", response.body().toString())
//                    _products.value = response.body() ?: emptyList()
//                    if (response.body().isNullOrEmpty()) {
//                        Log.e("Data Kosong", "Tidak ada data yang diterima dari server.")
//                    }
//                } else {
//                    val errorMessage = "Error: ${response.code()} - ${response.message()}"
//                    _error.value = errorMessage
//                }
//            } catch (e: Exception) {
//                _isLoading.value = false
//                _error.value = "Network error: ${e.message}"
//            }
//        }
//    }
//}




//class SearchProductViewModel : ViewModel() {
//    // TODO: Implement the ViewModel
//    private val _text = MutableLiveData<String>().apply {
//        value = "This is Search Produk Fragment"
//    }
//    val text: LiveData<String> = _text
//}
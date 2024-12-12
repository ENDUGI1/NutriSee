package com.bangkit.nutrisee.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.nutrisee.data.product.ApiProductConfig
import com.bangkit.nutrisee.data.product.ProductResponse
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

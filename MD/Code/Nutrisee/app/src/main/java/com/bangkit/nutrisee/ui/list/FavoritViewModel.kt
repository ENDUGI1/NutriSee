package com.bangkit.nutrisee.ui.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.nutrisee.data.product.ApiProductConfig
import com.bangkit.nutrisee.data.product.ProductResponse
import com.bangkit.nutrisee.data.product.ProductStorage
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritViewModel(application: Application) : AndroidViewModel(application) {

    private val _products = MutableLiveData<List<ProductResponse>>()
    val products: LiveData<List<ProductResponse>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val eventStorage: ProductStorage = ProductStorage(application)
    private val userPreferences: UserPreferences = UserPreferences.getInstance(application.userPreferencesDataStore)

    fun fetchFavoriteProducts() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userPreferences.getAccessToken().collect { token ->
                    if (token.isNotEmpty()) {
                        val apiService = ApiProductConfig.getApiService()
                        val response = apiService.getProducts("Bearer $token")

                        if (response.isSuccessful && response.body() != null) {
                            val products = response.body()!!
                            val favoriteProducts = products.filter { product ->
                                eventStorage.isProductStored(product.id.toString())
                            }
                            _products.postValue(favoriteProducts)
                            _isLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("FetchError", "Exception: $e")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
package com.bangkit.nutrisee.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.nutrisee.data.response.ArticlesItem
import com.bangkit.nutrisee.data.response.HealthNewsResponse
import com.bangkit.nutrisee.data.retrofit.ApiNewsConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchArticleViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<ArticlesItem>>()
    val articles: LiveData<List<ArticlesItem>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

fun fetchArticles() {
    // Set loading to true before starting the fetch
    _isLoading.value = true

    // Reset error
    _error.value = null

    val apiService = ApiNewsConfig.getApiService()
    val call = apiService.getNews(apiKey = "343117b1cf3549ab9c40350a712770e0")

    call.enqueue(object : Callback<HealthNewsResponse> {
        override fun onResponse(
            call: Call<HealthNewsResponse>,
            response: Response<HealthNewsResponse>
        ) {
            // Always set loading to false when response is received
            _isLoading.value = false

            if (response.isSuccessful) {
                val articlesList = response.body()?.articles
                    ?.filterNotNull()
                    ?.filter { it.title != null && it.title != "[Removed]" && it.urlToImage != null && it.urlToImage != ""} ?: emptyList()

                _articles.value = articlesList
            } else {
                val errorMessage = "Error: ${response.code()} - ${response.message()}"
                Log.e("SearchArticleViewModel", errorMessage)
                _error.value = errorMessage
            }
        }

        override fun onFailure(call: Call<HealthNewsResponse>, t: Throwable) {
            // Set loading to false on failure
            _isLoading.value = false

            val errorMessage = "Network error: ${t.message}"
            Log.e("SearchArticleViewModel", errorMessage)
            _error.value = errorMessage
        }
    })
}
}
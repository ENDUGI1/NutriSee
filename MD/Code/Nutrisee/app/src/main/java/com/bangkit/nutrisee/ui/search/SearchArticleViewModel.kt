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

    fun fetchArticles() {
        val apiService = ApiNewsConfig.getApiService()
        val call = apiService.getNews(apiKey = "343117b1cf3549ab9c40350a712770e0")

        call.enqueue(object : Callback<HealthNewsResponse> {
            override fun onResponse(
                call: Call<HealthNewsResponse>,
                response: Response<HealthNewsResponse>
            ) {
                if (response.isSuccessful) {
                    _articles.postValue((response.body()?.articles ?: emptyList()) as List<ArticlesItem>?)
                } else {
                    Log.e("SearchArticleViewModel", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<HealthNewsResponse>, t: Throwable) {
                Log.e("SearchArticleViewModel", "Failure: ${t.message}")
            }
        })
    }
}



package com.bangkit.nutrisee.data.retrofit

import com.bangkit.nutrisee.data.response.HealthNewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiNewsService {
    @GET("top-headlines")
    fun getNews(
        @Query("category") category: String = "health",
        @Query("apiKey") apiKey: String
    ): Call<HealthNewsResponse>
}

package com.bangkit.nutrisee.data.product

import com.bangkit.nutrisee.data.user.ApiUserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiProductConfig {
    companion object {
        private const val BASE_URL = "http://122.248.242.244:3000/api/v1/" // Ganti dengan URL API Anda

        fun getApiService(): ApiProductService {
            // Interceptor untuk logging request dan response
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            // OkHttpClient dengan interceptor
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            // Retrofit dengan OkHttpClient
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiProductService::class.java)
        }
    }
}
package com.bangkit.nutrisee.data.user

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiUserConfig {
    companion object {
        private const val BASE_URL = "https://nutriseeapi-838915593985.asia-southeast2.run.app/api/v1/"

        fun getApiService(): ApiUserService {
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

            return retrofit.create(ApiUserService::class.java)
        }
    }
}

//"http://47.236.141.252:5000/api/v1/"
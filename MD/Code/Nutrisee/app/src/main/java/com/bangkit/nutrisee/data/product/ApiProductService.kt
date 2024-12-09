package com.bangkit.nutrisee.data.product

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiProductService {
    @GET("/api/v1/product")
    suspend fun getProducts(
        @Header("Authorization") token: String
    ): Response<List<ProductResponse>>
}
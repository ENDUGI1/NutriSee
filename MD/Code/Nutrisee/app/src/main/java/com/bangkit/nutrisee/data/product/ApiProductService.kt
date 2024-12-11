package com.bangkit.nutrisee.data.product

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Call

interface ApiProductService {
    @GET("/api/v1/product")
    suspend fun getProducts(
        @Header("Authorization") token: String
    ): Response<List<ProductResponse>>

    @GET("/api/v1/product/history")
    suspend fun getProductsHistory(
        @Header("Authorization") token: String
    ): Response<List<ProductResponse>>

    @Multipart
    @POST("/api/v1/product")
    fun uploadProductData(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("protein") protein: RequestBody,
        @Part("sugar") sugar: RequestBody,
        @Part("sodium") sodium: RequestBody,
        @Part("saturatedFat") saturatedFat: RequestBody,
        @Part("calories") calories: RequestBody,
        @Part("fiber") fiber: RequestBody,
        @Part("estVegetableContain") estVegetableContain: RequestBody
    ): Call<ResponseBody>
}
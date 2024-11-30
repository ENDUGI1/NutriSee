package com.bangkit.nutrisee.data.user

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiUserService {
    @POST("/api/v1/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/api/v1/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
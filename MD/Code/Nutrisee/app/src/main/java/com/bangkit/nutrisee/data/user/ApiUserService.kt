package com.bangkit.nutrisee.data.user

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiUserService {
    @POST("/api/v1/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/api/v1/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/api/v1/forgot-password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    @GET("/api/v1/profile")
    fun getProfile(@Header("Authorization") token: String): Call<UserProfileResponse>
}
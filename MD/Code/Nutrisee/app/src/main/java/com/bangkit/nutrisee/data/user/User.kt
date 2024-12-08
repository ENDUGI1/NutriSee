package com.bangkit.nutrisee.data.user

import com.google.gson.annotations.SerializedName

data class User (
    val name: String,
    val email: String,
    val refreshToken: String,
    val token: String,
    val expiresIn: String
)

// Model untuk respons profil pengguna
data class UserProfileResponse(
    val data: UserProfileData
)

data class UserProfileData(
    val username: String,
    val email: String,
    val createdAt: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confPassword: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val accessToken: String,
    val refreshToken: RefreshToken
)

data class RefreshToken(
    val token: String,
    val expiresIn: String
)

data class RegisterResponse(
    val message: String
)
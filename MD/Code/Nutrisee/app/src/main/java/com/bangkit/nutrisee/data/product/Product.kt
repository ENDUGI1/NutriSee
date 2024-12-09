package com.bangkit.nutrisee.data.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("protein") val protein: Float,
    @SerializedName("sugar") val sugar: Float,
    @SerializedName("sodium") val sodium: Double,
    @SerializedName("saturatedFat") val saturatedFat: Float,
    @SerializedName("calories") val calories: Float,
    @SerializedName("fiber") val fiber: Float,
    @SerializedName("estVegetableContain") val estVegetableContain: Float,
    @SerializedName("grade") val grade: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
) : Parcelable
package com.bangkit.nutrisee.data.product

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductResponse(
    val id: String,
    val name: String,
    val image: String,
    val protein: Double,
    val sugar: Double,
    val sodium: Double,
    val saturatedFat: Double,
    val calories: Double,
    val fiber: Double,
    val estVegetableContain: Double,
    val grade: String,
    val userId: String,
    val createdAt: String,
    val updatedAt: String
) : Parcelable



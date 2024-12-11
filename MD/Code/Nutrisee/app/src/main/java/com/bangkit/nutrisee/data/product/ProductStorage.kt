package com.bangkit.nutrisee.data.product

import android.content.Context
import android.content.SharedPreferences

class ProductStorage(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("favorite_products", Context.MODE_PRIVATE)

    // Fungsi untuk menambahkan ID produk ke SharedPreferences
    fun saveProductId(productId: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(productId.toString(), true)
        editor.apply()
    }

    // Fungsi untuk mengecek apakah ID produk ada di SharedPreferences
    fun isProductStored(productId: String): Boolean {
        return sharedPreferences.getBoolean(productId.toString(), false)
    }

    // Fungsi untuk menghapus ID produk dari SharedPreferences
    fun removeProductId(productId: String) {
        val editor = sharedPreferences.edit()
        editor.remove(productId.toString()) // Menghapus berdasarkan ID produk
        editor.apply()
    }
    // Fungsi untuk mendapatkan semua ID produk yang disimpan
    fun getAllStoredProductIds(): Set<String> {
        return sharedPreferences.all.keys // Mengambil semua key yang disimpan
    }
    fun clearAllProducts() {
        val editor = sharedPreferences.edit()
        editor.clear() // Menghapus semua data di SharedPreferences
        editor.apply()
    }
}

package com.bangkit.nutrisee.ui.scan

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.nutrisee.MainActivity
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.product.ApiProductConfig
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ConfirmScanActivity : AppCompatActivity() {
    private lateinit var userPreferences: UserPreferences
    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_scan)
        supportActionBar?.hide()

        userPreferences = UserPreferences.getInstance(this.applicationContext.userPreferencesDataStore)

        val imgProduct: ImageView = findViewById(R.id.img_product)
        val etProductName: TextInputEditText = findViewById(R.id.et_product_name)
        val etSaturatedFat: TextInputEditText = findViewById(R.id.et_saturated_fat)
        val etSugar: TextInputEditText = findViewById(R.id.et_sugar)
        val etFiber: TextInputEditText = findViewById(R.id.et_fiber)
        val etProtein: TextInputEditText = findViewById(R.id.et_protein)
        val etSodium: TextInputEditText = findViewById(R.id.et_sodium)
        val etFruitsNuts: TextInputEditText = findViewById(R.id.et_fruits_nuts)
        val etEnergy: TextInputEditText = findViewById(R.id.et_energy)
        val btnConfirm: Button = findViewById(R.id.btn_confirm)
        val btnCencel: Button = findViewById(R.id.btn_cancel)

        imagePath = intent.getStringExtra("EXTRA_IMAGE_PATH")
        val imageUri = intent.getParcelableExtra<Uri>("EXTRA_IMAGE_URI")

        val finalImagePath = imagePath ?: (imageUri?.let { getImagePath(it) })

        if (finalImagePath != null) {
            val imageFile = File(finalImagePath)
            if (imageFile.exists()) {
                imgProduct.setImageURI(Uri.fromFile(imageFile))
                Log.e("image path", "onCreate: $finalImagePath")
            } else {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
        }

        btnCencel.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        btnConfirm.setOnClickListener {
            lifecycleScope.launch {
                val token = userPreferences.getAccessToken().firstOrNull()
                Log.d("token", "token: $token")
                if (token.isNullOrEmpty()) {
                    Toast.makeText(this@ConfirmScanActivity, "Token not found", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val name = etProductName.text.toString().trim()
                val saturatedFat = etSaturatedFat.text.toString().trim()
                val sugar = etSugar.text.toString().trim()
                val fiber = etFiber.text.toString().trim()
                val protein = etProtein.text.toString().trim()
                val sodium = etSodium.text.toString().trim()
                val fruitsNuts = etFruitsNuts.text.toString().trim()
                val energy = etEnergy.text.toString().trim()

                if (name.isEmpty() || finalImagePath == null || saturatedFat.isEmpty() || sugar.isEmpty() ||
                    fiber.isEmpty() || protein.isEmpty() || sodium.isEmpty() || fruitsNuts.isEmpty() || energy.isEmpty()
                ) {
                    Toast.makeText(this@ConfirmScanActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                uploadProductData(
                    token!!,
                    name,
                    saturatedFat,
                    sugar,
                    fiber,
                    protein,
                    sodium,
                    fruitsNuts,
                    energy,
                    finalImagePath
                )
            }
        }
    }

    private fun getImagePath(uri: Uri): String? {
        return when {
            uri.scheme == ContentResolver.SCHEME_CONTENT -> {
                getRealPathFromURI(uri)
            }
            uri.scheme == ContentResolver.SCHEME_FILE -> {
                uri.path
            }
            else -> null
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
            if (columnIndex != -1) {
                it.moveToFirst()
                return it.getString(columnIndex)
            }
        }
        return null
    }

    private fun uploadProductData(
        token: String,
        name: String,
        saturatedFat: String,
        sugar: String,
        fiber: String,
        protein: String,
        sodium: String,
        fruitsNuts: String,
        energy: String,
        imagePath: String
    ) {
        val apiService = ApiProductConfig.getApiService()

        val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val saturatedFatBody = saturatedFat.toRequestBody("text/plain".toMediaTypeOrNull())
        val sugarBody = sugar.toRequestBody("text/plain".toMediaTypeOrNull())
        val fiberBody = fiber.toRequestBody("text/plain".toMediaTypeOrNull())
        val proteinBody = protein.toRequestBody("text/plain".toMediaTypeOrNull())
        val sodiumBody = sodium.toRequestBody("text/plain".toMediaTypeOrNull())
        val fruitsNutsBody = fruitsNuts.toRequestBody("text/plain".toMediaTypeOrNull())
        val energyBody = energy.toRequestBody("text/plain".toMediaTypeOrNull())

        val file = File(imagePath)
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        apiService.uploadProductData(
            token = "Bearer $token",
            name = nameBody,
            image = imagePart,
            protein = proteinBody,
            sugar = sugarBody,
            sodium = sodiumBody,
            saturatedFat = saturatedFatBody,
            calories = energyBody,
            fiber = fiberBody,
            estVegetableContain = fruitsNutsBody
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("respon after upload", "onResponse: ${response.body()}")
                if (response.isSuccessful) {
                    Toast.makeText(this@ConfirmScanActivity, "Product uploaded successfully", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@ConfirmScanActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@ConfirmScanActivity, "Failed to upload product", Toast.LENGTH_SHORT).show()
                    Log.e("response gagal", "onResponse: gagal upload ke server")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ConfirmScanActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("error confirm", "onFailure: ${t.message}", )
            }
        })
    }
}
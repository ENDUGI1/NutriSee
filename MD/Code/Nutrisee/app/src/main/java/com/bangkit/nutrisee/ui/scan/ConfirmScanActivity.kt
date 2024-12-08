package com.bangkit.nutrisee.ui.scan

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.nutrisee.R


class ConfirmScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_scan)
        supportActionBar?.hide()

        val imgProduct: ImageView = findViewById(R.id.img_product)
        val imageUri = intent.getParcelableExtra<Uri>("EXTRA_IMAGE_URI")

        if (imageUri != null) {
            imgProduct.setImageURI(imageUri)
        } else {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }
}
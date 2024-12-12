package com.bangkit.nutrisee.ui.detailactivity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.product.ProductResponse
import com.bangkit.nutrisee.data.product.ProductStorage
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream

class DetailProductActivity : AppCompatActivity() {

    private lateinit var imgProduct: ImageView
    private lateinit var imgGradeScore: ImageView
    private lateinit var tvProductName: TextView
    private lateinit var tvSaturatedFat: TextView
    private lateinit var tvSugar: TextView
    private lateinit var tvFiber: TextView
    private lateinit var tvProtein: TextView
    private lateinit var tvSodium: TextView
    private lateinit var tvFruitsNuts: TextView
    private lateinit var tvEnergy: TextView
    private lateinit var btnShare: Button
    private lateinit var fabFavorite: FloatingActionButton

    private lateinit var productStorage: ProductStorage
    private var currentProductId: String = ""

    private fun shareScreenshot() {
        val rootView = findViewById<View>(android.R.id.content)

        val bitmap = Bitmap.createBitmap(
            rootView.width,
            rootView.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        rootView.draw(canvas)

        val screenshotFile = File(cacheDir, "product_details.jpg")
        try {
            val outputStream = FileOutputStream(screenshotFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        val screenshotUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            screenshotFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, screenshotUri)
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this nutritional product details from Nutrisee! " +
                        "Product: ${tvProductName.text}\n" +
                        "Discover more with Nutrisee nutrition app!"
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(
            Intent.createChooser(
                shareIntent,
                "Share Product Details"
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        supportActionBar?.hide()


        imgProduct = findViewById(R.id.img_product)
        imgGradeScore = findViewById(R.id.img_grade_score)
        tvProductName = findViewById(R.id.tv_product_name)
        tvSaturatedFat = findViewById(R.id.tv_saturated_fat)
        tvSugar = findViewById(R.id.tv_sugar)
        tvFiber = findViewById(R.id.tv_fiber)
        tvProtein = findViewById(R.id.tv_protein)
        tvSodium = findViewById(R.id.tv_sodium)
        tvFruitsNuts = findViewById(R.id.tv_fruits_nuts)
        tvEnergy = findViewById(R.id.tv_energy)
        fabFavorite = findViewById(R.id.fab_favorite)

        val fabShare: FloatingActionButton = findViewById(R.id.fab_share)
        fabShare.setOnClickListener {
            shareScreenshot()
        }
        val product = intent.getParcelableExtra<ProductResponse>("product") ?: return

        tvProductName.text = product.name
        Glide.with(this).load(product.image).into(imgProduct)

        when (product.grade) {
            "A" -> imgGradeScore.setImageResource(R.drawable.grade_a)
            "B" -> imgGradeScore.setImageResource(R.drawable.grade_b)
            "C" -> imgGradeScore.setImageResource(R.drawable.grade_c)
            "D" -> imgGradeScore.setImageResource(R.drawable.grade_d)
            "E" -> imgGradeScore.setImageResource(R.drawable.grade_e)
            else -> imgGradeScore.setImageResource(R.drawable.placeholder_image)
        }

        tvSaturatedFat.text = "Saturated Fat: ${product.saturatedFat}g"
        tvSugar.text = "Sugar: ${product.sugar}g"
        tvFiber.text = "Fiber: ${product.fiber}g"
        tvProtein.text = "Protein: ${product.protein}g"
        tvSodium.text = "Sodium: ${product.sodium}mg"
        tvFruitsNuts.text = "Fruits/Vegetables/Nuts: ${product.estVegetableContain}%"
        tvEnergy.text = "Energy: ${product.calories} kcal"


        productStorage = ProductStorage(this)

        currentProductId = product.id ?: ""

        if (productStorage.isProductStored(product.id)) {
            fabFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            fabFavorite.setImageResource(R.drawable.ic_favorite_border)
        }

        fabFavorite.setOnClickListener {
            if (productStorage.isProductStored(product.id)) {
                productStorage.removeProductId(product.id)
                fabFavorite.setImageResource(R.drawable.ic_favorite_border)
            } else {
                productStorage.saveProductId(product.id)
                fabFavorite.setImageResource(R.drawable.ic_favorite)
            }

            val intent = Intent().apply {
                putExtra("isFavoriteChanged", true)
                putExtra("productId", product.id)
            }
            setResult(Activity.RESULT_OK, intent)
        }
    }

}
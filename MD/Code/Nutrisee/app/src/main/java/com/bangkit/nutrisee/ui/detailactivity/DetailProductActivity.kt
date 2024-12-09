package com.bangkit.nutrisee.ui.detailactivity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.product.ProductResponse
import com.bumptech.glide.Glide

class DetailProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        val product = intent.getParcelableExtra<ProductResponse>("product")
        product?.let {
            findViewById<TextView>(R.id.tv_product_name).text = it.name
            findViewById<TextView>(R.id.img_grade_score).text = it.grade
            findViewById<TextView>(R.id.tv_energy).text = it.calories.toString()

            Glide.with(this)
                .load(it.image)
                .placeholder(R.drawable.placeholder_image)
                .into(findViewById(R.id.img_product))
        }
    }
}





//class DetailProductActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_detail_product)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}
package com.bangkit.nutrisee.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.product.ProductResponse
import com.bumptech.glide.Glide

class ProductAdapter(
    private var products: List<ProductResponse>,
    private val onItemClick: (ProductResponse) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.img_item_product)
        val gradeProduct: ImageView = itemView.findViewById(R.id.grade)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)



        fun bind(product: ProductResponse) {
            tvName.text = product.name
            Glide.with(itemView.context)
                .load(product.image)
                .into(imgProduct)

            // Grade image
            when (product.grade) {
                "A" -> gradeProduct.setImageResource(R.drawable.grade_a)
                "B" -> gradeProduct.setImageResource(R.drawable.grade_b)
                "C" -> gradeProduct.setImageResource(R.drawable.grade_c)
                "D" -> gradeProduct.setImageResource(R.drawable.grade_d)
                "E" -> gradeProduct.setImageResource(R.drawable.grade_e)
                else -> gradeProduct.setImageResource(R.drawable.placeholder_image)
            }

            // Item click listener
//            itemView.setOnClickListener { onItemClick(product) }
            itemView.setOnClickListener {
                onItemClick(product) // Memanggil lambda untuk klik
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }


    fun updateData(newProducts: List<ProductResponse>) {
        this.products = newProducts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = products.size
}
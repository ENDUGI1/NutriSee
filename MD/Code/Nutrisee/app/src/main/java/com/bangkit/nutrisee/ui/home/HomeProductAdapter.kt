package com.bangkit.nutrisee.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.product.ProductResponse
import com.bumptech.glide.Glide

class HomeProductAdapter(
    private var products: List<ProductResponse>,
    private val onItemClick: (ProductResponse) -> Unit
) : RecyclerView.Adapter<HomeProductAdapter.HomeProductViewHolder>() {

    inner class HomeProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgProduct: ImageView = itemView.findViewById(R.id.img_home_product)
        private val tvName: TextView = itemView.findViewById(R.id.tv_home_product_name)

        fun bind(product: ProductResponse) {
            tvName.text = product.name
            Glide.with(itemView.context)
                .load(product.image)
                .into(imgProduct)

            itemView.setOnClickListener {
                onItemClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_product, parent, false)
        return HomeProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    fun updateData(newProducts: List<ProductResponse>) {
        this.products = newProducts
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = products.size
}
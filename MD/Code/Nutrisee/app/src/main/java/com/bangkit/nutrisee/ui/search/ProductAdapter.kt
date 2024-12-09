package com.bangkit.nutrisee.ui.search

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.product.ProductResponse
import com.bangkit.nutrisee.ui.detailactivity.DetailProductActivity
import com.bumptech.glide.Glide

class ProductAdapter(private val onClick: (ProductResponse) -> Unit) :
    ListAdapter<ProductResponse, ProductAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_item_name)

        fun bind(product: ProductResponse) {
            tvTitle.text = product.name
            Glide.with(itemView.context)
                .load(product.image)
                .placeholder(R.drawable.placeholder_image)
                .into(imgPhoto)

            itemView.setOnClickListener {
                onClick(product)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductResponse>() {
            override fun areItemsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}





//class ProductAdapter(
//    private val onClick: (ProductResponse) -> Unit
//) : ListAdapter<ProductResponse, ProductAdapter.ProductViewHolder>(DiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_product, parent, false)
//        return ProductViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val product = getItem(position)
//        holder.bind(product, onClick)
//    }
//
//    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val nameTextView: TextView = itemView.findViewById(R.id.tv_item_name)
//        private val imageView: ImageView = itemView.findViewById(R.id.img_item_product)
//
//        fun bind(product: ProductResponse, onClick: (ProductResponse) -> Unit) {
//            nameTextView.text = product.name
//            // Assuming you use a library like Glide or Coil for image loading
//            Glide.with(itemView.context)
//                .load(product.image)
//                .placeholder(R.drawable.placeholder_image) // Placeholder image
//                .into(imageView)
//
//            itemView.setOnClickListener { onClick(product) }
//        }
//    }
//
//    class DiffCallback : DiffUtil.ItemCallback<ProductResponse>() {
//        override fun areItemsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
//            return oldItem == newItem
//        }
//    }
//}

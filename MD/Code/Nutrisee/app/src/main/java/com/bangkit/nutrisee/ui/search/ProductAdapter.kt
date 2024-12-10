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




//class ProductAdapter(private val products: List<ProductResponse>) :
//    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
//
//    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imgProduct: ImageView = itemView.findViewById(R.id.img_item_product)
//        val gradeProdct: ImageView = itemView.findViewById(R.id.grade)
//        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_row_product, parent, false)
//        return ProductViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val product = products[position]
//        holder.tvName.text = product.name
//        Glide.with(holder.itemView.context)
//            .load(product.image)
//            .into(holder.imgProduct)
//
//        // Set gambar berdasarkan grade
//        val context = holder.itemView.context
//        when (product.grade) {
//            "A" -> holder.gradeProdct.setImageResource(R.drawable.grade_a) // Gambar untuk grade A
//            "B" -> holder.gradeProdct.setImageResource(R.drawable.grade_b) // Gambar untuk grade B
//            "C" -> holder.gradeProdct.setImageResource(R.drawable.grade_c) // Gambar untuk grade C
//            "D" -> holder.gradeProdct.setImageResource(R.drawable.grade_d) // Gambar untuk grade D
//            "E" -> holder.gradeProdct.setImageResource(R.drawable.grade_e) // Gambar untuk grade E
//            else -> holder.gradeProdct.setImageResource(R.drawable.placeholder_image) // Gambar default
//        }
//    }
//
//    override fun getItemCount(): Int = products.size
//}




//class ProductAdapter(private val onClick: (ProductResponse) -> Unit) :
//    ListAdapter<ProductResponse, ProductAdapter.ProductViewHolder>(DIFF_CALLBACK) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_product, parent, false)
//        return ProductViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val product = getItem(position)
//        holder.bind(product)
//    }
//
//    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val imgPhoto: ImageView = itemView.findViewById(R.id.img_product)
//        private val tvTitle: TextView = itemView.findViewById(R.id.tv_item_name)
//
//        fun bind(product: ProductResponse) {
//            tvTitle.text = product.name
//            Glide.with(itemView.context)
//                .load(product.image)
//                .placeholder(R.drawable.placeholder_image)
//                .into(imgPhoto)
//
//            itemView.setOnClickListener {
//                onClick(product)
//            }
//        }
//    }
//
//    companion object {
//        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductResponse>() {
//            override fun areItemsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}





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

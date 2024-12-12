package com.bangkit.nutrisee.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.response.ArticlesItem
import com.bumptech.glide.Glide

class HomeArticleAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<ArticlesItem, HomeArticleAdapter.HomeArticleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_article, parent, false)
        return HomeArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    inner class HomeArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgArticle: ImageView = itemView.findViewById(R.id.img_home_article)
        private val tvArticleTitle: TextView = itemView.findViewById(R.id.tv_home_article_title)

        fun bind(article: ArticlesItem) {
            Log.d("Article image url", "url: ${article.urlToImage}")
            Glide.with(itemView.context)
                .load(article.urlToImage)
                .into(imgArticle)

            tvArticleTitle.text = article.title ?: "No Title"

            itemView.setOnClickListener {
                article.url?.let { url -> onClick(url) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
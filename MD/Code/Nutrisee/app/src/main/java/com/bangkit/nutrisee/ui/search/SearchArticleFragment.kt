package com.bangkit.nutrisee.ui.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nutrisee.R

class SearchArticleFragment : Fragment() {

    private lateinit var viewModel: SearchArticleViewModel
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var rvArticle: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search_article, container, false)

        progressBar = view.findViewById(R.id.progress_bar)
        rvArticle = view.findViewById(R.id.rv_article)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SearchArticleViewModel::class.java]

        articleAdapter = ArticleAdapter { url ->
            openBrowser(url)
        }

        rvArticle.layoutManager = LinearLayoutManager(requireContext())
        rvArticle.adapter = articleAdapter

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            rvArticle.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.submitList(articles)
        }

        viewModel.fetchArticles()
    }

    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
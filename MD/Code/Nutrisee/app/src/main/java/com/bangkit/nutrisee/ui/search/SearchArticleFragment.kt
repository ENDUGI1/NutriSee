package com.bangkit.nutrisee.ui.search

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.databinding.FragmentSearchArticleBinding
import com.bangkit.nutrisee.ui.list.ListViewModel

class SearchArticleFragment : Fragment() {

    private lateinit var viewModel: SearchArticleViewModel
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(SearchArticleViewModel::class.java)

        // Setup RecyclerView
        articleAdapter = ArticleAdapter { url ->
            openBrowser(url)
        }

        val rvArticle: RecyclerView = view.findViewById(R.id.rv_article)
        rvArticle.layoutManager = LinearLayoutManager(requireContext())
        rvArticle.adapter = articleAdapter

        // Observe data from ViewModel
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.submitList(articles)
        }

        // Fetch articles
        viewModel.fetchArticles()
    }

    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}







//class SearchArticleFragment : Fragment() {
//
//    companion object {
//        fun newInstance() = SearchArticleFragment()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_search_article, container, false)
//    }
//}












//class SearchArticleFragment : Fragment() {
//
//    private var _binding: FragmentSearchArticleBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(SearchArticleViewModel::class.java)
//
//        _binding = FragmentSearchArticleBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textSearchArticle
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
package com.bangkit.nutrisee.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import com.bangkit.nutrisee.ui.detailactivity.DetailProductActivity
import com.bangkit.nutrisee.ui.scan.ScanActivity
import com.bangkit.nutrisee.ui.search.SearchArticleViewModel
import com.bangkit.nutrisee.ui.search.SearchProductViewModel
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch



class HomeFragment : Fragment() {
    private lateinit var homeProductAdapter: HomeProductAdapter
    private lateinit var homeArticleAdapter: HomeArticleAdapter
    private lateinit var userPreferences: UserPreferences
    private lateinit var productViewModel: SearchProductViewModel
    private lateinit var articleViewModel: SearchArticleViewModel

    private lateinit var rvProduct: RecyclerView
    private lateinit var rvArticle: RecyclerView
    private lateinit var progressBarProduct: ProgressBar
    private lateinit var progressBarArticle: ProgressBar
    private lateinit var btnSeeAllProducts: TextView
    private lateinit var btnSeeAllArticles: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inisialisasi view
        rvProduct = view.findViewById(R.id.rv_home_product)
        rvArticle = view.findViewById(R.id.rv_home_article)
        progressBarProduct = view.findViewById(R.id.progress_bar_product)
        progressBarArticle = view.findViewById(R.id.progress_bar_article)
        btnSeeAllProducts = view.findViewById(R.id.btn_see_all_products)
        btnSeeAllArticles = view.findViewById(R.id.btn_see_all_articles)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        productViewModel = ViewModelProvider(this)[SearchProductViewModel::class.java]
        articleViewModel = ViewModelProvider(this)[SearchArticleViewModel::class.java]

        // Setup User Preferences
        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)

        // Setup Recycler View dan Adapter untuk Produk
        setupProductRecyclerView()

        // Setup Recycler View dan Adapter untuk Artikel
        setupArticleRecyclerView()

        // Observasi data produk
        observeProductData()

        // Observasi data artikel
        observeArticleData()

        // Setup tombol "See All"
        setupSeeAllButtons()

        // Fetch data
        fetchData()

        val cardScanProduct: MaterialCardView = view.findViewById(R.id.card_scan_product)
        cardScanProduct.setOnClickListener {
            // Navigate to ScanActivity (you'll need to create this)
            val intent = Intent(requireContext(), ScanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupProductRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvProduct.layoutManager = layoutManager

        homeProductAdapter = HomeProductAdapter(emptyList()) { product ->
            val intent = Intent(requireContext(), DetailProductActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
        rvProduct.adapter = homeProductAdapter
    }

    private fun setupArticleRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvArticle.layoutManager = layoutManager

        homeArticleAdapter = HomeArticleAdapter { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        rvArticle.adapter = homeArticleAdapter
    }

    private fun observeProductData() {
        productViewModel.products.observe(viewLifecycleOwner) { products ->
            // Batasi hanya 4 produk pertama
            val limitedProducts = products.take(4)
            homeProductAdapter.updateData(limitedProducts)
        }

//        productViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            progressBarProduct.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }
    }

    private fun observeArticleData() {
        articleViewModel.articles.observe(viewLifecycleOwner) { articles ->
            // Batasi hanya 4 artikel pertama
            val limitedArticles = articles.take(4)
            homeArticleAdapter.submitList(limitedArticles)
        }

        articleViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBarArticle.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupSeeAllButtons() {
        btnSeeAllProducts.setOnClickListener {
            // Navigasi ke SearchProductFragment
            findNavController().navigate(R.id.searchProductFragment)
            onDestroy()
        }

        btnSeeAllArticles.setOnClickListener {
            // Navigasi ke SearchArticleFragment
            findNavController().navigate(R.id.searchArticleFragment)
            onDestroy()
        }
    }

    private fun fetchData() {
        // Fetch produk
        lifecycleScope.launch {
            userPreferences.getAccessToken().collect { token ->
                if (token.isNotEmpty()) {
                    productViewModel.fetchProducts("Bearer $token")
                }
            }
        }

        // Fetch artikel
        articleViewModel.fetchArticles()
    }
}
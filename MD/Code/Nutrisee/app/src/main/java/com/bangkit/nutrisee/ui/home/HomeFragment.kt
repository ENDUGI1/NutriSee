package com.bangkit.nutrisee.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.bangkit.nutrisee.ui.profile.ProfileViewModel
import com.bangkit.nutrisee.ui.scan.ScanActivity
import com.bangkit.nutrisee.ui.search.SearchArticleViewModel
import com.bangkit.nutrisee.ui.search.SearchProductViewModel
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.flow.first
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
    private lateinit var userWelcome: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        rvProduct = view.findViewById(R.id.rv_home_product)
        rvArticle = view.findViewById(R.id.rv_home_article)
        progressBarProduct = view.findViewById(R.id.progress_bar_product)
        progressBarArticle = view.findViewById(R.id.progress_bar_article)
        btnSeeAllProducts = view.findViewById(R.id.btn_see_all_products)
        btnSeeAllArticles = view.findViewById(R.id.btn_see_all_articles)
        userWelcome = view.findViewById(R.id.tv_welcome)


        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)

        lifecycleScope.launch {
            val name = userPreferences.getName().first()

            if (name.isNullOrEmpty()) {
                val accessToken = userPreferences.getAccessToken().first()
                profileViewModel.getUserProfile(accessToken)

                profileViewModel.profileResult.observe(viewLifecycleOwner, { result ->
                    result.onSuccess { profileResponse ->
                        Log.d("UserProfile", "Username: ${profileResponse.data.username}, Email: ${profileResponse.data.email}")
                        lifecycleScope.launch {
                            userPreferences.updateUserDetails(
                                profileResponse.data.username,
                                profileResponse.data.email
                            )
                        }
                        lifecycleScope.launch {
                            val haloname = "Halo ${profileResponse.data.username}"
                            userWelcome.text = haloname
                        }
                    }
                    result.onFailure {
                        Log.e("UserProfileError", it.message ?: "Error fetching profile")
                    }
                })
            } else {
                val haloname = "Halo ${name}"
                userWelcome.text = haloname
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel = ViewModelProvider(this)[SearchProductViewModel::class.java]
        articleViewModel = ViewModelProvider(this)[SearchArticleViewModel::class.java]

        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)

        setupProductRecyclerView()
        setupArticleRecyclerView()

        observeProductData()
        observeArticleData()
        setupSeeAllButtons()

        fetchData()

        val cardScanProduct: MaterialCardView = view.findViewById(R.id.card_scan_product)
        cardScanProduct.setOnClickListener {
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
            val limitedProducts = products.take(4)
            homeProductAdapter.updateData(limitedProducts)
        }
    }

    private fun observeArticleData() {
        articleViewModel.articles.observe(viewLifecycleOwner) { articles ->
            val limitedArticles = articles.take(4)
            homeArticleAdapter.submitList(limitedArticles)
        }

        articleViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBarArticle.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupSeeAllButtons() {
        btnSeeAllProducts.setOnClickListener {
            findNavController().navigate(R.id.searchProductFragment)
            onDestroy()
        }

        btnSeeAllArticles.setOnClickListener {
            findNavController().navigate(R.id.searchArticleFragment)
            onDestroy()
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            userPreferences.getAccessToken().collect { token ->
                if (token.isNotEmpty()) {
                    productViewModel.fetchProducts("Bearer $token")
                }
            }
        }
        articleViewModel.fetchArticles()
    }
}
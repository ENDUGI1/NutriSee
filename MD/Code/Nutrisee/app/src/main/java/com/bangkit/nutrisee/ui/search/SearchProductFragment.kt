package com.bangkit.nutrisee.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.product.ApiProductConfig
import com.bangkit.nutrisee.data.product.ProductResponse
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import com.bangkit.nutrisee.ui.detailactivity.DetailProductActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchProductFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchInput: TextInputEditText
    private lateinit var userPreferences: UserPreferences

    // Make the adapter nullable and initialize it later
    private var productAdapter: ProductAdapter? = null

    // Store all products as a class-level variable
    private var allProducts: List<ProductResponse> = listOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_product, container, false)
        recyclerView = view.findViewById(R.id.rv_product)
        progressBar = view.findViewById(R.id.progress_bar)
        searchInput = view.findViewById(R.id.search_input)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup search input listener
        setupSearchListener()


        // Mengambil AccessToken
        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        lifecycleScope.launch {
            userPreferences.getAccessToken().collect { token ->
                if (token.isNotEmpty()) {
                    Log.e("accessToken fragmentproduct", "Access token : $token")
                    fetchProducts("Bearer $token")
                } else {
                    Log.e("accessToken", "Access token tidak ditemukan.")
                }
            }
        }
    }

    private fun setupSearchListener() {
        searchInput.doAfterTextChanged { text ->
            // Only filter if adapter is initialized and products exist
            productAdapter?.let { adapter ->
                val query = text.toString()
                val filteredList = allProducts.filter { product ->
                    product.name.contains(query, ignoreCase = true)
                }
                adapter.updateData(filteredList)
            }
        }
    }

    private fun fetchProducts(token: String) {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiService = ApiProductConfig.getApiService()
                val response = apiService.getProducts(token)

                if (response.isSuccessful && response.body() != null) {
                    val products = response.body()!!
                    allProducts = products

                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())

                        // Create adapter with click handling
                        productAdapter = ProductAdapter(products) { product ->
                            val intent = Intent(requireContext(), DetailProductActivity::class.java)
                            intent.putExtra("product", product)
                            startActivity(intent)
                        }
                        recyclerView.adapter = productAdapter
                    }
                } else {
                    Log.e("FetchError", "Error: ${response.message()}")
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                Log.e("FetchError", "Exception: $e")
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        searchInput.setText("") // Mengosongkan search bar
    }
}





//class SearchProductFragment : Fragment() {
//
//    private lateinit var viewModel: SearchProductViewModel
//    private lateinit var productAdapter: ProductAdapter
//    private lateinit var progressBar: ProgressBar
//    private lateinit var rvProduct: RecyclerView
//
//    private lateinit var userPreferences: UserPreferences
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val view = inflater.inflate(R.layout.fragment_search_product, container, false)
//
//        // Initialize views
//        progressBar = view.findViewById(R.id.progress_bar)
//        rvProduct = view.findViewById(R.id.rv_product)
//
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
//        // Initialize ViewModel
//        viewModel = ViewModelProvider(this)[SearchProductViewModel::class.java]
//
//        // Setup RecyclerView
//        productAdapter = ProductAdapter { product ->
//            openDetail(product)
//        }
//
//        rvProduct.layoutManager = LinearLayoutManager(requireContext())
//        rvProduct.adapter = productAdapter
//
//        // Observe loading state
//        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//            rvProduct.visibility = if (isLoading) View.GONE else View.VISIBLE
//        }
//
//        // Observe data from ViewModel
//        viewModel.products.observe(viewLifecycleOwner) { products ->
//            productAdapter.submitList(products)
//        }
//
//        // Fetch products
//
//        // Inisialisasi UserPreferences
//
//        // Mengambil AccessToken
//        lifecycleScope.launch {
//            userPreferences.getAccessToken().collect { token ->
//                if (token.isNotEmpty()) {
//                    Log.d("accessToken", "accessToken: $token")
//                    // Gunakan accessToken untuk fetch produk
//                    viewModel.fetchProducts("Bearer $token")
//                } else {
//                    Log.e("accessToken", "Access token tidak ditemukan.")
//                }
//            }
//        }
//    }
//
//    private fun openDetail(product: ProductResponse) {
//        val intent = Intent(requireContext(), DetailProductActivity::class.java).apply {
//            putExtra("product", product)
//        }
//        startActivity(intent)
//    }
//}





//class SearchProductFragment : Fragment() {
//
//    private lateinit var viewModel: SearchProductViewModel
//    private lateinit var adapter: ProductAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val view = inflater.inflate(R.layout.fragment_search_product, container, false)
//        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_product)
//        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
//
//        viewModel = ViewModelProvider(this)[SearchProductViewModel::class.java]
//        adapter = ProductAdapter { product ->
//            val intent = Intent(requireContext(), DetailProductActivity::class.java).apply {
//                putExtra("product", product)
//            }
//            startActivity(intent)
//        }
//
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = adapter
//
//        viewModel.products.observe(viewLifecycleOwner) { products ->
//            adapter.submitList(products)
//            progressBar.visibility = View.GONE
//        }
//
//        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }
//
//        viewModel.fetchProducts()
//        return view
//    }
//}




//class SearchProductFragment : Fragment() {
//
//    companion object {
//        fun newInstance() = SearchProductFragment()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_search_product, container, false)
//    }
//}











//class SearchProductFragment : Fragment() {
//
//    companion object {
//        fun newInstance() = SearchProductFragment()
//    }
//
//    private val viewModel: SearchProductViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // TODO: Use the ViewModel
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_search_product, container, false)
//    }
//}
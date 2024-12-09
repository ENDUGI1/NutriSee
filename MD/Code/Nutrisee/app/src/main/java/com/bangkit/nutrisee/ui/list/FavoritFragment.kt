package com.bangkit.nutrisee.ui.list

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.product.ApiProductConfig
import com.bangkit.nutrisee.data.product.ProductStorage
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import com.bangkit.nutrisee.ui.detailactivity.DetailProductActivity
import com.bangkit.nutrisee.ui.search.ProductAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var productAdapter: ProductAdapter
    private lateinit var viewModel: FavoritViewModel

    companion object {
        private const val REQUEST_CODE_DETAIL_PRODUCT = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorit, container, false)
        recyclerView = view.findViewById(R.id.rv_product)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(FavoritViewModel::class.java)

        // Set up the adapter with an empty list initially
        productAdapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(requireContext(), DetailProductActivity::class.java)
            intent.putExtra("product", product) // Ensure Product is Parcelable
            startActivityForResult(intent, REQUEST_CODE_DETAIL_PRODUCT)
        }

        recyclerView.adapter = productAdapter

        // Observe the LiveData for product changes
        viewModel.products.observe(viewLifecycleOwner, Observer { products ->
            productAdapter.updateData(products)
        })

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Trigger data loading
        viewModel.fetchFavoriteProducts()
    }

    // Handle the result from DetailProductActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAIL_PRODUCT && resultCode == Activity.RESULT_OK) {
            val isFavoriteChanged = data?.getBooleanExtra("isFavoriteChanged", false) ?: false
            if (isFavoriteChanged) {
                // Fetch updated list of favorite products after changes
                viewModel.fetchFavoriteProducts()
            }
        }
    }
}






//class FavoritFragment : Fragment() {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var progressBar: ProgressBar
//    private lateinit var userPreferences: UserPreferences
//    private lateinit var eventStorage: ProductStorage
//    private val productAdapter = ProductAdapter(emptyList()) { product ->
//        val intent = Intent(requireContext(), DetailProductActivity::class.java)
//        intent.putExtra("product", product) // Ensure Product is Parcelable
//        startActivityForResult(intent, REQUEST_CODE_DETAIL_PRODUCT)
//    }
//
//    companion object {
//        private const val REQUEST_CODE_DETAIL_PRODUCT = 1
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_favorit, container, false)
//        recyclerView = view.findViewById(R.id.rv_product)
//        progressBar = view.findViewById(R.id.progress_bar)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = productAdapter
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Inisialisasi EventStorage
//        eventStorage = ProductStorage(requireContext())
//        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
//
//        // Fetch token and products
//        lifecycleScope.launch {
//            userPreferences.getAccessToken().collect { token ->
//                if (token.isNotEmpty()) {
//                    fetchProducts("Bearer $token")
//                }
//            }
//        }
//    }
//
//    private fun fetchProducts(token: String) {
//        progressBar.visibility = View.VISIBLE
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                val apiService = ApiProductConfig.getApiService()
//                val response = apiService.getProducts(token)
//                if (response.isSuccessful && response.body() != null) {
//                    val products = response.body()!!
//                    val favoriteProducts = products.filter { product ->
//                        eventStorage.isProductStored(product.id.toString())
//                    }
//
//                    withContext(Dispatchers.Main) {
//                        progressBar.visibility = View.GONE
//                        productAdapter.updateData(favoriteProducts)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("FetchError", "Exception: $e")
//                withContext(Dispatchers.Main) {
//                    progressBar.visibility = View.GONE
//                }
//            }
//        }
//    }
//
//    // Handle the result from DetailProductActivity
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE_DETAIL_PRODUCT && resultCode == Activity.RESULT_OK) {
//            val isFavoriteChanged = data?.getBooleanExtra("isFavoriteChanged", false) ?: false
//            if (isFavoriteChanged) {
//                val productId = data?.getStringExtra("productId")
//                // Fetch updated list of favorite products after changes
//                lifecycleScope.launch {
//                    userPreferences.getAccessToken().collect { token ->
//                        if (token.isNotEmpty()) {
//                            fetchProducts("Bearer $token")
//                        }
//                    }
//                }
//            }
//        }
//    }
//}






//class FavoritFragment : Fragment() {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var progressBar: ProgressBar
//    private lateinit var userPreferences: UserPreferences
//    private lateinit var eventStorage: ProductStorage
//    private lateinit var productAdapter: ProductAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_favorit, container, false)
//        recyclerView = view.findViewById(R.id.rv_product)
//        progressBar = view.findViewById(R.id.progress_bar)
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Inisialisasi ProductStorage
//        eventStorage = ProductStorage(requireContext())
//
//        // Mengambil AccessToken
//        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
//        lifecycleScope.launch {
//            userPreferences.getAccessToken().collect { token ->
//                if (token.isNotEmpty()) {
//                    Log.e("accessToken fragmentproduct", "Access token : $token")
//                    fetchProducts("Bearer $token")
//                } else {
//                    Log.e("accessToken", "Access token tidak ditemukan.")
//                }
//            }
//        }
//    }
//
//    // Memuat produk dan menampilkan hanya produk favorit
//    private fun fetchProducts(token: String) {
//        progressBar.visibility = View.VISIBLE
//
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                val apiService = ApiProductConfig.getApiService()
//                val response = apiService.getProducts(token)
//
//                if (response.isSuccessful && response.body() != null) {
//                    val products = response.body()!!
//
//                    // Hanya pilih produk yang id-nya ada di SharedPreferences
//                    val favoriteProducts = products.filter { product ->
//                        eventStorage.isProductStored(product.id.toString()) // Cek apakah ID produk ada di SharedPreferences
//                    }
//
//                    withContext(Dispatchers.Main) {
//                        progressBar.visibility = View.GONE
//                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//                        // Set adapter dengan handling klik
//                        productAdapter = ProductAdapter(favoriteProducts) { product ->
//                            val intent = Intent(requireContext(), DetailProductActivity::class.java)
//                            intent.putExtra("product", product) // Pastikan `Product` Parcelable
//                            startActivityForResult(intent, 100) // Menunggu hasil dari DetailProductActivity
//                        }
//                        recyclerView.adapter = productAdapter
//                    }
//                } else {
//                    Log.e("FetchError", "Error: ${response.message()}")
//                    withContext(Dispatchers.Main) {
//                        progressBar.visibility = View.GONE
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("FetchError", "Exception: $e")
//                withContext(Dispatchers.Main) {
//                    progressBar.visibility = View.GONE
//                }
//            }
//        }
//    }
//
//    // Menangani hasil kembali dari DetailProductActivity
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
//            // Memuat ulang produk favorit setelah kembali
//            fetchProducts("Bearer ${userPreferences.getAccessToken().toString()}")
//        }
//    }
//}









//class FavoritFragment : Fragment() {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var progressBar: ProgressBar
//    private lateinit var userPreferences: UserPreferences
//    private lateinit var eventStorage: ProductStorage
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_favorit, container, false)
//        recyclerView = view.findViewById(R.id.rv_product)
//        progressBar = view.findViewById(R.id.progress_bar)
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Inisialisasi EventStorage
//        eventStorage = ProductStorage(requireContext())
//
//        // Mengambil AccessToken
//        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
//        lifecycleScope.launch {
//            userPreferences.getAccessToken().collect { token ->
//                if (token.isNotEmpty()) {
//                    Log.e("accessToken fragmentproduct", "Access token : $token")
//                    fetchProducts("Bearer $token")
//                } else {
//                    Log.e("accessToken", "Access token tidak ditemukan.")
//                }
//            }
//        }
//    }
//
//    private fun fetchProducts(token: String) {
//        progressBar.visibility = View.VISIBLE
//
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                val apiService = ApiProductConfig.getApiService()
//                val response = apiService.getProducts(token)
//
//                if (response.isSuccessful && response.body() != null) {
//                    val products = response.body()!!
//
//                    // Hanya pilih produk yang id-nya ada di SharedPreferences
//                    val favoriteProducts = products.filter { product ->
//                        eventStorage.isProductStored(product.id.toString()) // Cek apakah ID produk ada di SharedPreferences
//                    }
//
//                    withContext(Dispatchers.Main) {
//                        progressBar.visibility = View.GONE
//                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//                        // Set adapter dengan handling klik
//                        recyclerView.adapter = ProductAdapter(favoriteProducts) { product ->
//                            val intent = Intent(requireContext(), DetailProductActivity::class.java)
//                            intent.putExtra("product", product) // Pastikan `Product` Parcelable
//                            startActivity(intent)
//                        }
//                    }
//                } else {
//                    Log.e("FetchError", "Error: ${response.message()}")
//                    withContext(Dispatchers.Main) {
//                        progressBar.visibility = View.GONE
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("FetchError", "Exception: $e")
//                withContext(Dispatchers.Main) {
//                    progressBar.visibility = View.GONE
//                }
//            }
//        }
//    }
//}




//class FavoritFragment : Fragment() {
//
//    companion object {
//        fun newInstance() = FavoritFragment()
//    }
//
//    private val viewModel: FavoritViewModel by viewModels()
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
//        return inflater.inflate(R.layout.fragment_favorit, container, false)
//    }
//}
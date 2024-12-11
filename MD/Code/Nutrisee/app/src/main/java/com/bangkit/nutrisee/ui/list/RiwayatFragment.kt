package com.bangkit.nutrisee.ui.list

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.data.product.ApiProductConfig
import com.bangkit.nutrisee.data.user.UserPreferences
import com.bangkit.nutrisee.data.user.userPreferencesDataStore
import com.bangkit.nutrisee.ui.detailactivity.DetailProductActivity
import com.bangkit.nutrisee.ui.search.ProductAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RiwayatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_riwayat, container, false)
        recyclerView = view.findViewById(R.id.rv_product)
        progressBar = view.findViewById(R.id.progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengambil AccessToken
        userPreferences = UserPreferences.getInstance(requireContext().userPreferencesDataStore)
        lifecycleScope.launch {
            userPreferences.getAccessToken().collect { token ->
                if (token.isNotEmpty()) {
                    Log.e("accessToken RiwayatFragment", "Access token : $token")
                    fetchProductsHistory("Bearer $token")
                } else {
                    Log.e("accessToken", "Access token tidak ditemukan.")
                }
            }
        }
    }

    private fun fetchProductsHistory(token: String) {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiService = ApiProductConfig.getApiService()
                val response = apiService.getProductsHistory(token)

                if (response.isSuccessful && response.body() != null) {
                    val productHistory = response.body()!!
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())

                        // Set adapter dengan handling klik
                        recyclerView.adapter = ProductAdapter(productHistory) { product ->
                            val intent = Intent(requireContext(), DetailProductActivity::class.java)
                            intent.putExtra("product", product) // Pastikan `Product` Parcelable
                            startActivity(intent)
                        }
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
}
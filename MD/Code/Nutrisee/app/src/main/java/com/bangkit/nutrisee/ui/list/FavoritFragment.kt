package com.bangkit.nutrisee.ui.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.ui.detailactivity.DetailProductActivity
import com.bangkit.nutrisee.ui.search.ProductAdapter

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

        viewModel = ViewModelProvider(this).get(FavoritViewModel::class.java)

        productAdapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(requireContext(), DetailProductActivity::class.java)
            intent.putExtra("product", product)
            startActivityForResult(intent, REQUEST_CODE_DETAIL_PRODUCT)
        }

        recyclerView.adapter = productAdapter

        viewModel.products.observe(viewLifecycleOwner, Observer { products ->
            productAdapter.updateData(products)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchFavoriteProducts()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAIL_PRODUCT && resultCode == Activity.RESULT_OK) {
            val isFavoriteChanged = data?.getBooleanExtra("isFavoriteChanged", false) ?: false
            if (isFavoriteChanged) {
                viewModel.fetchFavoriteProducts()
            }
        }
    }
}
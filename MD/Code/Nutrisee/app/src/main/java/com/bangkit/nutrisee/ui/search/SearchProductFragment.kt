package com.bangkit.nutrisee.ui.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.nutrisee.R

class SearchProductFragment : Fragment() {

    companion object {
        fun newInstance() = SearchProductFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search_product, container, false)
    }
}











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
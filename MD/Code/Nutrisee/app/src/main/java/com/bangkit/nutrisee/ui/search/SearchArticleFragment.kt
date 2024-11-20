package com.bangkit.nutrisee.ui.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.databinding.FragmentSearchArticleBinding
import com.bangkit.nutrisee.ui.list.ListViewModel

class SearchArticleFragment : Fragment() {

    private var _binding: FragmentSearchArticleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(SearchArticleViewModel::class.java)

        _binding = FragmentSearchArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSearchArticle
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.bangkit.nutrisee.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayoutMediator

class SearchFragment : Fragment(){

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var SectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SectionsPagerAdapter = SectionsPagerAdapter(this)
        with(binding) {
            viewPager.adapter = SectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                val tabText = when (position) {
                    0 -> "Products"
                    1 -> "Articles"
                    else -> null
                }
                tab.customView = createCustomTabView(tabText)
            }.attach()
        }
    }

    // Fungsi untuk membuat custom view tab
    private fun createCustomTabView(text: String?): View {
        val textView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null) as TextView
        textView.text = text
        textView.typeface = ResourcesCompat.getFont(requireContext(), R.font.visbybold)
        return textView
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
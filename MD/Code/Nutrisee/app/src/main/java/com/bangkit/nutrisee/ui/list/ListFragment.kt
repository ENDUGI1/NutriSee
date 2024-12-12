package com.bangkit.nutrisee.ui.list


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayoutMediator

class ListFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var ListPagerAdapter: ListPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ListPagerAdapter = ListPagerAdapter(this)
        with(binding){
            viewPager.adapter = ListPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                val tabText = when (position) {
                    0 -> "History"
                    1 -> "Favorite"
                    else -> null
                }
                tab.customView = createCustomTabView(tabText)
            }.attach()

        }
    }

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
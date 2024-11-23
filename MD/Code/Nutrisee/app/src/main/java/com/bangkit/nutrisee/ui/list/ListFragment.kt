package com.bangkit.nutrisee.ui.list


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bangkit.nutrisee.databinding.FragmentListBinding
import com.bangkit.nutrisee.databinding.FragmentSearchBinding
import com.bangkit.nutrisee.ui.search.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class ListFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var ListPagerAdapter: ListPagerAdapter

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
        ListPagerAdapter = ListPagerAdapter(this)
        with(binding){
            viewPager.adapter = ListPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Riwayat"
                    1 -> "Favorit"
                    else -> null
                }
            }.attach()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

//class ListFragment : Fragment() {
//
//    private var _binding: FragmentListBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(ListViewModel::class.java)
//
//        _binding = FragmentListBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textList
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
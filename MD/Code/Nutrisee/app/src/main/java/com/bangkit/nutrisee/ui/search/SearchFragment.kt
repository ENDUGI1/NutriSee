package com.bangkit.nutrisee.ui.search

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.nutrisee.R
import com.bangkit.nutrisee.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayout
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
        with(binding){
            viewPager.adapter = SectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Articles"
                    1 -> "Products"
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
//    companion object {
//        @StringRes
//        private val TAB_TITLES = intArrayOf(
//            R.string.tab_text_search_article,
//            R.string.tab_text_search_product
//        )
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//
//        val sectionsPagerAdapter = SectionsPagerAdapter(this)
//        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
//        viewPager.adapter = sectionsPagerAdapter
//        val tabs: TabLayout = findViewById(R.id.tabs)
//        TabLayoutMediator(tabs, viewPager) { tab, position ->
//            tab.text = resources.getString(TAB_TITLES[position])
//        }.attach()
//        supportActionBar?.elevation = 0f
//    }


//class SearchFragment : Fragment() {
//
//    private lateinit var tabLayout: TabLayout
//    private lateinit var viewPager: ViewPager2
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val view = inflater.inflate(R.layout.fragment_search, container, false)
//
//        // Inisialisasi TabLayout dan ViewPager
//        tabLayout = view.findViewById(R.id.tabLayout)
//        viewPager = view.findViewById(R.id.viewPager)
//
//        // Set adapter untuk ViewPager2
//        val adapter = SectionsPagerAdapter(this)
//        viewPager.adapter = adapter
//
//        // Hubungkan TabLayout dengan ViewPager2
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = when (position) {
//                0 -> "Articles"
//                1 -> "Products"
//                else -> null
//            }
//        }.attach()
//
//        return view
//    }
//}
















//// tempalte

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [SearchFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class SearchFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_search, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment SearchFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            SearchFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}
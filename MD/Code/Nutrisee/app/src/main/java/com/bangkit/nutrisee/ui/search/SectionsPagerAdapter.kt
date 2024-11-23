package com.bangkit.nutrisee.ui.search

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = SearchArticleFragment()
            1 -> fragment = SearchProductFragment()
        }
        return fragment as Fragment
    }
}








//class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
//
//    override fun getItemCount(): Int {
//        return 2 // Jumlah tab
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        return when (position) {
//            0 -> SearchArticleFragment.newInstance()
//            1 -> SearchProductFragment.newInstance()
//            else -> throw IllegalStateException("Invalid position $position")
//        }
//    }
//}
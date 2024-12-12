package com.bangkit.nutrisee.ui.list

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ListPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = RiwayatFragment()
            1 -> fragment = FavoritFragment()
        }
        return fragment as Fragment
    }
}
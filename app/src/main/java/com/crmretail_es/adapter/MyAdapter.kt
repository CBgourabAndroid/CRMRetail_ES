package com.crmretail_es.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.crmretail_es.fragment.*

class MyAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return FoodingFragment()
            }
            1 -> {
                return LodgingFragment()
            }
            2 -> {
                // val movieFragment = MovieFragment()
                return PublicTrnsFragment()
            }
            3 -> {
                // val movieFragment = MovieFragment()
                return OtherFragment()
            }
            else -> return null!!
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}
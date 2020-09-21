package com.crmretail_es.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.crmretail_es.fragment.*

class TravellAdapter (private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return PrivateTrnsFragment()
            }
            1 -> {
                return PublicTrnsFragment()
            }

            else -> return null!!
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}
package com.crmretail_es.adapter

import android.content.Context
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.crmretail_es.fragment.AreaFragment
import com.crmretail_es.modelClass.AreaInfo
import com.google.android.material.tabs.TabLayout
import java.util.ArrayList

class CustomPagerAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int,
    var tablayout: TabLayout,
    var tablist: ArrayList<AreaInfo>,
    var toolbar: Toolbar,
    var searchToolbar: Toolbar
) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {


        return AreaFragment(tablayout, tablist,toolbar,searchToolbar)
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}
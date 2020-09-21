package com.crmretail_es.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.crmretail_es.R
import com.crmretail_es.adapter.TravellAdapter
import com.google.android.material.tabs.TabLayout

class TravellFragment : Fragment()  {

    lateinit var root: View
    lateinit var tablayout: TabLayout

    var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.travell_fragment, container, false)
        //setHasOptionsMenu(true)

        inti(root)
        return root
    }

    private fun inti(root: View) {


        tablayout=root.findViewById(R.id.tab_layout)
        viewPager =root. findViewById<ViewPager>(R.id.viewPager)

        tablayout!!.addTab(tablayout!!.newTab().setText("Private"))
        tablayout!!.addTab(tablayout!!.newTab().setText("Public"))

        tablayout.getTabAt(0)!!.select()
        tablayout.tabMode = TabLayout.MODE_FIXED

        tablayout.setTabTextColors(
            ContextCompat.getColorStateList(requireContext(),R.color.white)
        )
        tablayout.setSelectedTabIndicatorColor(resources.getColor(R.color.white))
        tablayout.setTabTextColors(resources.getColor(R.color.white),resources.getColor(R.color.grey)
        )


        val adapter = TravellAdapter(requireContext(), childFragmentManager, tablayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))

        tablayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}
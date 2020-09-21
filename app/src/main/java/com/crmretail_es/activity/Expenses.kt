package com.crmretail_es.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.crmretail_es.R
import com.crmretail_es.adapter.MyAdapter
import com.google.android.material.tabs.TabLayout

class Expenses : AppCompatActivity() {

    lateinit var tablayout: TabLayout

    var viewPager: ViewPager? = null

    lateinit var toolbar: Toolbar
    var names= arrayOf("Fooding"
        ,"Lodging",
        "Travelling","Other"
                )
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.expense_activity)
        toolbar = findViewById(R.id.toolbar) as Toolbar

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        verifyStoragePermissions(this)
        inti()
    }

    fun verifyStoragePermissions(activity: Activity) {
        // Check if we have write permission
        val permission: Int = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
        else{
           // Toast.makeText(this,"Permission Required",Toast.LENGTH_SHORT).show()
        }
    }

    private fun inti() {

        tablayout=findViewById(R.id.tab_layout)
        viewPager = findViewById<ViewPager>(R.id.viewPager)


        tablayout!!.addTab(tablayout!!.newTab().setText("Fooding"))
        tablayout!!.addTab(tablayout!!.newTab().setText("Lodging"))
        tablayout!!.addTab(tablayout!!.newTab().setText("Travelling"))
        tablayout!!.addTab(tablayout!!.newTab().setText("Others"))

        tablayout.getTabAt(0)!!.select()
        tablayout.tabMode = TabLayout.MODE_AUTO

        tablayout.setTabTextColors(
            ContextCompat.getColorStateList(this,R.color.white)
        )
        tablayout.setSelectedTabIndicatorColor(resources.getColor(R.color.white))
        tablayout.setTabTextColors(resources.getColor(R.color.white),resources.getColor(R.color.grey)
        )


        val adapter = MyAdapter(this, supportFragmentManager, tablayout!!.tabCount)
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
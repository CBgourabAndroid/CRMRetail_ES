package com.crmretail_es.activity

import android.app.Activity
import android.content.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.crmretail_es.R
import com.crmretail_es.adapter.CustomPagerAdapter
import com.crmretail_es.modelClass.AreaInfo
import com.crmretail_es.shared.UserShared
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OutletVisitActivity : AppCompatActivity() {

    lateinit var psh:UserShared
    lateinit var tablist: ArrayList<AreaInfo>

    lateinit var tablayout: TabLayout

    var viewPager: ViewPager? = null

    lateinit var toolbar: Toolbar
    lateinit var searchtollbar: Toolbar
    lateinit var item_search: MenuItem
    lateinit var search_menu: Menu
    lateinit var searchView: SearchView

    private var googleApiClient: GoogleApiClient? = null
    val REQUEST_CHECK_SETTINGS = 0x1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.outlet_visit_activity)
        toolbar = findViewById(R.id.toolbar) as Toolbar

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }
        psh=UserShared(this)


        LocalBroadcastManager.getInstance(this).registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    // String latitude = intent.getStringExtra("Latitude");
                    // String longitude = intent.getStringExtra("Longitude");
                    val provider = intent.getStringExtra("GpsStatus")
                    if (provider != null) {
                        Log.v("prooooo", provider)
                        if (provider == "Gps Disabled") {
                            myCheck()
                        }

                        // mMsgView.setText(getString(R.string.msg_location_service_started) + "\n Latitude : " + latitude + "\n Longitude: " + longitude);
                    }
                }
            }, IntentFilter("Hellooooo")
        )

        loadData()

        searchtollbar = findViewById(R.id.searchtoolbar) as Toolbar
        tablayout=findViewById(R.id.tab_layout)
        viewPager = findViewById<ViewPager>(R.id.viewPager)

        for (i in tablist.indices) {

            tablayout.addTab(tablayout.newTab().setText(tablist.get(i).zoneName), true)

        }

        tablayout.getTabAt(0)!!.select()
        tablayout.tabMode = TabLayout.MODE_AUTO

        tablayout.setTabTextColors(
            ContextCompat.getColorStateList(this,R.color.white)
        )
        tablayout.setSelectedTabIndicatorColor(resources.getColor(R.color.white))
        tablayout.setTabTextColors(resources.getColor(R.color.white),resources.getColor(R.color.grey)
        )





        val adapter = CustomPagerAdapter(this, supportFragmentManager, tablayout!!.tabCount,tablayout,tablist,toolbar,searchtollbar)
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

    fun loadData(){

        val gson = Gson()
        val json =psh.areaData
        val turnsType = object : TypeToken<ArrayList<AreaInfo>>() {}.type
        tablist=gson.fromJson(json,turnsType)


    }


    private fun myCheck() {


        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build()
            googleApiClient!!.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 30 * 1000.toLong()
            locationRequest.fastestInterval = 5 * 1000.toLong()
            val builder =
                LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)
            builder.setNeedBle(true)
            val result: PendingResult<LocationSettingsResult> =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
            result.setResultCallback(object : ResultCallback<LocationSettingsResult?> {
                override fun onResult(result: LocationSettingsResult) {
                    val status: Status = result.getStatus()
                    val state: LocationSettingsStates = result.getLocationSettingsStates()
                    when (status.statusCode) {
                        LocationSettingsStatusCodes.SUCCESS -> {
                            // Toast.makeText(getApplicationContext(), "GPS Is ON", Toast.LENGTH_SHORT).show()

                        }
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            status.startResolutionForResult(
                                this@OutletVisitActivity,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (e: IntentSender.SendIntentException) {
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                    }
                }
            })
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                //  Toast.makeText(applicationContext, "GPS enabled", Toast.LENGTH_LONG).show()

            } else {

                finishAffinity()
                finish()
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

}
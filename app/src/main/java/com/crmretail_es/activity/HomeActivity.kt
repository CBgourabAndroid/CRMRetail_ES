package com.crmretail_es.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.MainActivity
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.SplashScreen
import com.crmretail_es.adapter.CheckListAdapter
import com.crmretail_es.adapter.MainCatAdapter
import com.crmretail_es.modelClass.CheckList
import com.crmretail_es.modelClass.GeneralResponce
import com.crmretail_es.shared.Updatedlatlong
import com.crmretail_es.shared.UserShared
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : MainActivity() {

    lateinit var recyclerViewCategory: RecyclerView
    lateinit var categoryAdapter: MainCatAdapter
    var names= arrayOf("Visit"
        ,"Register",
        "Brand Track","Meeting Management","Duty Planner","Stop Duty")

    var images= arrayOf(R.drawable.visit
        ,R.drawable.register,
        R.drawable.brandtrack,R.drawable.eventman,R.drawable.dutyplanner,R.drawable.stopduty)
    lateinit var progressDialog : ProgressDialog
    lateinit var psh: UserShared
    lateinit var latPsh:Updatedlatlong
    lateinit var prefss: SharedPreferences
    lateinit var  prefs: SharedPreferences
    private var LATSTR=""
    private var LONGSTR=""
    lateinit var adapter:CheckListAdapter
    lateinit var dataList:ArrayList<CheckList>

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.fragment_home, container)

        progressDialog =ProgressDialog(this)
        psh=UserShared(this)
        latPsh=Updatedlatlong(this)
        prefss = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)

        //openOnDuty()

        recyclerViewCategory=findViewById(R.id.category_view)
        categoryAdapter = MainCatAdapter()

       // val layoutManager = AutoFitGridLayoutManager(this, 500)
        //recyclerViewCategory.setLayoutManager(layoutManager)
        recyclerViewCategory.layoutManager = GridLayoutManager(applicationContext,2) as RecyclerView.LayoutManager?
        recyclerViewCategory.adapter = categoryAdapter
        categoryAdapter.setDataListItems(this@HomeActivity,
            names,images
        )


        if (psh.dutyStatus){

            start_duty.visibility=View.GONE
            startLocationService()
            val nav_Menu: Menu = navView.getMenu()
            nav_Menu.findItem(R.id.nav_logout).setVisible(false)
        }
        else{
            start_duty.visibility=View.VISIBLE
            stopLocationService()
            val nav_Menu: Menu = navView.getMenu()
            nav_Menu.findItem(R.id.nav_logout).setVisible(true)
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

        start_duty.setOnClickListener {

            showDialogS(LATSTR,LONGSTR)
        }




    }

    fun loadData(){
        val gson = Gson()
        val json =psh.checkList
        val turnsType = object : TypeToken<ArrayList<CheckList>>() {}.type
        dataList=gson.fromJson(json,turnsType)


    }

    private fun showDialogS(latstr: String, longstr: String) {

        val viewGroup =findViewById<ViewGroup>(android.R.id.content)
        val dialogView = layoutInflater.inflate(R.layout.my_dialog,viewGroup,false)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(dialogView)

        val sdf = SimpleDateFormat("EEE, dd MMM yyyy hh:mm")
        val currentDate = sdf.format(Date())

        val homeClick=dialogView.findViewById<TextView>(R.id.startDutyClick)
        val addprivate=dialogView.findViewById<TextView>(R.id.addPrivateDetails)
        val txt_tv=dialogView.findViewById<TextView>(R.id.sbd_text_title)
        val radGrp=dialogView.findViewById<RadioGroup>(R.id.rbgrp)
        val checkListLayout=dialogView.findViewById<LinearLayout>(R.id.checkListLay)

        val recycler_view: RecyclerView =
            dialogView.findViewById(R.id.my_recycler_view) as RecyclerView
        val alert = dialogBuilder.create()
        alert.show()
        dataList=ArrayList()
        loadData()

        checkListLayout.visibility=View.VISIBLE
        recycler_view.setLayoutManager(GridLayoutManager(this, 2))
        recycler_view.setItemAnimator(DefaultItemAnimator())

        adapter = CheckListAdapter(this, dataList
        )
        recycler_view.setAdapter(adapter)

        txt_tv.setText(currentDate)
        getLastLocation()

        radGrp.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(arg0: RadioGroup?, id: Int) {
                when (id) {
                    R.id.radioButton -> {
                        addprivate.visibility=View.GONE
                        homeClick.visibility=View.VISIBLE



                    }
                    R.id.radioButton2 -> {

                        addprivate.visibility=View.VISIBLE
                        homeClick.visibility=View.GONE

                    }
                }
            }
        })

        addprivate.setOnClickListener {

            if (CheckListAdapter.xyz.size != 0&&dataList.size==CheckListAdapter.xyz.size) {
                //      replaceFragment(new NearByMapFragment(tabLayout,Latitude,Longtitude,xyz));
                val intent = Intent(this@HomeActivity, PrivateTransActivity::class.java)
                startActivityForResult(intent, 123)
                alert.dismiss()
            } else {
                Toast.makeText(this,"Please provide checklist details!!",Toast.LENGTH_SHORT).show()
            }
        }

        homeClick.setOnClickListener {

         //   start_duty.visibility=View.GONE

            if (CheckListAdapter.xyz.size != 0&&dataList.size==CheckListAdapter.xyz.size) {
                //      replaceFragment(new NearByMapFragment(tabLayout,Latitude,Longtitude,xyz));
                if(!PostInterface.isConnected(applicationContext)){

                    Toast.makeText(applicationContext, applicationContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
                else{

                    progressDialog.setMessage("Please wait ...")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    callStartDuty("Public")

                }
                alert.dismiss()
            } else {
                Toast.makeText(this,"Please provide checklist details!!",Toast.LENGTH_SHORT).show()
            }



           // startActivity(Intent(this@BookAppointment, HomeActivity::class.java))
          //  finish()
        }


    }


    fun getLocation() {

        var locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?

        var locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                var latitute = location!!.latitude
                var longitute = location!!.longitude

                Log.i("test", "Latitute: $latitute ; Longitute: $longitute")
                LATSTR=location!!.latitude.toString()
                LONGSTR=location!!.longitude.toString()
                //Toast.makeText(applicationContext, LATSTR+"\n"+LONGSTR, Toast.LENGTH_SHORT).show()

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }

        }

        try {
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (ex:SecurityException) {
            Toast.makeText(this, "Fehler bei der Erfassung!", Toast.LENGTH_SHORT).show()
        }
    }

    /*private fun openOnDuty() {

        val sdf = SimpleDateFormat("EEE, dd MMM yyyy hh:mm")
        val currentDate = sdf.format(Date())



        TTFancyGifDialog.Builder(this@HomeActivity)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(currentDate)
            .setPositiveBtnText("Start Duty")
            .setPositiveBtnBackground("#22b573")
            .setNegativeBtnText("Stop Duty")
            .setNegativeBtnBackground("#c1272d")
            .setGifResource(R.drawable.resize) //pass your gif, png or jpg
            .isCancellable(true)
            .OnPositiveClicked {
                //Toast.makeText(this@HomeActivity, "Start Duty", Toast.LENGTH_SHORT).show()


            }
            .OnNegativeClicked {
                Toast.makeText(this@HomeActivity, "Stop Duty", Toast.LENGTH_SHORT).show()
            }
            .build()

    }*/


    private fun callStartDuty( dutyType:String) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.startDuty("Bearer "+psh.accessToken,psh.id,LATSTR,LONGSTR,"2","").enqueue(object :
            Callback<GeneralResponce> {
            override fun onResponse(call: Call<GeneralResponce>, response: Response<GeneralResponce>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        start_duty.visibility=View.GONE

                        val editor = prefss.edit()
                        editor.putBoolean(getString(R.string.shared_duty_status), true)
                        editor.putString(getString(R.string.shared_duty_type), dutyType)
                        editor.commit()
                        val nav_Menu: Menu = navView.getMenu()
                        nav_Menu.findItem(R.id.nav_logout).setVisible(false)
                        startLocationService()


                    } else {
                        Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Login Expires", Toast.LENGTH_SHORT).show()

                    val myPrefs = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)
                    val editor = myPrefs.edit()
                    editor.clear()
                    editor.apply()


                    val intent = Intent(this@HomeActivity, SplashScreen::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)

                }
            }

            override fun onFailure(call: Call<GeneralResponce>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }

   /* fun startLocationService() {
        latPsh = Updatedlatlong(applicationContext)
        Log.i("GPSTRACKER", "called startLocationService from application")
        startService(Intent(this, LocationUpdate::class.java))
        prefs = getSharedPreferences("LATLONG_SHARED_PREF", Context.MODE_PRIVATE)
    }*/


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        LATSTR = location.latitude.toString()
                        LONGSTR = location.longitude.toString()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            LATSTR= mLastLocation.latitude.toString()
            LONGSTR= mLastLocation.longitude.toString()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==123){

            val message = data!!.getStringExtra("isOk")

            if (!message.equals("")){

                if(!PostInterface.isConnected(applicationContext)){

                    Toast.makeText(applicationContext, applicationContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
                else{

                    progressDialog.setMessage("Please wait ...")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    callStartDuty("Private")


                }
            }
            else{
                Toast.makeText(applicationContext, "Fill Details To Start Duty!!", Toast.LENGTH_SHORT).show()
            }


        }
    }







}
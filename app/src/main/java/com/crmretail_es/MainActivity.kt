package com.crmretail_es

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.ui.AppBarConfiguration
import com.crmretail_es.activity.*
import com.crmretail_es.modelClass.GeneralResponce
import com.crmretail_es.shared.Updatedlatlong
import com.crmretail_es.shared.UserShared
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

open class MainActivity : AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener{

    private lateinit var appBarConfiguration: AppBarConfiguration

    var pshlat: Updatedlatlong? = null
    var prefsll: SharedPreferences? = null

    private var googleApiClient: GoogleApiClient? = null
    val REQUEST_CHECK_SETTINGS = 0x1
    lateinit var pshs:UserShared
    lateinit var progressDialogs : ProgressDialog
    lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbars)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

       // callAutoLogout()



        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        progressDialogs =ProgressDialog(this)
        pshs=UserShared(this)
        pshlat = Updatedlatlong(this)
        //val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       /* appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/
        navView.setNavigationItemSelectedListener(this)
       // startService(Intent(baseContext, MyService::class.java))

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





    }

  /*  override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

   /* override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.


        when (item.itemId) {


            R.id.nav_home->{

                val i1 = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(i1)

            }

            R.id.nav_profile->{

                val i1 = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(i1)



            }

            R.id.nav_leave->{

                val i1 = Intent(this@MainActivity, LeaveApplication::class.java)
                startActivity(i1)



            }

            R.id.nav_holoy->{

                val i1 = Intent(this@MainActivity, HolyDaysActivity::class.java)
                startActivity(i1)



            }
            R.id.nav_prejob->{

                val i1 = Intent(this@MainActivity, PJPMainActivity::class.java)
                startActivity(i1)
            }

            R.id.nav_gallery->{

                val i1 = Intent(this@MainActivity, AttendanceActivity::class.java)
                startActivity(i1)



            }

            R.id.nav_event->{

                val i1 = Intent(this@MainActivity, EventAddActivity::class.java)
                startActivity(i1)



            }

            R.id.nav_send->{

                val i1 = Intent(this@MainActivity, Expenses::class.java)
                startActivity(i1)



            }
            R.id.nav_offDuty->{

                val i1 = Intent(this@MainActivity, CustomerListActivity::class.java)
                startActivity(i1)
            }



            R.id.nav_logout -> {

                logout()

            }


        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



    /* override fun onSupportNavigateUp(): Boolean {
         val navController = findNavController(R.id.nav_host_fragment)
         return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
     }*/

    private fun logout() {


        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle("Alert")
        dialogBuilder.setMessage("Do you want to logout?")
        dialogBuilder.setPositiveButton("OK", { dialog, whichButton ->

            if(!PostInterface.isConnected(this@MainActivity)){

                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
            else{

                progressDialogs.setMessage("Please wait ...")
                progressDialogs.setCancelable(false)
                progressDialogs.show()
                logoutApi(dialog)

            }


            // dialog.dismiss()
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->



            dialog.dismiss()
        })
        val b = dialogBuilder.create()
        b.show()





    }

    private fun logoutApi(dialog: DialogInterface) {


        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.logoutCall("Bearer "+pshs.accessToken,pshs.id,pshlat!!.userUpdatedLatitude,pshlat!!.userUpdatedLongitude).enqueue(object :
            Callback<GeneralResponce> {
            override fun onResponse(call: Call<GeneralResponce>, response: Response<GeneralResponce>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialogs.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {

                        Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        val myPrefs = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)
                        val editor = myPrefs.edit()
                        editor.clear()
                        editor.apply()


                        val intent = Intent(this@MainActivity, SplashScreen::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        dialog!!.dismiss()
                        stopLocationService()


                    } else {
                        Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        progressDialogs.dismiss()
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialogs.dismiss()
                    Toast.makeText(applicationContext, "Login Expires", Toast.LENGTH_SHORT).show()

                    val myPrefs = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)
                    val editor = myPrefs.edit()
                    editor.clear()
                    editor.apply()


                    val intent = Intent(this@MainActivity, SplashScreen::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    dialog!!.dismiss()
                    stopLocationService()
                }
            }

            override fun onFailure(call: Call<GeneralResponce>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialogs.dismiss()
            }
        })




    }


    override fun onBackPressed() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Do you want to close this application ?")
            .setCancelable(false)
            .setPositiveButton("Proceed", DialogInterface.OnClickListener {

                    dialog, _ -> finishAffinity()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {


                    dialog, _ -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Alert")
        alert.show()
    }

    open fun stopLocationService() {
        pshlat = Updatedlatlong(applicationContext)
        Log.i("GPSTRACKER", "called stopLocationService from application")
        stopService(Intent(this, LocationUpdate::class.java))
        prefsll = getSharedPreferences("LATLONG_SHARED_PREF", Context.MODE_PRIVATE)
    }

    open fun startLocationService() {
        pshlat = Updatedlatlong(applicationContext)
        Log.i("GPSTRACKER", "called startLocationService from application")
        startService(Intent(this, LocationUpdate::class.java))
        prefsll = getSharedPreferences("LATLONG_SHARED_PREF", Context.MODE_PRIVATE)
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
                                this@MainActivity,
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


    open fun callAutoLogout() {
        val alaramIntent =
            Intent(this, BootCompletedIntentReceiver::class.java)
        alaramIntent.action = "LogOutAction"
        Log.e("MethodCall", "AutoLogOutCall")
        alaramIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 0
        val alarmManager =
            this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.e("Logout", "Auto Logout set at..!" + calendar.time)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }








}

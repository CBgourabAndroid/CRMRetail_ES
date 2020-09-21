package com.crmretail_es

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.activity.LoginActivity
import com.crmretail_es.activity.HomeActivity
import com.crmretail_es.shared.PrefManager
import com.crmretail_es.shared.UserShared
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class SplashScreen : AppCompatActivity() {

    val REQUEST_CODE_PERMISSIONS = 101

    private var googleApiClient: GoogleApiClient? = null
    val REQUEST_CHECK_SETTINGS = 0x1
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3500 //3 seconds
    lateinit var prefManager: PrefManager
    var locationManager: LocationManager? = null
    var GpsStatus = false
    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            // val intent = Intent(applicationContext, MainActivity::class.java)
            //startActivity(intent)
            if (prefManager.isFirstTimeLaunch) {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P){

                    requestLocationPermission()
                }
                else{
                    requestPermission()
                }


              //

            }
            else{

                if (UserShared(this@SplashScreen).getLoggedInStatus()) {


                    startActivity(Intent(this@SplashScreen, HomeActivity::class.java))
                    finish()
                } else {

                    startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                    finish()
                }


            }

        }


    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.splash_screen)

        prefManager = PrefManager(this)


        /*  //4second splash time
          Handler().postDelayed({
              //start main activity
              startActivity(Intent(this@SplashScreen, MainActivity::class.java))
              //finish this activity
              finish()
          },4000)*/


        CheckGpsStatus()




    }




    fun CheckGpsStatus() {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        assert(locationManager != null)
        GpsStatus = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (GpsStatus == true) {
           // textview.setText("GPS Is Enabled")
            mDelayHandler = Handler()

            //Navigate with delay
            mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        } else {
          //  Toast.makeText(getApplicationContext(), "GPS Is Disabled", Toast.LENGTH_SHORT).show()
           // textview.setText("GPS Is Disabled")
          //  requestPermission()
            //finish()
            //requestPermission()

            myCheck()

        }


    }

    private fun myCheck() {


        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this@SplashScreen)
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
            result.setResultCallback(object : ResultCallback<LocationSettingsResult?>{
                override fun onResult(result: LocationSettingsResult) {
                    val status: Status = result.getStatus()
                    val state: LocationSettingsStates = result.getLocationSettingsStates()
                    when (status.statusCode) {
                        LocationSettingsStatusCodes.SUCCESS -> {
                            // Toast.makeText(getApplicationContext(), "GPS Is ON", Toast.LENGTH_SHORT).show()

                        }
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            status.startResolutionForResult(
                                this@SplashScreen,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (e: SendIntentException) {
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                    }
                }
            })
        }
    }


    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }


    private fun requestPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION


            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        // ScanFinction();
                        if (UserShared(this@SplashScreen).getLoggedInStatus()) {


                            startActivity(Intent(this@SplashScreen, HomeActivity::class.java))
                            finish()
                        } else {

                            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                            finish()
                        }
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        // show alert dialog navigating to Settings
                        //Toast.makeText(getApplicationContext(), "All permissions are Denied!", Toast.LENGTH_SHORT).show();
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener(object : PermissionRequestErrorListener {
                override fun onError(error: DexterError) {
                    Toast.makeText(applicationContext, "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show()
                }
            })
            .onSameThread()
            .check()
    }

    private fun showSettingsDialog() {



        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle("Need Permissions")
        dialogBuilder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        dialogBuilder.setPositiveButton("GOTO SETTINGS", { dialog, whichButton ->


            dialog.dismiss()
            openSettings()
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->



            dialog.dismiss()
        })
        val b = dialogBuilder.create()
        b.show()
    }


    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

  /*  override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }*/

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
               // Toast.makeText(applicationContext, "GPS enabled", Toast.LENGTH_LONG).show()
                mDelayHandler = Handler()

                //Navigate with delay
                mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

            } else {
                finish()
            }
        }
    }


    private fun requestLocationPermission() {
        val foreground = ActivityCompat.checkSelfPermission(
            this,

            Manifest.permission.ACCESS_COARSE_LOCATION
        ) === PackageManager.PERMISSION_GRANTED
        if (foreground) {
            val background = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) === PackageManager.PERMISSION_GRANTED
            if (background) {
                handleLocationUpdates()


            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    REQUEST_CODE_PERMISSIONS
                )
            }
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ), REQUEST_CODE_PERMISSIONS
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            var foreground = false
            var background = false
            for (i in permissions.indices) {
                if (permissions[i].equals(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        ignoreCase = true
                    )
                ) {
                    //foreground permission allowed
                    if (grantResults[i] >= 0) {
                        foreground = true
                       /* Toast.makeText(
                            applicationContext,
                            "Foreground location permission allowed",
                            Toast.LENGTH_SHORT
                        ).show()*/
                        continue
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Location Permission denied",
                            Toast.LENGTH_SHORT
                        ).show()
                        break
                    }
                }
                if (permissions[i].equals(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        ignoreCase = true
                    )
                ) {
                    if (grantResults[i] >= 0) {
                        foreground = true
                        background = true
                       /* Toast.makeText(
                            applicationContext,
                            "Background location location permission allowed",
                            Toast.LENGTH_SHORT
                        ).show()*/
                    } else {
                       /* Toast.makeText(
                            applicationContext,
                            "Background location location permission denied",
                            Toast.LENGTH_SHORT
                        ).show()*/
                    }
                }
            }
            if (foreground) {
                if (background) {
                    handleLocationUpdates()
                } else {
                    handleForegroundLocationUpdates()
                }
            }
        }
    }

    private fun handleLocationUpdates() {
        //foreground and background
        /*Toast.makeText(
            applicationContext,
            "Start Foreground and Background Location Updates",
            Toast.LENGTH_SHORT
        ).show()*/

        if (UserShared(this@SplashScreen).getLoggedInStatus()) {


            startActivity(Intent(this@SplashScreen, HomeActivity::class.java))
            finish()
        } else {

            startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
            finish()
        }
    }

    private fun handleForegroundLocationUpdates() {
        //handleForeground Location Updates
        Toast.makeText(
            applicationContext,
            "Start foreground location updates",
            Toast.LENGTH_SHORT
        ).show()

       // requestPermission()



    }


}
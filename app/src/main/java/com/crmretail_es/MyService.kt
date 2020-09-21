package com.crmretail_es

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.IBinder
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*


class MyService : Service() {
    var locationManager: LocationManager? = null
    var GpsStatus = false
    private var googleApiClient: GoogleApiClient? = null
    val REQUEST_CHECK_SETTINGS = 0x1

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show()
        myThread()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show()
    }


    open fun myThread() {
        val th: Thread = object : Thread() {
            override fun run() {
                try {

                    CheckGpsStatus()

                    sleep(10000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    // TODO: handle exception
                }
            }
        } //End of thread class
        th.start()
    } //End of myThread()


    fun CheckGpsStatus() {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        assert(locationManager != null)
        GpsStatus = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (GpsStatus == true) {
            // textview.setText("GPS Is Enabled")

        } else {
           // Toast.makeText(getApplicationContext(), "GPS Is Disabled", Toast.LENGTH_SHORT).show()
            // textview.setText("GPS Is Disabled")
            // requestPermission()
            //finish()
            //requestPermission()
           // stopLocationService()
            myCheck()

        }


    }

    private fun myCheck() {


        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(getApplicationContext())
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
                             Toast.makeText(getApplicationContext(), "GPS Is ON", Toast.LENGTH_SHORT).show()

                        }
                        /*LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            status.startResolutionForResult(
                                applicationContext as Activity?,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (e: IntentSender.SendIntentException) {
                        }*/
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED->{

                            Toast.makeText(getApplicationContext(), "GPS Is OFF", Toast.LENGTH_SHORT).show()
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {


                        }
                    }
                }
            })
        }
    }



   /* override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(applicationContext, "GPS enabled", Toast.LENGTH_LONG).show()


            } else {
                Toast.makeText(applicationContext, "GPS is not enabled", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }*/

}
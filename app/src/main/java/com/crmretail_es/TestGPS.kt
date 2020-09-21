package com.crmretail_es

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*


class TestGPS : AppCompatActivity(),GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener{

    private var latitudeTextView: TextView? = null
    private  var longitudeTextView:TextView? = null
    private var mylocation: Location? = null
    private var googleApiClient: GoogleApiClient? = null
    private val REQUEST_CHECK_SETTINGS_GPS = 0x1
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        latitudeTextView = findViewById<View>(R.id.textView2) as TextView
        longitudeTextView = findViewById<View>(R.id.textView3) as TextView
        setUpGClient()
    }

    @Synchronized
    private fun setUpGClient() {
        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, 0, this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location) {
        mylocation = location
        if (mylocation != null) {
            val latitude = mylocation!!.getLatitude()
            val longitude = mylocation!!.getLongitude()
            latitudeTextView!!.text = "Latitude : $latitude"
            longitudeTextView!!.text = "Longitude : $longitude"
            //Or Do whatever you want with your location
        }
    }

    override fun onConnected(bundle: Bundle?) {
        checkPermissions()
    }

    override fun onConnectionSuspended(i: Int) {
        //Do whatever you need
        //You can display a message here
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }


    private fun getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient!!.isConnected) {
                val permissionLocation = ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                    val locationRequest = LocationRequest()
                    locationRequest.interval = 3000
                    locationRequest.fastestInterval = 3000
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    val builder = LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest)
                    builder.setAlwaysShow(true)
                    LocationServices.FusedLocationApi
                        .requestLocationUpdates(googleApiClient, locationRequest, this)
                    val result: PendingResult<LocationSettingsResult> = LocationServices.SettingsApi
                        .checkLocationSettings(googleApiClient, builder.build())
                    result.setResultCallback(object : ResultCallback<LocationSettingsResult?> {
                        override fun onResult(result: LocationSettingsResult) {
                            val status: Status = result.status
                            when (status.getStatusCode()) {
                                LocationSettingsStatusCodes.SUCCESS -> {
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    val permissionLocation = ContextCompat
                                        .checkSelfPermission(
                                            this@TestGPS,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        )
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                            .getLastLocation(googleApiClient)
                                    }
                                }
                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                                     // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(
                                            this@TestGPS,
                                            REQUEST_CHECK_SETTINGS_GPS
                                        )
                                    } catch (e: SendIntentException) {
                                        // Ignore the error.
                                    }
                                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS_GPS -> when (resultCode) {
                Activity.RESULT_OK -> getMyLocation()
                Activity.RESULT_CANCELED -> finish()
            }
        }
    }

    private fun checkPermissions() {
        val permissionLocation = ContextCompat.checkSelfPermission(
            this@TestGPS,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(
                    this,
                    listPermissionsNeeded.toTypedArray(),
                    REQUEST_ID_MULTIPLE_PERMISSIONS
                )
            }
        } else {
            getMyLocation()
        }
    }







}
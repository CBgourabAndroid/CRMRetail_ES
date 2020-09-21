package com.crmretail_es

import android.app.Application
import android.content.*
import android.net.ConnectivityManager
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.api.GoogleApiClient
import java.net.SocketException

public class MyApplication : Application() {

    private var googleApiClient: GoogleApiClient? = null
    val REQUEST_CHECK_SETTINGS = 0x1

    override fun onCreate() {
        super.onCreate()


        val intent = Intent(this, GoogleService::class.java)
        startService(intent)


        //var msg=getDAAAA()



    }


    fun isConnected(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return if (netInfo != null && netInfo.isConnectedOrConnecting) {
            true
        } else false
    }


    private  fun getDAAAA(): String? {
        try {
            var msg:String=""
            LocalBroadcastManager.getInstance(this).registerReceiver(
                object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        // String latitude = intent.getStringExtra("Latitude");
                        // String longitude = intent.getStringExtra("Longitude");
                        val provider = intent.getStringExtra("GpsStatus")
                        msg=provider


                        if (provider != null) {

                            if (provider.equals("Gps Disabled")){


                            }

                            // mMsgView.setText(getString(R.string.msg_location_service_started) + "\n Latitude : " + latitude + "\n Longitude: " + longitude);
                        }
                    }
                }, IntentFilter("Hellooooo")
            )
            return msg


        } catch (ex: SocketException) {
            Log.e(ContentValues.TAG, ex.toString())
        }
        return null
    }

      fun reciverMethod(): String {
        var msg:String=""
        LocalBroadcastManager.getInstance(this).registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    // String latitude = intent.getStringExtra("Latitude");
                    // String longitude = intent.getStringExtra("Longitude");
                    val provider = intent.getStringExtra("GpsStatus")
                    msg=provider


                    if (provider != null) {

                        if (provider.equals("Gps Disabled")){


                        }

                        // mMsgView.setText(getString(R.string.msg_location_service_started) + "\n Latitude : " + latitude + "\n Longitude: " + longitude);
                    }
                }
            }, IntentFilter("Hellooooo")
        )

        return msg
    }




}



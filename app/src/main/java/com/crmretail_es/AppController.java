package com.crmretail_es;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.crmretail_es.shared.Updatedlatlong;
import com.crmretail_es.utils.ConnectivityReceiver;
import com.google.android.gms.common.api.GoogleApiClient;


/**
 * Created by Developer on 5/2/2017.
 */

public class AppController extends Application {

    private static AppController ourInstance;
    public static final String TAG = AppController.class
            .getSimpleName();
    MediaPlayer mMediaPlayer;
    public static synchronized AppController getInstance() {
        return ourInstance;
    }

    public static GoogleApiClient googleApiClient;


    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;


        Intent intent = new Intent(this, GoogleService.class);
        startService(intent);


        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                       // String latitude = intent.getStringExtra("Latitude");
                       // String longitude = intent.getStringExtra("Longitude");
                        String provider = intent.getStringExtra("GpsStatus");


                        if (provider != null ) {

                            Log.v("prooooo", provider);

                           // mMsgView.setText(getString(R.string.msg_location_service_started) + "\n Latitude : " + latitude + "\n Longitude: " + longitude);
                        }
                    }
                }, new IntentFilter("Hellooooo")
        );
       // startLocationService();
        //stopLocationService();

    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    private void stopLocationService() {

        psh=new Updatedlatlong(getApplicationContext());
        Log.i("GPSTRACKER", "called startLocationService from application");
        stopService(new Intent(this,LocationUpdate.class));

        prefs = getSharedPreferences("LATLONG_SHARED_PREF", MODE_PRIVATE);
    }

    Updatedlatlong psh;
    SharedPreferences prefs;

    public void startLocationService(){
        psh=new Updatedlatlong(getApplicationContext());
        Log.i("GPSTRACKER", "called startLocationService from application");
        startService(new Intent(this, LocationUpdate.class));

        prefs = getSharedPreferences("LATLONG_SHARED_PREF", MODE_PRIVATE);
    }




    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;
        }

        return false;
    }




}

package com.crmretail_es;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.crmretail_es.modelClass.LocationData;
import com.crmretail_es.shared.ConnectionDetector;
import com.crmretail_es.shared.Updatedlatlong;
import com.crmretail_es.shared.UserShared;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class LocationUpdate extends Service implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener {


	private class LocationListener implements
			com.google.android.gms.location.LocationListener {

		public LocationListener() {
		}

		@Override
		public void onLocationChanged(Location location) {
			prefs = getSharedPreferences("LATLONG_SHARED_PREF", MODE_PRIVATE);
			Log.e(TAG, "onLocationChanged: " + location);
			currentLat = location.getLatitude();
			currentLng = location.getLongitude();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			String currentDateTime = sdf.format(new Date());

			System.out.println(currentLat);
			System.out.println(currentLng);
			Log.e(TAG, "USEID " + pshuser.getId());
			Log.e(TAG, "LATITUDE " + currentLat);
			Log.e(TAG, "LONGITUDE " + currentLng);
			Log.e(TAG, "TIME " + currentDateTime);



			/*Intent i = new Intent();
            i.setAction("Refresh");
            Bundle b = new Bundle();
            b.putString("mlat", String.valueOf(currentLat));
            b.putString("mlng", String.valueOf(currentLng));
            i.putExtra("mBundle", b);
            sendBroadcast(i);*/

			if (currentLat != 0.0 && currentLng != 0.0) {
				try {
					Log.d("Latitude", String.valueOf(currentLat));
					Log.d("Longitude", String.valueOf(currentLng));

					//	Toast.makeText(getApplicationContext(), String.valueOf(currentLat), Toast.LENGTH_SHORT).show();
					Editor editor = prefs.edit();
					editor.putString(getString(R.string.shared_updated_lat), String.valueOf(currentLat));
					editor.putString(getString(R.string.shared_updated_long), String.valueOf(currentLng));
					editor.putString(getString(R.string.shared_updated_time), currentDateTime);
					editor.commit();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			//Toast.makeText(context, "currentLat: " + currentLat + "  //  currentLng: " + currentLng, Toast.LENGTH_SHORT).show();

			/**GOURAB STARTS**/
			if (firsttimeUpdate) {
				try {
					if (!userid.equals("")) {
						firsttimeUpdate = false;
						//checkLogoutAsyncToServer();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			/**GOURAB ENDS**/
		}
	}


	private class UpdateLatLogToServer extends AsyncTask<Void, Integer, String> {
		@Override
		protected String doInBackground(Void... params) {
			return uploadFile();
		}


		@Override
		protected void onCancelled() {

		}

		protected void onPostExecute(final Boolean success) {
			try {

				if (!responseString.equals("")) {
					String msage = "latitude: " + lat + "  longitude: " + lon + "   Userid: " + userid + " // " + responseString;
					//Toast.makeText(LocationUpdate.this, responseString, Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			/*progressDialog = ProgressDialog.show(PersonalRegistrationActivity.this,
					"",
					getString(R.string.progress_bar_loading_message),
					false);*/
			super.onPreExecute();
		}

		@SuppressWarnings("deprecation")
		private String uploadFile() {


			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(PostInterface.BaseURL+"update-location");

			try {


				/*to print in log*/

				ByteArrayOutputStream bytes = new ByteArrayOutputStream();

				reqEntity.writeTo(bytes);

				String content = bytes.toString();

				Log.e("MultiPartEntityRequest:", content);

				/*to print in log*/

				httppost.setEntity(reqEntity);
				httppost.setHeader("Authorization", "Bearer " + pshuser.getAccessToken());

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				/*	for (int i = 0; i <locationData.size(); i++) {

						if (lastUpdatedPos==i){

							locationData.clear();
							Log.v("Last Postion Delete",String.valueOf(i));
						}

					}*/

				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}
			return responseString;


		}
	}

	private static final float LOCATION_DISTANCE = 0;
	private static final int LOCATION_INTERVAL = 1000 * 8;
	public static final String LOCATION_RECEIVED = "com.arenew";
	public static boolean runGpsUpdate = true;
	private static final String TAG = "DRIVER";
	private ArrayList<LocationData> locationData;
	private Context context;
	private double currentLat, currentLng;
	private String driverId;
	//private LocationManager lm;
	private boolean firsttimeUpdate = false;
	private String lat = "";
	private String currentDateTime = "";
	private int lastUpdatedPos;
	//private Context context;
	private LocationListener locationListener;
	boolean loginStatus;
	private String lon = "";
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	private final LocationManager mLocationManager = null;
	// A request to connect to Location Services
	private LocationRequest mLocationRequest;
	private ConnectionDetector networkInfo;
	/**GOURAB STARTS**/
	private SharedPreferences prefs;
	private SharedPreferences prefs1;
	private Updatedlatlong psh;
	private UserShared pshuser;
	private MultipartEntity reqEntity;
	String responseString = null;
	private final HashMap<String, String> userdetailMap = new HashMap<String, String>();


	private String userid = "";


	private void myThread1() {

		prefs1 = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		prefs = getSharedPreferences("LATLONG_SHARED_PREF", MODE_PRIVATE);

		Thread th = new Thread() {

			@Override
			public void run() {
				try {
					Log.i("LocationService", "runGpsUpdate:" + runGpsUpdate);
					while (runGpsUpdate) {

						Thread.sleep(900000);
						//Thread.sleep(12*60*1000);
						//Thread.sleep(10000);
						try {
							if (!userid.equals("")) {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
								String currentDateTime = sdf.format(new Date());
								pshuser = new UserShared(getApplicationContext());
								psh = new Updatedlatlong(getApplicationContext());
								reqEntity = null;
								reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
								reqEntity.addPart("user_id", new StringBody(pshuser.getId()));
								reqEntity.addPart("lati", new StringBody(psh.getUserUpdatedLatitude()));
								reqEntity.addPart("longi", new StringBody(psh.getUserUpdatedLongitude()));
								//reqEntity.addPart("created_at", new StringBody(currentDateTime));

								Log.v("Hey still running..!!!", "After 5min Update??");
								UpdateLatLogToServer aTask = new UpdateLatLogToServer();
								aTask.execute((Void) null);



								//checkLogoutAsyncToServer();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}; //End of thread class

		th.start();





	}


	private void myThread2() {

		prefs1 = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		prefs = getSharedPreferences("LATLONG_SHARED_PREF", MODE_PRIVATE);
		Thread th = new Thread() {

			@Override
			public void run() {
				try {
					Log.i("LocationService", "runGpsUpdate:" + runGpsUpdate);
					while (runGpsUpdate) {

						Thread.sleep(900000);
						//Thread.sleep(12*60*1000);
						//Thread.sleep(10000);
						try {
							if (!userid.equals("")) {
								/*pshuser = new UserShared(getApplicationContext());
								psh = new Updatedlatlong(getApplicationContext());
								reqEntity = null;*/
								for (int i = 0; i <locationData.size() ; i++) {

									reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
									reqEntity.addPart("user_id", new StringBody(locationData.get(i).getuserId()));
									reqEntity.addPart("lati", new StringBody(locationData.get(i).getuserLAT()));
									reqEntity.addPart("longi", new StringBody(locationData.get(i).getuserLONG()));
								//	reqEntity.addPart("created_at",new StringBody(locationData.get(i).getdatetime()));

									Log.v("Hey still running..!!!", "Agree now??");
									lastUpdatedPos=i;
									Log.v("LastUpdate Postion",String.valueOf(i));
									UpdateLatLogToServer aTask = new UpdateLatLogToServer();
									aTask.execute((Void) null);
								}


								//checkLogoutAsyncToServer();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}; //End of thread class

		th.start();

	}


	public void myThread() {
		prefs1 = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		prefs = getSharedPreferences("LATLONG_SHARED_PREF", MODE_PRIVATE);
		Thread th = new Thread() {

			@Override
			public void run() {
				try {
					Log.i("LocationService", "runGpsUpdate:" + runGpsUpdate);
					while (runGpsUpdate) {
						//Thread.sleep(18000);

						//	boolean loginStatus = prefs.getBoolean(getString(R.string.shared_loggedin_status_user), false);

						Log.i("LocationService", String.valueOf(loginStatus));
						if (!loginStatus) {
							pshuser = new UserShared(getApplicationContext());
							psh = new Updatedlatlong(getApplicationContext());
							runGpsUpdate = false;
							lat = "0.0";
							lon = "0.0";
							reqEntity = null;

							reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
							reqEntity.addPart("user_id", new StringBody(pshuser.getId()));
							reqEntity.addPart("lati", new StringBody(lat));
							reqEntity.addPart("longi", new StringBody(lon));

							////

							UpdateLatLogToServer aTask = new UpdateLatLogToServer();
							aTask.execute((Void) null);
							break;
						}
						Thread.sleep(3000);
						//Thread.sleep(12*60*1000);
						//Thread.sleep(10000);
						try {
							if (!userid.equals("")) {
								pshuser = new UserShared(getApplicationContext());
								psh = new Updatedlatlong(getApplicationContext());
								//reqEntity = null;
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
								String currentDateTime = sdf.format(new Date());
								reqEntity.addPart("user_id", new StringBody(userid));
								reqEntity.addPart("lati", new StringBody(psh.getUserUpdatedLatitude()));
								reqEntity.addPart("longi", new StringBody(psh.getUserUpdatedLongitude()));
								//reqEntity.addPart("created_at", new StringBody(currentDateTime));

								Log.v("Hey still running..!!!", "Save Data??");
								UpdateLatLogToServer aTask = new UpdateLatLogToServer();
								aTask.execute((Void) null);
								//2020-06-17 20:15:45

							/*	LocationData data=new LocationData();
								data.setuserId(pshuser.getId());
								data.setuserLAT(psh.getUserUpdatedLatitude());
								data.setuserLONG(psh.getUserUpdatedLongitude());
								data.setdatetime(psh.getUserUpdatedtime());
								locationData.add(data);*/


								//checkLogoutAsyncToServer();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}; //End of thread class

		th.start();
	}//End of myThread()



	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		mLocationRequest.setInterval(LOCATION_INTERVAL);
		mLocationRequest.setFastestInterval(LOCATION_INTERVAL);
		startLocationUpates();
	}


	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate() {
		Log.e(TAG, "onCreate");
		context = this.getApplicationContext();


		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();

		/**GOURAB STARTS**/
		networkInfo = new ConnectionDetector(this.getApplicationContext());
		prefs1 = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		String jsonUserData = prefs1.getString(getString(R.string.user_info), getString(R.string.no_shared_data));
		Log.d("HomeActivity", "Sending to app class: " + jsonUserData);
		psh = new Updatedlatlong(this);
		pshuser = new UserShared(this);
		userid = pshuser.getId();
		//String visibilityStatus = psh.getUserVisibilityStatus();
		boolean bl = pshuser.getLoggedInStatus();
		String visibilityStatus = String.valueOf(bl);
		Log.d("LocationSS", "visibilityStatus:" + visibilityStatus);
		/*if(visibilityStatus.equals("1")){*/
		if (bl) {
			runGpsUpdate = true;
		} else {
			runGpsUpdate = false;
		}

		firsttimeUpdate = true;

		loginStatus = pshuser.getLoggedInStatus();
		locationData= new ArrayList();

		//myThread();
		//myThread2();

		mm();
		myThread1();
	}

	private void mm() {
		try {
			if (!userid.equals("")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
				String currentDateTime = sdf.format(new Date());
				pshuser = new UserShared(getApplicationContext());
				psh = new Updatedlatlong(getApplicationContext());
				reqEntity = null;
				reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				reqEntity.addPart("user_id", new StringBody(pshuser.getId()));
				reqEntity.addPart("lati", new StringBody(psh.getUserUpdatedLatitude()));
				reqEntity.addPart("longi", new StringBody(psh.getUserUpdatedLongitude()));
				//reqEntity.addPart("created_at", new StringBody(currentDateTime));

				Log.v("Hey still running..!!!", "First Update??");
				UpdateLatLogToServer aTask = new UpdateLatLogToServer();
				aTask.execute((Void) null);



				//checkLogoutAsyncToServer();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


/*	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy");
		super.onDestroy();

		runGpsUpdate = false;
		loginStatus=false;
	}*/


	/////////////////////////////////////////LOCATION UPDATE/////////////////////////////////////////////////

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand");
		super.onStartCommand(intent, flags, startId);
		boolean stopService = false;
		if (intent != null)
			stopService = intent.getBooleanExtra("stopservice", false);

		System.out.println("stopservice " + stopService);

		locationListener = new LocationListener();
		if (stopService)
			stopLocationUpdates();
		else {
			if (!mGoogleApiClient.isConnected())
				mGoogleApiClient.connect();
		}

		return START_STICKY;
	}


	private void showToastLong(String string) {


	}


	private void startLocationUpates() {
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, locationListener);
		// displayLocation();
	}

	public void stopLocationUpdates() {
		LocationServices.FusedLocationApi.removeLocationUpdates(
				mGoogleApiClient, locationListener);

		if (mGoogleApiClient.isConnected())
			mGoogleApiClient.disconnect();
	}





}
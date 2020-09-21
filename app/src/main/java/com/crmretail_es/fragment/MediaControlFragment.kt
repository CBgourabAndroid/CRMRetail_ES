package com.crmretail_es.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.crmretail_es.AppController
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.shared.UserShared
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class MediaControlFragment: Fragment() {


    lateinit var root: View

    var reqEntity : MultipartEntity?=null
    internal var responseString: String? = null
    lateinit var psh: UserShared
    lateinit var dl_upload_file: File
    lateinit var progressDialog: ProgressDialog
    private  val FILE_NAME = "photo.jpg"
    private  val REQUEST_CODE = 42
    var photoFile: File?=null
    var MediaType=""
    var Status=""
    private var LATSTR=""
    private var LONGSTR=""

    lateinit var takeMCpic:ImageView
    lateinit var mcPic:ImageView
    lateinit var mcMediaType:Spinner
    lateinit var mcStatus:Spinner
    lateinit var mcRemarks:EditText
    lateinit var submitMC:TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.contain_media_control, container, false)
        //setHasOptionsMenu(true)
        psh= UserShared(context)
        progressDialog = ProgressDialog(context)



        inti(root)
        getLocation()

        return root
    }

    fun getLocation() {

        var locationManager = requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?

        var locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                var latitute = location!!.latitude
                var longitute = location!!.longitude

                Log.i("test", "Latitute: $latitute ; Longitute: $longitute")
                LATSTR=location!!.latitude.toString()
                LONGSTR=location!!.longitude.toString()
                //Toast.makeText(this@NewRegisterActivity,LATSTR,Toast.LENGTH_SHORT).show()

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
            Toast.makeText(context, "Fehler bei der Erfassung!", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("NewApi")
    private fun inti(root: View) {

        takeMCpic=root.findViewById(R.id.take_Apic_MC)
        mcPic=root.findViewById(R.id.supportImageMC)
        mcMediaType=root.findViewById(R.id.spinner_mediaType)
        mcStatus=root.findViewById(R.id.spinner_status)
        mcRemarks=root.findViewById(R.id.remarksMC)
        submitMC=root.findViewById(R.id.submit_MC)


        takeMCpic.setOnClickListener {

            takePICTURE()
        }

        mcMediaType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                MediaType= mcMediaType.getItemAtPosition(position).toString()
            }

        }

        mcStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Status= mcStatus.getItemAtPosition(position).toString()
            }

        }


        submitMC.setOnClickListener {

            validation()
        }



    }

    private fun takePICTURE() {


        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)

        // This DOESN'T work for API >= 24 (starting 2016)
        // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)

        val fileProvider = FileProvider.getUriForFile(requireContext(), "com.crmretail", photoFile!!)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), "Unable to open camera", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val takenImage = data?.extras?.get("data") as Bitmap
            val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            mcPic.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }







    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun validation() {



        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false


        if (photoFile==null) {
            message = "Please Provide Supporting Image"
            focusView = mcPic
            cancel = true
            tempCond = false
        }

        if (cancel) {
            // focusView.requestFocus();
            if (!tempCond) {
                focusView!!.requestFocus()
            }
            showToastLong(message)
        } else {
            val imm = requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager




            // imm.hideSoftInputFromWindow(conetext.getWindowToken(), 0)

            try {


                reqEntity = MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE
                )


                reqEntity!!.addPart("user_id", StringBody(psh.id))
                reqEntity!!.addPart("type", StringBody("2"))
                reqEntity!!.addPart("media", StringBody(MediaType))
                reqEntity!!.addPart("location", StringBody(""))
                reqEntity!!.addPart("qty", StringBody(""))
                reqEntity!!.addPart("rate", StringBody(""))
                reqEntity!!.addPart("vendor", StringBody(""))
                reqEntity!!.addPart("remarks", StringBody(mcRemarks.text.toString()))
                reqEntity!!.addPart("status", StringBody(Status))
                reqEntity!!.addPart("lati", StringBody(LATSTR))
                reqEntity!!.addPart("longi", StringBody(LONGSTR))

                dl_upload_file = photoFile!!.absoluteFile
                reqEntity!!.addPart("file_url", FileBody(dl_upload_file))


            } catch (e: Exception) {
                e.printStackTrace()
            }


            if(!PostInterface.isConnected(requireContext())){

                Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
            else{

                progressDialog.setMessage("Please wait ...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                val editProfileAsyncTask = UploadFileToServer()
                editProfileAsyncTask.execute(null as Void?)


            }





        }

    }


    private inner class UploadFileToServer : AsyncTask<Void, Int, String>() {
        override fun onPreExecute() {
            // setting progress bar to zero
            /*progressDialog = ProgressDialog.show(PersonalRegistrationActivity.this,
					"",
					getString(R.string.progress_bar_loading_message),
					false);*/
            super.onPreExecute()
        }


        override fun doInBackground(vararg params: Void): String {
            return uploadFile()
        }

        private fun uploadFile(): String {

            val httpclient: HttpClient = DefaultHttpClient()
            val httppost = HttpPost(PostInterface.BaseURL + "brand-track")

            try {


                /*to print in log*/
                val bytes = ByteArrayOutputStream()
                reqEntity!!.writeTo(bytes)
                val content = bytes.toString()
                Log.e("MultiPartEntityRequest:", content)

                /*to print in log*/httppost.entity = reqEntity
                httppost.setHeader("Authorization", "Bearer " + psh.getAccessToken())

                // Making server call
                val response = httpclient.execute(httppost)
                val r_entity = response.entity
                val statusCode = response.statusLine.statusCode
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity)

                    /*	for (int i = 0; i <locationData.size(); i++) {

						if (lastUpdatedPos==i){

							locationData.clear();
							Log.v("Last Postion Delete",String.valueOf(i));
						}

					}*/
                } else {
                    responseString = ("Error occurred! Http Status Code: "
                            + statusCode)
                }
            } catch (e: ClientProtocolException) {
                responseString = e.toString()
            } catch (e: IOException) {
                responseString = e.toString()
            }
            return responseString!!


        }


        @SuppressLint("NewApi")
        override fun onPostExecute(result: String) {
            Log.e(AppController.TAG, "Response from server: $result")

            try {
                if (responseString != "") {

                    val jsonObject = JSONObject(responseString)
                    // val ddd =jsonObject.getJSONObject("result")
                    val Ack = jsonObject.getString("status").toInt()
                    val msg=jsonObject.getString("msg")
                    if (Ack == 200) {
                        progressDialog!!.dismiss()
                        progressDialog!!.cancel()
                        //  showToastLong("Successfully Updated")
                        showToastLong(msg)
                        requireActivity().finish()


                    } else {
                        progressDialog!!.dismiss()
                        progressDialog!!.cancel()
                        showToastLong(msg)
                    }
                } else {
                    progressDialog!!.dismiss()
                    progressDialog!!.cancel()
                    showToastLong("Sorry! Problem cannot be recognized.")
                }
            } catch (e: Exception) {
                progressDialog!!.dismiss()
                progressDialog!!.cancel()
                e.printStackTrace()
            }

        }

    }



    private fun showToastLong(message: String) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }
}
package com.crmretail_es.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.crmretail_es.AppController
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.shared.UserShared
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.contain_private_trans.*
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

class EventImageActivity : AppCompatActivity() {

    lateinit var take_Pic: ImageView
    lateinit var foodPic: ImageView
    private val REQUEST_CAMERA = 0
    private var picturepath: Uri? = null
    lateinit var dl_upload_file: File
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    lateinit var descriptionEdt: TextInputEditText
    lateinit var progressDialog : ProgressDialog

    private  val FILE_NAME = "photo.jpg"
    private  val REQUEST_CODE = 42
    var photoFile: File?=null
    var DESCRIPTION=""
    lateinit var psh: UserShared
    var reqEntity : MultipartEntity?=null
    internal var responseString: String? = null
    lateinit var prefss: SharedPreferences
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.event_picture_activity)

        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }
        progressDialog =ProgressDialog(this)
        psh= UserShared(this)
        prefss = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)
        inti()
        verifyStoragePermissions(this)
    }

    private fun inti() {


        descriptionEdt=findViewById(R.id.description)


        take_Pic=findViewById(R.id.takeApic)
        foodPic=findViewById(R.id.supportImage)


        take_Pic.setOnClickListener {

            takePICTURE()
        }

        submitFooding.setOnClickListener {


            validation()
        }

    }


    private fun takePICTURE() {


        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)

        // This DOESN'T work for API >= 24 (starting 2016)
        // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)

        val fileProvider = FileProvider.getUriForFile(this, "com.crmretail", photoFile!!)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CODE)
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }

    }

    fun verifyStoragePermissions(activity: Activity) {
        // Check if we have write permission
        val permission: Int = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
        else{
            // selectImage()
        }
    }


    @SuppressLint("NewApi")
    private fun validation() {


        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false

        DESCRIPTION=descriptionEdt.text.toString()

        if (TextUtils.isEmpty(DESCRIPTION)) {
            message = "Please Enter Your Description"
            focusView = descriptionEdt
            cancel = true
            tempCond = false
        }
        if (photoFile==null) {
            message = "Please Provide Supporting Image"
            focusView = descriptionEdt
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
            val imm = this
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


            try {


                reqEntity = MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE
                )

                reqEntity!!.addPart("user_id", StringBody(psh.id))
                reqEntity!!.addPart("event_id", StringBody(intent.getStringExtra("eventid").toString()))


                reqEntity!!.addPart("description", StringBody(DESCRIPTION))

                dl_upload_file = photoFile!!.absoluteFile
                reqEntity!!.addPart("file_url", FileBody(dl_upload_file))





            } catch (e: Exception) {
                e.printStackTrace()
            }


            if(!PostInterface.isConnected(this)){

                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        finish()
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
            val httppost = HttpPost(PostInterface.BaseURL + "event-image")

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
                        finish()

                        // finish()


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

        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

    }

    private fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val takenImage = data?.extras?.get("data") as Bitmap
            val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            foodPic.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}
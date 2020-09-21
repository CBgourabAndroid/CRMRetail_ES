package com.crmretail_es.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.crmretail_es.AppController
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.modelClass.PersonalInfo
import com.crmretail_es.shared.UserShared
import com.crmretail_es.utils.Utility
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.contain_profile.*
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
import java.util.*

class ProfileActivity : AppCompatActivity(){

    lateinit var psh : UserShared
    lateinit var userName:TextView
    lateinit var userNumber:TextView
    lateinit var userAddress:TextView
    lateinit var userEmail:TextView
    lateinit var userDlrCode:TextView
    lateinit var userImg:ImageView

    lateinit var datalist: ArrayList<PersonalInfo>

    private var userChoosenTask: String? = null
    private val REQUEST_CAMERA = 0
    private val SELECT_FILE = 1
    private val TAG: String? = null
    private var picturepath: Uri? = null
    var reqEntity : MultipartEntity?=null
    internal var responseString: String? = null
    lateinit var updateProfile:TextView
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
    lateinit var prefs: SharedPreferences

    var flag=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.profile_activity)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {


            finish()
        }
        psh= UserShared(this)
        progressDialog= ProgressDialog(this)
        prefs = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)



        init()
        onclick()
        //setdata()

        loadData()

        setdata()
        verifyStoragePermissions(this)


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


    fun loadData(){

        val gson = Gson()
        val json =psh.personalList
        val turnsType = object : TypeToken<ArrayList<PersonalInfo>>() {}.type
        datalist=gson.fromJson(json,turnsType)


    }


    private fun init() {


        userName=findViewById(R.id.tvName)
        userNumber=findViewById(R.id.tvNumber)
        userAddress=findViewById(R.id.tvAddress)
        userEmail=findViewById(R.id.tvEmail)
        userDlrCode=findViewById(R.id.tvDlrCode)
        userImg=findViewById(R.id.profile_img)

        updateProfile=findViewById(R.id.saveTxt)

        updateProfile.setOnClickListener {

            validation()
        }
    }

    private fun setdata() {

       /* Glide.with(this)
            .load(psh.userPic)
            .apply(RequestOptions().circleCrop())
            .into(profile_image)*/

        userName.text=datalist[0].name
        userNumber.text=datalist[0].contact
        userAddress.text=datalist[0].spDesignation
        tvEmail.text=datalist[0].reporting
        tvDlrCode.text=datalist[0].area
        tvdoj.text=datalist[0].doj
        tvdep.text=datalist[0].department
        tvempCode.text=datalist[0].emp_code

        var imgurl=""
        if (psh.newPic.equals("yes")){
            imgurl=psh.userPic
        }
        else{
            imgurl=datalist[0].profile_image
        }


        Glide.with(this)
            .load(imgurl)
            .error(R.drawable.ss)
            .placeholder(R.drawable.ss)
            .apply(RequestOptions().circleCrop())
            .into(userImg)
        //userEmail.text=psh.email
        //userDlrCode.text=psh.dlrcod


    }



    private fun onclick() {
        userImg.setOnClickListener {
           // selectImage()
            cameraIntent()
        }

    }


    override fun onBackPressed() {

        finish()
    }



    private fun selectImage() {

        val items = arrayOf<CharSequence>(
            resources.getString(R.string.takephoto),
            resources.getString(R.string.choosefromlib),
            resources.getString(R.string.cancel)
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.addphoto))
        builder.setItems(items) { dialog, item ->
            val result = Utility.checkPermission(this)

            if (items[item] == resources.getString(R.string.takephoto)) {
                userChoosenTask = resources.getString(R.string.takephoto)
                if (result)
                    cameraIntent()

            } else if (items[item] == resources.getString(R.string.choosefromlib)) {
                userChoosenTask = resources.getString(R.string.choosefromlib)
                if (result)
                    galleryIntent()

            } else if (items[item] == resources.getString(R.string.cancel)) {
                dialog.dismiss()
            }
        }
        builder.show()


    }

    private fun galleryIntent() {
        val intent = Intent(Intent.ACTION_PICK)
      //  photoFile = getPhotoFile(FILE_NAME)
        //val fileProvider = FileProvider.getUriForFile(this, "com.crmretail", photoFile!!)
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        intent.type = "image/*"
        //intent.action = Intent.ACTION_GET_CONTENT//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)


    }

    private fun cameraIntent() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)
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

    private fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (userChoosenTask == getString(R.string.takephoto))
                    cameraIntent()
                else if (userChoosenTask == getString(R.string.choosefromlib))
                    galleryIntent()
            } else {
                //code for deny
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val takenImage = data?.extras?.get("data") as Bitmap
            flag=1

            val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            //foodPic.setImageBitmap(takenImage)
            Glide.with(this)
                .load(takenImage)
                .apply(RequestOptions().circleCrop())
                .into(userImg)
        }
        else if(requestCode==SELECT_FILE&&resultCode==Activity.RESULT_OK){
            flag=2
           // onSelectFromGalleryResult(data)
            picturepath=data!!.data

            //val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            Glide.with(this)
                .load(picturepath)
                .apply(RequestOptions().circleCrop())
                .into(userImg)
        }

        else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }



    private fun onSelectFromGalleryResult(data: Intent?) {

        if (!TextUtils.isEmpty(picturepath.toString())) {
            Glide.with(this)
                .load(picturepath)
                .apply(RequestOptions().circleCrop())
                .into(userImg)


        }
    }


    @SuppressLint("NewApi")
    private fun validation() {


        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false

        if (photoFile==null) {
            message = "Please Provide Your Profile Image!!"
            focusView = userImg
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

                if (flag==1){
                    dl_upload_file = photoFile!!.absoluteFile
                    reqEntity!!.addPart("file_url", FileBody(dl_upload_file))
                }
                else if(flag==2){
                    photoFile = File(picturepath!!.getPath())
                    dl_upload_file = photoFile!!.absoluteFile
                    reqEntity!!.addPart("file_url", FileBody(dl_upload_file))
                }






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

    private fun showToastLong(message: String) {

        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

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
            val httppost = HttpPost(PostInterface.BaseURL + "personal-image")

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
                    if (Ack == 200) {
                        progressDialog!!.dismiss()
                        progressDialog!!.cancel()
                        //  showToastLong("Successfully Updated")



                     //   datalist[0].setProfile_image(jsonObject.getString("image"))

                        val editor = prefs.edit()
                        editor.putString("newImg","yes")
                        editor.putString("userImg",jsonObject.getString("image"))
                        editor.commit()
                        showToastLong("Successfully Updated")
                        finish()

                        // finish()


                    } else {
                        progressDialog!!.dismiss()
                        progressDialog!!.cancel()
                        showToastLong("Something went wrong!!")
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

}
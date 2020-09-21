package com.crmretail_es.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.AppController
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.fragment.SellingBrandAdapter
import com.crmretail_es.modelClass.SelleingBrand
import com.crmretail_es.shared.UserShared
import kotlinx.android.synthetic.main.contain_new_register.*
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewRegisterActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var progressDialog:ProgressDialog

    private  val FILE_NAME = "photo.jpg"
    private  val REQUEST_CODE = 42
    var photoFile: File?=null
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    lateinit var sellingList:ArrayList<SelleingBrand>
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: SellingBrandAdapter
    private var LATSTR=""
    private var LONGSTR=""
    private var SituatedType=""
    private var InjunctionRoad=""
    private var Spaceofhoarding=""

    private var NatureOfBusiness=""
    private var OutCome=""

    var reqEntity : MultipartEntity?=null
    internal var responseString: String? = null
    lateinit var psh: UserShared
    lateinit var dl_upload_file: File

    lateinit var feedBackBtn:TextView

    lateinit var currentDateStart: String
    var calendar = Calendar.getInstance()
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH)
    var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    var hour = calendar.get(Calendar.HOUR_OF_DAY)
    var minute = calendar.get(Calendar.MINUTE)
    lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.new_register_activity)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }
        progressDialog= ProgressDialog(this)
        psh= UserShared(this)

        sellingList= ArrayList()



        verifyStoragePermissions(this)

        inti()
        setData()
        getLocation()
    }

    fun getLocation() {

        var locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?

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
            Toast.makeText(this, "Fehler bei der Erfassung!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inti() {
        recyclerView=findViewById(R.id.recyv_brand)
        takeApicNew.setOnClickListener {

            //verifyStoragePermissions(this)

            takePICTURE()
        }
        add_sellingBrand.setOnClickListener {

            val intent = Intent(this, AddSellingBrand::class.java)
            startActivityForResult(intent, 123)
        }
        spinner_situated?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                SituatedType= spinner_situated.getItemAtPosition(position).toString()
            }

        }
        spinner_junctionRoad?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                InjunctionRoad= spinner_junctionRoad.getItemAtPosition(position).toString()
            }

        }
        spinner_space_of_hoarding?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Spaceofhoarding= spinner_space_of_hoarding.getItemAtPosition(position).toString()
            }

        }

        nature?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                NatureOfBusiness= nature.getItemAtPosition(position).toString()
            }

        }

        outcome?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                OutCome= outcome.getItemAtPosition(position).toString()
            }

        }

        submitNewRegister.setOnClickListener {

            validation()
        }

        feedBackBtn=findViewById(R.id.feedbackBtn)
        feedBackBtn.setOnClickListener {

            val i1 = Intent(this, FeedBackActivity::class.java)
            startActivity(i1)

        }

        nextfollowupdate.setOnClickListener {

            dateFun()
        }

    }

    private fun dateFun() {


        datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { datePicker, fyear, fmonth, fday ->


                val mont = fmonth + 1
                var mt = ""
                var dy = ""

                if (mont < 10) {
                    mt = "0" + mont.toString()
                } else {
                    mt = mont.toString()
                }

                if (fday < 10) {

                    dy = "0" + fday.toString()
                } else {

                    dy = fday.toString()
                }

                val sss= PostInterface.getCalculatedDate("yyyy-MM-dd", -7)
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val beforeDate: Date = sdf.parse(sss)
                val current: Date = sdf.parse(fyear.toString() + "-" + mt + "-" + dy)
               /* if(beforeDate.before(current)){

                }
                else{
                    Toast.makeText(this,"More then 7days not allowed!!",Toast.LENGTH_SHORT).show()
                }*/

                currentDateStart = fyear.toString() + "-" + mt + "-" + dy
                nextfollowupdate.setText(PostInterface.format_date(currentDateStart))


            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()


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
            // Toast.makeText(this,"Permission Required",Toast.LENGTH_SHORT).show()
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

    private fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val takenImage = data?.extras?.get("data") as Bitmap
            val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            supportImageNew.setImageBitmap(takenImage)
        }
        else if (requestCode==123){

            val returnValueName = data?.getStringExtra("name")
            if (!returnValueName.equals("")){

                val returnValueName = data?.getStringExtra("name")
                val returnValueqty = data?.getStringExtra("qty")
                // val returnValueImage = data.getStringExtra("pic")


                val model=SelleingBrand()
                model.brandName=returnValueName
                model.brandQty=returnValueqty
                sellingList.add(model)
                setData()
            }
            else{

                Toast.makeText(this,"NOT ADDED!!",Toast.LENGTH_SHORT).show()
            }






        }

        else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun setData() {

        if (sellingList.size>0){

            adapter = SellingBrandAdapter(this)
            // recyclerViewCategory.layoutManager = GridLayoutManager(this,3) as RecyclerView.LayoutManager?

            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
            recyclerView.adapter = adapter
            adapter.setDataListItems(sellingList)

        }
    }

    private fun validation() {

        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false



        if (TextUtils.isEmpty(shopName.text.toString())) {
            message = "Please Enter Shop Name"
            focusView = shopName
            cancel = true
            tempCond = false
        }

        if (TextUtils.isEmpty(contactPerson.text.toString())) {
            message = "Please Enter Contact Person Name"
            focusView = contactPerson
            cancel = true
            tempCond = false
        }
        if (TextUtils.isEmpty(contactPMNumber.text.toString())) {
            message = "Please Enter Contact Person Mobile Number"
            focusView = contactPMNumber
            cancel = true
            tempCond = false
        }

        if (TextUtils.isEmpty(ownerName.text.toString())) {
            message = "Please Enter Proprietor Name"
            focusView = ownerName
            cancel = true
            tempCond = false
        }

        if (TextUtils.isEmpty(shopAddress.text.toString())) {
            message = "Please Enter Shop Address"
            focusView = ownerName
            cancel = true
            tempCond = false
        }


        if (TextUtils.isEmpty(currentDateStart)) {
            message = "Please Select Next Follow-Up Date"
            focusView = ownerName
            cancel = true
            tempCond = false
        }




        if (photoFile==null) {
            message = "Please Provide Shop Image"
            focusView = supportImageNew
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
                reqEntity!!.addPart("name", StringBody(shopName.text.toString()))
                reqEntity!!.addPart("email", StringBody(contactPemail.text.toString()))
                reqEntity!!.addPart("contact", StringBody(contactPMNumber.text.toString()))
                reqEntity!!.addPart("contact_person", StringBody(contactPerson.text.toString()))
                reqEntity!!.addPart("block_id", StringBody(blockID.text.toString()))
                reqEntity!!.addPart("space", StringBody(shopSpacesqft.text.toString()))
                reqEntity!!.addPart("discussion", StringBody(new_register_discuss.text.toString()))
                reqEntity!!.addPart("address", StringBody(shopAddress.text.toString()))
                reqEntity!!.addPart("owner", StringBody(ownerName.text.toString()))
                reqEntity!!.addPart("owner_contact", StringBody(ownerContactnumber.text.toString()))
                reqEntity!!.addPart("position", StringBody(SituatedType))
                reqEntity!!.addPart("l_junction", StringBody(InjunctionRoad))
                reqEntity!!.addPart("hoarding", StringBody(Spaceofhoarding))
                reqEntity!!.addPart("lati", StringBody(LATSTR))
                reqEntity!!.addPart("longi", StringBody(LONGSTR))

                reqEntity!!.addPart("nature_of_business", StringBody(NatureOfBusiness))
                reqEntity!!.addPart("no_of_siblings", StringBody(noofsib.text.toString()))
                reqEntity!!.addPart("next_followup_date", StringBody(currentDateStart))
                reqEntity!!.addPart("out_come", StringBody(OutCome))






                for (j in 0 until sellingList.size){

                    val key1="brands["+j+"][brand]"

                    reqEntity!!.addPart(key1, StringBody(sellingList[j].brandName))


                    val key2="brands["+j+"][lifting]"

                    reqEntity!!.addPart(key2, StringBody(sellingList[j].brandQty))


                }

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
            val httppost = HttpPost(PostInterface.BaseURL + "new-reg")

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
                        startActivity(Intent(this@NewRegisterActivity, HomeActivity::class.java))
                        finish()


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

}
package com.crmretail_es.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.AppController
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.adapter.AllPlaceAdapter
import com.crmretail_es.adapter.CustListAdapter
import com.crmretail_es.modelClass.CUstomer
import com.crmretail_es.modelClass.CustomerResponse
import com.crmretail_es.modelClass.Location
import com.crmretail_es.modelClass.LocationResponce
import com.crmretail_es.shared.UserShared
import com.crmretail_es.utils.ConnectivityReceiver
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.contain_add_event.*
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventAddActivity : AppCompatActivity() {

    lateinit var currentDate: String
    lateinit var currentDateStart: String
    lateinit var currentDateEnd: String

    var startTime=""
    var endTime=""
    var eventType=""
    var eventTypeID=""

    lateinit var reson: String
    lateinit var eventDateLay: LinearLayout
    lateinit var startTimeLay: LinearLayout
    lateinit var eventDateTxt: EditText
    lateinit var startTimeTxt: EditText
    lateinit var endTimeTxt:EditText
    lateinit var applyLeave: TextView
    lateinit var locationEtd: EditText
    lateinit var budgetEdt:EditText
    lateinit var no_of_people:EditText

    var calendar = Calendar.getInstance()
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH)
    var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    var hour = calendar.get(Calendar.HOUR_OF_DAY)
    var minute = calendar.get(Calendar.MINUTE)
    lateinit var datePickerDialog: DatePickerDialog
    var flag = 0
    lateinit var toolbar: Toolbar
    lateinit var progressDialog: ProgressDialog
    lateinit var psh: UserShared
    lateinit var dataList:ArrayList<Location>
    private var adapter: AllPlaceAdapter? = null
    private var Cadapter: CustListAdapter? = null
    var popUpView: View? = null
    var reqEntity : MultipartEntity?=null
    internal var responseString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.add_event_activity)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        progressDialog = ProgressDialog(this)
        psh = UserShared(this)


        val sdf = SimpleDateFormat("yyyy-MM-dd")
        currentDate = sdf.format(Date())

        currentDateStart = ""
        currentDateEnd = ""

        inti()


        if(!PostInterface.isConnected(this@EventAddActivity)){

            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
        else{

            progressDialog.setMessage("Please wait ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            callDataList()
        }

       // checkConnection()
    }

    private fun checkConnection() {
        val isConnected = ConnectivityReceiver.isConnected()
        showSnack(isConnected)
    }
    private fun showSnack(isConnected: Boolean) {
        val message: String
        val color: Int
        if (isConnected) {
            message = "Good! Connected to Internet"
            color = Color.WHITE
        } else {
            message = "Sorry! Not connected to internet"
            color = Color.RED
        }
        val snackbar = Snackbar
            .make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_LONG)
        val sbView = snackbar.view
        val textView =
            sbView.findViewById<View>(R.id.snackbar_text) as TextView
        textView.setTextColor(color)
        snackbar.show()
    }

    private fun inti() {


        eventDateLay = findViewById(R.id.event_date_lay)
        eventDateTxt = findViewById(R.id.event_date_txt)
        startTimeLay = findViewById(R.id.start_time_lay)
        startTimeTxt = findViewById(R.id.start_time_txt)
        applyLeave = findViewById(R.id.apply_leave)
        locationEtd = findViewById(R.id.leave_reasion)
        endTimeTxt=findViewById(R.id.end_time_txt)
        no_of_people=findViewById(R.id.event_no_of_heads)
        budgetEdt=findViewById(R.id.event_budget_txt)


        //  fromDateTxt.setText(PostInterface.format_date2(currentDateStart))
        //   toDateTxt.setText(PostInterface.format_date2(currentDateEnd))

        eventDateTxt.setOnClickListener {

            dateFun()

        }

        startTimeTxt.setOnClickListener {

            timeFun()
            flag = 1

        }
        endTimeTxt.setOnClickListener {

            timeFun()
            flag=2
        }

        applyLeave.setOnClickListener {


            validation()


            /**/
        }

        locationEtd.setOnClickListener {


            if (eventType.equals("Masson Meet")||eventType.equals("Engineer Meet")){


                if(!PostInterface.isConnected(this@EventAddActivity)){

                    Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
                else{

                    progressDialog.setMessage("Please wait ...")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    callCustList()
                }

            }
            else{

                viewAllLocation()
            }


        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val languages = resources.getStringArray(R.array.eventType)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, languages
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    eventType=languages[position].toString()

                    if (eventType.equals("Dealer/Sub-dealer Meet")){
                        eventTypeID="1"
                    }
                    else if(eventType.equals("Masson Meet")){
                        eventTypeID="2"
                    }
                    else if(eventType.equals("Engineer Meet")){
                        eventTypeID="3"
                    }
                    else if(eventType.equals("other")){
                        eventTypeID="4"
                    }



                    if (eventType.equals("Masson Meet")||eventType.equals("Engineer Meet")){

                        multiTxtH.setText("* Dealer/Sub-dealer")
                        locationEtd.setText("")
                        locationEtd.setHint("Enter  dealer/sub-dealer name")
                    }
                    else{
                        multiTxtH.setText("* Location")
                        locationEtd.setText("")
                        locationEtd.setHint("Enter meeting location")

                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        }





    private fun callCustList() {



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.getcustomer("Bearer "+psh.accessToken,psh.id).enqueue(object :
            Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {


                        //  Toast.makeText(applicationContext, "Good", Toast.LENGTH_SHORT).show()

                        if (response.body()!!.cUstomer!=null&& response.body()!!.cUstomer.size>0){


                            viewAllCustomer(response.body()!!.cUstomer as ArrayList<CUstomer>)

                        }
                        else{

                            Toast.makeText(this@EventAddActivity,"No Data Found!!",Toast.LENGTH_SHORT).show()
                        }





                    } else {
                        // Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Fail!!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }

    private fun timeFun() {


        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            if (flag == 1) {

                startTimeTxt.setText( SimpleDateFormat("HH:mm:ss").format(cal.time))
                startTime=SimpleDateFormat("HH:mm:ss").format(cal.time)


            } else if (flag == 2) {

                val timess=SimpleDateFormat("HH:mm:ss").format(cal.time)

                if (PostInterface.checkTime(startTime, timess)){

                    endTime=SimpleDateFormat("HH:mm:ss").format(cal.time)
                    endTimeTxt.setText( SimpleDateFormat("HH:mm:ss").format(cal.time))

                }
                else { // setdate_tv.setText(MyApplication.formatdate(dateClicked.toString()));
                    Toast.makeText(
                        this,
                        "Previous Time Not Allowed!!",
                        Toast.LENGTH_LONG
                    ).show()
                }







                //currentDateEnd = fyear.toString() + "-" + mt + "-" + dy
                //  toDateTxt.setText(PostInterface.format_date(currentDateEnd))


            }
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

    }


    private fun viewAllCustomer(arrayList: ArrayList<CUstomer>) {

        val mpopup: PopupWindow
        popUpView =
            layoutInflater.inflate(R.layout.dialog_near_by_all, null) // inflating popup layout

        mpopup = PopupWindow(
            popUpView,
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT,
            true
        ) //Creation of popup

        // mpopup.setAnimationStyle(android.R.style.Animation_Dialog);

        // mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
        mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0)

        val recycler_view: RecyclerView =
            popUpView!!.findViewById(R.id.my_recycler_view) as RecyclerView
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler_view.setLayoutManager(mLayoutManager)
        recycler_view.setItemAnimator(DefaultItemAnimator())
        val done: TextView = popUpView!!.findViewById(R.id.donetxt)



        Cadapter = CustListAdapter(this,     arrayList,
            done,
            mpopup,locationEtd
        )
        recycler_view.setAdapter(Cadapter)

        done.setOnClickListener {

            if (CustListAdapter.xyz.size != 0) {
                //      replaceFragment(new NearByMapFragment(tabLayout,Latitude,Longtitude,xyz));
                mpopup.dismiss()
            } else {
                showToastLong("Please Select Atlist One Customer")
            }
        }

    }

    private fun viewAllLocation() {

        val mpopup: PopupWindow
        popUpView =
            layoutInflater.inflate(R.layout.dialog_near_by_all, null) // inflating popup layout

        mpopup = PopupWindow(
            popUpView,
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT,
            true
        ) //Creation of popup

        // mpopup.setAnimationStyle(android.R.style.Animation_Dialog);

        // mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
        mpopup.showAtLocation(popUpView, Gravity.CENTER, 0, 0)

        val recycler_view: RecyclerView =
            popUpView!!.findViewById(R.id.my_recycler_view) as RecyclerView
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycler_view.setLayoutManager(mLayoutManager)
        recycler_view.setItemAnimator(DefaultItemAnimator())
        val done: TextView = popUpView!!.findViewById(R.id.donetxt)



        adapter = AllPlaceAdapter(this,     dataList,
            done,
            mpopup,locationEtd
        )
        recycler_view.setAdapter(adapter)

        done.setOnClickListener {

            if (AllPlaceAdapter.xyz.size != 0) {
                //      replaceFragment(new NearByMapFragment(tabLayout,Latitude,Longtitude,xyz));
                mpopup.dismiss()
            } else {
                showToastLong("Please Select Atlist One Place")
            }
        }
    }

    private fun validation() {


        reson = locationEtd.text.toString()

        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false

        if (TextUtils.isEmpty(currentDateStart)) {
            message = "Enter Meeting Date"
            focusView = eventDateTxt
            cancel = true
            tempCond = false
        }
        if (TextUtils.isEmpty(startTime)) {
            message = "Enter Start Time"
            focusView = startTimeTxt
            cancel = true
            tempCond = false
        }
       /* if (TextUtils.isEmpty(endTime)) {
            message = "Enter EndTime"
            focusView = endTimeTxt
            cancel = true
            tempCond = false
        }*/

        if (eventType.equals("Masson Meet")||eventType.equals("Engineer Meet")){


            if (TextUtils.isEmpty(reson)) {
                message = "Please Enter Customer Name"
                focusView = locationEtd
                cancel = true
                tempCond = false
            }

        }
        else{

            if (TextUtils.isEmpty(reson)) {
                message = "Please Enter Location"
                focusView = locationEtd
                cancel = true
                tempCond = false
            }

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




            // imm.hideSoftInputFromWindow(conetext.getWindowToken(), 0)

            try {


                reqEntity = MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE
                )



                reqEntity!!.addPart("user_id", StringBody(psh.id))
                reqEntity!!.addPart("event_type", StringBody(eventTypeID))
                reqEntity!!.addPart("event_date", StringBody(currentDateStart))
                reqEntity!!.addPart("start_time", StringBody(startTime))
               // reqEntity!!.addPart("end_time", StringBody(endTime))

                reqEntity!!.addPart("noa", StringBody(no_of_people.text.toString()))
                reqEntity!!.addPart("budget", StringBody(budgetEdt.text.toString()))


                if (eventType.equals("Masson Meet")||eventType.equals("Engineer Meet")){


                    for (j in 0 until CustListAdapter.xyz!!.size){

                        val key1="locations["+j+"][location]"

                        reqEntity!!.addPart(key1, StringBody(CustListAdapter.xyz!![j].toString()))


                    }

                }
                else{

                    for (j in 0 until AllPlaceAdapter.xyz!!.size){

                        val key1="locations["+j+"][location]"

                        reqEntity!!.addPart(key1, StringBody(AllPlaceAdapter.xyz!![j].toString()))


                    }


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

        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

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

                currentDateStart = fyear.toString() + "-" + mt + "-" + dy
                eventDateTxt.setText(PostInterface.format_date(currentDateStart))






            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()


    }


    private fun callDataList() {



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.getLocationList("Bearer "+psh.accessToken,psh.id).enqueue(object :
            Callback<LocationResponce> {
            override fun onResponse(call: Call<LocationResponce>, response: Response<LocationResponce>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.locations
                    Log.i("onSuccess", avv.toString());
                    if (avv.size === 0) {

                    } else {

                    }

                    if (avv.size>0) {

                        dataList= ArrayList()

                        for (j in 0 until avv.size){

                            val model= Location()
                            model.zoneId=avv[j].zoneId
                            model.zoneName=avv[j].zoneName
                            dataList.add(model)


                        }



                    } else {
                        // Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Fail!!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LocationResponce>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
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
            val httppost = HttpPost(PostInterface.BaseURL + "new-event")

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





}

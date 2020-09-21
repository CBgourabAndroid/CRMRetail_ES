package com.crmretail_es.activity

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.crmretail_es.AppController
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.modelClass.GeneralResponce2
import com.crmretail_es.nointernet.NILeaveModel
import com.crmretail_es.nointernet.NIShared
import com.crmretail_es.shared.UserShared
import com.crmretail_es.utils.ConnectivityReceiver
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LeaveApplicationActivity: AppCompatActivity(),ConnectivityReceiver.ConnectivityReceiverListener {

    lateinit var currentDate:String
    lateinit var currentDateStart:String
    lateinit var currentDateEnd:String
    lateinit var reson:String
    lateinit var fromDateLay: LinearLayout
    lateinit var toDateLay: LinearLayout
    lateinit var fromDateTxt: EditText
    lateinit var toDateTxt: EditText
    lateinit var applyLeave:TextView
    lateinit var reasionEtd:EditText

    var calendar = Calendar.getInstance()
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH)
    var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    var hour = calendar.get(Calendar.HOUR_OF_DAY)
    var minute = calendar.get(Calendar.MINUTE)
    lateinit var datePickerDialog: DatePickerDialog
    var flag=0
    lateinit var toolbar: Toolbar
    lateinit var progressDialog : ProgressDialog
    lateinit var psh:UserShared

    var INC=0
    lateinit var NIdataList:ArrayList<NILeaveModel>
    lateinit var pshNI:NIShared
    lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.leave_application_activity)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        progressDialog= ProgressDialog(this)
        psh=UserShared(this)
        pshNI= NIShared(this)
        prefs = getSharedPreferences("MY_SHARED_PREF_GB", Context.MODE_PRIVATE)

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        currentDate = sdf.format(Date())

        currentDateStart=""
        currentDateEnd=""

        inti()
        checkConnection()
    }


    override fun onBackPressed() {

        val intent = Intent()
        intent.putExtra("isOk", "")
        setResult(123, intent)
        finish()
    }

    private fun inti() {


        fromDateLay=findViewById(R.id.from_date_lay)
        fromDateTxt=findViewById(R.id.from_date_txt)
        toDateLay=findViewById(R.id.to_date_lay)
        toDateTxt=findViewById(R.id.to_date_txt)
        applyLeave=findViewById(R.id.apply_leave)
        reasionEtd=findViewById(R.id.leave_reasion)


      //  fromDateTxt.setText(PostInterface.format_date2(currentDateStart))
     //   toDateTxt.setText(PostInterface.format_date2(currentDateEnd))

        fromDateTxt.setOnClickListener {

            dateFun()
            flag=1
        }

        toDateTxt.setOnClickListener {

            dateFun()
            flag=2
        }

        applyLeave.setOnClickListener {


            validation()



            /**/
        }






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
            INC=1
            if (!pshNI.laList.equals("")){


                callNIDATALIST()
            }
        } else {
            message = "Sorry! Not connected to internet"
            color = Color.RED
            INC=2
        }
        val snackbar = Snackbar
            .make(findViewById(R.id.coordinatorLayout), message, Snackbar.LENGTH_LONG)
        val sbView = snackbar.view
        val textView =
            sbView.findViewById<View>(R.id.snackbar_text) as TextView
        textView.setTextColor(color)
        snackbar.show()
    }



    override fun onResume() {
        super.onResume()

        AppController.getInstance().setConnectivityListener(this@LeaveApplicationActivity)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showSnack(isConnected)
    }

    private fun validation() {


        reson=reasionEtd.text.toString()

        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false

        if (TextUtils.isEmpty(currentDateStart)) {
            message = "Enter Leave From Date"
            focusView = fromDateTxt
            cancel = true
            tempCond = false
        }
        if (TextUtils.isEmpty(currentDateEnd)) {
            message = "Enter Leave Upto Date"
            focusView = toDateTxt
            cancel = true
            tempCond = false
        }

        if (TextUtils.isEmpty(reson)) {
            message = "Enter leave details"
            focusView = reasionEtd
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



            if (PostInterface.checkDate(currentDateStart,currentDateEnd)) {

                if (INC==1){
                    if(!PostInterface.isConnected(this@LeaveApplicationActivity)){

                        Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                    }
                    else{

                        progressDialog.setMessage("Please wait...")
                        progressDialog.setCancelable(false)
                        progressDialog.show()
                        val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(reasionEtd.windowToken, 0)
                        callDataList()
                    }
                }
                else{

                    saveFData(currentDateStart,currentDateEnd,reson)


                }




            } else { // setdate_tv.setText(MyApplication.formatdate(dateClicked.toString()));
                Toast.makeText(
                    this,
                    "Previous Date Not Allowed!!",
                    Toast.LENGTH_LONG
                ).show()
            }




        }

    }


    private fun showToastLong(message: String) {

        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

    }


    private fun dateFun() {



        datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { datePicker, fyear, fmonth, fday ->


                val mont=fmonth+1
                var mt=""
                var dy=""

                if (mont<10){
                    mt="0"+mont.toString()
                }
                else{
                    mt=mont.toString()
                }

                if (fday<10){

                    dy="0"+fday.toString()
                }
                else{

                    dy=fday.toString()
                }




                if (flag==1){
                    currentDateStart=fyear.toString()+"-"+mt+"-"+dy
                    fromDateTxt.setText(PostInterface.format_date(currentDateStart))


                }
                else if(flag==2){

                    currentDateEnd=fyear.toString()+"-"+mt+"-"+dy
                    toDateTxt.setText(PostInterface.format_date(currentDateEnd))



                }


            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()


    }


    private fun saveFData(
        currentDateStart: String,
        currentDateEnd: String,
        reson: String
    ) {

        val model=NILeaveModel()
        model.startdate=currentDateStart
        model.enddate=currentDateEnd
        model.reason=reson
        NIdataList!!.add(model)

        val gson = Gson()
        val json =gson.toJson(NIdataList!!)
        val editor = prefs.edit()
        editor.putString("LeaveApplication",json)
        editor.commit()


    }


    private fun callDataList() {



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.applyLeave("Bearer "+psh.accessToken,psh.id,currentDateStart,currentDateEnd,reson).enqueue(object :
            Callback<GeneralResponce2> {
            override fun onResponse(call: Call<GeneralResponce2>, response: Response<GeneralResponce2>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {


                         Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
                         val intent = Intent()
                         intent.putExtra("isOk", "ok")
                         setResult(123, intent)
                         finish()


                    } else {
                        Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
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

            override fun onFailure(call: Call<GeneralResponce2>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }




    fun callNIDATALIST(){
        val gson = Gson()
        val json =pshNI.laList
        val turnsType = object : TypeToken<java.util.ArrayList<NILeaveModel>>() {}.type
        NIdataList=gson.fromJson(json,turnsType)
        //setdata()

        for (i in 0 until NIdataList.size) {

          //  calltheApiNow(NIdataList[i].startdate,NIdataList[i].enddate,NIdataList[i].reason)

        }

     //   calltheApiNow()


    }

    private fun calltheApiNow(
        startdate: String,
        enddate: String,
        reason: String
    ) {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.applyLeave("Bearer "+psh.accessToken,psh.id,startdate,enddate,reason).enqueue(object :
            Callback<GeneralResponce2> {
            override fun onResponse(call: Call<GeneralResponce2>, response: Response<GeneralResponce2>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {


                      


                    } else {
                        Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_LONG).show()
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

            override fun onFailure(call: Call<GeneralResponce2>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }

}
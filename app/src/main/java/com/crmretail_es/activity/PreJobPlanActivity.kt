package com.crmretail_es.activity

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.adapter.PrePlaceAdapter
import com.crmretail_es.modelClass.Location
import com.crmretail_es.modelClass.LocationResponce
import com.crmretail_es.shared.UserShared
import kotlinx.android.synthetic.main.contain_pre_job.*
import kotlinx.android.synthetic.main.pre_job_plan_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class PreJobPlanActivity : AppCompatActivity() {

    lateinit var progressDialog : ProgressDialog
    lateinit var psh: UserShared
    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView
    var currentDateStart=""
    var calendar = Calendar.getInstance()
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH)
    var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    var hour = calendar.get(Calendar.HOUR_OF_DAY)
    var minute = calendar.get(Calendar.MINUTE)
    lateinit var datePickerDialog: DatePickerDialog

    lateinit var dataList:ArrayList<Location>
    private var adapter: PrePlaceAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.pre_job_plan_activity)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }


        progressDialog= ProgressDialog(this)
        psh=UserShared(this)


        recyclerView = findViewById(R.id.pre_rcy_view)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(mLayoutManager)
        recyclerView.setItemAnimator(DefaultItemAnimator())

        pre_date_txt.setOnClickListener {

            dateFun()

        }
        if(!PostInterface.isConnected(this@PreJobPlanActivity)){

            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
        else{

            progressDialog.setMessage("Please wait ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            callDataList()
        }


        submitPreJob.setOnClickListener {

            if (PrePlaceAdapter.xyz.size != 0) {
                //      replaceFragment(new NearByMapFragment(tabLayout,Latitude,Longtitude,xyz));
                //mpopup.dismiss()
                validation()
            } else {
                showToastLong("Please Select Atlist One Place")
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("date", "")
        intent.putExtra("locationArray", "")
        intent.putExtra("locationArrayName","")
        setResult(124, intent)
        finish()
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

                val sss = fyear.toString() + "-" + mt + "-" + dy
                currentDateStart=dy+"-"+mt+"-"+fyear.toString()
                pre_date_txt.setText(PostInterface.format_date(sss))



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

                        setData()



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

    private fun setData() {

        adapter = PrePlaceAdapter(this,     dataList
           )
        recyclerView.setAdapter(adapter)


    }

    private fun validation() {



        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false

        if (TextUtils.isEmpty(currentDateStart)) {
            message = "Please select duty date!!"
            focusView = pre_date_txt
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




            // imm.hideSoftInputFromWindow(conetext.getWindowToken(), 0)



            val ss=PrePlaceAdapter.xyz
            val nn=PrePlaceAdapter.abc
            Toast.makeText(applicationContext,ss.toString(),Toast.LENGTH_LONG).show()

            val intent = Intent()
            intent.putExtra("date", currentDateStart)
           // intent.putExtra("locationArray", ss.toString())
            intent.putStringArrayListExtra("locationArray", ss as ArrayList<String?>?)
            intent.putStringArrayListExtra("locationArrayName", nn as ArrayList<String?>?)
            setResult(124, intent)
            finish()









        }

    }







    private fun showToastLong(message: String) {

        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    }
}
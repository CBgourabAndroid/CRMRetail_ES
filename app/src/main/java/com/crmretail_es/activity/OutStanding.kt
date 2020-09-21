package com.crmretail_es.activity

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.modelClass.OutstandingResponce
import com.crmretail_es.shared.UserShared
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.*
import com.crmretail_es.modelClass.Outstanding
import kotlinx.android.synthetic.main.outstanding_activity.*

class OutStanding : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var progressDialog:ProgressDialog
    lateinit var psh:UserShared

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.outstanding_activity)

        psh=UserShared(this)
        progressDialog= ProgressDialog(this)
        toolbar = findViewById(R.id.toolbar) as Toolbar

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

     //   Toast.makeText(this,intent.getStringExtra("shopid").toString(),Toast.LENGTH_SHORT).show()

        if(!PostInterface.isConnected(this@OutStanding)){

            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
        else{

            progressDialog.setMessage("Please wait ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            callDataList()
        }


    }


    private fun callDataList() {



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.outstanding("Bearer "+psh.accessToken,intent.getStringExtra("shopid").toString()).enqueue(object :
            Callback<OutstandingResponce> {
            override fun onResponse(call: Call<OutstandingResponce>, response: Response<OutstandingResponce>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {

                        val str=response.body()!!.outstanding

                       // Toast.makeText(applicationContext, "Good", Toast.LENGTH_SHORT).show()

                        loadData(response.body()!!.outstanding)


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

            override fun onFailure(call: Call<OutstandingResponce>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }

    private fun loadData(outstanding: Outstanding?) {


        upto30.text=outstanding!!.upto30Days.toString()
        upto45.text=outstanding!!.get31To45Days().toString()
        upto60.text=outstanding!!.get46To60Days().toString()
        upto90.text=outstanding!!.get61To90Days().toString()
        more90.text=outstanding!!.moreThan90Days.toString()
        totaldue.text=outstanding!!.totalDues.toString()



    }


}
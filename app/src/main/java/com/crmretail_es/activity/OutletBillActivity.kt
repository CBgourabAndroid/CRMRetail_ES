package com.crmretail_es.activity

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.adapter.BillAdapter
import com.crmretail_es.modelClass.Bill
import com.crmretail_es.modelClass.BillResponce
import com.crmretail_es.shared.UserShared
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OutletBillActivity :AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var progressDialog: ProgressDialog
    lateinit var psh: UserShared
    lateinit var recyclerView: RecyclerView
    lateinit var billAdapter: BillAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.outlet_bill_activity)

        psh=UserShared(this)
        progressDialog= ProgressDialog(this)
        toolbar = findViewById(R.id.toolbar) as Toolbar

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }


        recyclerView = findViewById(R.id.report_rcy_view)

        if(!PostInterface.isConnected(this@OutletBillActivity)){

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

        userAPI.billing("Bearer "+psh.accessToken,intent.getStringExtra("shopid").toString()).enqueue(object :
            Callback<BillResponce> {
            override fun onResponse(call: Call<BillResponce>, response: Response<BillResponce>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {


                       // Toast.makeText(applicationContext, "Good", Toast.LENGTH_SHORT).show()

                        if (response.body()!!.bills!=null&& response.body()!!.bills.size>0){

                            billAdapter= BillAdapter()
                            recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL ,false)
                            recyclerView.adapter = billAdapter
                            billAdapter.setHistoryListItems(this@OutletBillActivity,
                                response.body()!!.bills as ArrayList<Bill>
                            )
                        }
                        else{

                            Toast.makeText(this@OutletBillActivity,"No Data Found!!",Toast.LENGTH_SHORT).show()
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

            override fun onFailure(call: Call<BillResponce>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }
}
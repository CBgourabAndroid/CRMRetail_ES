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
import com.crmretail_es.adapter.LedgerAdapter
import com.crmretail_es.modelClass.Ledger
import com.crmretail_es.modelClass.LedgerResponse
import com.crmretail_es.shared.UserShared
import kotlinx.android.synthetic.main.place_order_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomerLedgerActivity : AppCompatActivity() {


    lateinit var toolbar:Toolbar
    lateinit var recyclerViewCategory: RecyclerView
    lateinit var progressDialog:ProgressDialog
    lateinit var psh:UserShared
    lateinit var ldgAdapter:LedgerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.customer_ledger_activity)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {

            finish()
        }

        progressDialog= ProgressDialog(this)
        psh= UserShared(this)
        recyclerViewCategory = findViewById(R.id.category_rcy_view)
        nametxt.setText(" "+intent.getStringExtra("shopName"))


        if(!PostInterface.isConnected(this@CustomerLedgerActivity)){

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

        userAPI.ledger("Bearer "+psh.accessToken,intent.getStringExtra("shopid").toString()).enqueue(object :
            Callback<LedgerResponse> {
            override fun onResponse(call: Call<LedgerResponse>, response: Response<LedgerResponse>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {


                        //  Toast.makeText(applicationContext, "Good", Toast.LENGTH_SHORT).show()

                        if (response.body()!!.ledger!=null&& response.body()!!.ledger.size>0){

                            ldgAdapter= LedgerAdapter()
                            recyclerViewCategory.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL ,false)
                            recyclerViewCategory.adapter = ldgAdapter
                            ldgAdapter.setHistoryListItems(this@CustomerLedgerActivity,
                                response.body()!!.ledger as ArrayList<Ledger>
                            )
                        }
                        else{

                            Toast.makeText(this@CustomerLedgerActivity,"No Data Found!!",Toast.LENGTH_SHORT).show()
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

            override fun onFailure(call: Call<LedgerResponse>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }

}
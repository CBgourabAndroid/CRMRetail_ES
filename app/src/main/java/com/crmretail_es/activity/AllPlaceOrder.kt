package com.crmretail_es.activity

import android.app.ProgressDialog
import android.content.Intent
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
import com.crmretail_es.adapter.OrderListAdapter
import com.crmretail_es.modelClass.OrderList
import com.crmretail_es.modelClass.OrderListResponse
import com.crmretail_es.shared.UserShared
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AllPlaceOrder : AppCompatActivity() {

    lateinit var progressDialog : ProgressDialog
    lateinit var psh: UserShared
    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView

    lateinit var addOrder: FloatingActionButton
    lateinit var dataList:ArrayList<OrderList>
    lateinit var hAdapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.all_place_order)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }
        progressDialog= ProgressDialog(this)
        psh=UserShared(this)
        recyclerView = findViewById(R.id.report_rcy_view)

        if(!PostInterface.isConnected(this@AllPlaceOrder)){

            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
        else{

            progressDialog.setMessage("Please wait ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            callDataList()
        }


        addOrder=findViewById(R.id.fab)
        addOrder.setOnClickListener {

            val i1 = Intent(this, PlaceOrderActivity::class.java)
            i1.putExtra("shopid",intent.getStringExtra("shopid"))
            i1.putExtra("shopName", intent.getStringExtra("shopName"))
            i1.putExtra("shopAddress", intent.getStringExtra("shopAddress"))
            i1.putExtra("shopNumber", intent.getStringExtra("shopNumber"))
            i1.putExtra("type", "")
            startActivityForResult(i1, 123)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==123){

            val message = data!!.getStringExtra("isOk")

            if (!message.equals("")){

                if(!PostInterface.isConnected(applicationContext)){

                    Toast.makeText(applicationContext, applicationContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
                else{

                    progressDialog.setMessage("Please wait ...")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    callDataList()


                }
            }
            else{
                Toast.makeText(applicationContext, "No Data Added!!", Toast.LENGTH_SHORT).show()
            }


        }
    }


    private fun callDataList() {



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.getOrderList("Bearer "+psh.accessToken,intent.getStringExtra("shopid")).enqueue(object :
            Callback<OrderListResponse> {
            override fun onResponse(call: Call<OrderListResponse>, response: Response<OrderListResponse>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {

                        dataList= ArrayList()


                        // Toast.makeText(applicationContext, "Good", Toast.LENGTH_SHORT).show()

                        if (response.body()!!.orderList!=null&& response.body()!!.orderList.size>0){

                            hAdapter= OrderListAdapter()
                            recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL ,false)
                            recyclerView.adapter = hAdapter

                            dataList=response.body()!!.orderList as ArrayList<OrderList>

                            hAdapter.setHistoryListItems(this@AllPlaceOrder,
                                dataList
                            )
                        }
                        else{

                            Toast.makeText(this@AllPlaceOrder,"No Data Found!!",Toast.LENGTH_SHORT).show()
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

            override fun onFailure(call: Call<OrderListResponse>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }
}
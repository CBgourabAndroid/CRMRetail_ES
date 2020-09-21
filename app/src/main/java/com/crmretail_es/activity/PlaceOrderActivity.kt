package com.crmretail_es.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.AppController
import com.crmretail_es.R
import com.crmretail_es.adapter.ProductAdapter
import com.crmretail_es.modelClass.EditModel
import com.crmretail_es.modelClass.ProductInfo
import com.crmretail_es.shared.UserShared
import com.crmretail_es.utils.ConnectivityReceiver
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.place_order_activity.*

class PlaceOrderActivity: AppCompatActivity(),ConnectivityReceiver.ConnectivityReceiverListener {

    lateinit var psh:UserShared
    lateinit var productlist: ArrayList<ProductInfo>
    lateinit var toolbar:Toolbar
    lateinit var recyclerViewCategory: RecyclerView
    lateinit var allAdapter:ProductAdapter
    var editModelArrayList: ArrayList<EditModel>? = null
    var str=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.place_order_activity)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        psh= UserShared(this)
        recyclerViewCategory = findViewById(R.id.category_rcy_view)
        productlist=ArrayList()


        nametxt.setText(" "+intent.getStringExtra("shopName"))
        addressTxt.setText(" "+intent.getStringExtra("shopAddress"))
        numberTxt.setText( " "+intent.getStringExtra("shopNumber"))


        loadData()

        setData()

        placeOrderNow.setOnClickListener {

        //    str= ProductAdapter.saveDataArray
        //    Toast.makeText(this,str,Toast.LENGTH_LONG).show()

          /*
           *//* for (i in 0 until CustomAdapter.editModelArrayList.size) {



            }*//*

            Toast.makeText(this,str,Toast.LENGTH_LONG).show()*/
        }

        //checkConnection()
    }

    override fun onBackPressed() {

        val intent = Intent()
        intent.putExtra("isOk", "ok")
        setResult(123, intent)
        finish()
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

    override fun onResume() {
        super.onResume()

        AppController.getInstance().setConnectivityListener(this@PlaceOrderActivity)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showSnack(isConnected)
    }


    private fun setData() {

        allAdapter = ProductAdapter(this,productlist,intent.getStringExtra("shopid").toString(),placeOrderNow,intent.getStringExtra("type").toString())
        // recyclerViewCategory.addItemDecoration(MiddleDividerItemDecoration(context!!, MiddleDividerItemDecoration.ALL))
        recyclerViewCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        recyclerViewCategory.adapter = allAdapter
        allAdapter.setHistoryListItems(this,productlist)

       /* editModelArrayList = populateList()
        allAdapter = CustomAdapter(this, editModelArrayList)
        recyclerViewCategory.setAdapter(allAdapter)
        recyclerViewCategory.setLayoutManager(
            LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL,
                false
            )
        )*/

    }

    fun loadData(){
        val gson = Gson()
        val json =psh.productList
        val turnsType = object : TypeToken<ArrayList<ProductInfo>>() {}.type
        productlist=gson.fromJson(json,turnsType)


    }

    private fun populateList(): ArrayList<EditModel>? {
        val list = ArrayList<EditModel>()
        for (i in 0 until productlist.size){
            val editModel = EditModel()
            editModel.editTextValue = "0"
            editModel.productId=productlist.get(i).productId
            editModel.productName=productlist[i].productName
            editModel.productPrice=productlist[i].productPrice
            list.add(editModel)
        }

        return list
    }
}
package com.crmretail_es.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.adapter.BrandInfoAdapter
import com.crmretail_es.adapter.DiscussionAdapter
import com.crmretail_es.modelClass.*
import com.crmretail_es.shared.UserShared
import kotlinx.android.synthetic.main.contain_feedback_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class FeedBackDetailsActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var rcyBrandInfo: RecyclerView
    lateinit var rcyDiscussion:RecyclerView
    lateinit var progressDialog: ProgressDialog
    lateinit var psh: UserShared
    lateinit var BIadapter:BrandInfoAdapter
    lateinit var discussionAdapter:DiscussionAdapter
    private var LATSTR=""
    private var LONGSTR=""
    lateinit var submitNewRegister:TextView
    lateinit var nameTXT:TextView
    lateinit var contactTXT:TextView
    lateinit var contactpersonTXT:TextView
    lateinit var addressTXT:TextView
    lateinit var spaceTXT:TextView
    lateinit var discuss_edt:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.feedback_details_activity)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        progressDialog= ProgressDialog(this)
        psh= UserShared(this)

        inti()



        if(!PostInterface.isConnected(this@FeedBackDetailsActivity)){

            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
        else{

            progressDialog.setMessage("Please wait ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            callDataList()
        }

        getLocation()





    }

    private fun inti() {
        rcyBrandInfo=findViewById(R.id.brand_info)
        rcyDiscussion=findViewById(R.id.discussionList)

        nameTXT=findViewById(R.id.nameTxt)
        contactTXT=findViewById(R.id.contactTxt)
        contactpersonTXT=findViewById(R.id.contactpersonTxt)
        addressTXT=findViewById(R.id.addressTxt)
        spaceTXT=findViewById(R.id.space_Txt)

        discuss_edt=findViewById(R.id.new_register_discuss)

        submitNewRegister=findViewById(R.id.submit_New_Register)

        submitNewRegister.setOnClickListener {


            validation()
        }


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


    private fun callDataList() {



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.getFeedbackDetails("Bearer "+psh.accessToken,intent.getStringExtra("regid").toString()).enqueue(object :
            Callback<FeedbackDetailsResponse> {
            override fun onResponse(call: Call<FeedbackDetailsResponse>, response: Response<FeedbackDetailsResponse>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {

                        if (response.body()!!.regInfo!=null&& response.body()!!.regInfo.size>0){

                            nameTXT.setText(response.body()!!.regInfo[0].name)
                            contactTXT.setText("Contact : "+response.body()!!.regInfo[0].contact)
                            contactpersonTXT.setText("Contact Person : "+response.body()!!.regInfo[0].contactPerson)
                            addressTXT.setText("Address : "+response.body()!!.regInfo[0].address)
                            spaceTXT.setText("Sapce Available : "+response.body()!!.regInfo[0].space)


                        }
                        else{
                            regDetails.visibility= View.GONE
                            Toast.makeText(this@FeedBackDetailsActivity,"No Data Found In Registration !!",Toast.LENGTH_SHORT).show()
                        }



                        //  Toast.makeText(applicationContext, "Good", Toast.LENGTH_SHORT).show()

                        if (response.body()!!.brandInfo!=null&& response.body()!!.brandInfo.size>0){

                            BIadapter = BrandInfoAdapter(this@FeedBackDetailsActivity)
                            // recyclerViewCategory.layoutManager = GridLayoutManager(this,3) as RecyclerView.LayoutManager?

                            rcyBrandInfo.layoutManager = LinearLayoutManager(this@FeedBackDetailsActivity, LinearLayoutManager.VERTICAL ,false)
                            rcyBrandInfo.adapter = BIadapter
                            BIadapter.setDataListItems((response.body()!!.brandInfo as ArrayList<BrandInfo>))


                        }
                        else{
                            rcyBrandInfo.visibility= View.GONE
                            Toast.makeText(this@FeedBackDetailsActivity,"No Data Found In Brand !!",Toast.LENGTH_SHORT).show()
                        }



                        if (response.body()!!.discussion!=null&& response.body()!!.discussion.size>0){

                            discussionAdapter = DiscussionAdapter(this@FeedBackDetailsActivity)
                            // recyclerViewCategory.layoutManager = GridLayoutManager(this,3) as RecyclerView.LayoutManager?

                            rcyDiscussion.layoutManager = LinearLayoutManager(this@FeedBackDetailsActivity, LinearLayoutManager.VERTICAL ,false)
                            rcyDiscussion.adapter = discussionAdapter
                            discussionAdapter.setDataListItems((response.body()!!.discussion as ArrayList<Discussion>))


                        }
                        else{
                            rcyDiscussion.visibility= View.GONE
                            Toast.makeText(this@FeedBackDetailsActivity,"No Data Found In Discussion !!",Toast.LENGTH_SHORT).show()
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

            override fun onFailure(call: Call<FeedbackDetailsResponse>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }

    private fun validation() {

        val DiscussStr = discuss_edt.getText().toString()

        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false




        if (TextUtils.isEmpty(DiscussStr)) {
            message = "Please enter discussion in details"
            focusView = new_register_discuss
            cancel = true
            tempCond = false
        }




        if (LATSTR==null&& LONGSTR==null||LATSTR.equals("0.0")&&LONGSTR.equals("0.0")){
            message = "Please check your GPS"


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



            if(!PostInterface.isConnected(this@FeedBackDetailsActivity)){

                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
            else{

                progressDialog.setMessage("Please wait ...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                callNewFeedBack(DiscussStr)
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(discuss_edt.windowToken, 0)

            }


        }




    }

    private fun callNewFeedBack(DiscussStr: String?) {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.discussionCall("Bearer "+psh.accessToken,DiscussStr!!,intent.getStringExtra("regid").toString(),LATSTR,LONGSTR).enqueue(object : Callback<GeneralResponce2> {
            override fun onResponse(call: Call<GeneralResponce2>, response: Response<GeneralResponce2>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    showToastLong(response.message().toString())

                    startActivity(Intent(this@FeedBackDetailsActivity, FeedBackActivity::class.java))
                    finish()

                }
                else{
                    showToastLong(response.message().toString())
                    progressDialog.dismiss()
                }
            }

            override fun onFailure(call: Call<GeneralResponce2>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }

    private fun showToastLong(message: String) {

        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

    }
}
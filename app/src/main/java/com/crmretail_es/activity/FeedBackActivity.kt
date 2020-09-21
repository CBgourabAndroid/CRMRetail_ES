package com.crmretail_es.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.adapter.FeedbackAdapter
import com.crmretail_es.modelClass.FeedbackResponse
import com.crmretail_es.modelClass.Notclosed
import com.crmretail_es.shared.UserShared
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class FeedBackActivity: AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var recyclerViewCategory: RecyclerView
    lateinit var progressDialog: ProgressDialog
    lateinit var psh: UserShared
    lateinit var etsearch: EditText

    lateinit var  dataArrayList: ArrayList<Notclosed>
    lateinit var  array_sort: ArrayList<Notclosed>
    internal var textlength = 0
    lateinit var cAdapter:FeedbackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.feed_back_activity)

        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        progressDialog= ProgressDialog(this)
        psh= UserShared(this)
        recyclerViewCategory = findViewById(R.id.category_rcy_view)
        etsearch=findViewById(R.id.editText)

        dataArrayList = ArrayList<Notclosed>()
        array_sort= ArrayList<Notclosed>()

        if(!PostInterface.isConnected(this@FeedBackActivity)){

            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
        else{

            progressDialog.setMessage("Please wait ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            callDataList()
        }


        etsearch!!.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                array_sort.clear()

                for (i in dataArrayList.indices) {

                    if (dataArrayList[i].name.toLowerCase().trim().contains(etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        || dataArrayList[i].contact.toLowerCase().trim().contains(etsearch!!.text.toString().toLowerCase().trim{it<=' '})) {

                        array_sort.add(dataArrayList[i])
                    }
                }
                cAdapter = FeedbackAdapter(this@FeedBackActivity, array_sort)
                recyclerViewCategory!!.adapter = cAdapter
                recyclerViewCategory!!.layoutManager =
                    LinearLayoutManager(this@FeedBackActivity, LinearLayoutManager.VERTICAL, false)


            }
        })
    }

    override fun onBackPressed() {

        startActivity(Intent(this@FeedBackActivity, HomeActivity::class.java))
        finish()
    }


    private fun callDataList() {



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.getFeedbacklist("Bearer "+psh.accessToken,psh.id).enqueue(object :
            Callback<FeedbackResponse> {
            override fun onResponse(call: Call<FeedbackResponse>, response: Response<FeedbackResponse>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {


                        //  Toast.makeText(applicationContext, "Good", Toast.LENGTH_SHORT).show()

                        if (response.body()!!.notclosed!=null&& response.body()!!.notclosed.size>0){

                            dataArrayList=response.body()!!.notclosed as ArrayList<Notclosed>
                            Collections.sort(dataArrayList,ALPHABETICAL_ORDER)
                            cAdapter = FeedbackAdapter(this@FeedBackActivity, dataArrayList)
                            recyclerViewCategory!!.adapter = cAdapter
                            recyclerViewCategory!!.layoutManager =
                                LinearLayoutManager(this@FeedBackActivity, LinearLayoutManager.VERTICAL, false)



                            val controller =
                                AnimationUtils.loadLayoutAnimation(this@FeedBackActivity, R.anim.layout_animation_down_to_up);

                            recyclerViewCategory.setLayoutAnimation(controller)
                            recyclerViewCategory.getAdapter()!!.notifyDataSetChanged()
                            recyclerViewCategory.scheduleLayoutAnimation()

                        }
                        else{

                            Toast.makeText(this@FeedBackActivity,"No Data Found!!",Toast.LENGTH_SHORT).show()
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

            override fun onFailure(call: Call<FeedbackResponse>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }


    private val ALPHABETICAL_ORDER: Comparator<Notclosed> =
        object : Comparator<Notclosed> {

            override fun compare(o1: Notclosed?, o2: Notclosed?): Int {

                var res = java.lang.String.CASE_INSENSITIVE_ORDER.compare(o1!!.name, o2!!.name)
                if (res == 0) {
                    res = o1!!.name.compareTo(o2!!.name)
                }
                return res
            }
        }
}
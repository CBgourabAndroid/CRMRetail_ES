package com.crmretail_es.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.AppController
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.adapter.DateLocAdapter
import com.crmretail_es.modelClass.DateLocModel
import com.crmretail_es.shared.UserShared
import kotlinx.android.synthetic.main.contain_add_date_location.*
import kotlinx.android.synthetic.main.pjp_main_activity.*
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class PJPMainActivity: AppCompatActivity() {


    lateinit var recyclerView: RecyclerView
    lateinit var dateLocList:ArrayList<DateLocModel>
    lateinit var adapter:DateLocAdapter
    lateinit var toolbar: Toolbar
    lateinit var progressDialog:ProgressDialog
    lateinit var psh:UserShared
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
        setContentView(R.layout.pjp_main_activity)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        dateLocList= ArrayList()
        progressDialog= ProgressDialog(this)
        psh= UserShared(this)

        inti()



    }



    private fun inti() {

        recyclerView=findViewById(R.id.recyv_fooding)
        add_dateLoc.setOnClickListener {

            val intent = Intent(this, PreJobPlanActivity::class.java)
            startActivityForResult(intent, 124)
        }

        submitPJob.setOnClickListener {

            if (dateLocList.size != 0) {
                //      replaceFragment(new NearByMapFragment(tabLayout,Latitude,Longtitude,xyz));
                //mpopup.dismiss()

                dataInput()

            } else {
                showToastLong("Please provide your job planning")
            }
        }
    }

    private fun dataInput() {

        try {


             reqEntity = MultipartEntity(
                 HttpMultipartMode.BROWSER_COMPATIBLE
             )



             reqEntity!!.addPart("user_id", StringBody(psh.id))

             for (j in 0 until dateLocList!!.size){

                  val dateKey="duty_date["+j+"]"

                 reqEntity!!.addPart(dateKey, StringBody(dateLocList[j].date))

                 for (k in 0 until dateLocList[j].loc.size){

                     val key1="areaList["+j+"]["+k+"]"

                     reqEntity!!.addPart(key1, StringBody(dateLocList[j].loc[k].toString()))
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

    private fun showToastLong(message: String) {

        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
        // super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode==124){
                val returnValueDate = data.getStringExtra("date")
                if (!returnValueDate.equals("")){

                    val returnValueDate = data.getStringExtra("date")
                    //val returnValueLoc = data.getStringExtra("locationArray")
                    val returnValueLoc =
                        data.getStringArrayListExtra("locationArray")
                    val returnValueLocName =
                        data.getStringArrayListExtra("locationArrayName")
                    // val returnValueImage = data.getStringExtra("pic")

                    val model= DateLocModel()
                    model.date=returnValueDate
                    model.loc=returnValueLoc
                    model.locName=returnValueLocName
                    dateLocList.add(model)

                    setData()
                }



            }


            else {

                // failed to capture image
                Toast.makeText(
                    this,
                    "Sorry! Failed ", Toast.LENGTH_SHORT
                )
                    .show()

            }
        } else {
            Toast.makeText(
                this,
                "User cancelled ", Toast.LENGTH_SHORT
            )
                .show()
        }


    }

    private fun setData() {


        if (dateLocList.size>0){

            adapter = DateLocAdapter(applicationContext)
            // recyclerViewCategory.layoutManager = GridLayoutManager(this,3) as RecyclerView.LayoutManager?

            recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL ,false)
            recyclerView.adapter = adapter
            adapter.setDataListItems(dateLocList)
        }


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
            val httppost = HttpPost(PostInterface.BaseURL + "mo-duty")

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
                        finish()
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
package com.crmretail_es.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.AppController.TAG
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.activity.CustomerListActivity
import com.crmretail_es.activity.OutletVisitActivity
import com.crmretail_es.modelClass.ProductInfo
import com.crmretail_es.modelClass.SaveProduct
import com.crmretail_es.shared.UserShared
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
import kotlin.collections.ArrayList

class ProductAdapter(
    var conetext: Context,
    var originalData: ArrayList<ProductInfo>,
    var custID: String,
    var placeOrderNow: TextView,
    var type:String
)
    : RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {

    //private var originalData: ArrayList<_8>? = null
    private var filteredData: ArrayList<ProductInfo>? = null
    var saveDataArray:ArrayList<SaveProduct>?=null
    lateinit var progressDialog:ProgressDialog
    var reqEntity : MultipartEntity?=null
    internal var responseString: String? = null
    lateinit var psh:UserShared
    lateinit var et_proqty: EditText
    //private val inflater: LayoutInflater? = null



    private val viewPool = RecyclerView.RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return originalData!!.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        saveDataArray= ArrayList()
        progressDialog= ProgressDialog(conetext)
        psh= UserShared(conetext)

        holder.tv_nametxt.setText(originalData!!.get(position).productName)
        // holder.tv_price.setText(price)
        // holder.tv_validity.setText(validity)

        /*https://stackoverflow.com/questions/12116092/android-random-string-generator*/

        for (i in originalData!!.indices){

            val data=SaveProduct()
            data.productId=originalData!![i].productId
            data.productName=originalData!![i].productName
            data.productPrice=originalData!![i].productPrice
            data.productqty="0.0"
            saveDataArray!!.add(data)
        }

      /*  holder.et_proqty.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(
                v: View?,
                keyCode: Int,
                event: KeyEvent?
            ): Boolean {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    if (saveDataArray!!.size>0){
                        for (j in 0 until saveDataArray!!.size){

                            if (originalData[j].productId.equals(saveDataArray!![j].productId)){

                                saveDataArray!!.removeAt(j)
                                break

                            }

                        }
                    }

                   // Toast.makeText(conetext,saveDataArray!!.size.toString(),Toast.LENGTH_SHORT).show()
                }
                return false
            }
        })*/
        holder.et_proqty.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val name = s.toString()
                if (!name.isEmpty()){

                    val data=SaveProduct()
                    data.productId=originalData!![position].productId
                    data.productName=originalData!![position].productName
                    data.productPrice=originalData!![position].productPrice
                    data.productqty=name.toDouble().toString()
                    saveDataArray!!.set(position,data)


                      // Toast.makeText(conetext,saveDataArray!!.size.toString(),Toast.LENGTH_SHORT).show()
                }
                else{

                   //  Toast.makeText(conetext,saveDataArray!!.size.toString(),Toast.LENGTH_SHORT).show()
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val name = s.toString()





            }
        })

        placeOrderNow.setOnClickListener {


            if (saveDataArray!!.size>0){


                //Toast.makeText(conetext,saveDataArray!!.size.toString(),Toast.LENGTH_SHORT).show()
                validation()
            }
            else{

                Toast.makeText(conetext,"Please Enter Product Quantity",Toast.LENGTH_SHORT).show()
            }
        }






    }


    fun setHistoryListItems(context: Activity,
                            historyList: ArrayList<ProductInfo>
    ){
        this.originalData = historyList
        this.filteredData = ArrayList()
        this.filteredData!!.addAll(historyList)
        this.conetext=context


        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tv_nametxt: TextView = itemView!!.findViewById(R.id.productName)
        val et_proqty:EditText=itemView!!.findViewById(R.id.prduct_qty)




    }



    // Filter Class
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        originalData!!.clear()
        if (charText.length == 0) {
            originalData!!.addAll(filteredData!!)
        } else {
            for (wp in filteredData!!) {
                if (wp.productName.toLowerCase(Locale.getDefault()).contains(charText)||
                    wp.productPrice.contains(charText)
                ) {
                    originalData!!.add(wp)
                }
            }
        }

        notifyDataSetChanged()
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun validation() {





        val cancel = false
        val message = ""
        val focusView: View? = null
        val tempCond = false




        if (cancel) {
            // focusView.requestFocus();
            if (!tempCond) {
                focusView!!.requestFocus()
            }
            showToastLong(message)
        } else {
            val imm = conetext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

           // imm.hideSoftInputFromWindow(conetext.getWindowToken(), 0)

            try {


                reqEntity = MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE
                )

                reqEntity!!.addPart("user_id", StringBody(psh.id))

                reqEntity!!.addPart("customerId", StringBody(custID))

                if (!type.equals("")){

                    reqEntity!!.addPart("order_type", StringBody("2"))
                }
                else{
                    reqEntity!!.addPart("order_type", StringBody(""))
                }

                for (j in 0 until saveDataArray!!.size){

                    if (!saveDataArray!![j].productqty.equals("0.0")){

                        val key1="products["+j+"][productId]"

                        reqEntity!!.addPart(key1, StringBody(saveDataArray!![j].productId.toString()))

                        val key2="products["+j+"][orderQty]"

                        reqEntity!!.addPart(key2, StringBody(saveDataArray!![j].productqty.toString()))
                    }



                }






            } catch (e: Exception) {
                e.printStackTrace()
            }


            if(!PostInterface.isConnected(conetext)){

                Toast.makeText(conetext, conetext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
            else{

                progressDialog.setMessage("Please wait ...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                val editProfileAsyncTask = UploadFileToServer()
                editProfileAsyncTask.execute(null as Void?)


            }




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
            val httppost = HttpPost(PostInterface.BaseURL + "place-order")

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

                    saveDataArray!!.clear()
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
            Log.e(TAG, "Response from server: $result")

            try {
                if (responseString != "") {

                    val jsonObject = JSONObject(responseString)
                    // val ddd =jsonObject.getJSONObject("result")
                    val Ack = jsonObject.getString("status").toInt()
                    val msg=jsonObject.getString("message")
                    if (Ack == 200) {
                        progressDialog!!.dismiss()
                        progressDialog!!.cancel()
                      //  showToastLong("Successfully Updated")
                        saveDataArray!!.clear()
                        showToastLong(msg)
                        if (!type.equals("")){

                            conetext!!.startActivity(Intent(conetext, CustomerListActivity::class.java))
                        }
                        else{
                            conetext!!.startActivity(Intent(conetext, OutletVisitActivity::class.java))
                        }

                        //finish();


                    } else {
                        progressDialog!!.dismiss()
                        progressDialog!!.cancel()
                        showToastLong(msg)
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




    private fun showToastLong(message: String) {
        Toast.makeText(conetext, message, Toast.LENGTH_LONG).show()


    }
}
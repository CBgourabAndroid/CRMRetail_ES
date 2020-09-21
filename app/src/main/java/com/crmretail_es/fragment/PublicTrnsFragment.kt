package com.crmretail_es.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.AppController
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.activity.AddTravelDetails
import com.crmretail_es.adapter.PublicDataAdapter
import com.crmretail_es.modelClass.PublicTravelModel
import com.crmretail_es.shared.UserShared
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PublicTrnsFragment  : Fragment() {


    lateinit var root: View
    lateinit var currentDateStart: String
    var calendar = Calendar.getInstance()
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH)
    var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    var hour = calendar.get(Calendar.HOUR_OF_DAY)
    var minute = calendar.get(Calendar.MINUTE)
    lateinit var datePickerDialog: DatePickerDialog
    lateinit var foodingDate: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var addFood: TextView
    lateinit var saveData: TextView
    lateinit var adapter: PublicDataAdapter
    lateinit var publicDataList: ArrayList<PublicTravelModel>
    var reqEntity : MultipartEntity?=null
    internal var responseString: String? = null
    lateinit var psh: UserShared
    lateinit var dl_upload_file: File
    lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_travaling, container, false)
        //setHasOptionsMenu(true)
        psh= UserShared(context)
        progressDialog = ProgressDialog(context)
        publicDataList= ArrayList()

        inti(root)
        return root
    }

    @SuppressLint("NewApi")
    private fun inti(root: View) {
        currentDateStart=""

        recyclerView=root.findViewById(R.id.recyv_fooding)
        addFood=root.findViewById(R.id.add_foodBtn)
        foodingDate=root.findViewById(R.id.fooding_date_txt)
        saveData=root.findViewById(R.id.submitFooding)


        addFood.setOnClickListener {

            val intent = Intent(context, AddTravelDetails::class.java)
            intent.putExtra("from","public")
            startActivityForResult(intent, 126)
        }

        setData()
        saveData.setOnClickListener {


            validation()

        }
        foodingDate.setOnClickListener {

            dateFun()
        }


    }

    private fun dateFun() {


        datePickerDialog = DatePickerDialog(
            requireContext(),
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

                val sss= PostInterface.getCalculatedDate("yyyy-MM-dd", -7)
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val beforeDate: Date = sdf.parse(sss)
                val current: Date = sdf.parse(fyear.toString() + "-" + mt + "-" + dy)
                if(beforeDate.before(current)){
                    currentDateStart = fyear.toString() + "-" + mt + "-" + dy
                    foodingDate.setText(PostInterface.format_date(currentDateStart))
                }
                else{
                    Toast.makeText(context,"More then 7days not allowed!!", Toast.LENGTH_SHORT).show()
                }


            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()


    }

    private fun setData() {


         if (publicDataList.size>0){

             adapter = PublicDataAdapter(requireContext())
             // recyclerViewCategory.layoutManager = GridLayoutManager(this,3) as RecyclerView.LayoutManager?

             recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
             recyclerView.adapter = adapter
             adapter.setDataListItems(publicDataList)
         }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        // super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode==126){

                val returnValuetravelType = data.getStringExtra("travelType")

                if (!returnValuetravelType.equals("")){

                    val model= PublicTravelModel()
                    model.travelType=data.getStringExtra("travelType")
                    model.publicTravelWith=data.getStringExtra("publictranswith")
                    model.publicStartLocation=data.getStringExtra("startLoc")
                    model.publicEndLocation=data.getStringExtra("endLoc")
                    model.publicAmt=data.getStringExtra("Amt")
                    val myFile = File(data.getStringExtra("picture"))
                    model.supportingPic=myFile
                    publicDataList.add(model)

                    setData()
                }






            }


            else {

                // failed to capture image
                Toast.makeText(
                    context,
                    "Sorry! Failed ", Toast.LENGTH_SHORT
                )
                    .show()

            }
        } else {
            Toast.makeText(
                context,
                "User cancelled ", Toast.LENGTH_SHORT
            )
                .show()
        }


    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun validation() {



        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false

        if (TextUtils.isEmpty(currentDateStart)) {
            message = "Enter Lodging Date"
            focusView = foodingDate
            cancel = true
            tempCond = false
        }
        if (publicDataList.size<1) {
            message = "Enter Add Public Travelling Details"
            focusView = addFood
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
            val imm = requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager




            // imm.hideSoftInputFromWindow(conetext.getWindowToken(), 0)

            try {


                reqEntity = MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE
                )



                reqEntity!!.addPart("user_id", StringBody(psh.id))
                reqEntity!!.addPart("type", StringBody("3"))
                reqEntity!!.addPart("exp_date", StringBody(currentDateStart))

                for (j in 0 until publicDataList.size){

                    val key1="expenses["+j+"][purpose]"

                    reqEntity!!.addPart(key1, StringBody(publicDataList[j].publicTravelWith))


                    val key2="expenses["+j+"][amount]"

                    reqEntity!!.addPart(key2, StringBody(publicDataList[j].publicAmt))

                    val key3="expenses["+j+"][stFrom]"

                    reqEntity!!.addPart(key3, StringBody(publicDataList[j].publicAmt))

                    val key4="expenses["+j+"][endTo]"

                    reqEntity!!.addPart(key4, StringBody(publicDataList[j].publicAmt))


                    val key5="file_url_"+j
                   // val myUri: Uri = Uri.parse(publicDataList[j].supportingPic)
                    dl_upload_file = publicDataList[j].supportingPic.absoluteFile
                    reqEntity!!.addPart(key5, FileBody(dl_upload_file))


                }






            } catch (e: Exception) {
                e.printStackTrace()
            }


            if(!PostInterface.isConnected(requireContext())){

                Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getRealPathFromURI_API19(context: Context, uri: Uri): String? {
        var filePath = ""
        try { // FIXME NPE error when select image from QuickPic, Dropbox etc

            val wholeID = DocumentsContract.getDocumentId(uri)
            // Split at colon, use second item in the array
            val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val column = arrayOf(MediaStore.Images.Media.DATA)
            // where id is equal to
            val sel = MediaStore.Images.Media._ID + "=?"
            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null
            )
            val columnIndex = cursor!!.getColumnIndex(column[0])
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex)
            }

            cursor.close()

            return filePath
        } catch (e: Exception) { // this is the fix lol
            val result: String?
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = uri.path
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
                cursor.close()
            }
            return result
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
            val httppost = HttpPost(PostInterface.BaseURL + "expenses")

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
                        publicDataList.clear()
                        recyclerView.visibility=View.GONE
                        currentDateStart=""
                        foodingDate.setText("")
                        foodingDate.setHint("Enter expense date")
                        // finish()


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

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    }
}
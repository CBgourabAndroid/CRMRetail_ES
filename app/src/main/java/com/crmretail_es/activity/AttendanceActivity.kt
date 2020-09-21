package com.crmretail_es.activity

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.modelClass.attandence.AttendanceResponce
import com.crmretail_es.modelClass.attandence.Duty
import com.crmretail_es.shared.UserShared
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sun.bob.mcalendarview.MCalendarView
import sun.bob.mcalendarview.MarkStyle
import sun.bob.mcalendarview.listeners.OnDateClickListener
import sun.bob.mcalendarview.vo.DateData
import java.util.*
import kotlin.collections.ArrayList


class AttendanceActivity: AppCompatActivity() {

    lateinit var  calendarView : MCalendarView
    lateinit var psh:UserShared
    lateinit var progressDialog:ProgressDialog
    var im_back: ImageView? = null
    lateinit var dataList:ArrayList<Duty>
    var ly_left: LinearLayout? = null
    var ly_right: LinearLayout? = null
    private var selectedDate: DateData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.attendance_activity)
        psh= UserShared(this)
        progressDialog=ProgressDialog(this)

        init()
        im_back!!.setOnClickListener { finish() }


        if(!PostInterface.isConnected(this@AttendanceActivity)){

            Toast.makeText(this@AttendanceActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
        else{

            progressDialog.setMessage("Please wait ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            callDataList()
        }



       // getSundays()


    }

    private fun getSundays() {


        val calendar: Calendar = Calendar.getInstance()
        selectedDate = DateData(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        calendarView.markDate(selectedDate)


        /*calendarView.markDate(DateData( 2020,8,14).setMarkStyle(MarkStyle(MarkStyle.BACKGROUND,
            Color.parseColor("#EBF11707"))))*/


    }

    fun init() {

        im_back = findViewById<View>(R.id.image_back) as ImageView
        ly_left = findViewById<View>(R.id.layout_left) as LinearLayout
        ly_right = findViewById<View>(R.id.layout_right) as LinearLayout


        calendarView=findViewById(R.id.calendar)

        // calendarView.setDateCell(R.layout.layout_date_cell).setMarkedCell(R.layout.layout_mark_cell);





    }

    private fun callDataList() {



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.attendence("Bearer "+psh.accessToken,psh.id).enqueue(object :
            Callback<AttendanceResponce> {
            override fun onResponse(call: Call<AttendanceResponce>, response: Response<AttendanceResponce>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.duties
                    Log.i("onSuccess", avv.toString());
                    if (avv.size === 0) {

                    } else {

                    }

                    if (avv.size>0) {

                        dataList= ArrayList()

                        for (j in 0 until avv.size){

                            val model= Duty()
                            model.dutyDate=avv[j].dutyDate
                            model.dutyStart=avv[j].dutyStart
                            model.dutyEnd=avv[j].dutyEnd
                            model.dutyTime=avv[j].dutyTime
                            model.remarks=avv[j].remarks
                            dataList.add(model)



                        }


                        setData(dataList)





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

            override fun onFailure(call: Call<AttendanceResponce>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }

    private fun setData(dataList: ArrayList<Duty>) {





        for (i in 0 until dataList.size) {
            /*calendarView.markDate(
                PostInterface.format_date6(dataList[i].dutyDate).toInt(),
                PostInterface.format_date5(dataList[i].dutyDate).toInt(),
                PostInterface.format_date4(dataList[i].dutyDate).toInt()
            ) //mark multiple dates with this code.*/

           



                    if (dataList[i].remarks.equals("Present")){
                        calendarView.markDate(DateData( PostInterface.format_date6(dataList[i].dutyDate).toInt(),
                            PostInterface.format_date5(dataList[i].dutyDate).toInt(),
                            PostInterface.format_date4(dataList[i].dutyDate).toInt()).setMarkStyle(MarkStyle(MarkStyle.BACKGROUND,
                            Color.parseColor("#1A951F"))))

                    }
                  else if (dataList[i].remarks.equals("Absent")){

                        calendarView.markDate(DateData( PostInterface.format_date6(dataList[i].dutyDate).toInt(),
                            PostInterface.format_date5(dataList[i].dutyDate).toInt(),
                            PostInterface.format_date4(dataList[i].dutyDate).toInt()).setMarkStyle(MarkStyle(MarkStyle.BACKGROUND,
                            Color.parseColor("#EBF11707"))))

                    }
                    else if (dataList[i].remarks.equals("Half Day")){

                        calendarView.markDate(DateData( PostInterface.format_date6(dataList[i].dutyDate).toInt(),
                            PostInterface.format_date5(dataList[i].dutyDate).toInt(),
                            PostInterface.format_date4(dataList[i].dutyDate).toInt()).setMarkStyle(MarkStyle(MarkStyle.BACKGROUND,
                            Color.parseColor("#F36F21"))))

                    }
                 /* else{
                        calendarView.markDate(DateData( PostInterface.format_date6(dataList[i].dutyDate).toInt(),
                            PostInterface.format_date5(dataList[i].dutyDate).toInt(),
                            PostInterface.format_date4(dataList[i].dutyDate).toInt()).setMarkStyle(MarkStyle(MarkStyle.BACKGROUND,
                            Color.parseColor("#EBDF0F00"))))

                    }*/
        }






        calendarView.setOnDateClickListener(object : OnDateClickListener() {
            override fun onDateClick(view: View?, date: DateData) {

                var mt=""
                var dy=""

                if (date.month < 10) {
                    mt = "0" + date.month.toString()
                } else {
                    mt = date.month.toString()
                }

                if (date.day < 10) {

                    dy = "0" + date.day.toString()
                } else {

                    dy = date.day.toString()
                }

                val ddd=date.year.toString()+"-"+mt.toString()+"-"+dy

                showDetails(ddd)

            }
        })

        /* calendarView.setOnDateClickListener(object : OnDateClickListener() {
            override fun onDateClick(view: View?, date: DateData) {
                Toast.makeText(
                    this@AttendanceActivity,
                    String.format("%d-%d", date.month, date.day),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }).setOnMonthChangeListener(object : OnMonthChangeListener() {
            override fun onMonthChange(year: Int, month: Int) {
                (findViewById<View>(R.id.ind) as TextView).text = String.format(
                    "%d-%d",
                    year,
                    month
                )
                //                Toast.makeText(MainActivity.this, String.format("%d-%d", year, month), Toast.LENGTH_SHORT).show();
//                calendarView.markDate(year, month, 5);
//                MarkedDates.getInstance().notifyObservers();
            }
        }).setMarkedStyle(MarkStyle.BACKGROUND)

            .markDate(2020, 7, 1).markDate(2020, 7, 25)
            .markDate(2020, 7, 15)
            .markDate(DateData(2020, 7, 8).setMarkStyle(MarkStyle(MarkStyle.BACKGROUND, Color.GREEN)))
            .hasTitle(false)
*/



    }

    private fun showDetails(StrDate: String) {

        var aa=""
        var bb=""
        var cc=""
        var dd=""


        for (i in 0 until dataList.size) {

            val jjj=dataList[i].dutyDate.toString()




            if (StrDate.equals(jjj)){
                aa=dataList[i].dutyStart.toString()
                bb=dataList[i].dutyEnd.toString()
                cc=dataList[i].dutyTime.toString()+"hr"
                dd=dataList[i].remarks.toString()

                val sss=dataList[i].dutyEnd.toString()

                  if (!sss.equals("")){
                      showMP(StrDate,aa,bb,cc,dd)
                   }
                  else{
                    showPP(StrDate)
               }
            }




        }



    }

    private fun showPP(strDate: String) {

        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Duty Date")
        //set message for alert dialog
        builder.setMessage(strDate)
        //builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Ok"){dialogInterface, which ->

            dialogInterface.cancel()

            //Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("View Details"){dialogInterface, which ->
            dialogInterface.cancel()

            viewDDetails()

        }


        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

        //performing positive action

    }

    private fun viewDDetails() {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Absent")
        //set message for alert dialog
        builder.setMessage("You don't stop the duty on that day!!")
        //builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Ok"){dialogInterface, which ->

            dialogInterface.cancel()

            //Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
        }


        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun showMP(strDate: String, aa: String, bb: String, cc: String, dd: String) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Duty Date")
        //set message for alert dialog
        builder.setMessage(strDate)
        //builder.setIcon(android.R.drawable.ic_dialog_alert)
//performing positive action
        builder.setPositiveButton("Ok"){dialogInterface, which ->
            dialogInterface.cancel()
            //Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("View Details"){dialogInterface, which ->
            dialogInterface.cancel()

            viewDetails(aa,bb,cc,dd)


        }

        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()



    }

    private fun viewDetails(aa: String, bb: String, cc: String, dd: String) {

        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(dd)
        //set message for alert dialog
        builder.setMessage("Duty Start On  : "+aa+"\nDuty End At: "+bb+"\nTotal Working Hours : "+cc
                )
        //builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Ok"){dialogInterface, which ->

            dialogInterface.cancel()

            //Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
        }


        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
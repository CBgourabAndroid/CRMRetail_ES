package com.crmretail_es.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.adapter.CalanderAdapter
import com.crmretail_es.modelClass.Duty
import com.crmretail_es.modelClass.DutyResponce
import com.crmretail_es.shared.UserShared
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.CompactCalendarView.CompactCalendarViewListener
import com.riontech.calendar.dao.EventData
import com.riontech.calendar.dao.dataAboutDate
import com.riontech.calendar.utils.CalendarUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DutyPlaner: AppCompatActivity() {

    lateinit var progressDialog:ProgressDialog
    lateinit var psh:UserShared
    var compactCalendarView: CompactCalendarView? = null
    var tx_date: TextView? = null
    var tx_today:TextView? = null
    var ly_detail: LinearLayout? = null
    var ly_left: LinearLayout? = null
    var ly_right:LinearLayout? = null
    var im_back: ImageView? = null
    var recyclerView: RecyclerView? = null
    var tx_item: TextView? = null
    var df: SimpleDateFormat? = null
    var formattedDate: String? = null
    lateinit var mAdapter :CalanderAdapter
    private val simpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val DateFormat =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var c: Date? = null
  //  private var customCalendar: CustomCalendar? = null

    lateinit var dataList:ArrayList<Duty>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.activity_calander)

        progressDialog= ProgressDialog(this)
        psh= UserShared(this)

         init()
         calendarlistener()
         Setdate()



        if(!PostInterface.isConnected(this@DutyPlaner)){

            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
        else{

            progressDialog.setMessage("Please wait ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            callDataList(formattedDate)
        }

        tx_item!!.text = "$formattedDate No events available in this day"

        ly_right!!.setOnClickListener {
            compactCalendarView!!.showCalendarWithAnimation()
            compactCalendarView!!.showNextMonth()
        }

        ly_left!!.setOnClickListener {
            compactCalendarView!!.showCalendarWithAnimation()
            compactCalendarView!!.showPreviousMonth()
        }

        tx_today!!.setOnClickListener {
            val intent = Intent(this@DutyPlaner, DutyPlaner::class.java)
            startActivity(intent)
            finish()
        }

        im_back!!.setOnClickListener { finish() }

        // customCalendar = findViewById<View>(R.id.customCalendar) as CustomCalendar


    }

    private fun calendarlistener() {
        compactCalendarView!!.setListener(object : CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                //EventViewAsy().execute("" + id, DateFormat.format(dateClicked), user_type)

                if(!PostInterface.isConnected(this@DutyPlaner)){

                    Toast.makeText(this@DutyPlaner, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
                else{

                    progressDialog.setMessage("Please wait ...")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    callDataList(DateFormat.format(dateClicked))
                }
                tx_item!!.setText(DateFormat.format(dateClicked) + " No events available in this day")
            }



            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                compactCalendarView!!.removeAllEvents()
                tx_date!!.setText(simpleDateFormat.format(firstDayOfNewMonth))
                if(!PostInterface.isConnected(this@DutyPlaner)){

                    Toast.makeText(this@DutyPlaner, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
                else{

                    progressDialog.setMessage("Please wait ...")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    callDataList(DateFormat.format(firstDayOfNewMonth))
                }
               // EventListAsy().execute("" + id, DateFormat.format(firstDayOfNewMonth), user_type)
               // EventViewAsy().execute("" + id, DateFormat.format(firstDayOfNewMonth), user_type)
                tx_item!!.setText(DateFormat.format(firstDayOfNewMonth) + " No events available in this day")
            }
        })
    }

    //get current date
    fun Setdate() {
        c = Calendar.getInstance().time
        df = SimpleDateFormat("yyyy-MM-dd")
        formattedDate = df!!.format(c)
    }

    //variable initialization
    fun init() {
        compactCalendarView =
            findViewById<View>(R.id.compactcalendar_view) as CompactCalendarView
        tx_date = findViewById<View>(R.id.text) as TextView
        ly_left = findViewById<View>(R.id.layout_left) as LinearLayout
        ly_right = findViewById<View>(R.id.layout_right) as LinearLayout
        im_back = findViewById<View>(R.id.image_back) as ImageView
        tx_today = findViewById<View>(R.id.text_today) as TextView
        ly_detail = findViewById<View>(R.id.layout_detail) as LinearLayout
        recyclerView = findViewById<View>(R.id.list_recycleView) as RecyclerView
        tx_item = findViewById<View>(R.id.text_item) as TextView




    }



    private fun callDataList(datetoshow: String?) {



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.dutyplaner("Bearer "+psh.accessToken,psh.id).enqueue(object :
            Callback<DutyResponce> {
            override fun onResponse(call: Call<DutyResponce>, response: Response<DutyResponce>) {
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

                            if (avv[j].dutyDate.equals(datetoshow)){

                                val model=Duty()
                                model.dutyId=avv[j].dutyId
                                model.zoneName=avv[j].zoneName
                                model.dutyDate=avv[j].dutyDate
                                model.status=avv[j].status
                                dataList.add(model)
                            }



                        }

                        tx_item!!.visibility = View.GONE
                        recyclerView!!.visibility = View.VISIBLE

                        setData(dataList)







                    } else {
                        // Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        tx_item!!.visibility = View.VISIBLE
                        recyclerView!!.visibility = View.GONE
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Fail!!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DutyResponce>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }

    private fun setData(duties: List<Duty>) {

    /*    for (j in 0 until duties.size) {

            val eventCount = 3
            customCalendar!!.addAnEvent(duties.get(j).dutyDate, eventCount, getEventDataList(eventCount))

        }*/


        mAdapter= CalanderAdapter()
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL ,false)
        recyclerView!!.adapter = mAdapter
        mAdapter.setHistoryListItems(this@DutyPlaner,
            duties as ArrayList<Duty>
        )

    }


    fun getEventDataList(count: Int): ArrayList<EventData?>? {
        val eventDataList: ArrayList<EventData?> = ArrayList<EventData?>()
        for (i in 0 until count) {
            val dateData = EventData()
            val dataAboutDates: ArrayList<dataAboutDate?> =ArrayList<dataAboutDate?>()
            dateData.section = CalendarUtils.getNAMES()[Random().nextInt(CalendarUtils.getNAMES().size)]
            val dataAboutDate = dataAboutDate()
            val index = Random().nextInt(CalendarUtils.getEVENTS().size)
            dataAboutDate.title = CalendarUtils.getEVENTS()[index]
            dataAboutDate.subject = CalendarUtils.getEventsDescription()[index]
            dataAboutDates.add(dataAboutDate)
            dateData.data = dataAboutDates
            eventDataList.add(dateData)
        }
        return eventDataList
    }
}
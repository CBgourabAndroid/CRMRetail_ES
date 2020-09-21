package com.crmretail_es

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.crmretail_es.modelClass.*
import com.crmretail_es.modelClass.attandence.AttendanceResponce
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


interface PostInterface {





    companion object {
        //const val BaseURL = "http://api.shakambharigroup.in/api/v1/"
        const val BaseURL = "http://14.99.107.86:8004/api/v1/"


        fun isConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

        fun format_date(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val d = SimpleDateFormat("EEE, dd MMM yyyy")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }


        fun format_date4(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val d = SimpleDateFormat("dd")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }

        fun format_date5(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val d = SimpleDateFormat("MM")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }

        fun format_date6(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val d = SimpleDateFormat("yyyy")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }


        fun format_date3(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val d = SimpleDateFormat("MMM")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }

        fun format_edatem(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("dd-MM-yyyy")
            val d = SimpleDateFormat("MMM")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }

        fun format_edated(fdate: String): String {

            // "Sun, 14 Aug\\n2019 5:30Am"

            var datetime: String? = null
            val inputFormat = SimpleDateFormat("dd-MM-yyyy")
            val d = SimpleDateFormat("dd")
            try {
                val convertedDate = inputFormat.parse(fdate)
                datetime = d.format(convertedDate!!)

            } catch (e: ParseException) {

            }

            return datetime!!


        }

        fun getCalculatedDate(dateFormat: String?, days: Int): String? {
            val cal: Calendar = Calendar.getInstance()
            val s = SimpleDateFormat(dateFormat)
            cal.add(Calendar.DAY_OF_YEAR, days)
            return s.format(Date(cal.getTimeInMillis()))
        }

        @SuppressLint("SimpleDateFormat")
        fun checkDate(startDate: String?, endDate: String?): Boolean {
            var b = false
            val format2 = SimpleDateFormat("hh:mm")
            try {
                val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                val odf: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                val cud = SimpleDateFormat("yyyy-MM-dd").parse(startDate)
                val cld = SimpleDateFormat("yyyy-MM-dd").parse(endDate)
                val strD = inputFormat.format(cud)
                val endD = inputFormat.format(cld)
                b = if (inputFormat.parse(endD).before(inputFormat.parse(strD))) {
                    false // If start date is before end date.
                } else {
                    true // If start date is after the end date.
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return b
        }


        @SuppressLint("SimpleDateFormat")
        fun checkTime(startDate: String?, endDate: String?): Boolean {
            var b = false
            val format2 = SimpleDateFormat("hh:mm")
            try {
                val inputFormat: DateFormat = SimpleDateFormat("HH:mm:ss")
                val odf: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                val cud = SimpleDateFormat("HH:mm:ss").parse(startDate)
                val cld = SimpleDateFormat("HH:mm:ss").parse(endDate)
                val strD = inputFormat.format(cud)
                val endD = inputFormat.format(cld)
                b = if (inputFormat.parse(endD).before(inputFormat.parse(strD))) {
                    false // If start date is before end date.
                } else {
                    true // If start date is after the end date.
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return b
        }

    }

    @FormUrlEncoded
    @POST("login")
    fun loginCall( @Field("email") email: String,
                   @Field("password") password: String,
                   @Field("app_type") app_type: String,
                   @Field("user_type") user_type: String,
                   @Field("lati") lati: String,
                   @Field("longi") longi: String): Call<LoginResponse>



    @FormUrlEncoded
    @POST("logout")
    fun logoutCall(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String,
                   @Field("lati") lati: String,
                   @Field("longi") longi: String): Call<GeneralResponce>

    /*@FormUrlEncoded
    @POST("duty-start")
    fun startDuty( @Header("Authorization") String token,
                   @Field("user_id") user_id: String,
                   @Field("lati") lati: String,
                   @Field("longi") longi: String
                   ): Call<LoginResponse>*/
    @FormUrlEncoded
    @POST("duty-activity")
    fun startDuty(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String,
        @Field("lati") lati: String,
        @Field("longi") longi: String,
        @Field("duty_type") duty_type: String,
        @Field("customerId")customerId:String
    ): Call<GeneralResponce>


    @FormUrlEncoded
    @POST("outstanding")
    fun outstanding(
        @Header("Authorization") token: String,
        @Field("customerId") customerId: String
    ): Call<OutstandingResponce>


    @FormUrlEncoded
    @POST("payments")
    fun payments(
        @Header("Authorization") token: String,
        @Field("customerId") customerId: String
    ): Call<PaymentResponce>


    @FormUrlEncoded
    @POST("ledger")
    fun ledger(
        @Header("Authorization") token: String,
        @Field("customer_id") customerId: String
    ): Call<LedgerResponse>

    @FormUrlEncoded
    @POST("all-customer")
    fun getcustomer(
        @Header("Authorization") token: String,
        @Field("user_id") customerId: String
    ): Call<CustomerResponse>

    @FormUrlEncoded
    @POST("not-closed")
    fun getFeedbacklist(
        @Header("Authorization") token: String,
        @Field("user_id") customerId: String
    ): Call<FeedbackResponse>

    @FormUrlEncoded
    @POST("not-closed-by-id")
    fun getFeedbackDetails(
        @Header("Authorization") token: String,
        @Field("registration_id") customerId: String
    ): Call<FeedbackDetailsResponse>


    @FormUrlEncoded
    @POST("billing")
    fun billing(
        @Header("Authorization") token: String,
        @Field("customerId") customerId: String
    ): Call<BillResponce>


    @FormUrlEncoded
    @POST("duties")
    fun dutyplaner(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String
    ): Call<DutyResponce>

    @FormUrlEncoded
    @POST("holidays")
    fun holidays(
        @Header("Authorization") token: String,
        @Field("id") id: String
    ): Call<HolydayResponse>


    @FormUrlEncoded
    @POST("leave-apply")
    fun applyLeave(
        @Header("Authorization") token: String,
        @Field("id") id: String,
        @Field("fromDate") fromDate: String,
        @Field("toDate") toDate: String,
        @Field("reason") reason: String

    ): Call<GeneralResponce2>

    @FormUrlEncoded
    @POST("attendance")
    fun attendence(
        @Header("Authorization") token: String,
        @Field("id") user_id: String
    ): Call<AttendanceResponce>


    @FormUrlEncoded
    @POST("get-user-location")
    fun getLocationList(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String
    ): Call<LocationResponce>



    @FormUrlEncoded
    @POST("save-discussion")
    fun discussionCall(@Header("Authorization") token: String,
                   @Field("discussion") discussion: String,
                   @Field("reg_id") reg_id: String,
                   @Field("lati") lati: String,
                   @Field("longi") longi: String): Call<GeneralResponce2>


    @FormUrlEncoded
    @POST("event-list")
    fun getEventList(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String
    ): Call<EventListResponse>


    @FormUrlEncoded
    @POST("event-occurance")
    fun startEvent(
        @Header("Authorization") token: String,
        @Field("event_id") event_id: String,
        @Field("start_time") start_time: String,
        @Field("start_lati") lati: String,
        @Field("start_longi") longi: String,
        @Field("occurance_type") duty_type: String
    ): Call<GeneralResponce2>

    @FormUrlEncoded
    @POST("event-occurance")
    fun endEvent(
        @Header("Authorization") token: String,
        @Field("event_id") event_id: String,
        @Field("end_time") start_time: String,
        @Field("end_lati") lati: String,
        @Field("end_longi") longi: String,
        @Field("occurance_type") duty_type: String,
        @Field("remarks") remarks: String

    ): Call<GeneralResponce2>

    @FormUrlEncoded
    @POST("leave-list")
    fun getLeaveList(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: String
    ): Call<LeaveListResponse>

    @FormUrlEncoded
    @POST("order-list")
    fun getOrderList(
        @Header("Authorization") token: String,
        @Field("customer_id") customer_id: String
    ): Call<OrderListResponse>






}
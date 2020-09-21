package com.crmretail_es.adapter

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.SplashScreen
import com.crmretail_es.activity.EventImageActivity
import com.crmretail_es.modelClass.EventList
import com.crmretail_es.modelClass.GeneralResponce2
import com.crmretail_es.shared.Updatedlatlong
import com.crmretail_es.shared.UserShared
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventListAdapter  : RecyclerView.Adapter<EventListAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var dataList: ArrayList<EventList>
    lateinit var psh: UserShared
    lateinit var progressDialog: ProgressDialog
    lateinit var latPsh: Updatedlatlong
    lateinit var prefss: SharedPreferences

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var lbl_textViewType: TextView
        internal var lbl_textViewtime: TextView
        internal var lbl_textViewloc: TextView

        internal var lbl_textViewbud: TextView
        internal var lbl_textViewna: TextView

        internal var lbl_firstD: TextView
        internal var lbl_monthD: TextView

        internal var menuImg: ImageView


        init {

            lbl_textViewType = itemView.findViewById(R.id.textViewType)
            lbl_textViewtime = itemView.findViewById(R.id.textViewtime)
            lbl_textViewloc = itemView.findViewById(R.id.textViewloc)
            lbl_textViewbud = itemView.findViewById(R.id.textViewbudget)
            lbl_textViewna = itemView.findViewById(R.id.textViewnoa)
            lbl_firstD=itemView.findViewById(R.id.firstD)
            lbl_monthD=itemView.findViewById(R.id.monthD)
            menuImg=itemView.findViewById(R.id.img_menu)





        }
    }

    fun setHistoryListItems(
        context: Context,
        dataList: ArrayList<EventList>

    ){
        this.dataList = dataList
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {


        psh= UserShared(context)
        prefss = context.getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)
        progressDialog= ProgressDialog(context)
        latPsh= Updatedlatlong(context)


        holder.lbl_textViewType.setText(dataList[listPosition].eventType.toString())
        holder.lbl_textViewtime.setText("Timming : "+dataList[listPosition].startTime.toString())
        holder.lbl_textViewbud.setText("Budget : ₹"+dataList[listPosition].budget.toString())
        holder.lbl_textViewna.setText("No Of Attendance : "+dataList[listPosition].noOfAttn.toString())
        if (dataList[listPosition].location!=null){
            holder.lbl_textViewloc.setText("Location : "+dataList[listPosition].location.toString())

        }
        else{
            holder.lbl_textViewloc.setText("Location : "+"Not Provided")

        }


        holder.lbl_monthD.setText(PostInterface.format_edatem(dataList[listPosition].eventDate))
        holder.lbl_firstD.setText(PostInterface.format_edated(dataList[listPosition].eventDate))


        holder.itemView.setOnClickListener {

            if (psh.eventSts.equals("")&&psh.eventID.equals("")){

                confirmTask(listPosition)
            }
            else if (psh.eventSts.equals("start")&&psh.eventID.equals(dataList[listPosition].eventId.toString())){

                confirmEnd(listPosition)
            }
            else{

                Toast.makeText(context,"Not Allowed",Toast.LENGTH_SHORT).show()
            }



        }




    }




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event_list, parent, false)


        return EventListAdapter.MyViewHolder(view)
    }



    override fun getItemCount(): Int {
        return dataList.size
    }

    private fun confirmTask(listPosition: Int) {


        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Do you want to Start this Event?")
            .setCancelable(false)
            .setPositiveButton("Confirm", DialogInterface.OnClickListener {



                    dialog, _ -> dialog.cancel()


                if(!PostInterface.isConnected(context)){

                    Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
                else{

                    progressDialog!!.setMessage("Please wait ...")
                    progressDialog!!.setCancelable(false)
                    progressDialog!!.show()
                    callStartEndEvent(listPosition)

                }





            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {


                    dialog, _ -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Alert")
        alert.show()

    }

    private var alertDialog: android.app.AlertDialog? = null

    private fun confirmEnd(listPosition: Int) {



        val inflater: LayoutInflater = LayoutInflater.from(context)
        val subView: View = inflater.inflate(R.layout.dialog_end_event, null)
        val event_Title=subView.findViewById<TextView>(R.id.eventTitle)
        val end_event=subView.findViewById<TextView>(R.id.endEvent)
        val add_event_pic=subView.findViewById<TextView>(R.id.addPicture)
        val remarks=subView.findViewById<EditText>(R.id.remarks_edt)

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        builder.setTitle(" ")
        builder.setView(subView)
        alertDialog = builder.create()
        alertDialog!!.setOnShowListener(object : DialogInterface.OnShowListener{
            override fun onShow(dialog: DialogInterface?) {
                val button: Button = alertDialog!!.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                if (button != null) {
                    button.setEnabled(false)
                }
            }
        })

        event_Title.setText(dataList[listPosition].eventType)

        add_event_pic.setOnClickListener {

            val intent = Intent(context, EventImageActivity::class.java)
            intent.putExtra("eventid",dataList[listPosition].eventId.toString())
            context.startActivity(intent)
            alertDialog!!.dismiss()
        }

        end_event.setOnClickListener {

            if (remarks.text.toString().isNotEmpty()){


                if(!PostInterface.isConnected(context)){

                    Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
                else{

                    progressDialog!!.setMessage("Please wait ...")
                    progressDialog!!.setCancelable(false)
                    progressDialog!!.show()
                    endEvent(listPosition,remarks.text.toString())

                }


            }
            else{

                Toast.makeText(context, "Please Provide Your Remarks About The Event!!", Toast.LENGTH_SHORT).show()


            }
        }


        builder.setPositiveButton(" ", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {}
        })
        builder.setNegativeButton(" ", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {}
        })
        alertDialog!!.show()

    }

    private fun callStartEndEvent( listPosition: Int) {


        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH:mm")
        val formatedTime = formatter.format(date)


        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.startEvent("Bearer "+psh.accessToken,dataList[listPosition].eventId.toString(),formatedTime,latPsh.userUpdatedLatitude,latPsh.userUpdatedLongitude,"1").enqueue(object :
            Callback<GeneralResponce2> {
            override fun onResponse(call: Call<GeneralResponce2>, response: Response<GeneralResponce2>) {
                println("onResponse")
                System.out.println(response.toString())
                val statusCode = response.code()
                if (response.code()==200){
                    progressDialog.dismiss()


                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {
                        progressDialog.dismiss()
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()


                        val editor = prefss.edit()
                        editor.putString(context.getString(R.string.shared_event_status), "start")
                        editor.putString(context.getString(R.string.shared_event_id), dataList[listPosition].eventId.toString())
                        editor.commit()



                    } else {
                        progressDialog.dismiss()
                        if (response.body()!!.message.equals("Event already started")){

                            val editor = prefss.edit()
                            editor.putString(context.getString(R.string.shared_event_status), "start")
                            editor.putString(context.getString(R.string.shared_event_id), dataList[listPosition].eventId.toString())
                            editor.commit()
                            Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        }
                        else{
                            Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        }

                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(context, "Login Expires"+statusCode, Toast.LENGTH_SHORT).show()

                    val myPrefs = context.getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)
                    val editor = myPrefs.edit()
                    editor.clear()
                    editor.apply()


                    val intent = Intent(context, SplashScreen::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)

                }
            }

            override fun onFailure(call: Call<GeneralResponce2>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })


    }


    private fun endEvent(listPosition: Int, toString: String) {


        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH:mm")
        val formatedTime = formatter.format(date)


        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.endEvent("Bearer "+psh.accessToken,dataList[listPosition].eventId.toString(),formatedTime,latPsh.userUpdatedLatitude,latPsh.userUpdatedLongitude,"2",toString).enqueue(object :
            Callback<GeneralResponce2> {
            override fun onResponse(call: Call<GeneralResponce2>, response: Response<GeneralResponce2>) {
                println("onResponse")
                System.out.println(response.toString())
                val statusCode = response.code()
                if (response.code()==200){
                    progressDialog.dismiss()


                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {
                        progressDialog.dismiss()
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        alertDialog!!.dismiss()

                        val editor = prefss.edit()
                        editor.putString(context.getString(R.string.shared_event_status), "")
                        editor.putString(context.getString(R.string.shared_event_id), "")
                        editor.commit()



                    } else {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(context, "Login Expires"+statusCode, Toast.LENGTH_SHORT).show()

                    val myPrefs = context.getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)
                    val editor = myPrefs.edit()
                    editor.clear()
                    editor.apply()


                    val intent = Intent(context, SplashScreen::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)

                }
            }

            override fun onFailure(call: Call<GeneralResponce2>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })



    }




}
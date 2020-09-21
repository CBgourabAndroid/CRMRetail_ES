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
import com.crmretail_es.activity.*
import com.crmretail_es.modelClass.GeneralResponce
import com.crmretail_es.shared.Updatedlatlong
import com.crmretail_es.shared.UserShared
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainCatAdapter : RecyclerView.Adapter<MainCatAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var names: Array<String>
    lateinit var images: Array<Int>
    lateinit var psh:UserShared
    lateinit var progressDialog : ProgressDialog
    lateinit var latPsh: Updatedlatlong
    lateinit var prefss: SharedPreferences

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var lbl_tv: TextView
        internal var imgView: ImageView


        init {

            lbl_tv = itemView.findViewById(R.id.songname2)
            imgView = itemView.findViewById(R.id.img2)


        }
    }

    fun setDataListItems(
        context: Context,
        names: Array<String>,
        images: Array<Int>

        ){
        this.names = names
        this.images = images
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {
        psh=UserShared(context)
        progressDialog = ProgressDialog(context)
        latPsh=Updatedlatlong(context)
        prefss = context.getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)

        holder.lbl_tv.setText(names!![listPosition])
        holder.imgView.setImageResource(images[listPosition])


        holder.imgView.setOnClickListener {


            if (psh.dutyStatus) {

                if (names!![listPosition].equals("Visit")) {

                    if (psh.areaData!=null){

                        context!!.startActivity(Intent(context, OutletVisitActivity::class.java))
                    }
                    else{

                        Toast.makeText(context,"No Dealer Found!!",Toast.LENGTH_LONG).show()
                    }


                }
                else if (names!![listPosition].equals("Stop Duty")){

                    showDialogS()



                }
                else if (names!![listPosition].equals("Duty Planner")){

                    context!!.startActivity(Intent(context, DutyPlaner::class.java))
                }

                else if (names!![listPosition].equals("Register")){

                    context!!.startActivity(Intent(context, NewRegisterActivity::class.java))
                }

                else if (names!![listPosition].equals("Brand Track")){

                    context!!.startActivity(Intent(context, BrandTrackActivity::class.java))
                }
                else if(names!![listPosition].equals("Meeting Management")){

                    context!!.startActivity(Intent(context, AllEventListActivity::class.java))
                }
            }
            else{

                Toast.makeText(context,
                    "Off-Duty you are not allow!!", Toast.LENGTH_SHORT).show()
            }
        }



       /*     holder.itemView.setOnClickListener {

                if (psh.dutyStatus){

                if (names!![listPosition].equals("Visit")){

                    context!!.startActivity(Intent(context, OutletVisitActivity::class.java))
                }
                else if (names!![listPosition].equals("Stop Duty")){

                    check_Option()
                }
            }
                else{

                    Toast.makeText(context,
                        "Off-Duty you are not allow!!", Toast.LENGTH_SHORT).show()
                }
        }*/

    }


    private fun check_Option() {

        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Do you want to Stop Duty?\n\n Select Transport Type")
        val items =
            arrayOf("Public", "Private")
        val checkedItem = 1
        var SSS=""
        dialogBuilder.setSingleChoiceItems(
            items,
            checkedItem,
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 ->{

                        SSS="public"

                    }
                    1 -> {
                        SSS="private"
                    }

                }
            })
            .setCancelable(false)
            .setPositiveButton("Confirm", DialogInterface.OnClickListener {

                    dialog, _ -> dialog.cancel()

                if (SSS.equals("public")){

                    if(!PostInterface.isConnected(context)){

                        Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                    }
                    else{

                        progressDialog.setMessage("Please wait ...")
                        progressDialog.setCancelable(false)
                        progressDialog.show()
                        callStopDuty()


                    }
                }
                else{

                    context!!.startActivity(Intent(context, StopDutyActivity::class.java))


                }



            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {


                    dialog, _ -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Alert")
        alert.show()

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainCatAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_one, parent, false)


        return MainCatAdapter.MyViewHolder(view)
    }



    override fun getItemCount(): Int {
        return names.size
    }


    // Filter Class
    /*fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        originalData!!.clear()
        if (charText.length == 0) {
            originalData!!.addAll(filteredData!!)
        } else {
            for (wp in filteredData!!) {
                if (wp.specialityname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    originalData!!.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }*/

    private fun showDialogS() {

        val builder =
            AlertDialog.Builder(context)
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.my_dialog, null)
        val homeClick = view.findViewById<TextView>(R.id.startDutyClick)
        val addprivate = view.findViewById<TextView>(R.id.addPrivateDetails)
        val txt_tv = view.findViewById<TextView>(R.id.sbd_text_title)
        val temImg = view.findViewById<ImageView>(R.id.sbd_view_animation)
        val radGrp = view.findViewById<RadioGroup>(R.id.rbgrp)
        val rbgLayout=view.findViewById<LinearLayout>(R.id.rgb_lay)
        builder.setView(view)






            /*builder.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.dismiss() }*/
            val alertDialog = builder.create()
            alertDialog.show()
        temImg.visibility = View.GONE
        radGrp.visibility=View.GONE
        rbgLayout.visibility=View.GONE

        if (psh.dutyType.equals("Public")){
            txt_tv.setText("Do you want to Stop Duty for Today?\n\n\n You Have Used Public Transport For Today!!\n\n\n")
            homeClick.visibility = View.VISIBLE
            homeClick.setText("Stop Duty")
        }
        else{
            txt_tv.setText("Do you want to Stop Duty for Today?\n\n\n You Have Used Private Transport For Today!!\n\n\n")
            addprivate.visibility = View.VISIBLE
            addprivate.setText("Add Details")

        }



        addprivate.setOnClickListener {

            context!!.startActivity(Intent(context, StopDutyActivity::class.java))
            alertDialog.dismiss()
        }

        homeClick.setOnClickListener {

            //   start_duty.visibility=View.GONE


            if (!PostInterface.isConnected(context)) {

                Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT)
                    .show()
            } else {

                progressDialog.setMessage("Please wait ...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                callStopDuty()


            }
        }
    }




    private fun callStopDuty() {


        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.startDuty("Bearer "+psh.accessToken,psh.id,latPsh.userUpdatedLatitude,latPsh.userUpdatedLongitude,"4","").enqueue(object :
            Callback<GeneralResponce> {
            override fun onResponse(call: Call<GeneralResponce>, response: Response<GeneralResponce>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {

                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        val editor = prefss.edit()
                        editor.putBoolean(context.getString(R.string.shared_duty_status), false)
                        editor.commit()
                        context.startActivity(Intent(context, HomeActivity::class.java))


                    } else {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.dismiss()
                    
                    Toast.makeText(context, "Login Expires", Toast.LENGTH_SHORT).show()

                    val myPrefs = context.getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)
                    val editor = myPrefs.edit()
                    editor.clear()
                    editor.apply()


                    val intent = Intent(context, SplashScreen::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<GeneralResponce>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })

    }




}
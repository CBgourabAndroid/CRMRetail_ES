package com.crmretail_es.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.SplashScreen
import com.crmretail_es.activity.*
import com.crmretail_es.modelClass.GeneralResponce
import com.crmretail_es.modelClass.ZoneArray
import com.crmretail_es.shared.Updatedlatlong
import com.crmretail_es.shared.UserShared
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class AreaAdapter ( var conetext: Context,var originalData:ArrayList<ZoneArray>)
    : RecyclerView.Adapter<AreaAdapter.MyViewHolder>() {

    //private var originalData: ArrayList<_8>? = null
    private var filteredData: ArrayList<ZoneArray>? = null
    //private val inflater: LayoutInflater? = null
    lateinit var psh:UserShared
    lateinit var progressDialog: ProgressDialog
    lateinit var latPsh: Updatedlatlong
    lateinit var prefss: SharedPreferences



    private val viewPool = RecyclerView.RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_plan,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return originalData!!.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        psh= UserShared(conetext)
        prefss = conetext.getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)
        progressDialog= ProgressDialog(conetext)
        latPsh= Updatedlatlong(conetext)

        if (originalData[position].customerId.toString().equals(psh.visitShop)){

           holder.orderPayLay.visibility=View.VISIBLE

        }
        else if (psh.visitShop.equals("gb")){

            holder.orderPayLay.visibility=View.GONE


        }
        else if (!originalData[position].customerId.toString().equals(psh.visitShop)){

            holder.orderPayLay.visibility=View.GONE

        }





        //val talktime = dataArrayList[position].getNames()
        // val data = data[position]
        // val validity = validity[position]
        // val price = price[position]

        holder.tv_nametxt.setText("Name : "+originalData!!.get(position).customerName)
        holder.tv_addressTxt.setText("Address : "+originalData!!.get(position).customerAddress)
        holder.tv_numberTxt.setText("Contact Number : "+originalData!!.get(position).customerContact)
        // holder.tv_price.setText(price)
        // holder.tv_validity.setText(validity)

        /*https://stackoverflow.com/questions/12116092/android-random-string-generator*/

        holder.menuImg.setOnClickListener {

            if (originalData[position].customerId.toString().equals(psh.visitShop)){

                menuEnd(holder.menuImg, position,holder.orderPayLay)

            }
            else if (psh.visitShop.equals("gb")){

                menuFunction(holder.menuImg, position,holder.orderPayLay)
            }
            else if (!originalData[position].customerId.toString().equals(psh.visitShop)){

                Toast.makeText(conetext,"Not Allowed",Toast.LENGTH_SHORT).show()


            }



        }


        holder.order_lay.setOnClickListener {

            confirmTask("take the order",position)
        }

        holder.outstanding_lay.setOnClickListener {

            confirmTask("see outstanding", position)
        }

        holder.bill_lay.setOnClickListener {

            confirmTask("take the bill", position)
        }

        holder.payment_lay.setOnClickListener {

            confirmTask("take the payment", position)
        }

        holder.ledger_Lay.setOnClickListener {

            confirmTask("Ledger", position)
        }

        holder.proceed_txt.setOnClickListener {

            val i1 = Intent(conetext, VisitMenuActivity::class.java)
            i1.putExtra("shopid",originalData!![position].customerId.toString())
            i1.putExtra("shopName", originalData!!.get(position).customerName)
            i1.putExtra("shopAddress", originalData!!.get(position).customerAddress)
            i1.putExtra("shopNumber", originalData!!.get(position).customerContact)
            i1.putExtra("type", "")
            conetext.startActivity(i1)

        }






    }

    private fun confirmTask(txt: String, position: Int) {

        val dialogBuilder = AlertDialog.Builder(conetext)
        dialogBuilder.setMessage("Do you want to "+txt+"?")
            .setCancelable(false)
            .setPositiveButton("Confirm", DialogInterface.OnClickListener {



                    dialog, _ -> dialog.cancel()


                if (txt.equals("take the order")){


                    val i1 = Intent(conetext, AllPlaceOrder::class.java)
                    i1.putExtra("shopid",originalData!![position].customerId.toString())
                    i1.putExtra("shopName", originalData!!.get(position).customerName)
                    i1.putExtra("shopAddress", originalData!!.get(position).customerAddress)
                    i1.putExtra("shopNumber", originalData!!.get(position).customerContact)
                    i1.putExtra("type", "")
                    conetext.startActivity(i1)

                }
                else if (txt.equals("see outstanding")){

                    val i1 = Intent(conetext, OutStanding::class.java)
                    i1.putExtra("shopid",originalData!![position].customerId.toString())
                    i1.putExtra("shopName", originalData!!.get(position).customerName)
                    i1.putExtra("shopAddress", originalData!!.get(position).customerAddress)
                    i1.putExtra("shopNumber", originalData!!.get(position).customerContact)
                    conetext.startActivity(i1)

                }

                else if (txt.equals("take the bill")){

                    val i1 = Intent(conetext, OutletBillActivity::class.java)
                    i1.putExtra("shopid",originalData!![position].customerId.toString())
                    i1.putExtra("shopName", originalData!!.get(position).customerName)
                    i1.putExtra("shopAddress", originalData!!.get(position).customerAddress)
                    i1.putExtra("shopNumber", originalData!!.get(position).customerContact)
                    conetext.startActivity(i1)

                }

                else if (txt.equals("take the payment")){

                    val i1 = Intent(conetext, OutletPayment::class.java)
                    i1.putExtra("shopid",originalData!![position].customerId.toString())
                    i1.putExtra("shopName", originalData!!.get(position).customerName)
                    i1.putExtra("shopAddress", originalData!!.get(position).customerAddress)
                    i1.putExtra("shopNumber", originalData!!.get(position).customerContact)
                    conetext.startActivity(i1)

                }
                else if (txt.equals("Ledger")){

                    val i1 = Intent(conetext, CustomerLedgerActivity::class.java)
                    i1.putExtra("shopid",originalData!![position].customerId.toString())
                    i1.putExtra("shopName", originalData!!.get(position).customerName)
                    i1.putExtra("shopAddress", originalData!!.get(position).customerAddress)
                    i1.putExtra("shopNumber", originalData!!.get(position).customerContact)
                    conetext.startActivity(i1)
                }


            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {


                    dialog, _ -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Alert")
        alert.show()

    }


    fun setHistoryListItems(context: Activity,
                            historyList: ArrayList<ZoneArray>
    ){
        this.originalData = historyList
        this.filteredData = ArrayList()
        this.filteredData!!.addAll(historyList)
        this.conetext=context


        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tv_nametxt: TextView = itemView!!.findViewById(R.id.nametxt)
        val tv_addressTxt: TextView = itemView!!.findViewById(R.id.addressTxt)
        val tv_numberTxt: TextView = itemView!!.findViewById(R.id.numberTxt)
        val order_lay:LinearLayout=itemView!!.findViewById(R.id.orderLay)
        val outstanding_lay:LinearLayout=itemView!!.findViewById(R.id.outstandingLay)
        val bill_lay:LinearLayout=itemView!!.findViewById(R.id.billLay)
        val payment_lay:LinearLayout=itemView!!.findViewById(R.id.paymentLay)
        val menuImg:ImageView=itemView!!.findViewById(R.id._menu)
        val orderPayLay:LinearLayout=itemView!!.findViewById(R.id.orderPaymentLay)
       // val tv_visitTxt: TextView = itemView!!.findViewById(R.id.visitTxt)
       val ledger_Lay:LinearLayout=itemView!!.findViewById(R.id.ledgerLay)
        val proceed_txt:TextView=itemView!!.findViewById(R.id.proceedTxt)


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
                if (wp.customerName.toLowerCase(Locale.getDefault()).contains(charText)||
                    wp.customerContact.contains(charText)
                ) {
                    originalData!!.add(wp)
                }
            }
        }

        notifyDataSetChanged()
    }



    @SuppressLint("RestrictedApi")
    private fun menuFunction(
        menuImg: ImageView,
        listPosition: Int,
        orderPostLay: LinearLayout
    ) {


        val menuBuilder = MenuBuilder(conetext)
        val inflater = MenuInflater(conetext)
        inflater.inflate(R.menu.options_menu_w_o_v, menuBuilder)

        @SuppressLint("RestrictedApi")
        val optionsMenu = MenuPopupHelper(conetext, menuBuilder, menuImg)
        optionsMenu.setForceShowIcon(true)


        // Set Item Click Listener
        menuBuilder.setCallback(object : MenuBuilder.Callback {
            override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu3 // Handle option1 Click
                    -> {
                        optionsMenu.dismiss()

                        if(!PostInterface.isConnected(conetext)){

                            Toast.makeText(conetext,conetext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                        }
                        else{

                            progressDialog!!.setMessage("Please wait ...")
                            progressDialog!!.setCancelable(false)
                            progressDialog!!.show()
                            callStartDuty(listPosition,"6",orderPostLay)

                        }



                        return true
                    }


                    else -> return false
                }
            }

            override fun onMenuModeChange(menu: MenuBuilder) {}
        })


// Display the menu
        optionsMenu.show()
    }

    @SuppressLint("RestrictedApi")
    private fun menuEnd(
        menuImg: ImageView,
        listPosition: Int,
        orderPostLay: LinearLayout
    ) {


        val menuBuilder = MenuBuilder(conetext)
        val inflater = MenuInflater(conetext)
        inflater.inflate(R.menu.end_visit_menu, menuBuilder)

        @SuppressLint("RestrictedApi")
        val optionsMenu = MenuPopupHelper(conetext, menuBuilder, menuImg)
        optionsMenu.setForceShowIcon(true)


        // Set Item Click Listener
        menuBuilder.setCallback(object : MenuBuilder.Callback {
            override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu4 // Handle option1 Click
                    -> {
                        optionsMenu.dismiss()

                        if(!PostInterface.isConnected(conetext)){

                            Toast.makeText(conetext, conetext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                        }
                        else{

                            progressDialog!!.setMessage("Please wait ...")
                            progressDialog!!.setCancelable(false)
                            progressDialog!!.show()
                            callStartDuty(listPosition,"7",orderPostLay)

                        }



                        return true
                    }


                    else -> return false
                }
            }

            override fun onMenuModeChange(menu: MenuBuilder) {}
        })


// Display the menu
        optionsMenu.show()
    }

    private fun callStartDuty(
        listPosition: Int,
        str: String,
        orderPostLay: LinearLayout
    ) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.startDuty("Bearer "+psh.accessToken,psh.id,latPsh.userUpdatedLatitude,latPsh.userUpdatedLongitude,str,originalData[listPosition].customerId.toString()).enqueue(object :
            Callback<GeneralResponce> {
            override fun onResponse(call: Call<GeneralResponce>, response: Response<GeneralResponce>) {
                println("onResponse")
                System.out.println(response.toString())
                val statusCode = response.code()
                if (response.code()==200){
                    progressDialog.dismiss()


                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {
                        progressDialog.dismiss()
                        Toast.makeText(conetext, response.body()!!.message, Toast.LENGTH_SHORT).show()


                        if (str.equals("6")){

                            val editor = prefss.edit()
                            editor.putString(conetext.getString(R.string.shared_visit_status), originalData[listPosition].customerId.toString())
                            editor.commit()

                            orderPostLay.visibility=View.VISIBLE

                        }
                        else{
                            val editor = prefss.edit()
                            editor.putString(conetext.getString(R.string.shared_visit_status), "gb")
                            editor.commit()

                            orderPostLay.visibility=View.GONE
                        }



                    } else {
                        Toast.makeText(conetext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(conetext, "Login Expires"+statusCode, Toast.LENGTH_SHORT).show()

                    val myPrefs = conetext.getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)
                    val editor = myPrefs.edit()
                    editor.clear()
                    editor.apply()


                    val intent = Intent(conetext, SplashScreen::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    conetext.startActivity(intent)

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
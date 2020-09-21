package com.crmretail_es.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.R
import com.crmretail_es.activity.*
import com.crmretail_es.modelClass.CUstomer
import java.util.*
import kotlin.collections.ArrayList

class CustomerAdapter  (var conetext: Context, var originalData:ArrayList<CUstomer>)
    : RecyclerView.Adapter<CustomerAdapter.MyViewHolder>() {

    //private var originalData: ArrayList<_8>? = null
    private var filteredData: ArrayList<CUstomer>? = null
    //private val inflater: LayoutInflater? = null


    private val viewPool = RecyclerView.RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return originalData!!.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.tv_nametxt.setText("Name : "+originalData!!.get(position).customerName)
        holder.tv_addressTxt.setText("Address : "+originalData!!.get(position).customerAddress)
        holder.tv_numberTxt.setText("Contact Number : "+originalData!!.get(position).customerContact)
        // holder.tv_price.setText(price)
        // holder.tv_validity.setText(validity)

        /*https://stackoverflow.com/questions/12116092/android-random-string-generator*/




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






    }

    private fun confirmTask(txt: String, position: Int) {

        val dialogBuilder = AlertDialog.Builder(conetext)
        dialogBuilder.setMessage("Do you want to "+txt+"?")
            .setCancelable(false)
            .setPositiveButton("Confirm", DialogInterface.OnClickListener {



                    dialog, _ -> dialog.cancel()


                if (txt.equals("take the order")){


                    val i1 = Intent(conetext, PlaceOrderActivity::class.java)
                    i1.putExtra("shopid",originalData!![position].customerId.toString())
                    i1.putExtra("shopName", originalData!!.get(position).customerName)
                    i1.putExtra("shopAddress", originalData!!.get(position).customerAddress)
                    i1.putExtra("shopNumber", originalData!!.get(position).customerContact)
                    i1.putExtra("type", "2")
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
                            historyList: ArrayList<CUstomer>
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
        val order_lay: LinearLayout =itemView!!.findViewById(R.id.orderLay)
        val outstanding_lay: LinearLayout =itemView!!.findViewById(R.id.outstandingLay)
        val bill_lay: LinearLayout =itemView!!.findViewById(R.id.billLay)
        val payment_lay: LinearLayout =itemView!!.findViewById(R.id.paymentLay)
        val menuImg: ImageView =itemView!!.findViewById(R.id._menu)
        val orderPayLay: LinearLayout =itemView!!.findViewById(R.id.orderPaymentLay)
        // val tv_visitTxt: TextView = itemView!!.findViewById(R.id.visitTxt)
        val ledger_Lay: LinearLayout =itemView!!.findViewById(R.id.ledgerLay)


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













}
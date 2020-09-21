package com.crmretail_es.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.R
import com.crmretail_es.activity.*
import com.crmretail_es.modelClass.Notclosed
import java.util.*
import kotlin.collections.ArrayList

class FeedbackAdapter (var conetext: Context, var originalData:ArrayList<Notclosed>)
    : RecyclerView.Adapter<FeedbackAdapter.MyViewHolder>() {

    //private var originalData: ArrayList<_8>? = null
    private var filteredData: ArrayList<Notclosed>? = null
    //private val inflater: LayoutInflater? = null


    private val viewPool = RecyclerView.RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feedback,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return originalData!!.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.tv_nametxt.setText(originalData!!.get(position).name)
        holder.tv_addressTxt.setText("Contact : "+originalData!!.get(position).contact)
        // holder.tv_price.setText(price)
        // holder.tv_validity.setText(validity)

        /*https://stackoverflow.com/questions/12116092/android-random-string-generator*/


        holder.itemView.setOnClickListener {

            val i1 = Intent(conetext, FeedBackDetailsActivity::class.java)
            i1.putExtra("regid",originalData[position].registrationId.toString())
            conetext.startActivity(i1)

        }







    }

    private fun confirmTask(txt: String, position: Int) {

        val dialogBuilder = AlertDialog.Builder(conetext)
        dialogBuilder.setMessage("Do you want to "+txt+"?")
            .setCancelable(false)
            .setPositiveButton("Confirm", DialogInterface.OnClickListener {



                    dialog, _ -> dialog.cancel()





            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {


                    dialog, _ -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Alert")
        alert.show()

    }


    fun setHistoryListItems(context: Activity,
                            historyList: ArrayList<Notclosed>
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
                if (wp.name.toLowerCase(Locale.getDefault()).contains(charText)||
                    wp.contact.contains(charText)
                ) {
                    originalData!!.add(wp)
                }
            }
        }

        notifyDataSetChanged()
    }













}
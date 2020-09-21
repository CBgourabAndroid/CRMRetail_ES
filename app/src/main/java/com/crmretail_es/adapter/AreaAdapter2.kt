package com.crmretail_es.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.R
import com.crmretail_es.modelClass.ZoneArray
import java.util.*

class AreaAdapter2 ( val conetext: Context)
    : RecyclerView.Adapter<AreaAdapter2.MyViewHolder>() {

    private var originalData: ArrayList<ZoneArray>? = null
    private var filteredData: ArrayList<ZoneArray>? = null
    //private val inflater: LayoutInflater? = null
    lateinit var context: Activity


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








    }





    fun setHistoryListItems(context: Activity,
                            historyList: ArrayList<ZoneArray>
    ){
        this.originalData = historyList
        this.filteredData = ArrayList()
        this.filteredData!!.addAll(historyList)
        this.context=context


        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tv_nametxt: TextView = itemView!!.findViewById(R.id.nametxt)
        val tv_addressTxt: TextView = itemView!!.findViewById(R.id.addressTxt)
        val tv_numberTxt: TextView = itemView!!.findViewById(R.id.numberTxt)
        // val tv_visitTxt: TextView = itemView!!.findViewById(R.id.visitTxt)



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
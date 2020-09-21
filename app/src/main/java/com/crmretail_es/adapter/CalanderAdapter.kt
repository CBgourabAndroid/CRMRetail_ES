package com.crmretail_es.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.R
import com.crmretail_es.modelClass.Duty

class CalanderAdapter : RecyclerView.Adapter<CalanderAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var dataList: ArrayList<Duty>

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var lbl_date: TextView
        internal var lbl_job: TextView
        internal var lbl_status:TextView


        init {

            lbl_job = itemView.findViewById(R.id.textFishName)
            lbl_date = itemView.findViewById(R.id.billdate)
            lbl_status=itemView.findViewById(R.id.status)


        }
    }

    fun setHistoryListItems(
        context: Context,
        dataList: ArrayList<Duty>

    ){
        this.dataList = dataList
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {

        holder.lbl_date.setText("Date : "+dataList[listPosition].dutyDate.toString())
        holder.lbl_job.setText("Duty Planed : "+dataList[listPosition].zoneName.toString())

        if (dataList[listPosition].status.equals("0")){

            holder.lbl_status.setText("Not Approved Yet")
            holder.lbl_status.setTextColor(Color.parseColor("#EBF11707"))
        }
        else{
            holder.lbl_status.setText("Granted")
            holder.lbl_status.setTextColor(Color.parseColor("#1A951F"))
        }




    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalanderAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_adpter_item, parent, false)


        return CalanderAdapter.MyViewHolder(view)
    }



    override fun getItemCount(): Int {
        return dataList.size
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


}
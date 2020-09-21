package com.crmretail_es.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.modelClass.Holiday

class HolyDayAdapter  : RecyclerView.Adapter<HolyDayAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var dataList: ArrayList<Holiday>

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var lbl_dateevent: TextView
        internal var lbl_monthD: TextView
        internal var lbl_firstD: TextView


        init {

            lbl_dateevent = itemView.findViewById(R.id.dateevent)
            lbl_monthD = itemView.findViewById(R.id.monthD)
            lbl_firstD = itemView.findViewById(R.id.firstD)



        }
    }

    fun setHistoryListItems(
        context: Context,
        dataList: ArrayList<Holiday>

    ){
        this.dataList = dataList
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {

        holder.lbl_dateevent.setText(dataList[listPosition].purpose.toString())
        holder.lbl_monthD.setText(PostInterface.format_date3(dataList[listPosition].holidayDate))

        if (PostInterface.format_date4(dataList[listPosition].holidayDate).equals("01")){
            holder.lbl_firstD.setText(PostInterface.format_date4(dataList[listPosition].holidayDate)+"st")
        }
        else if (PostInterface.format_date4(dataList[listPosition].holidayDate).equals("02")){
            holder.lbl_firstD.setText(PostInterface.format_date4(dataList[listPosition].holidayDate)+"nd")
        }

        else if (PostInterface.format_date4(dataList[listPosition].holidayDate).equals("03")){
            holder.lbl_firstD.setText(PostInterface.format_date4(dataList[listPosition].holidayDate)+"rd")
        }
        else{

            holder.lbl_firstD.setText(PostInterface.format_date4(dataList[listPosition].holidayDate)+"th")
        }





    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolyDayAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_holyday, parent, false)


        return HolyDayAdapter.MyViewHolder(view)
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
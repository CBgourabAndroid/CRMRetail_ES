package com.crmretail_es.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.R
import com.crmretail_es.modelClass.LeaveList

class LeaveListAdapter  : RecyclerView.Adapter<LeaveListAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var dataList: ArrayList<LeaveList>

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var lStatusColor: TextView
        internal var lbl_textViewStatus: TextView
        internal var lbl_textViewtime: TextView
        internal var lbl_textViewReason: TextView


        init {

            lStatusColor = itemView.findViewById(R.id.stats_color)
            lbl_textViewStatus = itemView.findViewById(R.id.textViewStatus)
            lbl_textViewtime = itemView.findViewById(R.id.textViewtime)
            lbl_textViewReason = itemView.findViewById(R.id.textViewReason)



        }
    }

    fun setHistoryListItems(
        context: Context,
        dataList: ArrayList<LeaveList>

    ){
        this.dataList = dataList
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {


        holder.lbl_textViewStatus.setText("Status : "+dataList[listPosition].leaveStatus.toString())
        holder.lbl_textViewtime.setText("Time Frame :  "+dataList[listPosition].fromDate.toString()+" To "+dataList[listPosition].toDate.toString())
        holder.lbl_textViewReason.setText("Reason of Leave\n"+dataList[listPosition].reason.toString())

        if (dataList[listPosition].leaveStatus.equals("Not Approved")){

            holder.lStatusColor.setBackgroundColor(Color.parseColor("#EBF11707"));
        }
        else if (dataList[listPosition].leaveStatus.equals("Approved")){

            holder.lStatusColor.setBackgroundColor(Color.parseColor("#1A951F"));
        }
        else{

            holder.lStatusColor.setBackgroundColor(Color.parseColor("#F36F21"));
        }





    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaveListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leave, parent, false)


        return LeaveListAdapter.MyViewHolder(view)
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
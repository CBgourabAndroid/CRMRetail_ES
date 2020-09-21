package com.crmretail_es.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.modelClass.Bill

class BillAdapter : RecyclerView.Adapter<BillAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var dataList: ArrayList<Bill>

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var lbl_date: TextView
        internal var lbl_amount: TextView
        internal var lbl_trnsid: TextView
        internal var lbl_qty: TextView


        init {

            lbl_date = itemView.findViewById(R.id.billdate)
            lbl_amount = itemView.findViewById(R.id.billamt)
            lbl_trnsid = itemView.findViewById(R.id.billno)
            lbl_qty = itemView.findViewById(R.id.billqty)


        }
    }

    fun setHistoryListItems(
        context: Context,
        dataList: ArrayList<Bill>

    ){
        this.dataList = dataList
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {

        holder.lbl_date.setText(PostInterface.format_date(dataList[listPosition].transactionDate.toString()))
        holder.lbl_amount.setText("₹ "+dataList[listPosition].drAmount.toString())
        holder.lbl_trnsid.setText(dataList[listPosition].transactionNo.toString())
        holder.lbl_qty.setText(dataList[listPosition].transactionQty.toString()+" Mt")




    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BillAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booklist, parent, false)


        return BillAdapter.MyViewHolder(view)
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
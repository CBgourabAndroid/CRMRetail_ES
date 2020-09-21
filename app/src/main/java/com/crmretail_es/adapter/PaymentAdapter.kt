package com.crmretail_es.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.R
import com.crmretail_es.modelClass.Payment

class PaymentAdapter: RecyclerView.Adapter<PaymentAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var dataList: ArrayList<Payment>

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var lbl_date: TextView
        internal var lbl_amount: TextView
        internal var lbl_trnsid: TextView


        init {

            lbl_date = itemView.findViewById(R.id.payDate)
            lbl_amount = itemView.findViewById(R.id.payamt)
            lbl_trnsid = itemView.findViewById(R.id.paytrnsid)


        }
    }

    fun setHistoryListItems(
        context: Context,
        dataList: ArrayList<Payment>

    ){
        this.dataList = dataList
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {

        holder.lbl_date.setText(dataList[listPosition].transactionDate.toString())
        holder.lbl_amount.setText(dataList[listPosition].drAmount.toString())
        holder.lbl_trnsid.setText(dataList[listPosition].transactionNo.toString())




    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment_history, parent, false)


        return PaymentAdapter.MyViewHolder(view)
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
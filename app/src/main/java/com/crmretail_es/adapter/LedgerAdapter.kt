package com.crmretail_es.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.R
import com.crmretail_es.modelClass.Ledger

class LedgerAdapter : RecyclerView.Adapter<LedgerAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var dataList: ArrayList<Ledger>

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var lbl_transactiontype: TextView
      //  internal var lbl_transaction_no: TextView
        internal var lbl_transaction_date: TextView
        internal var lbl_cr_amount: TextView
        internal var lbl_dr_amount: TextView
        internal var lbl_balance: TextView


        init {

            lbl_transactiontype = itemView.findViewById(R.id.transaction_type)
         //   lbl_transaction_no = itemView.findViewById(R.id.transaction_no)
            lbl_transaction_date = itemView.findViewById(R.id.transaction_date)
            lbl_cr_amount = itemView.findViewById(R.id.cr_amount)
            lbl_dr_amount = itemView.findViewById(R.id.dr_amount)
            lbl_balance = itemView.findViewById(R.id.balance)


        }
    }

    fun setHistoryListItems(
        context: Context,
        dataList: ArrayList<Ledger>

    ){
        this.dataList = dataList
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {

        holder.lbl_transactiontype.setText(dataList[listPosition].transactionType.toString())
        //holder.lbl_transaction_no.setText(dataList[listPosition].transactionNo.toString()+"  ")
        holder.lbl_transaction_date.setText(dataList[listPosition].transactionDate.toString())

        holder.lbl_cr_amount.setText(dataList[listPosition].crAmount.toString()+"/-")
        holder.lbl_dr_amount.setText(dataList[listPosition].drAmount.toString()+"/-")
        holder.lbl_balance.setText(dataList[listPosition].balance.toString()+"/-")




    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LedgerAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ledger, parent, false)


        return LedgerAdapter.MyViewHolder(view)
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
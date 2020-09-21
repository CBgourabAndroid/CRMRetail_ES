package com.crmretail_es.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.R
import com.crmretail_es.modelClass.OrderList
import kotlin.collections.ArrayList

class OrderListAdapter : RecyclerView.Adapter<OrderListAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var dataList: ArrayList<OrderList>

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var products: TextView
        internal var total_amount: TextView
        internal var created_at: TextView
        internal var dealerApproved: TextView
        internal var orgApproved: TextView
        internal var received: TextView
        internal var qualtities: TextView



        init {

            products = itemView.findViewById(R.id.textFishName)
            total_amount = itemView.findViewById(R.id.pricetxt)
            created_at = itemView.findViewById(R.id.datetime)
            dealerApproved = itemView.findViewById(R.id.dlrap)
            orgApproved = itemView.findViewById(R.id.orgapv)
            received = itemView.findViewById(R.id.rcvsts)
            qualtities = itemView.findViewById(R.id.textView11)



        }
    }

    fun setHistoryListItems(
        context: Context,
        dataList: ArrayList<OrderList>

    ){
        this.dataList = dataList
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {

       // holder.products.setText(dataList[listPosition].products.toString())
       // holder.qualtities.setText(dataList[listPosition].qualtities.toString())

        val prolist: List<String> = dataList[listPosition].products.toString().split(",")
        val qunlist: List<String> = dataList[listPosition].qualtities.toString().split(",")

        for (i in 0 until prolist.size){

            holder.products.append(prolist[i]+"   -   "+qunlist[i])
            holder.products.append("\n")


        }

        holder.total_amount.setText("₹ "+dataList[listPosition].totalAmount.toString())
        holder.created_at.setText(dataList[listPosition].createdAt.toString())
        holder.dealerApproved.setText(dataList[listPosition].dealerApproved.toString())
        holder.orgApproved.setText(dataList[listPosition].orgApproved.toString())
        holder.received.setText(dataList[listPosition].received.toString())







    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_list, parent, false)


        return OrderListAdapter.MyViewHolder(view)
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
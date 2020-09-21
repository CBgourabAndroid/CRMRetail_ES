package com.crmretail_es.adapter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.R
import com.crmretail_es.modelClass.BrandInfo

class BrandInfoAdapter (val context: Context) : RecyclerView.Adapter<BrandInfoAdapter.MyViewHolder>() {

    var dataList : ArrayList<BrandInfo>?=null
    lateinit var progressDialog: ProgressDialog


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_brand_info,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        progressDialog= ProgressDialog(context)
        holder.tv_name.setText(dataList!![position].currentBrand)
        holder.tv_speciality.setText(dataList!![position].yearlyLifting+"/PM")





    }




    fun setDataListItems(
        historyList: ArrayList<BrandInfo>
    ){
        this.dataList = historyList

        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tv_name: TextView = itemView!!.findViewById(R.id.textFishName)
        val tv_speciality: TextView = itemView!!.findViewById(R.id.textViewname)

    }



}
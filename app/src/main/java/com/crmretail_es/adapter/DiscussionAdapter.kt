package com.crmretail_es.adapter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.modelClass.Discussion

class DiscussionAdapter (val context: Context) : RecyclerView.Adapter<DiscussionAdapter.MyViewHolder>() {

    var dataList : ArrayList<Discussion>?=null
    lateinit var progressDialog: ProgressDialog


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discussion,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        progressDialog= ProgressDialog(context)
        holder.tv_discuss.setText(dataList!![position].discussion)
        holder.tv_date.setText(PostInterface.format_date(dataList!![position].disDate))


    }




    fun setDataListItems(
        historyList: ArrayList<Discussion>
    ){
        this.dataList = historyList

        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tv_discuss: TextView = itemView!!.findViewById(R.id.discussTxt)
        val tv_date: TextView = itemView!!.findViewById(R.id.dateTxt)

    }



}
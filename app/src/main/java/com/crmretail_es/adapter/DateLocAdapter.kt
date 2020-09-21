package com.crmretail_es.adapter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.R
import com.crmretail_es.modelClass.DateLocModel


class DateLocAdapter(val context: Context) : RecyclerView.Adapter<DateLocAdapter.MyViewHolder>() {

    var dataList : ArrayList<DateLocModel>?=null
    lateinit var progressDialog: ProgressDialog


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_list,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.img_cat.visibility=View.GONE
        holder.tv_name.setText(dataList!![position].date)
        val s: String = TextUtils.join(", ", dataList!![position].locName)
        holder.tv_speciality.setText(s)


        holder.itemView.setOnClickListener {

            menuFunction(holder.menuImg, position)



        }


    }




    fun setDataListItems(
        historyList: ArrayList<DateLocModel>
    ){
        this.dataList = historyList

        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tv_name: TextView = itemView!!.findViewById(R.id.textFishName)
        val tv_speciality: TextView = itemView!!.findViewById(R.id.textViewname)
        val img_cat: ImageView = itemView!!.findViewById(R.id.ivFish)
        val menuImg: ImageView =itemView!!.findViewById(R.id.img_menu)




    }

    @SuppressLint("RestrictedApi")
    private fun menuFunction(menuImg: ImageView, listPosition: Int) {


        val menuBuilder = MenuBuilder(context)
        val inflater = MenuInflater(context)
        inflater.inflate(R.menu.menu_delete, menuBuilder)

        @SuppressLint("RestrictedApi")
        val optionsMenu = MenuPopupHelper(context, menuBuilder, menuImg)
        optionsMenu.setForceShowIcon(true)


        // Set Item Click Listener
        menuBuilder.setCallback(object : MenuBuilder.Callback {
            override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu3 // Handle option1 Click
                    -> {
                        optionsMenu.dismiss()

                        dataList!!.removeAt(listPosition)
                        notifyDataSetChanged()


                        return true
                    }



                    else -> return false
                }
            }

            override fun onMenuModeChange(menu: MenuBuilder) {}
        })


// Display the menu
        optionsMenu.show()
    }


}
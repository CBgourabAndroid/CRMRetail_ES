package com.crmretail_es.fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.SplashScreen
import com.crmretail_es.adapter.AreaAdapter
import com.crmretail_es.adapter.AreaAdapter2
import com.crmretail_es.modelClass.AreaInfo
import com.crmretail_es.modelClass.GeneralResponce
import com.crmretail_es.modelClass.ZoneArray
import com.crmretail_es.shared.Updatedlatlong
import com.crmretail_es.shared.UserShared
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class AreaFragment (var tablayout: TabLayout, var tablist: ArrayList<AreaInfo>, var toolbar: Toolbar, var searchToolbar: Toolbar) : Fragment() {


    lateinit var recyclerViewPlans: RecyclerView
    lateinit var plansAdapter: AreaAdapter
    lateinit var plansAdapter2: AreaAdapter2
    lateinit var etsearch: EditText
    internal var textlength = 0
    lateinit var search_menu: Menu
    lateinit var item_search: MenuItem
    lateinit var  dataArrayList: ArrayList<ZoneArray>
    lateinit var  array_sort: ArrayList<ZoneArray>
    lateinit var  psh:UserShared


  /*  companion object {

        lateinit var array_sort: ArrayList<_8>
    }*/
   lateinit var progressDialog: ProgressDialog

    lateinit var root: View
    lateinit var latPsh: Updatedlatlong
    lateinit var prefss: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_area, container, false)
        //setHasOptionsMenu(true)
        prefss = requireContext().getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)




        recyclerViewPlans = root!!.findViewById(R.id.recyv_plans)
        etsearch=root!!.findViewById(R.id.editText)
        psh=UserShared(context)
        progressDialog= ProgressDialog(context)
        latPsh= Updatedlatlong(context)
        dataArrayList = ArrayList<ZoneArray>()


        array_sort= ArrayList<ZoneArray>()

        //loadData()
        val position: Int = tablayout.selectedTabPosition
        dataArrayList=tablist[position].zoneArray as ArrayList<ZoneArray>
        Collections.sort(dataArrayList,ALPHABETICAL_ORDER)
        plansAdapter = AreaAdapter(requireContext(), dataArrayList)
        recyclerViewPlans!!.adapter = plansAdapter
        recyclerViewPlans!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)



        val controller =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_down_to_up);

        recyclerViewPlans.setLayoutAnimation(controller)
        recyclerViewPlans.getAdapter()!!.notifyDataSetChanged()
        recyclerViewPlans.scheduleLayoutAnimation()


       // val position: Int = tablayout.selectedTabPosition

        /*if (tablist[position].districtName.equals("East Burdwan")){


        }
        else{
            plansAdapter2 = AreaAdapter2(requireActivity())
            recyclerViewPlans.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerViewPlans.adapter = plansAdapter2
            plansAdapter2.setHistoryListItems(requireActivity(), dataArrayList2)
        }*/


        recyclerViewPlans!!.addOnItemTouchListener(
            RecyclerTouchListener(
                requireActivity(),
                recyclerViewPlans!!,
                object : ClickListener {

                    override fun onClick(view: View, position: Int) {
                        /* Toast.makeText(
                             context,
                             dataArrayList[position].get_Talktime(),
                             Toast.LENGTH_SHORT
                         ).show()*/
                        val menuImg:ImageView=view.findViewById(R.id._menu)
                        val orderPostLay=view.findViewById<LinearLayout>(R.id.orderPaymentLay)

                     /*   if (psh.visitShop.equals("gb")){

                            menuFunction(menuImg, position,orderPostLay)
                        }
                        else {

                            menuEnd(menuImg, position,orderPostLay)
                        }*/





                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )




         etsearch!!.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                array_sort.clear()
                val position: Int = tablayout.selectedTabPosition

                for (i in dataArrayList.indices) {

                    if (dataArrayList[i].customerName.toLowerCase().trim().contains(etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        || dataArrayList[i].customerContact.toLowerCase().trim().contains(etsearch!!.text.toString().toLowerCase().trim{it<=' '})) {

                        array_sort.add(dataArrayList[i])
                    }
                }
                plansAdapter = AreaAdapter(context!!, array_sort)
                recyclerViewPlans!!.adapter = plansAdapter
                recyclerViewPlans!!.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


            }
        })

        // setHasOptionsMenu(true);
        bindData()



        return root
    }



    private fun setupTab() {







    }

    private fun bindData() {
        tablayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                for (i in tablist.indices) {
                    if (tab.position === i) {
                    //    category_name = filterPlaces.get(i).toString()
                      //  placeApi(currentLocation, category_name)
                        dataArrayList=tablist[i].zoneArray as ArrayList<ZoneArray>
                        Collections.sort(dataArrayList,ALPHABETICAL_ORDER)
                        plansAdapter = AreaAdapter(requireContext(), dataArrayList)
                        recyclerViewPlans!!.adapter = plansAdapter
                        recyclerViewPlans!!.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)



                        val controller =
                            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_down_to_up);

                        recyclerViewPlans.setLayoutAnimation(controller)
                        recyclerViewPlans.getAdapter()!!.notifyDataSetChanged()
                        recyclerViewPlans.scheduleLayoutAnimation()
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


    }



    fun loadData(){
        val gson = Gson()
        val json =psh.areaData
        val turnsType = object : TypeToken<ArrayList<ZoneArray>>() {}.type

        dataArrayList=gson.fromJson(json,turnsType)
        Collections.sort(dataArrayList,ALPHABETICAL_ORDER)




    }

    private val ALPHABETICAL_ORDER: Comparator<ZoneArray> =
        object : Comparator<ZoneArray> {

            override fun compare(o1: ZoneArray?, o2: ZoneArray?): Int {

                var res = java.lang.String.CASE_INSENSITIVE_ORDER.compare(o1!!.customerName, o2!!.customerName)
                if (res == 0) {
                    res = o1!!.customerName.compareTo(o2!!.customerName)
                }
                return res
            }
        }



   /* private fun populateList(): ArrayList<_81> {

        val list = ArrayList<_81>()

        for (i in talktime.indices) {
            val imageModel = _8()
            imageModel.customerId(talktime!![i])
            imageModel.setDataPlan(data!![i])
            imageModel.setvalidity(validity!![i])
            imageModel.setprice(price!![i])
            imageModel.setdescription(description!![i])
            list.add(imageModel)
        }

        return list
    }*/


    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    internal class RecyclerTouchListener(
        context: Context,
        recyclerView: RecyclerView,
        private val clickListener: ClickListener?
    ) : RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector

        init {
            gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }

                    override fun onLongPress(e: MotionEvent) {
                        val child = recyclerView.findChildViewUnder(e.x, e.y)
                        if (child != null && clickListener != null) {
                            clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                        }
                    }
                })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }



    @SuppressLint("RestrictedApi")
    private fun menuEnd(
        menuImg: ImageView,
        listPosition: Int,
        orderPostLay: LinearLayout
    ) {


        val menuBuilder = MenuBuilder(context)
        val inflater = MenuInflater(context)
        inflater.inflate(R.menu.options_menu_w_o_v, menuBuilder)

        @SuppressLint("RestrictedApi")
        val optionsMenu = MenuPopupHelper(requireContext(), menuBuilder, menuImg)
        optionsMenu.setForceShowIcon(true)


        // Set Item Click Listener
        menuBuilder.setCallback(object : MenuBuilder.Callback {
            override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu3 // Handle option1 Click
                    -> {
                        optionsMenu.dismiss()

                        if(!PostInterface.isConnected(context!!)){

                            Toast.makeText(context!!, context!!.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                        }
                        else{

                            progressDialog!!.setMessage("Please wait ...")
                            progressDialog!!.setCancelable(false)
                            progressDialog!!.show()
                            callStartDuty(listPosition,"7",orderPostLay)

                        }



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



    @SuppressLint("RestrictedApi")
    private fun menuFunction(
        menuImg: ImageView,
        listPosition: Int,
        orderPostLay: LinearLayout
    ) {


        val menuBuilder = MenuBuilder(context)
        val inflater = MenuInflater(context)
        inflater.inflate(R.menu.options_menu_w_o_v, menuBuilder)

        @SuppressLint("RestrictedApi")
        val optionsMenu = MenuPopupHelper(requireContext(), menuBuilder, menuImg)
        optionsMenu.setForceShowIcon(true)


        // Set Item Click Listener
        menuBuilder.setCallback(object : MenuBuilder.Callback {
            override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu3 // Handle option1 Click
                    -> {
                        optionsMenu.dismiss()

                        if(!PostInterface.isConnected(context!!)){

                            Toast.makeText(context!!, context!!.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                        }
                        else{

                            progressDialog!!.setMessage("Please wait ...")
                            progressDialog!!.setCancelable(false)
                            progressDialog!!.show()
                            callStartDuty(listPosition,"6",orderPostLay)

                        }



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


    private fun callStartDuty(
        listPosition: Int,
        str: String,
        orderPostLay: LinearLayout
    ) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.startDuty("Bearer "+psh.accessToken,psh.id,latPsh.userUpdatedLatitude,latPsh.userUpdatedLongitude,str,dataArrayList[listPosition].customerId.toString()).enqueue(object :
            Callback<GeneralResponce> {
            override fun onResponse(call: Call<GeneralResponce>, response: Response<GeneralResponce>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {
                        progressDialog.dismiss()
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()


                        if (str.equals("6")){

                            val editor = prefss.edit()
                            editor.putString(getString(R.string.shared_visit_status), dataArrayList[listPosition].customerId.toString())
                            editor.commit()

                            orderPostLay.visibility=View.VISIBLE

                        }
                        else{
                            val editor = prefss.edit()
                            editor.putString(getString(R.string.shared_visit_status), "end")
                            editor.commit()

                            orderPostLay.visibility=View.GONE
                        }



                    } else {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(context, "Login Expires", Toast.LENGTH_SHORT).show()

                    val myPrefs = context!!.getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)
                    val editor = myPrefs.edit()
                    editor.clear()
                    editor.apply()


                    val intent = Intent(context, SplashScreen::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)

                }
            }

            override fun onFailure(call: Call<GeneralResponce>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }










}
package com.crmretail_es.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.crmretail_es.R
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class AddSellingBrand : AppCompatActivity() {

    lateinit var backImg: ImageView
    lateinit var saveData: TextView
    lateinit var brandName: TextInputEditText
    lateinit var brandQuantity: TextInputEditText

    var aa=""
    var bb=""
    var BrandName=""
    var Quantity=""
    var expandableListView: ExpandableListView? = null
    var expandableListAdapter: ExpandableListAdapter? = null
    var expandableListTitle: List<String>? = null
    var expandableListDetail: HashMap<String, List<String>>? =
        null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.add_selling_brand)

        backImg=findViewById(R.id.imageView4)
        backImg.setOnClickListener {

            onBackPressed()
        }


        inti()
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("name", "")
        intent.putExtra("qty", "")
        setResult(123, intent)
        finish()
    }

    private fun inti() {


        brandName=findViewById(R.id.foodTypeName)
        brandQuantity=findViewById(R.id.foodTypePrice)
        saveData=findViewById(R.id.savetxt)

        expandableListView = findViewById(R.id.expandableListView) as ExpandableListView
        expandableListDetail = ExpandableListDataPump.getData()
        expandableListTitle = ArrayList(expandableListDetail!!.keys)
        expandableListAdapter =
            CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail)
        expandableListView!!.setAdapter(expandableListAdapter)
        expandableListView!!.setOnGroupExpandListener { groupPosition ->
            /*Toast.makeText(
                applicationContext,
                expandableListTitle!!.get(groupPosition) + " List Expanded.",
                Toast.LENGTH_SHORT
            ).show()*/
            aa=expandableListTitle!!.get(groupPosition)
        }

        expandableListView!!.setOnGroupCollapseListener { groupPosition ->
           /* Toast.makeText(
                applicationContext,
                expandableListTitle!!.get(groupPosition) + " List Collapsed.",
                Toast.LENGTH_SHORT
            ).show()*/
        }

        expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            /*Toast.makeText(
                applicationContext,
                expandableListTitle!!.get(groupPosition) + " -> "
                        + expandableListDetail!!.get(
                    expandableListTitle!!.get(groupPosition)
                )!![childPosition], Toast.LENGTH_SHORT
            ).show()*/
            BrandName=expandableListTitle!!.get(groupPosition) + "-"+ expandableListDetail!!.get(
                expandableListTitle!!.get(groupPosition)
            )!![childPosition]

            expandableListView!!.collapseGroup(groupPosition)
            brandName.setText(BrandName)

            false
        }

        saveData.setOnClickListener {


            validation()
        }

    }



    @SuppressLint("NewApi")
    private fun validation() {


        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false

       // BrandName=brandName.text.toString()
        Quantity=brandQuantity.text.toString()


        if (TextUtils.isEmpty(BrandName)) {
            message = "Please Select Brand Name"
            focusView = brandName
            cancel = true
            tempCond = false
        }




        if (TextUtils.isEmpty(Quantity)) {
            message = "Please Enter Quantity"
            focusView = brandQuantity
            cancel = true
            tempCond = false
        }



        if (cancel) {
            // focusView.requestFocus();
            if (!tempCond) {
                focusView!!.requestFocus()
            }
            showToastLong(message)
        } else {
            val imm = this
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            val intent = Intent()
            intent.putExtra("name", BrandName)
            intent.putExtra("qty", Quantity)
            setResult(123, intent)
            finish()




        }



    }


    private fun showToastLong(message: String) {

        Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()

    }




}
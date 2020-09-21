package com.crmretail_es.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.crmretail_es.PostInterface
import com.crmretail_es.R
import com.crmretail_es.activity.AddTravelDetails
import com.crmretail_es.modelClass.PrivateTravelModel
import com.crmretail_es.modelClass.PublicTravelModel
import java.text.SimpleDateFormat
import java.util.*

class TravellingFragment  : Fragment() {


    lateinit var root: View
    lateinit var currentDateStart: String
    var calendar = Calendar.getInstance()
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH)
    var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    var hour = calendar.get(Calendar.HOUR_OF_DAY)
    var minute = calendar.get(Calendar.MINUTE)
    lateinit var datePickerDialog: DatePickerDialog
    lateinit var foodingDate: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var addFood: TextView
    lateinit var saveData:TextView

    lateinit var privateDataList:ArrayList<PrivateTravelModel>
    lateinit var publicDataList:ArrayList<PublicTravelModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_travaling, container, false)
        //setHasOptionsMenu(true)

        inti(root)
        return root
    }

    @SuppressLint("NewApi")
    private fun inti(root: View) {
        currentDateStart=""

        recyclerView=root.findViewById(R.id.recyv_fooding)
        addFood=root.findViewById(R.id.add_foodBtn)
        foodingDate=root.findViewById(R.id.fooding_date_txt)
        saveData=root.findViewById(R.id.submitFooding)


        addFood.setOnClickListener {

            val intent = Intent(context, AddTravelDetails::class.java)
            startActivityForResult(intent, 125)
        }

        setData()
        saveData.setOnClickListener {


           // validation()

        }
        foodingDate.setOnClickListener {

            dateFun()
        }


    }

    private fun dateFun() {


        datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { datePicker, fyear, fmonth, fday ->


                val mont = fmonth + 1
                var mt = ""
                var dy = ""

                if (mont < 10) {
                    mt = "0" + mont.toString()
                } else {
                    mt = mont.toString()
                }

                if (fday < 10) {

                    dy = "0" + fday.toString()
                } else {

                    dy = fday.toString()
                }

                val sss= PostInterface.getCalculatedDate("yyyy-MM-dd", -7)
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val beforeDate: Date = sdf.parse(sss)
                val current: Date = sdf.parse(fyear.toString() + "-" + mt + "-" + dy)
                if(beforeDate.before(current)){
                    currentDateStart = fyear.toString() + "-" + mt + "-" + dy
                    foodingDate.setText(PostInterface.format_date(currentDateStart))
                }
                else{
                    Toast.makeText(context,"More then 7days not allowed!!", Toast.LENGTH_SHORT).show()
                }


            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()


    }

    private fun setData() {


       /* if (foodDataList.size>0){

            adapter = FoodListAdapter(requireContext())
            // recyclerViewCategory.layoutManager = GridLayoutManager(this,3) as RecyclerView.LayoutManager?

            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
            recyclerView.adapter = adapter
            adapter.setDataListItems(foodDataList)
        }*/


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        // super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode==125){

                val returnValuetravelType = data.getStringExtra("travelType")

                if (!returnValuetravelType.equals("")){

                    if (returnValuetravelType.equals("Private")){
                        val model= PrivateTravelModel()
                        model.travelType=data.getStringExtra("travelType")
                        model.travelwith=data.getStringExtra("privatetrans")
                        model.privateTransKm=data.getStringExtra("privatetranskm")
                       // model.sopportPic=data.getStringExtra("picture")
                        privateDataList.add(model)

                    }
                    else if (returnValuetravelType.equals("Public")){
                        val model= PublicTravelModel()
                        model.travelType==data.getStringExtra("travelType")
                        model.publicTravelWith==data.getStringExtra("publictranswith")
                        model.publicStartLocation==data.getStringExtra("startLoc")
                        model.publicEndLocation==data.getStringExtra("endLoc")
                        model.publicAmt==data.getStringExtra("Amt")

                    }

                }


                setData()



            }


            else {

                // failed to capture image
                Toast.makeText(
                    context,
                    "Sorry! Failed ", Toast.LENGTH_SHORT
                )
                    .show()

            }
        } else {
            Toast.makeText(
                context,
                "User cancelled ", Toast.LENGTH_SHORT
            )
                .show()
        }


    }
}
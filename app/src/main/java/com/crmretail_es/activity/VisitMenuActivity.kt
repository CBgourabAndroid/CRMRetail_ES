package com.crmretail_es.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.crmretail_es.R
import kotlinx.android.synthetic.main.contain_menu_visit.*
import kotlinx.android.synthetic.main.visit_menu_activity.*

class VisitMenuActivity : AppCompatActivity() {


    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.visit_menu_activity)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }
        inti()
    }

    override fun onBackPressed() {

        startActivity(Intent(this, OutletVisitActivity::class.java))
    }

    private fun inti() {

        textView6.text=intent.getStringExtra("shopName")

        placeOrder.setOnClickListener {

            val i1 = Intent(this, AllPlaceOrder::class.java)
            i1.putExtra("shopid",intent.getStringExtra("shopid"))
            i1.putExtra("shopName", intent.getStringExtra("shopName"))
            i1.putExtra("shopAddress", intent.getStringExtra("shopAddress"))
            i1.putExtra("shopNumber", intent.getStringExtra("shopNumber"))
            i1.putExtra("type", "")
           startActivity(i1)
        }

        outstanding.setOnClickListener {

            val i1 = Intent(this, OutStanding::class.java)
            i1.putExtra("shopid",intent.getStringExtra("shopid"))
            i1.putExtra("shopName", intent.getStringExtra("shopName"))
            i1.putExtra("shopAddress", intent.getStringExtra("shopAddress"))
            i1.putExtra("shopNumber", intent.getStringExtra("shopNumber"))
            startActivity(i1)
        }
        ledger.setOnClickListener {

            val i1 = Intent(this, CustomerLedgerActivity::class.java)
            i1.putExtra("shopid",intent.getStringExtra("shopid"))
            i1.putExtra("shopName", intent.getStringExtra("shopName"))
            i1.putExtra("shopAddress", intent.getStringExtra("shopAddress"))
            i1.putExtra("shopNumber", intent.getStringExtra("shopNumber"))
            startActivity(i1)
        }
        branding.setOnClickListener {

            val i1 = Intent(this, BrandingActivity::class.java)
            i1.putExtra("shopid",intent.getStringExtra("shopid"))
            i1.putExtra("shopName", intent.getStringExtra("shopName"))
            i1.putExtra("shopAddress", intent.getStringExtra("shopAddress"))
            i1.putExtra("shopNumber", intent.getStringExtra("shopNumber"))
            startActivity(i1)
        }
        persentStock.setOnClickListener {

            val i1 = Intent(this, PresentStockActivity::class.java)
            i1.putExtra("shopid",intent.getStringExtra("shopid"))
            i1.putExtra("shopName", intent.getStringExtra("shopName"))
            i1.putExtra("shopAddress", intent.getStringExtra("shopAddress"))
            i1.putExtra("shopNumber", intent.getStringExtra("shopNumber"))
            i1.putExtra("type", "")
            startActivity(i1)
        }

    }
}
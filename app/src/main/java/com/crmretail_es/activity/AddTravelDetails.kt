package com.crmretail_es.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.crmretail_es.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.add_traveldata.*
import java.io.*

class AddTravelDetails :AppCompatActivity() {

    lateinit var backImg: ImageView
    lateinit var saveData: TextView
    lateinit var take_Pic: ImageView
    lateinit var foodPic: ImageView
    private val REQUEST_CAMERA = 0
    private var picturepath: Uri? = null
    lateinit var dl_upload_file: File
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    lateinit var tansKM:TextInputEditText

    lateinit var publicTravelWith:TextInputEditText
    lateinit var startLocation:TextInputEditText
    lateinit var endLocation:TextInputEditText
    lateinit var amtEdt:TextInputEditText

    var TravelType=""
    var Travelwith=""
    var privateTransKm=""

    var PublicTravelWith=""
    var PublicStartLocation=""
    var PublicEndLocation=""
    var PublicAmt=""
    private  val FILE_NAME = "photo.jpg"
    private  val REQUEST_CODE = 42
    var photoFile: File?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.add_traveldata)

        backImg=findViewById(R.id.imageView4)
        backImg.setOnClickListener {

            onBackPressed()
        }


        inti()
    }

    private fun inti() {

        if (intent.getStringExtra("from").equals("public")){
            privateLay.visibility=View.GONE
            publicLay.visibility=View.VISIBLE
            TravelType="Public"

        }
        else if(intent.getStringExtra("from").equals("private")){

            privateLay.visibility=View.VISIBLE
            publicLay.visibility=View.GONE
            TravelType="Private"
        }

        Travelwith="Car"


        val spinnerwith = findViewById<Spinner>(R.id.spinner)
        val languageswith = resources.getStringArray(R.array.travelwith)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, languageswith
            )
            spinnerwith.adapter = adapter

            spinnerwith.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    Travelwith=languageswith[position].toString()

                    //eventType=languages[position].toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        tansKM=findViewById(R.id.transkm)

        publicTravelWith=findViewById(R.id.travelwithtypePublic)
        startLocation=findViewById(R.id.start_Location)
        endLocation=findViewById(R.id.end_Location)
        amtEdt=findViewById(R.id.amountpublic)


        take_Pic=findViewById(R.id.takeApic)
        foodPic=findViewById(R.id.supportImage)
        saveData=findViewById(R.id.savetxt)

        take_Pic.setOnClickListener {

            takePICTURE()
        }

        saveData.setOnClickListener {


            validation()
        }

    }

    private fun takePICTURE() {


        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)

        // This DOESN'T work for API >= 24 (starting 2016)
        // takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)

        val fileProvider = FileProvider.getUriForFile(this, "com.crmretail", photoFile!!)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CODE)
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }

    }
    fun verifyStoragePermissions(activity: Activity) {
        // Check if we have write permission
        val permission: Int = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
        else{
            selectImage()
        }
    }

    override fun onBackPressed() {
        if (intent.getStringExtra("from").equals("public")){
            val intent = Intent()
            intent.putExtra("travelType", "")
            intent.putExtra("pict", "")
            intent.putExtra("publictranswith", "")
            intent.putExtra("startLoc", "")
            intent.putExtra("endLoc", "")
            intent.putExtra("Amt", "")
            setResult(126, intent)
            finish()

        }
        else if(intent.getStringExtra("from").equals("private")){

            val intent = Intent()
            intent.putExtra("travelType", "")
            intent.putExtra("privatetrans", "")
            intent.putExtra("privatetranskm", "")
            intent.putExtra("picture", "")
            setResult(125, intent)
            finish()
        }

    }
    private fun selectImage() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)


    }

 /*   @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
                if (data.data != null) {
                    picturepath = data.data

                } else {

                    val photo = data.extras!!.get("data") as Bitmap?
                    picturepath = getImageUri(this, photo!!)
                }

                if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
                    onCaptureImageResult(data)
                }


            }

            *//* else if (requestCode == PLACE_PICKER_REQUEST) {
                  if (resultCode == RESULT_OK) {
                      val place = PlacePicker.getPlace(context, data)
                      var addressText = place.name.toString()
                      //addressText += "\n" + place.address.toString()
                      shop_location.setText(addressText)

                  }
              }*//*
            else if (resultCode == Activity.RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(
                    this,
                    "User cancelled !!", Toast.LENGTH_SHORT
                )
                    .show()
            }

            else if (requestCode == 200) {
                if (resultCode == Activity.RESULT_OK) {

                }
                else if (resultCode == Activity.RESULT_CANCELED) {
                }
            }


            else {

                // failed to capture image
                Toast.makeText(
                    this,
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT
                )
                    .show()

            }
        } else {
            Toast.makeText(
                this,
                "User cancelled ", Toast.LENGTH_SHORT
            )
                .show()
        }


    }
*/
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title1", null)
        return Uri.parse(path)
    }

    private fun onCaptureImageResult(data: Intent) {
        val thumbnail = data.extras!!.get("data") as Bitmap?
        val bytes = ByteArrayOutputStream()
        thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

        val destination = File(
            Environment.getExternalStorageDirectory(),
            System.currentTimeMillis().toString() + ".jpg"
        )

        val fo: FileOutputStream
        try {
            destination.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }



        foodPic.setImageBitmap(thumbnail)
        // this.profileImage.setRotation(90);
    }


    @SuppressLint("NewApi")
    private fun validation() {


        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false

        privateTransKm=tansKM.text.toString()

        PublicTravelWith=publicTravelWith.text.toString()
        PublicStartLocation=startLocation.text.toString()
        PublicEndLocation=endLocation.text.toString()
        PublicAmt=amtEdt.text.toString()

        if (intent.getStringExtra("from").equals("private")){

            if (TextUtils.isEmpty(privateTransKm)) {
                message = "Please Enter Meter Reading"
                focusView = tansKM
                cancel = true
                tempCond = false
            }
            if (photoFile==null) {
                message = "Please Provide Supporting Image"
                focusView = tansKM
                cancel = true
                tempCond = false
            }
        }
        else if(intent.getStringExtra("from").equals("public")){

            if (photoFile==null) {
                message = "Please Provide Supporting Image"
                focusView = publicTravelWith
                cancel = true
                tempCond = false
            }
            if (TextUtils.isEmpty(PublicTravelWith)) {
                message = "Please Enter Transport Type"
                focusView = publicTravelWith
                cancel = true
                tempCond = false
            }
            if (TextUtils.isEmpty(PublicStartLocation)) {
                message = "Please Enter Start Location"
                focusView = startLocation
                cancel = true
                tempCond = false
            }
            if (TextUtils.isEmpty(PublicEndLocation)) {
                message = "Please Enter End Location"
                focusView = endLocation
                cancel = true
                tempCond = false
            }
            if (TextUtils.isEmpty(PublicAmt)) {
                message = "Please Enter Amount"
                focusView = amtEdt
                cancel = true
                tempCond = false
            }


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


            if (intent.getStringExtra("from").equals("public")){
                val intent = Intent()
                intent.putExtra("travelType", PublicTravelWith)
                intent.putExtra("picture", photoFile.toString())
                intent.putExtra("publictranswith", PublicTravelWith)
                intent.putExtra("startLoc", PublicStartLocation)
                intent.putExtra("endLoc", PublicEndLocation)
                intent.putExtra("Amt", PublicAmt)
                setResult(126, intent)
                finish()

            }
            else if(intent.getStringExtra("from").equals("private")){

                val intent = Intent()
                intent.putExtra("travelType", TravelType)
                intent.putExtra("privatetrans", Travelwith)
                intent.putExtra("privatetranskm", privateTransKm)
                intent.putExtra("picture", photoFile.toString())
                setResult(125, intent)
                finish()
            }


        }



    }

    private fun showToastLong(message: String) {

        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

    }

    private fun getPhotoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val takenImage = data?.extras?.get("data") as Bitmap
            val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
            foodPic.setImageBitmap(takenImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

}
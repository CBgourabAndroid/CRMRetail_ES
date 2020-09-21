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
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.crmretail_es.R
import com.google.android.material.textfield.TextInputEditText
import java.io.*


class AddFoodingDetails : AppCompatActivity() {

    lateinit var backImg: ImageView
    lateinit var saveData: TextView
    lateinit var take_Pic: ImageView
    lateinit var foodPic:ImageView
    lateinit var foodName:TextInputEditText
    lateinit var foodPrice:TextInputEditText
    private val REQUEST_CAMERA = 0
    private var picturepath: Uri? = null
    lateinit var dl_upload_file: File
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    var FoodName=""
    var FoodPrice=""
    var PicPath=""
    var mytest=""

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
        setContentView(R.layout.add_fooding)

        backImg=findViewById(R.id.imageView4)
        backImg.setOnClickListener {

            onBackPressed()
        }


        inti()
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("name", "")
        intent.putExtra("price", "")
        intent.putExtra("pic", "")
        setResult(124, intent)
        finish()
    }

    private fun inti() {

        take_Pic=findViewById(R.id.takeApic)
        foodPic=findViewById(R.id.supportImage)
        foodName=findViewById(R.id.foodTypeName)
        foodPrice=findViewById(R.id.foodTypePrice)
        saveData=findViewById(R.id.savetxt)
        val spinner = findViewById<Spinner>(R.id.spinner)
        take_Pic.setOnClickListener {

            //verifyStoragePermissions(this)

            takePICTURE()
        }

        saveData.setOnClickListener {


            validation()
        }

        if (intent.getStringExtra("from").equals("lodging")){
            spinner.visibility=View.GONE
            foodName.visibility=View.VISIBLE
        }
        else{
            spinner.visibility=View.VISIBLE
            foodName.visibility=View.GONE
        }


        val languages = resources.getStringArray(R.array.foodType)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, languages
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    FoodName=languages[position].toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
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

    @SuppressLint("NewApi")
    private fun validation() {


        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false
        FoodPrice=foodPrice.text.toString()

        if (intent.getStringExtra("from").equals("lodging")){
            FoodName=foodName.text.toString()
        }





        if (TextUtils.isEmpty(FoodName)) {
            message = "Please Enter Expenses Name"
            focusView = foodName
            cancel = true
            tempCond = false
        }




        if (TextUtils.isEmpty(FoodPrice)) {
            message = "Please Enter Expenses Amount"
            focusView = foodPrice
            cancel = true
            tempCond = false
        }

        if (intent.getStringExtra("from").equals("lodging")){

              if (photoFile==null) {
                message = "Please Provide Supporting Image"
                focusView = foodName
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

            if (photoFile==null){

                val intent = Intent()
                intent.putExtra("name", FoodName)
                intent.putExtra("price", FoodPrice)
                intent.putExtra("pic", "")
                setResult(124, intent)
                finish()
            }
            else{
                val intent = Intent()
                intent.putExtra("name", FoodName)
                intent.putExtra("price", FoodPrice)
                intent.putExtra("pic", photoFile.toString())
                setResult(124, intent)
                finish()

            }




        }



    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getRealPathFromURI_API19(context: Context, uri: Uri): String? {
        var filePath = ""
        try { // FIXME NPE error when select image from QuickPic, Dropbox etc

            val wholeID = DocumentsContract.getDocumentId(uri)
            // Split at colon, use second item in the array
            val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val column = arrayOf(MediaStore.Images.Media.DATA)
            // where id is equal to
            val sel = MediaStore.Images.Media._ID + "=?"
            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null
            )
            val columnIndex = cursor!!.getColumnIndex(column[0])
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex)
            }

            cursor.close()

            return filePath
        } catch (e: Exception) { // this is the fix lol
            val result: String?
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = uri.path
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
                cursor.close()
            }
            return result
        }

    }

    private fun showToastLong(message: String) {

        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

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

    private fun selectImage() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)


    }

   /* @SuppressLint("MissingSuperCall")
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


    }*/

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
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
